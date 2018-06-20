package ee.vladislavg.isikukood;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class Utils {

    private static final String PERSONAL_CODE_REGEX = "^[1-6][0-9]{2}[0-1][1-9][0-3][1-9]\\d{4}$";
    private static final List<Integer> MULTIPLIERS_1 = new ArrayList<>();
    private static final List<Integer> MULTIPLIERS_2 = new ArrayList<>();
    private static final String INVALID_PERSONAL_CODE_MESSAGE = "Invalid Estonian personal identification code";

    static {
        MULTIPLIERS_1.add(1);
        MULTIPLIERS_1.add(2);
        MULTIPLIERS_1.add(3);
        MULTIPLIERS_1.add(4);
        MULTIPLIERS_1.add(5);
        MULTIPLIERS_1.add(6);
        MULTIPLIERS_1.add(7);
        MULTIPLIERS_1.add(8);
        MULTIPLIERS_1.add(9);
        MULTIPLIERS_1.add(1);
        MULTIPLIERS_2.add(3);
        MULTIPLIERS_2.add(4);
        MULTIPLIERS_2.add(5);
        MULTIPLIERS_2.add(6);
        MULTIPLIERS_2.add(7);
        MULTIPLIERS_2.add(8);
        MULTIPLIERS_2.add(9);
        MULTIPLIERS_2.add(1);
        MULTIPLIERS_2.add(2);
        MULTIPLIERS_2.add(3);
    }

    static boolean isValid(String personalCode) {
        if (personalCode == null
                || personalCode.isEmpty()
                || !personalCode.matches(PERSONAL_CODE_REGEX)) {
            return false;
        }

        try {
            parseDateOfBirth(personalCode);
        } catch (DateTimeParseException e) {
            return false;
        }

        int lastNumber = Character.getNumericValue(personalCode.charAt(personalCode.length() - 1));
        return parseControlNumber(personalCode) == lastNumber;
    }

    static LocalDate getDateOfBirth(String personalCode) {
        validatePersonalCode(personalCode);
        return parseDateOfBirth(personalCode);
    }

    static int getAge(String personalCode) {
        validatePersonalCode(personalCode);
        return Period.between(parseDateOfBirth(personalCode), LocalDate.now()).getYears();
    }

    static Gender getGender(String personalCode) {
        validatePersonalCode(personalCode);
        int genderIdentifier = Character.getNumericValue(personalCode.charAt(0));

        switch (genderIdentifier) {
            case 1:
            case 3:
            case 5:
                return Gender.MALE;
            case 2:
            case 4:
            case 6:
                return Gender.FEMALE;
            default:
                throw new IsikukoodException(INVALID_PERSONAL_CODE_MESSAGE);
        }
    }

    static int getControlNumber(String personalCode) {
        validatePersonalCode(personalCode);
        return parseControlNumber(personalCode);
    }

    private static int parseControlNumber(String personalCode) {
        String[] numberArray = personalCode.substring(0, personalCode.length() - 1).split("");
        List<Integer> numberList = Arrays.stream(numberArray)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        int sum = 0;
        int controlNumber;

        for (int i = 0; i < numberList.size(); i++) {
            sum += numberList.get(i) * MULTIPLIERS_1.get(i);
        }

        controlNumber = sum % 11;

        if (controlNumber == 10) {
            sum = 0;

            for (int i = 0; i < numberList.size(); i++) {
                sum += numberList.get(i) * MULTIPLIERS_2.get(i);
            }

            controlNumber = sum % 11;
        }

        if (controlNumber == 10) {
            return 0;
        }

        return controlNumber;
    }

    private static LocalDate parseDateOfBirth(String personalCode) {
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
                throw new IsikukoodException(INVALID_PERSONAL_CODE_MESSAGE);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dateString, formatter);
    }

    private static void validatePersonalCode(String personalCode) {
        if (!isValid(personalCode)) {
            throw new IsikukoodException(INVALID_PERSONAL_CODE_MESSAGE);
        }
    }
}
