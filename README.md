# Appointment-Manager
An appointment management tool built using Java </br>

## File Overview </br>
/C195-Master/src/C195Master/ </br>

 C195Master.java - Main application file.
 
 /Controller - Contains a mixture of .fxml and .java files which configure the user interface of the application.
 
 /LanguageResources - Contains language packages to translate the login page to either English or Spanish
 
 /Model - Contains files to create Java objects.
 
 /Utilities - Contains the java file to connect to establish a database connection as well as a file to create expectations for incorrect text field information.
 
 /drivers - Contains the drivers needed to connect to the database.
 

## Login Screen </br>
 ![alt text](https://github.com/Aaron-Artz/Appointment-Manager/blob/main/Appointment-Manager-Pictures/LoginScreen.PNG?raw=true)</br>
 The login screen validates that the username and password exist in the database. It will also translate the text to English or Spanish based on the users location.
 
## Main Screen </br>
 ![alt text](https://github.com/Aaron-Artz/Appointment-Manager/blob/main/Appointment-Manager-Pictures/MainScreen.PNG?raw=true)</br>
 The appointments will be populated in the table either weekly or monthly depending on the radio button selected. The times of the appointment are adjusted to the users current time zone. users can add, modify or delete appointments using the bottom buttons and track reports and customers using the right buttons.

## Customers Screen </br>
 ![alt text](https://github.com/Aaron-Artz/Appointment-Manager/blob/main/Appointment-Manager-Pictures/CustomerScreen.PNG?raw=true)</br>
 Available customers are populated from the database into the table on the left. Selecting a customer and pressing the modify button will populate the fields on the right to be edited by the user.

## Add Appointment Screen </br>
 ![alt text](https://github.com/Aaron-Artz/Appointment-Manager/blob/main/Appointment-Manager-Pictures/AddAppointmentScreen.PNG?raw=true)</br>
 To add an appointment, you must first select a customer from the left table. If the customer doesn't exist, the user can select New Customer which will take them to the customers screen. Once a customer it selected the name field will be auto populated and the user can fill out the remaining appointment information. The exceptions.java utility will ensure all fields are filled with only appropriate information.

## Modify Appointment Screen </br>
 ![alt text](https://github.com/Aaron-Artz/Appointment-Manager/blob/main/Appointment-Manager-Pictures/ModifyAppointmentScreen.PNG?raw=true)</br>
The modify appointment screen is like the add appointment screen, only there in no customer information table. This is designed this to prevent users from accidently changing the customer tied to the appointment.

## Reports Screen </br>
 ![alt text](https://github.com/Aaron-Artz/Appointment-Manager/blob/main/Appointment-Manager-Pictures/ReportsScreen.PNG?raw=true)</br>
 The reports screen has three auto-generated reports. The first is the number of appointments by type. The second, in an employee schedule based off appointment times. and the last is the number of appointments by customer.

