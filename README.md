# java-isikukood [![Download](https://api.bintray.com/packages/vladislavg/java-isikukood/java-isikukood/images/download.svg)](https://bintray.com/vladislavg/java-isikukood/java-isikukood/_latestVersion)

Extract personal data from an Estonian personal identification code (isikukood) with this lightweight library.

## Import
The library is available on Maven Central and JCenter.
### Gradle
```groovy
dependencies {
    compile('com.github.vladislavgoltjajev:java-isikukood:1.4')
}
```
### Maven
```xml
<dependency>
    <groupId>com.github.vladislavgoltjajev</groupId>
    <artifactId>java-isikukood</artifactId>
    <version>1.4</version>
</dependency>
```

## Usage
The library requires at least Java 8.
```java
class Test {
    
    public void test() {
        Isikukood isikukood = new Isikukood("47508030046");
        Boolean isValid = isikukood.isValid();                      // true
        LocalDate dateOfBirth = isikukood.getDateOfBirth();         // 1975-08-03
        String gender = isikukood.getGender();                      // F
        Integer controlNumber = isikukood.getControlNumber();       // 6
        
        Isikukood invalidIsikukood = new Isikukood("123");
        isValid = invalidIsikukood.isValid();                       // false
        
        try {
            dateOfBirth = invalidIsikukood.getDateOfBirth();        // throws IsikukoodException
        } catch (IsikukoodException e) {
            e.printStackTrace();
        }
    }
}
```

## API
<table class="table1">
  <tr>
    <th>Method</th>
    <th>Arguments</th>
    <th>Return type</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Isikukood</td>
    <td>String personalCode</td>
    <td>Isikukood</td>
    <td>Creates an instance of the Isikukood object.</td>
  </tr>
  <tr>
    <td>isValid</td>
    <td>-</td>
    <td>Boolean</td>
    <td>Checks if the personal code is valid.</td>
  </tr>
  <tr>
    <td>getDateOfBirth</td>
    <td>-</td>
    <td>LocalDate</td>
    <td>Returns the person's date of birth. Throws an exception if the personal code is invalid.</td>
  </tr>
  <tr>
     <td>getGender</td>
     <td>-</td>
     <td>String</td>
     <td>Returns the person's gender ("M" or "F"). Throws an exception if the personal code is invalid.</td>
   </tr>
   <tr>
     <td>getAge</td>
     <td>-</td>
     <td>Integer</td>
     <td>Returns the person's age in years. Throws an exception if the personal code is invalid.</td>
   </tr>
   <tr>
     <td>getControlNumber</td>
     <td>-</td>
     <td>Integer</td>
     <td>Returns the personal code's control number. Throws an exception if the personal code is invalid.</td>
   </tr>
</table>
