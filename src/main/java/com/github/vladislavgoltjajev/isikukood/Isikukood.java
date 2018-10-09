package com.github.vladislavgoltjajev.isikukood;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Isikukood {

    private String gender;
    private LocalDate dateOfBirth;
    private boolean isValid;

    public Isikukood(String personalCode) {
        if (personalCode != null
                && !personalCode.isEmpty()
                && personalCode.matches("^[1-6]\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{4}$")) {
            gender = parseGender(personalCode);
            dateOfBirth = parseDateOfBirth(personalCode);

            if (dateOfBirth == null) {
                isValid = false;
            } else {
                isValid = hasCorrectControlNumber(personalCode);
            }
        } else {
            isValid = false;
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public String getGender() {
        return isValid ? gender : null;
    }

    public LocalDate getDateOfBirth() {
        return isValid ? dateOfBirth : null;
    }

    public Integer getAge() {
        LocalDate dateOfBirth = getDateOfBirth();
        return isValid && dateOfBirth != null && !dateOfBirth.isAfter(LocalDate.now())
                ? Period.between(dateOfBirth, LocalDate.now()).getYears() : null;
    }

    /**
     * The first digit of the personal code shows the person's gender.
     * 1, 3, 5 - male.
     * 2, 4, 6 - female.
     */
    private String parseGender(String personalCode) {
        int genderIdentifier = Character.getNumericValue(personalCode.charAt(0));

        switch (genderIdentifier) {
            case 1:
            case 3:
            case 5:
                return "M";
            case 2:
            case 4:
            case 6:
                return "F";
            default:
                return null;
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
        int genderIdentifier = Character.getNumericValue(personalCode.charAt(0));

        switch (genderIdentifier) {
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            return LocalDate.parse(dateString, formatter);
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
    private boolean hasCorrectControlNumber(String personalCode) {
        String[] numberArray = personalCode.substring(0, personalCode.length() - 1).split("");
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

        return parsedControlNumber == Character.getNumericValue(personalCode.charAt(personalCode.length() - 1));
    }
}
