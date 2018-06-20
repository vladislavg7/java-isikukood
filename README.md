# java-isikukood
Library for working with Estonian personal identification codes.

## Usage
```java
class Test {
    
    public void test() {
        Isikukood isikukood = new Isikukood("47508030046");
        boolean isValid = isikukood.isValid();                      // true
        LocalDate dateOfBirth = isikukood.getDateOfBirth();         // 1975-08-03
        Gender gender = isikukood.getGender();                      // FEMALE
        String genderCode = isikukood.getGender().getGenderCode();  // F
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
    <td>String|Long personalCode</td>
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
     <td>Gender</td>
     <td>Returns the person's gender. The Gender object contains codes ("M" and "F") that can be extracted. Throws an exception if the personal code is invalid.</td>
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
