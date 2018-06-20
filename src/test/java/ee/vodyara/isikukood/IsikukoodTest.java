package ee.vodyara.isikukood;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IsikukoodTest {

    @Test
    public void checkValidPersonalCodes() {
        List<String> validStringCodes = new ArrayList<>();
        validStringCodes.add("17605130008");
        validStringCodes.add("29912120004");
        validStringCodes.add("34503020000");
        validStringCodes.add("47508030046");
        validStringCodes.add("50109130003");
        validStringCodes.add("60302050016");

        for (String personalCode: validStringCodes) {
            Isikukood isikukood = new Isikukood(personalCode);
            assertTrue(isikukood.isValid());
        }

        List<Long> validLongCodes = new ArrayList<>();
        validLongCodes.add(17605130008L);
        validLongCodes.add(29912120004L);
        validLongCodes.add(34503020000L);
        validLongCodes.add(47508030046L);
        validLongCodes.add(50109130003L);
        validLongCodes.add(60302050016L);

        for (Long personalCode: validLongCodes) {
            Isikukood isikukood = new Isikukood(personalCode);
            assertTrue(isikukood.isValid());
        }
    }

    @Test
    public void checkInvalidPersonalCodes() {
        List<String> invalidStringCodes = new ArrayList<>();
        invalidStringCodes.add(null);
        invalidStringCodes.add("");
        invalidStringCodes.add("123");
        invalidStringCodes.add("37605030291");
        invalidStringCodes.add("77605030291");
        invalidStringCodes.add("60319113016");
        invalidStringCodes.add("99999999999");

        for (String personalCode: invalidStringCodes) {
            Isikukood isikukood = new Isikukood(personalCode);
            assertFalse(isikukood.isValid());
        }

        List<Long> invalidLongCodes = new ArrayList<>();
        invalidLongCodes.add(null);
        invalidLongCodes.add(0L);
        invalidLongCodes.add(123L);
        invalidLongCodes.add(37605030291L);
        invalidLongCodes.add(77605030291L);
        invalidLongCodes.add(60319113016L);
        invalidLongCodes.add(99999999999L);

        for (Long personalCode: invalidLongCodes) {
            Isikukood isikukood = new Isikukood(personalCode);
            assertFalse(isikukood.isValid());
        }
    }

    @Test
    public void getGender() {
        List<String> malePersonalCodes = new ArrayList<>();
        malePersonalCodes.add("17605130008");
        malePersonalCodes.add("34503020000");
        malePersonalCodes.add("50109130003");

        for (String personalCode : malePersonalCodes) {
            Isikukood isikukood = new Isikukood(personalCode);
            assertEquals(Gender.MALE, isikukood.getGender());
        }

        List<String> femalePersonalCodes = new ArrayList<>();
        femalePersonalCodes.add("29912120004");
        femalePersonalCodes.add("47508030046");
        femalePersonalCodes.add("60302050016");

        for (String personalCode : femalePersonalCodes) {
            Isikukood isikukood = new Isikukood(personalCode);
            assertEquals(Gender.FEMALE, isikukood.getGender());
        }
    }

    @Test
    public void getDateOfBirth() {
        Isikukood isikukood = new Isikukood("34503020000");
        LocalDate dateOfBirth = isikukood.getDateOfBirth();
        assertEquals(1945, dateOfBirth.getYear());
        assertEquals(3, dateOfBirth.getMonthValue());
        assertEquals(2, dateOfBirth.getDayOfMonth());
        isikukood = new Isikukood("29912120004");
        dateOfBirth = isikukood.getDateOfBirth();
        assertEquals(1899, dateOfBirth.getYear());
        assertEquals(12, dateOfBirth.getMonthValue());
        assertEquals(12, dateOfBirth.getDayOfMonth());
    }

    @Test
    public void getControlNumber() {
        Isikukood isikukood = new Isikukood("34503020000");
        assertEquals(0, isikukood.getControlNumber());
        isikukood = new Isikukood("60302050016");
        assertEquals(6, isikukood.getControlNumber());
    }
}
