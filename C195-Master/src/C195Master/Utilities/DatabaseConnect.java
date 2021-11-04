package C195Master.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class DatabaseConnect {

    public static final String databaseName = "U05TEU";
    public static final String serverName = "jdbc:mysql://3.227.166.251:3306/" + databaseName;
    public static final String dbPort = "3306";
    public static final String username = "U05TEU";
    public static final String password = "53688599730";
    public static final String driver = "com.mysql.cj.jdbc.Driver";
    // public static final String driver = "com.mysql.jdbc.Driver";stest
    public static Connection connection;

// Method to connect to database
    public static Connection dbConnect() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        connection = DriverManager.getConnection(serverName, username, password);
        System.out.println("Connection Successful!");
        return connection;
    }

    public static void dbDisconnect() throws SQLException {
        connection.close();
        System.out.println("Connection terminated.");
    }
}
