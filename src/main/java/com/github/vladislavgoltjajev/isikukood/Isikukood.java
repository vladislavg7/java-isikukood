package com.github.vladislavgoltjajev.isikukood;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Isikukood {

    private String personalCode;
    private String gender;
    private LocalDate dateOfBirth;
    private Integer controlNumber;

    public Isikukood(String personalCode) {
        this.personalCode = personalCode;
        parseAndSetData();
    }

    public Boolean isValid() {
        return gender != null
                && dateOfBirth != null
                && controlNumber != null;
    }

    public String getGender() {
        validate();
        return gender;
    }

    public Integer getAge() {
        validate();
        return Period.between(getDateOfBirth(), LocalDate.now()).getYears();
    }

    public LocalDate getDateOfBirth() {
        validate();
        return dateOfBirth;
    }

    public Integer getControlNumber() {
        validate();
        return controlNumber;
    }

    private void parseAndSetGender() {
        int genderIdentifier = Character.getNumericValue(personalCode.charAt(0));

        switch (genderIdentifier) {
            case 1:
            case 3:
            case 5:
                gender = "M";
                break;
            default:
                gender = "F";
        }
    }

    private void parseAndSetControlNumber() {
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

        if (parsedControlNumber == Character.getNumericValue(personalCode.charAt(personalCode.length() - 1))) {
            controlNumber = parsedControlNumber;
        }
    }

    private void parseAndSetDateOfBirth() {
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
            dateOfBirth = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            // Do nothing
        }
    }

    private void parseAndSetData() {
        if (personalCode != null
                && !personalCode.isEmpty()
                && personalCode.matches("^[1-6]\\d{2}(0[1-9]|1[0-2])([0-2]\\d|3[01])\\d{4}$")) {
            parseAndSetGender();
            parseAndSetDateOfBirth();

            // No need to calculate the control number if the personal code is already invalid
            if (dateOfBirth != null) {
                parseAndSetControlNumber();
            }
        }
    }

    private void validate() {
        if (!isValid()) {
            throw new IsikukoodException("Invalid Estonian personal identification code: " + personalCode);
        }
    }
}
