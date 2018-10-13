package com.github.vladislavgoltjajev.isikukood;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public final class Isikukood {

    public static final String MALE = "M";
    public static final String FEMALE = "F";
    private static final String PERSONAL_CODE_REGEX = "^[1-6]\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{4}$";
    private static final LocalDate START_DATE = LocalDate.of(1800, Month.JANUARY, 1);
    private static final LocalDate END_DATE = LocalDate.of(2099, Month.DECEMBER, 31);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private String gender;
    private LocalDate dateOfBirth;
    private boolean isValid;

    public Isikukood(String personalCode) {
        if (personalCode != null
                && !personalCode.isEmpty()
                && personalCode.matches(PERSONAL_CODE_REGEX)) {
            LocalDate dateOfBirth = parseDateOfBirth(personalCode);

            if (dateOfBirth != null) {
                int lastDigit = Character.getNumericValue(personalCode.charAt(personalCode.length() - 1));

                if (lastDigit == calculateControlNumber(personalCode)) {
                    isValid = true;
                    this.gender = parseGender(personalCode);
                    this.dateOfBirth = dateOfBirth;
                }
            }
        } else {
            isValid = false;
        }
    }

    public static String generatePersonalCode(String gender, LocalDate dateOfBirth, int birthOrderNumber) {
        if (!MALE.equalsIgnoreCase(gender) && !FEMALE.equalsIgnoreCase(gender)) {
            throw new IsikukoodException(String.format("Gender must be \"%s\" or \"%s\"", MALE, FEMALE));
        }

        if (dateOfBirth == null || dateOfBirth.isBefore(START_DATE) || dateOfBirth.isAfter(END_DATE)) {
            throw new IsikukoodException(String.format("Birth year must be between %d and %d", START_DATE.getYear(),
                    END_DATE.getYear()));
        }

        if (birthOrderNumber < 0 || birthOrderNumber > 999) {
            throw new IsikukoodException("Birth order number must be between 0 and 999");
        }

        int genderIdentifier = calculateGenderIdentifier(gender, dateOfBirth);
        String formattedDateOfBirth = dateOfBirth.format(dateFormatter).substring(2);
        String personalCode = genderIdentifier + formattedDateOfBirth + String.format("%03d", birthOrderNumber);
        return personalCode + calculateControlNumber(personalCode);
    }

    public static String generatePersonalCode(String gender, LocalDate dateOfBirth) {
        return generatePersonalCode(gender, dateOfBirth, generateRandomBirthOrderNumber());
    }

    public static String generateRandomPersonalCode() {
        return generatePersonalCode(generateRandomGender(),
                generateRandomDateOfBirth(),
                generateRandomBirthOrderNumber());
    }

    public boolean isValid() {
        return isValid;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Integer getAge() {
        if (!isValid) {
            return null;
        }

        LocalDate dateOfBirth = getDateOfBirth();
        return !dateOfBirth.isAfter(LocalDate.now()) ? Period.between(dateOfBirth, LocalDate.now()).getYears() : null;
    }

    /**
     * The first digit of the personal code shows the person's gender.
     * 1, 3, 5- male.
     * 2, 4, 6 - female.
     */
    private String parseGender(String personalCode) {
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
     * 1, 2 - years 1800-1899.
     * 3, 4 - years 1900-1999.
     * 5, 6 - years 2000-2099.
     */
    private LocalDate parseDateOfBirth(String personalCode) {
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
            return LocalDate.parse(dateString, dateFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * The last digit of the personal code shows the control number.
     * The control number is calculated by multiplying the first 10 digits of the personal code by the corresponding
     * number in an array of multipliers [1, 2, 3, 4, 5, 6, 7, 8, 9, 1], summing up each result and calculating
     * the remainder of the sum divided by 11.
     * If the control number is 10, the process is repeated with a different array of multipliers
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
                parsedControlNumber = 0;
            }
        }

        return parsedControlNumber;
    }

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

    private static String generateRandomGender() {
        return new Random().nextInt(2) == 0 ? MALE : FEMALE;
    }

    private static LocalDate generateRandomDateOfBirth() {
        int days = (int) ChronoUnit.DAYS.between(START_DATE, END_DATE);
        return START_DATE.plusDays(new Random().nextInt(days + 1));
    }

    private static int generateRandomBirthOrderNumber() {
        return new Random().nextInt(1000);
    }

    private int getGenderIdentifier(String personalCode) {
        return Character.getNumericValue(personalCode.charAt(0));
    }
}
