# java-isikukood
Library for parsing Estonian personal identification codes.  
  
[![Download](https://api.bintray.com/packages/vladislavg/java-isikukood/java-isikukood/images/download.svg)](https://bintray.com/vladislavg/java-isikukood/java-isikukood/_latestVersion)

## Import
The library is available on Maven Central and JCenter.
### Gradle
```groovy
dependencies {
    compile group: 'com.github.vladislavgoltjajev', name: 'java-isikukood', version: '1.1'
}
```
### Maven
```xml
<dependency>
    <groupId>com.github.vladislavgoltjajev</groupId>
    <artifactId>java-isikukood</artifactId>
    <version>1.1</version>
    <type>pom</type>
</dependency>
```

## Usage
```java
class Test {
    
    public void test() {
        Isikukood isikukood = new Isikukood("47508030046");
        boolean isValid = isikukood.isValid();                      // true
        LocalDate dateOfBirth = isikukood.getDateOfBirth();         // 1975-08-03
        Gender gender = isikukood.getGender();                      // F
        int controlNumber = isikukood.getControlNumber();           // 6
        
        Isikukood invalidIsikukood = new Isikukood("123");
        isValid = invalidIsikukood.isValid();                       // false
        
        try {
            dateOfBirth = invalidIsikukood.getDateOfBirth();        // throws exception
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
    <td>boolean</td>
    <td>Checks if the personal code is valid. Throws an exception if the personal code is invalid.</td>
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
     <td>Returns the person's gender. Throws an exception if the personal code is invalid.</td>
   </tr>
   <tr>
     <td>getAge</td>
     <td>-</td>
     <td>int</td>
     <td>Returns the person's age. Throws an exception if the personal code is invalid.</td>
   </tr>
   <tr>
     <td>getControlNumber</td>
     <td>-</td>
     <td>int</td>
     <td>Returns the personal code's control number. Throws an exception if the personal code is invalid.</td>
   </tr>
</table>
