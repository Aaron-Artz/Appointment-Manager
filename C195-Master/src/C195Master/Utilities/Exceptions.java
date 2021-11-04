package C195Master.Utilities;

import C195Master.Model.Appointment;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class Exceptions {
    public static String validFieldsException(String customerName) {
        String validWarning = "";
        if (!stringValidation(customerName)) {
            validWarning = validWarning + "The customer name may only contain alphabet characters.";
        }
        return validWarning;
    }

    // Provides warning responses to empty fields in Customer screen.
    public static String emptyFieldsException(String customerName, String address, String city, String country, String postalCode, String phone) {
        String emptyWarning = "";
        if (customerName.length() < 1) {
            emptyWarning = emptyWarning + "\n The customer name field must have a value.";
        }
        if (address.length() < 1) {
            emptyWarning = emptyWarning + "\n The address field must have a value.";
        }
        if (city.length() < 1) {
            emptyWarning = emptyWarning + "\n The city field must have a value.";
        }
        if (country.length() < 1) {
            emptyWarning = emptyWarning + "\n The country field must have a value.";
        }
        if (postalCode.length() < 1) {
            emptyWarning = emptyWarning + "\n The postal code field must have a value.";
        }
        if (phone.length() < 1) {
            emptyWarning = emptyWarning + "\n The phone field must have a value.";
        }
        return emptyWarning;
    }

    public static String emptyAppFields(String customerName, String title, String description, String location, String contact, String type, String url, String date, String startTime, String endTime) {
        String errorWarning = "";
        if (customerName.length() < 1){
            errorWarning = errorWarning + "\nThe customer name field can not be empty";
        }
        if (title.length() < 1) {
            errorWarning = errorWarning + "\nThe title field can not be empty";
        }
        if (description.length() < 1){
            errorWarning = errorWarning + "\nThe description field can not be empty";
        }
        if (location.length() < 1) {
            errorWarning = errorWarning + "\n The location field can not be empty";
        }
        if (contact.length() < 1) {
            errorWarning = errorWarning + "\nThe contact field can not be empty";
        }
        if (type.length() < 1) {
            errorWarning = errorWarning + "\nThe type field can not be left empty";
        }
        if (url.length() < 1) {
            errorWarning = errorWarning + "\nThe url field can not be left empty";
        }
        if (date.length() < 1) {
            errorWarning = errorWarning + "\nThe date field can not be left empty";
        }
        if (startTime.length() < 1) {
            errorWarning = errorWarning + "\nThe start field can not be left empty";
        }
        if (endTime.length() < 1) {
            errorWarning = errorWarning + "\nThe end field can not be left empty";
        }

        return errorWarning;
    }

    // Varifies the only characters in a given string are alphabetical or a space.
    public static boolean stringValidation (String str) {
        str = str.toLowerCase();
        char[] charArray = str.toCharArray();
        for (int i = 0; 1 < charArray.length; i++) {
            char ch = charArray[i];
            if (!((ch >= 'a' && ch <= 'z') || (ch == ' ')));
                return false;
        }
        return true;
    }

    public static String invalidTimeFields(String startDate, String endDate, int appointmentId) throws ParseException, SQLException {
        String errorWarning = "";
        Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDate); // Defines a Date variable for start date
        Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endDate); // Defines a Date variable for start date


        if (start.compareTo(end) > 0) {
            errorWarning = errorWarning + "\nThe start time must be before the end time."; // Appointment tiems bust have a start date before end date.
        }
        if (start.compareTo(end) == 0) {
            errorWarning = errorWarning + "\nThe appointment must be at least 30 minutes."; //appointment times must be different
        }
        if (Appointment.validAppointment(startDate, endDate, appointmentId) == false) {
            errorWarning = errorWarning + "\nThe appointment times you've selected conflic with an existing appointment."; //Appointment times in use for another appointment.
        }
        return  errorWarning;
    }
}
