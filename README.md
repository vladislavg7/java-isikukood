:exclamation::exclamation::exclamation:
## Library is no longer supported
Development moved to [this](https://github.com/vladislavgoltjajev/java-personal-code) repo.  
:exclamation::exclamation::exclamation:

# java-isikukood
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://github.com/vladislavgoltjajev/java-isikukood/blob/master/LICENSE) 

Extract personal data from and generate Estonian personal identification codes (isikukood) with this lightweight library.

## Import
The library is available on Maven Central and JCenter.  
JDK 8 or higher is required for the library to work.
### Gradle
```groovy
dependencies {
    implementation 'com.github.vladislavgoltjajev:java-isikukood:2.1'
}
```
### Maven
```xml
<dependency>
    <groupId>com.github.vladislavgoltjajev</groupId>
    <artifactId>java-isikukood</artifactId>
    <version>2.1</version>
</dependency>
```

## Usage
```java
public class Test {

    public static void main(String[] args) {
        EstonianId estonianId = new EstonianId("47508030046");
        boolean isValid = estonianId.isValid();                 // true
        String gender = estonianId.getGender();                 // F
        LocalDate dateOfBirth = estonianId.getDateOfBirth();    // 1975-08-03
        Integer age = estonianId.getAge();                      // 43

        EstonianId invalidEstonianId = new EstonianId("123");
        isValid = invalidEstonianId.isValid();                  // false
        gender = invalidEstonianId.getGender();                 // null
        dateOfBirth = invalidEstonianId.getDateOfBirth();       // null
        age = invalidEstonianId.getAge();                       // null

        String personalCode = EstonianId.generateRandomPersonalCode(); // 35207049817

        try {
            gender = EstonianId.MALE;
            dateOfBirth = LocalDate.of(1984, 3, 15);
            personalCode = EstonianId.generatePersonalCode(gender, dateOfBirth);           // 38403153949
            personalCode = EstonianId.generatePersonalCode(gender, dateOfBirth, 7);        // 38403150076
            personalCode = EstonianId.generatePersonalCode("A", LocalDate.of(1799, 1, 1)); // Throws exception.
        } catch (EstonianIdException e) {
            // Handle exception.
        }
    }
}
```

## API
### Field summary
<table class="table1">
    <tr>
        <th>Modifier and type</th>
        <th>Field</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>static String</td>
        <td>FEMALE</td>
        <td>Female gender classifier "F".</td>
    </tr>
    <tr>
        <td>static String</td>
        <td>MALE</td>
        <td>Male gender classifier "M".</td>
    </tr>
</table>

### Constructor summary
<table class="table1">
    <tr>
        <th>Constructor</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>EstonianId(String personalCode)</td>
        <td>Constructs a new EstonianId object using the specified personal code.</td>
    </tr>
</table>

### Method summary
<table class="table1">
    <tr>
        <th>Modifier and type</th>
        <th>Method</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>static String</td>
        <td>generatePersonalCode(String gender, LocalDate dateOfBirth)</td>
        <td>Generates a personal code using the specified gender and date of birth. Throws an exception if the gender is not "M" or "F" or the birth year is before 1800 or after 2099.</td>
    </tr>
    <tr>
        <td>static String</td>
        <td>generatePersonalCode(String gender, LocalDate dateOfBirth, int birthOrderNumber)</td>
        <td>Generates a personal code using the specified gender, date of birth and birth order number. Throws an exception if the gender is not "M" or "F", the birth year is before 1800 or after 2099 or the birth order number is less than 0 or more than 999.</td>
    </tr>
    <tr>
        <td>static String</td>
        <td>generateRandomPersonalCode()</td>
        <td>Generates a random personal code.</td>
    </tr>
    <tr>
        <td>Integer</td>
        <td>getAge()</td>
        <td>Calculates the person's age in years. Returns null if the personal code is invalid or the date of birth is in the future.</td>
    </tr>
    <tr>
        <td>LocalDate</td>
        <td>getDateOfBirth()</td>
        <td>Returns the person's date of birth. Returns null if the personal code is invalid.</td>
    </tr>
    <tr>
        <td>String</td>
        <td>getGender()</td>
        <td>Returns the person's gender ("M" or "F"). Returns null if the personal code is invalid.</td>
    </tr>
    <tr>
        <td>boolean</td>
        <td>isValid()</td>
        <td>Returns whether or not the personal code is valid.</td>
    </tr>
</table>

## Buy me a beer? :beer:
Please [donate](https://www.paypal.me/VladislavGoltjajev) if you like my work.
