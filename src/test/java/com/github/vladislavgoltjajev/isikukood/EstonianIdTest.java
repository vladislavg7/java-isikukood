package com.github.vladislavgoltjajev.isikukood;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static com.github.vladislavgoltjajev.isikukood.EstonianId.FEMALE;
import static com.github.vladislavgoltjajev.isikukood.EstonianId.MALE;
import static org.junit.Assert.*;

public class EstonianIdTest {

    @Test
    public void checkValidPersonalCode() {
        List<String> validPersonalCodes = new ArrayList<>();
        validPersonalCodes.add("17605130008");
        validPersonalCodes.add("29912120004");
        validPersonalCodes.add("34503020000");
        validPersonalCodes.add("47508030046");
        validPersonalCodes.add("50109130003");
        validPersonalCodes.add("60302050016");
        validPersonalCodes.add("60002290003");
        validPersonalCodes.add("39912310174");
        validPersonalCodes.add("50002290046");

        for (String personalCode : validPersonalCodes) {
            EstonianId estonianId = new EstonianId(personalCode);
            assertTrue(estonianId.isValid());
        }
    }

    @Test
    public void checkInvalidPersonalCode() {
        List<String> invalidPersonalCodes = new ArrayList<>();
        invalidPersonalCodes.add(null);
        invalidPersonalCodes.add("");
        invalidPersonalCodes.add("123");
        invalidPersonalCodes.add("37605030291");
        invalidPersonalCodes.add("77605030291");
        invalidPersonalCodes.add("60319113016");
        invalidPersonalCodes.add("99999999999");
        invalidPersonalCodes.add("39912310173");
        invalidPersonalCodes.add("39002310001");
        invalidPersonalCodes.add("50102290005");

        for (String personalCode : invalidPersonalCodes) {
            EstonianId estonianId = new EstonianId(personalCode);
            assertFalse(estonianId.isValid());
        }
    }

    @Test
    public void getGender() {
        List<String> malePersonalCodes = new ArrayList<>();
        malePersonalCodes.add("17605130008");
        malePersonalCodes.add("34503020000");
        malePersonalCodes.add("50109130003");

        for (String personalCode : malePersonalCodes) {
            EstonianId estonianId = new EstonianId(personalCode);
            assertEquals(MALE, estonianId.getGender());
        }

        List<String> femalePersonalCodes = new ArrayList<>();
        femalePersonalCodes.add("29912120004");
        femalePersonalCodes.add("47508030046");
        femalePersonalCodes.add("60302050016");

        for (String personalCode : femalePersonalCodes) {
            EstonianId estonianId = new EstonianId(personalCode);
            assertEquals(FEMALE, estonianId.getGender());
        }

        EstonianId estonianId = new EstonianId("123");
        assertNull(estonianId.getGender());
    }

    @Test
    public void getDateOfBirth() {
        EstonianId estonianId = new EstonianId("34503020000");
        LocalDate dateOfBirth = estonianId.getDateOfBirth();
        assertNotNull(dateOfBirth);
        assertEquals(1945, dateOfBirth.getYear());
        assertEquals(Month.MARCH, dateOfBirth.getMonth());
        assertEquals(2, dateOfBirth.getDayOfMonth());

        estonianId = new EstonianId("29912120004");
        dateOfBirth = estonianId.getDateOfBirth();
        assertNotNull(dateOfBirth);
        assertEquals(1899, dateOfBirth.getYear());
        assertEquals(Month.DECEMBER, dateOfBirth.getMonth());
        assertEquals(12, dateOfBirth.getDayOfMonth());

        estonianId = new EstonianId("123");
        assertNull(estonianId.getDateOfBirth());
    }

    @Test
    public void getAge() {
        EstonianId estonianId = new EstonianId("34503020000");
        Integer age = estonianId.getAge();
        assertNotNull(age);
        assertTrue(age >= 73);

        estonianId = new EstonianId("60302050016");
        age = estonianId.getAge();
        assertNotNull(age);
        assertTrue(age >= 15);

        estonianId = new EstonianId("123");
        assertNull(estonianId.getAge());
    }

    @Test
    public void generatePersonalCode() throws EstonianIdException {
        String personalCode = EstonianId.generatePersonalCode(MALE, LocalDate.of(2000, Month.JANUARY, 1));
        assertTrue(new EstonianId(personalCode).isValid());

        personalCode = EstonianId.generatePersonalCode(FEMALE, LocalDate.of(1986, Month.FEBRUARY, 28));
        assertTrue(new EstonianId(personalCode).isValid());

        personalCode = EstonianId.generatePersonalCode(FEMALE, LocalDate.of(2000, Month.FEBRUARY, 29));
        assertTrue(new EstonianId(personalCode).isValid());

        personalCode = EstonianId.generatePersonalCode(MALE, LocalDate.of(1875, Month.APRIL, 20));
        assertTrue(new EstonianId(personalCode).isValid());

        personalCode = EstonianId.generatePersonalCode(FEMALE, LocalDate.of(2048, Month.JULY, 18));
        assertTrue(new EstonianId(personalCode).isValid());

        personalCode = EstonianId.generatePersonalCode(FEMALE, LocalDate.of(2048, Month.JULY, 18), 1);
        assertTrue(new EstonianId(personalCode).isValid());
    }

    @Test
    public void generateInvalidPersonalCode() {
        try {
            EstonianId.generatePersonalCode(null, null);
            fail();
        } catch (EstonianIdException e) {
            assertTrue(e.getMessage().contains("Gender"));
        }

        try {
            EstonianId.generatePersonalCode("A", null);
            fail();
        } catch (EstonianIdException e) {
            assertTrue(e.getMessage().contains("Gender"));
        }

        try {
            EstonianId.generatePersonalCode("A", LocalDate.of(1799, Month.JANUARY, 1));
            fail();
        } catch (EstonianIdException e) {
            assertTrue(e.getMessage().contains("Gender"));
        }

        try {
            EstonianId.generatePersonalCode(MALE, null);
            fail();
        } catch (EstonianIdException e) {
            assertTrue(e.getMessage().contains("year"));
        }

        try {
            EstonianId.generatePersonalCode(MALE, LocalDate.of(1799, Month.DECEMBER, 31));
            fail();
        } catch (EstonianIdException e) {
            assertTrue(e.getMessage().contains("year"));
        }

        try {
            EstonianId.generatePersonalCode(MALE, LocalDate.of(2000, Month.DECEMBER, 31), 1000);
            fail();
        } catch (EstonianIdException e) {
            assertTrue(e.getMessage().contains("order"));
        }

        try {
            EstonianId.generatePersonalCode(MALE, LocalDate.of(2000, Month.DECEMBER, 31), -1);
            fail();
        } catch (EstonianIdException e) {
            assertTrue(e.getMessage().contains("order"));
        }
    }

    @Test
    public void generateRandomPersonalCode() {
        for (int i = 0; i < 10000; i++) {
            String randomPersonalCode = EstonianId.generateRandomPersonalCode();
            EstonianId estonianId = new EstonianId(randomPersonalCode);
            assertTrue(estonianId.isValid());
            assertNotNull(estonianId.getGender());
            assertNotNull(estonianId.getDateOfBirth());
        }
    }
}
