package com.github.vladislavgoltjajev.isikukood;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.github.vladislavgoltjajev.isikukood.EstonianId.FEMALE;
import static com.github.vladislavgoltjajev.isikukood.EstonianId.MALE;

@UtilityClass
final class EstonianIdUtils {

    private static final LocalDate START_DATE = LocalDate.of(1800, Month.JANUARY, 1);
    private static final LocalDate END_DATE = LocalDate.of(2099, Month.DECEMBER, 31);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuuMMdd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final Random RANDOM = new Random();
    private static final String PERSONAL_CODE_REGEX = "^[1-6]\\d{2}(((0[13578]|1[02])(0[1-9]|[12]\\d|3[01]))"
            + "|((0[469]|11)(0[1-9]|[12]\\d|30))"
            + "|(02(0[1-9]|1\\d|2[0-9])))"
            + "\\d{4}$"; // Does not handle leap years.

    static boolean initialValidationPassed(String personalCode) {
        return personalCode != null
                && !personalCode.isEmpty()
                && personalCode.matches(PERSONAL_CODE_REGEX);
    }

    @SneakyThrows
    static String generateRandomPersonalCode() {
        return generatePersonalCode(getRandomGender(), generateRandomDateOfBirth(), generateRandomBirthOrderNumber());
    }

    static String generatePersonalCode(String gender, LocalDate dateOfBirth) throws EstonianIdException {
        return generatePersonalCode(gender, dateOfBirth, generateRandomBirthOrderNumber());
    }

    static String generatePersonalCode(String gender, LocalDate dateOfBirth, int birthOrderNumber)
            throws EstonianIdException {
        if (!MALE.equalsIgnoreCase(gender) && !FEMALE.equalsIgnoreCase(gender)) {
            throw new EstonianIdException(String.format("Gender must be \"%s\" or \"%s\"", MALE, FEMALE));
        }

        if (dateOfBirth == null || dateOfBirth.isBefore(START_DATE) || dateOfBirth.isAfter(END_DATE)) {
            throw new EstonianIdException(String.format("Birth year must be between %d and %d", START_DATE.getYear(),
                    END_DATE.getYear()));
        }

        if (birthOrderNumber < 0 || birthOrderNumber > 999) {
            throw new EstonianIdException("Birth order number must be between 0 and 999");
        }

        String personalCodeWithoutControlNumber = calculateGenderIdentifier(gender, dateOfBirth)
                + dateOfBirth.format(DATE_FORMATTER).substring(2)
                + String.format("%03d", birthOrderNumber);
        return personalCodeWithoutControlNumber + calculateControlNumber(personalCodeWithoutControlNumber);
    }

    /**
     * Returns null if the date of birth is in the future.
     */
    static Integer calculateAge(LocalDate dateOfBirth) {
        return dateOfBirth != null
                && !dateOfBirth.isAfter(LocalDate.now())
                ? Period.between(dateOfBirth, LocalDate.now()).getYears() : null;
    }

    /**
     * Checks if the control number (last digit) in the personal code is correctly calculated.
     */
    static boolean isControlNumberValid(String personalCode) {
        return Character.getNumericValue(personalCode.charAt(personalCode.length() - 1))
                == calculateControlNumber(personalCode);
    }

    /**
     * Returns the gender defined by the first digit of the personal code.
     * <p>
     * 1, 3, 5 - male.
     * 2, 4, 6 - female.
     */
    static String parseGender(String personalCode) {
        switch (getGenderIdentifier(personalCode)) {
            case 1:
            case 3:
            case 5:
                return MALE;
            default:
                return FEMALE;
        }
    }

    /**
     * Digits 2 through 7 of the personal code show the person's birth date in the format yyddMM.
     * Using the first digit, it is possible to acquire the person's full year of birth.
     * <p>
     * 1, 2 - years 1800-1899.
     * 3, 4 - years 1900-1999.
     * 5, 6 - years 2000-2099.
     */
    static LocalDate parseDateOfBirth(String personalCode) {
        String dateString = personalCode.substring(1, 7);

        switch (getGenderIdentifier(personalCode)) {
            case 1:
            case 2:
                dateString = "18" + dateString;
                break;
            case 3:
            case 4:
                dateString = "19" + dateString;
                break;
            default:
                dateString = "20" + dateString;
        }

        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * The control number (last digit) is calculated by multiplying the first 10 digits of the personal code by
     * the corresponding number in an array of multipliers [1, 2, 3, 4, 5, 6, 7, 8, 9, 1],
     * summing up each result and calculating the remainder of the sum divided by 11.
     * If the resulting control number is 10, the process is repeated with a different array of multipliers
     * [3, 4, 5, 6, 7, 8, 9, 1, 2, 3].
     * If the control number is 10 again, it is set to 0.
     */
    private static int calculateControlNumber(String personalCode) {
        String[] numberArray = personalCode.substring(0, 10).split("");
        List<Integer> numberList = Arrays.stream(numberArray)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        int sum = 0;
        int[] multipliers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};

        for (int i = 0; i < numberList.size(); i++) {
            sum += numberList.get(i) * multipliers[i];
        }

        int parsedControlNumber = sum % 11;

        if (parsedControlNumber == 10) {
            sum = 0;
            multipliers = new int[]{3, 4, 5, 6, 7, 8, 9, 1, 2, 3};

            for (int i = 0; i < numberList.size(); i++) {
                sum += numberList.get(i) * multipliers[i];
            }

            parsedControlNumber = sum % 11;

            if (parsedControlNumber == 10) {
                return 0;
            }
        }

        return parsedControlNumber;
    }

    /**
     * Calculates the gender identifier for the given person's gender and birth year.
     * <p>
     * Male:
     * years 1800-1899 - 1
     * years 1900-1999 - 3
     * years 2000-2099 - 5
     * <p>
     * Female:
     * years 1800-1899 - 2
     * years 1900-1999 - 4
     * years 2000-2099 - 6
     */
    private static int calculateGenderIdentifier(String gender, LocalDate dateOfBirth) {
        int birthYear = dateOfBirth.getYear();

        if (MALE.equalsIgnoreCase(gender)) {
            if (birthYear >= 1800 && birthYear <= 1899) {
                return 1;
            } else if (birthYear >= 1900 && birthYear <= 1999) {
                return 3;
            }

            return 5;
        }

        if (birthYear >= 1800 && birthYear <= 1899) {
            return 2;
        } else if (birthYear >= 1900 && birthYear <= 1999) {
            return 4;
        }

        return 6;
    }

    private static String getRandomGender() {
        return RANDOM.nextInt(2) == 0 ? MALE : FEMALE;
    }

    /**
     * Generates a random date between the earliest and latest possible birth dates (inclusive).
     */
    private static LocalDate generateRandomDateOfBirth() {
        int days = (int) ChronoUnit.DAYS.between(START_DATE, END_DATE);
        return START_DATE.plusDays(RANDOM.nextInt(days + 1));
    }

    private static int generateRandomBirthOrderNumber() {
        return RANDOM.nextInt(1000);
    }

    /**
     * Returns the gender identifier (first digit) from the personal code.
     */
    private static int getGenderIdentifier(String personalCode) {
        return Character.getNumericValue(personalCode.charAt(0));
    }
}
