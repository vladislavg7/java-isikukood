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
    private final Validator validator = new Validator();

    public Isikukood(String personalCode) {
        this.personalCode = personalCode;
    }

    public boolean isValid() {
        return validator.isValid();
    }

    public LocalDate getDateOfBirth() {
        validator.validateAllExceptControlNumber();
        return parseDateOfBirth();
    }

    public String getGender() {
        validator.validateAllExceptControlNumber();
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
                throw new IsikukoodException(Validator.INVALID_CODE_ERROR);
        }
    }

    public int getAge() {
        validator.validateAllExceptControlNumber();
        return Period.between(parseDateOfBirth(), LocalDate.now()).getYears();
    }

    public int getControlNumber() {
        validator.validateAllExceptControlNumber();
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
                throw new IsikukoodException(Validator.INVALID_CODE_ERROR);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dateString, formatter);
    }

    private final class Validator {

        static final String INVALID_CODE_ERROR = "Invalid Estonian personal identification code";

        boolean isValid() {
            return isValidExceptControlNumber() && hasValidControlNumber();
        }

        boolean isValidExceptControlNumber() {
            return isNotNullAndHasValidContent()
                    && hasValidGender()
                    && hasValidDateOfBirth();
        }

        boolean isNotNullAndHasValidContent() {
            return personalCode != null && !personalCode.isEmpty() && personalCode.matches("^\\d{11}$");
        }

        boolean hasValidGender() {
            return personalCode.matches("^[1-6]\\d{10}$");
        }

        boolean hasValidDateOfBirth() {
            try {
                parseDateOfBirth();
            } catch (DateTimeParseException e) {
                return false;
            }

            return true;
        }

        boolean hasValidControlNumber() {
            int lastNumber = Character.getNumericValue(personalCode.charAt(personalCode.length() - 1));
            return getControlNumber() == lastNumber;
        }

        void validateAllExceptControlNumber() {
            if (!isValidExceptControlNumber()) {
                throw new IsikukoodException(INVALID_CODE_ERROR);
            }
        }
    }
}
