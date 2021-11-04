package C195Master.Model;

import C195Master.Utilities.DatabaseConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class User {
    private static int userID;
    private static String username;
    private static String password;
    private int active;
    private Date createDate;
    private String createBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;


//constructor
    public User() {
        userID = 0;
        username = null;
        password = null;
    }

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    // Getters
    public static int getUserID() {
        return userID;
    }
    public static String getUsername() {
        return username;
    }
    public static String getPassword() {
        return password;
    }
    public int getActive() {
        return active;
    }
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

    // Setters
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setActive(int active) {
        this.active = active;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    // Database Queries

    //User Queries

    public static Connection dbConnect = DatabaseConnect.connection;

    //Gets list of all current users
    public static ObservableList<User> getCurrentUsers () {
        ObservableList<User> currentUsers = FXCollections.observableArrayList();
        String getUserStatement = "SELECT * FROM user";
        try {
            PreparedStatement statement = dbConnect.prepareStatement(getUserStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User currentUser = new User();
                currentUser.setUserID(resultSet.getInt("userId"));
                currentUser.setUsername(resultSet.getString("userName"));
                currentUser.setPassword(resultSet.getString("password"));
                currentUsers.add(currentUser);
            }

        }
            catch (SQLException e) {
            e.printStackTrace();
            }
        return currentUsers;

    }
    // Returns user IDfor matching username.

    public static int currentUserID(String username) throws SQLException {
        Statement stmnt = DatabaseConnect.connection.createStatement();
        String stmntQuery = "SELECT userId FROM user WHERE userName = '" + username + "'";
        ResultSet result = stmnt.executeQuery(stmntQuery);

        int userID = 0;
        while (result.next()) {
            userID = result.getInt("userId");
            System.out.println("User ID: " + userID); // THIS WORKS!!!!!!
        }
        return userID;
    }


        //   Validates user login by checking to see if username + password return any rows.
    public static Boolean validateLogin(String username, String password) throws SQLException {
        Statement stmnt = DatabaseConnect.connection.createStatement();
        String stmtQuery = "SELECT * FROM user WHERE BINARY userName = '" + username + "' AND BINARY password = '" + password + "';";
        ResultSet result = stmnt.executeQuery(stmtQuery);

        if (result.isBeforeFirst()) {
            System.out.println("This is a valid User");
            return true;
        }
        return false;
    }

}
