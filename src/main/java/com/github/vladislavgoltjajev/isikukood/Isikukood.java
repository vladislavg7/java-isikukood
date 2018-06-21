package com.github.vladislavgoltjajev.isikukood;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Isikukood {

    private final String personalCode;

    public Isikukood(String personalCode) {
        this.personalCode = personalCode;
    }

    public boolean isValid() {
        if (personalCode == null
                || personalCode.isEmpty()
                || !personalCode.matches("^\\d{11}$")) {
            return false;
        }

        try {
            parseDateOfBirth();
        } catch (DateTimeParseException e) {
            return false;
        }

        int lastNumber = Character.getNumericValue(personalCode.charAt(personalCode.length() - 1));
        return parseControlNumber() == lastNumber;
    }

    public LocalDate getDateOfBirth() {
        validatePersonalCode();
        return parseDateOfBirth();
    }

    /**
     * Returns the gender from the identification code.
     * The gender is based on the first number of the code:
     * odd  - male
     * even - female
     *
     * @return Gender.
     */
    public String getGender() {
        validatePersonalCode();
        int genderIdentifier = Character.getNumericValue(personalCode.charAt(0));

        if (genderIdentifier % 2 == 0) {
            return "F";
        }

        return "M";
    }

    public int getAge() {
        validatePersonalCode();
        return Period.between(parseDateOfBirth(), LocalDate.now()).getYears();
    }

    public int getControlNumber() {
        validatePersonalCode();
        return parseControlNumber();
    }

    /**
     * Calculates the control number for the first 10 numbers of the identification code.
     * The calculation involves multiplying each of those numbers with its corresponding multiplier,
     * summing them up and calculating the modulo 11 value.
     * If the value is 10, the operation is repeated with a different set of multipliers.
     * If the value is 10 again, the control number is 0.
     *
     * @return Control number.
     */
    private int parseControlNumber() {
        String[] numberArray = personalCode.substring(0, personalCode.length() - 1).split("");
        List<Integer> numberList = Arrays.stream(numberArray)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        int sum = 0;
        int controlNumber;
        int[] multipliers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};

        for (int i = 0; i < numberList.size(); i++) {
            sum += numberList.get(i) * multipliers[i];
        }

        controlNumber = sum % 11;

        if (controlNumber == 10) {
            sum = 0;
            multipliers = new int[]{3, 4, 5, 6, 7, 8, 9, 1, 2, 3};

            for (int i = 0; i < numberList.size(); i++) {
                sum += numberList.get(i) * multipliers[i];
            }

            controlNumber = sum % 11;
        }

        if (controlNumber == 10) {
            return 0;
        }

        return controlNumber;
    }

    /**
     * Calculates the date of birth from the identification code.
     * The birth year is based on the first number of the first number of the code:
     * 1 or 2 - 1800-1899
     * 3 or 4 - 1900-1999
     * 5 or 6 - 2000-2099
     * Higher numbers will most likely indicate the years 2100-2199.
     *
     * @return Date of birth.
     */
    private LocalDate parseDateOfBirth() {
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
            case 5:
            case 6:
                dateString = "20" + dateString;
                break;
            default:
                dateString = "21" + dateString;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dateString, formatter);
    }

    private void validatePersonalCode() {
        if (!isValid()) {
            throw new IsikukoodException("Invalid Estonian personal identification code");
        }
    }
}
