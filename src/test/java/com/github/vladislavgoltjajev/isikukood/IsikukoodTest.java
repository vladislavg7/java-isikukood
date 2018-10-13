package com.github.vladislavgoltjajev.isikukood;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IsikukoodTest {

    @Test
    public void checkValidPersonalCode() {
        List<String> validStringCodes = new ArrayList<>();
        validStringCodes.add("17605130008");
        validStringCodes.add("29912120004");
        validStringCodes.add("34503020000");
        validStringCodes.add("47508030046");
        validStringCodes.add("50109130003");
        validStringCodes.add("60302050016");
        validStringCodes.add("60002290003");
        validStringCodes.add("39912310174");

        for (String personalCode : validStringCodes) {
            Isikukood isikukood = new Isikukood(personalCode);
            assertTrue(isikukood.isValid());
        }
    }

    @Test
    public void checkInvalidPersonalCode() {
        List<String> invalidStringCodes = new ArrayList<>();
        invalidStringCodes.add(null);
        invalidStringCodes.add("");
        invalidStringCodes.add("123");
        invalidStringCodes.add("37605030291");
        invalidStringCodes.add("77605030291");
        invalidStringCodes.add("60319113016");
        invalidStringCodes.add("99999999999");
        invalidStringCodes.add("39912310173");

        for (String personalCode : invalidStringCodes) {
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
            assertEquals(Isikukood.MALE, isikukood.getGender());
        }

        List<String> femalePersonalCodes = new ArrayList<>();
        femalePersonalCodes.add("29912120004");
        femalePersonalCodes.add("47508030046");
        femalePersonalCodes.add("60302050016");

        for (String personalCode : femalePersonalCodes) {
            Isikukood isikukood = new Isikukood(personalCode);
            assertEquals(Isikukood.FEMALE, isikukood.getGender());
        }

        Isikukood isikukood = new Isikukood("123");
        assertNull(isikukood.getGender());
    }

    @Test
    public void getDateOfBirth() {
        Isikukood isikukood = new Isikukood("34503020000");
        LocalDate dateOfBirth = isikukood.getDateOfBirth();
        assertNotNull(dateOfBirth);
        assertEquals(1945, dateOfBirth.getYear());
        assertEquals(Month.MARCH, dateOfBirth.getMonth());
        assertEquals(2, dateOfBirth.getDayOfMonth());
        isikukood = new Isikukood("29912120004");
        dateOfBirth = isikukood.getDateOfBirth();
        assertNotNull(dateOfBirth);
        assertEquals(1899, dateOfBirth.getYear());
        assertEquals(Month.DECEMBER, dateOfBirth.getMonth());
        assertEquals(12, dateOfBirth.getDayOfMonth());
        isikukood = new Isikukood("123");
        assertNull(isikukood.getDateOfBirth());
    }

    @Test
    public void getAge() {
        Isikukood isikukood = new Isikukood("34503020000");
        assertNotNull(isikukood.getAge());
        assertTrue(isikukood.getAge() >= 73);
        isikukood = new Isikukood("60302050016");
        assertNotNull(isikukood.getAge());
        assertTrue(isikukood.getAge() >= 15);
        isikukood = new Isikukood("123");
        assertNull(isikukood.getAge());
    }

    @Test
    public void generatePersonalCode() {
        String personalCode = Isikukood.generatePersonalCode(Isikukood.MALE, LocalDate.of(2000, Month.JANUARY, 1));
        assertTrue(new Isikukood(personalCode).isValid());
        personalCode = Isikukood.generatePersonalCode(Isikukood.FEMALE, LocalDate.of(1986, Month.FEBRUARY, 28));
        assertTrue(new Isikukood(personalCode).isValid());
        personalCode = Isikukood.generatePersonalCode(Isikukood.MALE, LocalDate.of(1875, Month.APRIL, 20));
        assertTrue(new Isikukood(personalCode).isValid());
        personalCode = Isikukood.generatePersonalCode(Isikukood.FEMALE, LocalDate.of(2048, Month.JULY, 18));
        assertTrue(new Isikukood(personalCode).isValid());
        personalCode = Isikukood.generatePersonalCode(Isikukood.FEMALE, LocalDate.of(2048, Month.JULY, 18), 1);
        assertTrue(new Isikukood(personalCode).isValid());
    }

    @Test
    public void generateInvalidPersonalCode() {
        try {
            Isikukood.generatePersonalCode(null, null);
            fail();
        } catch (IsikukoodException e) {
            assertTrue(e.getMessage().contains("Gender"));
        }

        try {
            Isikukood.generatePersonalCode("A", null);
            fail();
        } catch (IsikukoodException e) {
            assertTrue(e.getMessage().contains("Gender"));
        }

        try {
            Isikukood.generatePersonalCode("A", LocalDate.of(1799, Month.JANUARY, 1));
            fail();
        } catch (IsikukoodException e) {
            assertTrue(e.getMessage().contains("Gender"));
        }

        try {
            Isikukood.generatePersonalCode(Isikukood.MALE, null);
            fail();
        } catch (IsikukoodException e) {
            assertTrue(e.getMessage().contains("year"));
        }

        try {
            Isikukood.generatePersonalCode(Isikukood.MALE, LocalDate.of(1799, Month.DECEMBER, 31));
            fail();
        } catch (IsikukoodException e) {
            assertTrue(e.getMessage().contains("year"));
        }

        try {
            Isikukood.generatePersonalCode(Isikukood.MALE, LocalDate.of(2000, Month.DECEMBER, 31), 1000);
            fail();
        } catch (IsikukoodException e) {
            assertTrue(e.getMessage().contains("order"));
        }

        try {
            Isikukood.generatePersonalCode(Isikukood.MALE, LocalDate.of(2000, Month.DECEMBER, 31), -1);
            fail();
        } catch (IsikukoodException e) {
            assertTrue(e.getMessage().contains("order"));
        }
    }

    @Test
    public void generateRandomPersonalCode() {
        for (int i = 0; i < 100; i++) {
            String randomPersonalCode = Isikukood.generateRandomPersonalCode();
            Isikukood isikukood = new Isikukood(randomPersonalCode);
            assertTrue(isikukood.isValid());
        }
    }
}
