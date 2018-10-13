# java-isikukood [![Download](https://api.bintray.com/packages/vladislavg/java-isikukood/java-isikukood/images/download.svg)](https://bintray.com/vladislavg/java-isikukood/java-isikukood/_latestVersion)

Extract personal data from and generate Estonian personal identification codes (isikukood) with this lightweight library.

## Import
The library is available on Maven Central and JCenter.
### Gradle
```groovy
dependencies {
    compile('com.github.vladislavgoltjajev:java-isikukood:1.7')
}
```
### Maven
```xml
<dependency>
    <groupId>com.github.vladislavgoltjajev</groupId>
    <artifactId>java-isikukood</artifactId>
    <version>1.7</version>
</dependency>
```

## Usage
The library requires at least Java 8.
```java
public class Test {
    
    public static void main(String[] args) {
        Isikukood personalCode = new Isikukood("47508030046");
        boolean isValid = personalCode.isValid();               // true
        String gender = personalCode.getGender();               // F
        LocalDate dateOfBirth = personalCode.getDateOfBirth();  // 1975-08-03
        Integer age = personalCode.getAge();                    // 43
        
        Isikukood invalidPersonalCode = new Isikukood("123");
        isValid = invalidPersonalCode.isValid();                // false
        gender = invalidPersonalCode.getGender();               // null
        dateOfBirth = invalidPersonalCode.getDateOfBirth();     // null
        age = invalidPersonalCode.getAge();                     // null
        
        String gender = Isikukood.MALE;
        LocalDate dateOfBirth = LocalDate.of(1984, 3, 15);
        String generatedPersonalCode = Isikukood.generatePersonalCode(gender, dateOfBirth); // 38403153949
        generatedPersonalCode = Isikukood.generatePersonalCode(gender, dateOfBirth, 7);     // 38403150076
        String randomPersonalCode = Isikukood.generateRandomPersonalCode();                 // 35207049817
        
        try {
            Isikukood.generatePersonalCode("A", LocalDate.of(1799, 1, 1)); // Throws exception
        } catch (IsikukoodException e) {
            // Handle exception
        }
    }
}
```

## API
<table class="table1">
  <tr>
    <th>Method</th>
    <th>Return type</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Isikukood(String personalCode)</td>
    <td>Isikukood</td>
    <td>Creates an instance of the Isikukood object.</td>
  </tr>
  <tr>
    <td>static generatePersonalCode(String gender, LocalDate dateOfBirth)</td>
    <td>String</td>
    <td>Generates a personal code for the specified gender and date of birth. Throws an exception if the gender is not "M" or "F" or the birth year is before 1800 or after 2099.</td>
  </tr>
  <tr>
    <td>static generatePersonalCode(String gender, LocalDate dateOfBirth, int birthOrderNumber)</td>
    <td>String</td>
    <td>Generates a personal code for the specified gender,date of birth and birth order number. Throws an exception if the gender is not "M" or "F", the birth year is before 1800 or after 2099 or the birth order number is less than 0 or more than 999.</td>
  </tr>
  <tr>
    <td>static generateRandomPersonalCode()</td>
    <td>String</td>
    <td>Generates a random personal code.</td>
  </tr>
  <tr>
    <td>isValid()</td>
    <td>boolean</td>
    <td>Returns whether or not the personal code is valid.</td>
  </tr>
  <tr>
    <td>getDateOfBirth()</td>
    <td>LocalDate</td>
    <td>Returns the person's date of birth. Returns null if the personal code is invalid.</td>
  </tr>
  <tr>
    <td>getGender()</td>
    <td>String</td>
    <td>Returns the person's gender ("M" or "F"). Returns null if the personal code is invalid.</td>
  </tr>
  <tr>
    <td>getAge()</td>
    <td>Integer</td>
    <td>Returns the person's age in years. Returns null if the personal code is invalid or the date of birth is in the future.</td>
  </tr>
</table>

## Buy me a beer? :beer:

Please [donate](https://www.paypal.me/VladislavGoltjajev) if you like my work.