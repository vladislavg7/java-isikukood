package ee.vodyara.isikukood;

import java.time.LocalDate;

public final class Isikukood {

    private final String personalCode;

    public Isikukood(Long personalCode) {
        this.personalCode = String.valueOf(personalCode);
    }

    public Isikukood(String personalCode) {
        this.personalCode = personalCode;
    }

    public boolean isValid() {
        return Utils.isValid(personalCode);
    }

    public LocalDate getDateOfBirth() {
        return Utils.getDateOfBirth(personalCode);
    }

    public Gender getGender() {
        return Utils.getGender(personalCode);
    }

    public int getAge() {
        return Utils.getAge(personalCode);
    }

    public int getControlNumber() {
        return Utils.getControlNumber(personalCode);
    }

    public static String generatePersonalCode(String gender, LocalDate birthDate) {
        return null;
    }

    public static String generatePersonalCode(Gender gender, LocalDate birthDate) {
        return null;
    }
}
