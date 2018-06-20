package ee.vladislavg.isikukood;

public enum Gender {
    MALE("M"),
    FEMALE("F");

    private String genderCode;

    Gender(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGenderCode() {
        return genderCode;
    }
}
