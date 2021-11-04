package C195Master.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class Report {
    private String month;
    private String type;
    private String typeCount;
    private int constultation;
    private int checkUp;
    private int cancelation;
    private int other;
    public String username;
    public int appointmentId;
    public String customerName;
    public String start;
    public String end;


    //Getters
    public String getMonth() {
        return month;
    }
    public int getCancelation() {
        return cancelation;
    }
    public int getCheckUp() {
        return checkUp;
    }
    public int getConstultation() {
        return constultation;
    }
    public int getOther() {
        return other;
    }
    public String getTypeCount() { return typeCount; }
    public String getType() {
        return type;
    }
    public int getAppointmentId() { return appointmentId; }
    public String getCustomerName() { return customerName; }
    public String getEnd() { return end; }
    public String getStart() { return start; }
    public String getUsername() { return username;
    }
    //Setters

    public void setCancelation(int cancelation) {
        this.cancelation = cancelation;
    }
    public void setCheckUp(int checkUp) {
        this.checkUp = checkUp;
    }
    public void setConstultation(int constultation) {
        this.constultation = constultation;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public void setOther(int other) {
        this.other = other;
    }
    public void setType(String type) { this.type = type; }
    public void setTypeCount(String typeCount) {
        this.typeCount = typeCount;
    }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public void setEnd(String end) { this.end = end; }
    public void setStart(String start) { this.start = start; }
    public void setUsername(String username) { this.username = username; }

    //Constructor
    public Report () {}

    public static ObservableList<Report> getReportTypeByMonth() throws SQLException {
        ObservableList<Report> reportByType = FXCollections.observableArrayList();
        PreparedStatement statement = dbConnect.prepareStatement("SELECT MONTHNAME(start) as MONTH, type, COUNT(type) as types FROM appointment GROUP BY MONTH, type");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Report report = new Report();
            report.setMonth(resultSet.getString(1));
            report.setType(resultSet.getString(2));
            report.setTypeCount(resultSet.getString(3).toString());
            reportByType.addAll(report);
        }
        return reportByType;
    }




    public static ObservableList<Report> getUserSchedule() throws SQLException {
        String offset = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()).toString(); //Gets local offset in String format
        ObservableList<Report> userSchedule = FXCollections.observableArrayList();
        PreparedStatement statement = dbConnect.prepareStatement("SELECT userName, appointmentId, customerName, DATE_FORMAT((CONVERT_TZ(start, \"+00:00\", ?)), '%Y-%m-%d %H: %i'), DATE_FORMAT((CONVERT_TZ(end, \"+00:00\", ?)), '%Y-%m-%d %H: %i'), type \n" +
                    "FROM appointment as a JOIN user as u on a.userId = u.userId JOIN customer as c on a.customerId = c.customerId\n" +
                    "ORDER BY userName, start;");

        statement.setString(1, offset);
        statement.setString(2, offset);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Report report = new Report();
            report.setUsername(resultSet.getString(1));
            report.setAppointmentId(resultSet.getInt(2));
            report.setCustomerName(resultSet.getString(3));
            report.setStart(resultSet.getString(4));
            report.setEnd(resultSet.getString(5));
            report.setType(resultSet.getString(6));
            userSchedule.addAll(report);
        }
        return userSchedule;
    }

    public  static ObservableList<Report> getCustomerTypeCount() throws SQLException {
        ObservableList<Report> customerTypes = FXCollections.observableArrayList();
        PreparedStatement statment = dbConnect.prepareStatement("SELECT customerName, type, Count(type) FROM appointment a JOIN customer c ON a.customerId = c.customerId GROUP BY a.customerId, type;");
        ResultSet resultSet = statment.executeQuery();

        while (resultSet.next()) {
            Report report = new Report();
            report.setCustomerName(resultSet.getString(1));
            report.setType(resultSet.getString(2));
            report.setTypeCount(resultSet.getString(3));
            customerTypes.addAll(report);
        }
        return  customerTypes;
    }


}


    

