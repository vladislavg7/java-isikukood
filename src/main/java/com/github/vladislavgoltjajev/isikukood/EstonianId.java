package com.github.vladislavgoltjajev.isikukood;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public final class EstonianId {

    public static final String MALE = "M";
    public static final String FEMALE = "F";

    private boolean valid;
    private String gender;
    private LocalDate dateOfBirth;

    public EstonianId(String personalCode) {
        if (EstonianIdUtils.initialValidationPassed(personalCode)) {
            LocalDate parsedDateOfBirth = EstonianIdUtils.parseDateOfBirth(personalCode);

            if (parsedDateOfBirth != null && EstonianIdUtils.isControlNumberValid(personalCode)) {
                valid = true;
                gender = EstonianIdUtils.parseGender(personalCode);
                dateOfBirth = parsedDateOfBirth;
            }
        }
    }

    public static String generateRandomPersonalCode() {
        return EstonianIdUtils.generateRandomPersonalCode();
    }

    public static String generatePersonalCode(String gender, LocalDate dateOfBirth) throws EstonianIdException {
        return EstonianIdUtils.generatePersonalCode(gender, dateOfBirth);
    }

    public static String generatePersonalCode(String gender, LocalDate dateOfBirth, int birthOrderNumber)
            throws EstonianIdException {
        return EstonianIdUtils.generatePersonalCode(gender, dateOfBirth, birthOrderNumber);
    }

    public Integer getAge() {
        return EstonianIdUtils.calculateAge(dateOfBirth);
    }
}
