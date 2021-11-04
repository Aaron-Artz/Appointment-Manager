package C195Master.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static C195Master.Model.User.dbConnect;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class Appointment {
    private String customerName;
    private int appointmentID = 0;
    private int customerID = 0;
    private int userID = 0;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private String start;
    private String end;
    private LocalDate day;
    private Date createDate;
    private String createBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    private String userName;
    private static int passAppointmentId; // Used to pass appointment information from MainScreen to ModifyAppointment.



    // Getters

    public String getUserName() {
        return userName;
    }
    public String getCustomerName() {
        return customerName;
    }
    public int getAppointmentID() {
        return appointmentID;
    }
    public int getCustomerID() {
        return customerID;
    }
    public int getUserID() {
        return userID;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getLocation() {
        return location;
    }
    public String getContact() {
        return contact;
    }
    public String getType() {
        return type;
    }
    public String getUrl() {
        return url;
    }
    public String getStart() {
        return start;
    }
    public String getEnd() { return end; }
    public LocalDate getDay() { return day; }
    public Date getCreateDate() {
        return createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
    public static int getPassAppointmentId() { return passAppointmentId; } // Only use in ModifyAppointmentController

    // Setters

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setStart(String start) {
        this.start = start;
    }
    public void setEnd(String end) {
        this.end = end;
    }
    public void setDay(LocalDate day) { this.day = day; }
    public void setCreateDate(Date createDate) {this.createDate = createDate; }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    public static void setPassAppointmentId(int appointmentId) {
        passAppointmentId = appointmentId;
    }


    public Appointment() {}

    // Checks to see if any rows are returned within the next 15 minutes to find upcoming appointments
    // Used in loginScreen loginButtonAction.
    public static boolean getUpcomingAppointment() throws SQLException {
        Boolean upcomingAppointment = false;
        PreparedStatement statment = dbConnect.prepareStatement("SELECT * FROM appointment WHERE start BETWEEN NOW() AND NOW() + INTERVAL 15 MINUTE");
        ResultSet resultSet = statment.executeQuery();
        if (resultSet.isBeforeFirst()) {
            upcomingAppointment = true;
            return upcomingAppointment;
        }
        else {
            upcomingAppointment = false;
            return upcomingAppointment;
        }
    }


    // Checks database to make sure there are no appointments with times already in the range of the requested times.
    // returns true if appointment has no conflicts
    public static Boolean validAppointment (String startDate, String endDate, int appointmentId) throws SQLException {
        boolean isValid = false;
        String offset = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()).toString(); // Gets  local offset in String format
        PreparedStatement statement = dbConnect.prepareStatement("SELECT * FROM appointment WHERE " +
                                                                "((((start <= CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\"))" +
                                                                " AND (end >= CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\"))) " +
                                                                "OR ((start <= CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\")) " +
                                                                "AND (end >= CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\"))))" +

                                                                " OR ((start >= CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\"))" +
                                                                " AND (start <= CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\"))" +
                                                                " OR (end >= CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\"))" +
                                                                " AND (end <= CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\")))) AND appointmentId != ?");
        statement.setString(1, startDate);
        statement.setString(2, offset);
        statement.setString(3, startDate);
        statement.setString(4, offset);     // Sets up first part of WHERE clause
        statement.setString(5, endDate);    // Checks to see if selected dates are within a current appointment.
        statement.setString(6, offset);
        statement.setString(7, endDate);
        statement.setString(8, offset);

        statement.setString(9, startDate);
        statement.setString(10, offset);
        statement.setString(11, endDate);
        statement.setString(12, offset);        //Sets up second part of WHERE clause
        statement.setString(13, startDate);     //Checks to see if selected dates has an appointment within them
        statement.setString(14, offset);
        statement.setString(15, endDate);
        statement.setString(16, offset);

        statement.setInt(17, appointmentId);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.isBeforeFirst()) {
            isValid = false;
            return isValid;
        }
        else {
            isValid = true;
            return isValid;
        }
    }


    // Deletes appointment based on appointmentID primary key
    public static void deleteAppointment(int appointmentId) throws SQLException {
        try {
            System.out.println("***** Deleting Appointment!*****");
            PreparedStatement statement = dbConnect.prepareStatement("DELETE FROM appointment WHERE appointmentId = ?");
            statement.setInt(1, appointmentId);
            statement.executeUpdate();
            System.out.println("Appointment successfully deleted");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Adds appointment to database.
    // Converts time from local to database timezone in SQL query.
    public static void addAppointment(int customerId, int userId, String title, String desc, String location, String contact, String type, String url, String start, String end, String userName) throws SQLException {
        String offset = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()).toString(); // Gets  local offset in String format

        PreparedStatement statement = dbConnect.prepareStatement("INSERT INTO appointment (customerId, userId, " +
                "title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\"), " +
                "CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\"), sysdate(), ?, sysdate(), ?)");
            statement.setInt(1, customerId);
            statement.setInt(2, userId);
            statement.setString(3, title);
            statement.setString(4, desc);
            statement.setString(5, location);
            statement.setString(6, contact);
            statement.setString(7, type);
            statement.setString(8, url);
            statement.setString(9, start);
            statement.setString(10, offset);
            statement.setString(11, end);
            statement.setString(12, offset);
            statement.setString(13, userName);
            statement.setString(14, userName);

            statement.executeUpdate();
        }



    // Gets all available appointments.
    // Converts timezones in query.
    public static ObservableList<Appointment> getCurrentAppointments (Boolean isWeekly) {
        String offset = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()).toString(); //Gets local offset in String format
        ObservableList<Appointment> currentAppointments = FXCollections.observableArrayList();
        PreparedStatement statement = null;
        try {
            if (isWeekly) {
                statement = dbConnect.prepareStatement("SELECT appointmentID, customerName, type, " +
                        "DATE_FORMAT((CONVERT_TZ(start, \"+00:00\", ?)), '%Y-%m-%d %H: %i'), " +
                        "DATE_FORMAT((CONVERT_TZ(end, \"+00:00\", ?)), '%Y-%m-%d %H: %i'), userName " +
                        "FROM appointment as a JOIN user u on a.userId = u.userId JOIN customer c " +
                        "ON c.customerId = a.customerId WHERE start < NOW() + INTERVAL 7 DAY AND start >= CURDATE();");
            }
            else if (!isWeekly) {
                statement = dbConnect.prepareStatement("SELECT appointmentID, customerName, type, " +
                        "DATE_FORMAT((CONVERT_TZ(start, \"+00:00\", ?)), '%Y-%m-%d %H: %i'), " +
                        "DATE_FORMAT((CONVERT_TZ(end, \"+00:00\", ?)), '%Y-%m-%d %H: %i'), userName " +
                        "FROM appointment as a JOIN user u on a.userId = u.userId JOIN customer c " +
                        "ON c.customerId = a.customerId WHERE start <= LAST_DAY(NOW()) AND start > LAST_DAY(NOW() - INTERVAL 1 MONTH)");
            }
            else {
                System.out.println("There was an error checking for isWeekly Boolean in Appointment.getCurrentAppointments method");
            }
            statement.setString(1, offset);
            statement.setString(2, offset);
            // System.out.println(statement); // Use to very proper SQL query.

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Appointment currentAppointment = new Appointment();
                currentAppointment.setAppointmentID(resultSet.getInt("appointmentId"));
                currentAppointment.setCustomerName(resultSet.getString("customerName"));
                currentAppointment.setType(resultSet.getString("type"));
                currentAppointment.setStart(resultSet.getString(4));
                currentAppointment.setEnd(resultSet.getString(5));
                currentAppointment.setUserName(resultSet.getString("userName"));
                currentAppointments.add(currentAppointment);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return currentAppointments;
    }


    // Gets a specific appointment from an appointment ID
    // Used in ModifyAppointmentController
    public static Appointment getAppointment (int appointmentId) throws SQLException {

        String offset = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()).toString(); //Gets local offset in String format
        System.out.println("Zone offset is : " + offset);
        Appointment appointment = new Appointment();
        PreparedStatement statement = dbConnect.prepareStatement("SELECT appointmentID, customerName, title, " +
                "description, location, contact, type, TIME(CONVERT_TZ(start, \"+00:00\", ?)), " +      // CONVERT_TZ(start, "+00:00", ?)
                "TIME(CONVERT_TZ(end, \"+00:00\", ?)), url, DATE(start) FROM appointment as a \n" +
                "JOIN user u on a.userId = u.userId JOIN customer c ON c.customerId = a.customerId WHERE appointmentId = " + appointmentId);
        statement.setString(1, offset);
        statement.setString(2, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
            appointment.setAppointmentID(resultSet.getInt("appointmentId"));
            appointment.setCustomerName(resultSet.getString("customerName"));
            appointment.setTitle(resultSet.getString("title"));
            appointment.setDescription(resultSet.getString("description"));
            appointment.setLocation(resultSet.getString("location"));
            appointment.setContact(resultSet.getString("contact"));
            appointment.setType(resultSet.getString("type"));
            //appointment.setStartTime(resultSet.getTime(8));
            //System.out.println("Set time is: " + appointment.getStartTime());
            appointment.setStart(resultSet.getString(8));
            appointment.setEnd(resultSet.getString(9));
            appointment.setUrl(resultSet.getString("url"));
            appointment.setDay(resultSet.getDate(11).toLocalDate());
            }
        return appointment;
    }


    public static void updateAppointment(int appointmentId, int userId, String title, String desc, String location, String contact, String type, String url, String start, String end, String userName) throws SQLException {
        String offset = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()).toString(); //Gets local offset in String format
        System.out.println("Updating Appointment");
        PreparedStatement statement = dbConnect.prepareStatement("UPDATE appointment SET title = ?, description = ?, location = ?, contact = ?, type = ?, start = CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\"), end = CONVERT_TZ((STR_TO_DATE(?, \"%Y-%m-%d %H:%i\")), ?, \"+00:00\"), lastUpdate = sysdate(), lastUpdateBy = ?, url = ? WHERE appointmentId = ?;");
        statement.setString(1, title);
        statement.setString(2, desc);
        statement.setString(3, location);
        statement.setString(4, contact);
        statement.setString(5, type);
        statement.setString(6, start);
        statement.setString(7, offset);
        statement.setString(8, end);
        statement.setString(9, offset);
        statement.setString(10, userName);
        statement.setString(11, url);
        statement.setInt(12, appointmentId);

        statement.executeUpdate();
    }
}
