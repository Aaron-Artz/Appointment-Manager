package C195Master.Model;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import static C195Master.Model.User.dbConnect;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class Customer {

    private int customerID =0;
    private String customerName;
    private int active =0;
    private String address;
    private String address2;
    private String city;
    private String postalCode;
    private String phone;
    private String country;
    private Date createDate;
    private String createdBy;
    private Date lastUpdate;
    private String lastUpdateBy;

    public Customer() {
    }

    public Customer(String customerName, String address, String address2, String city, String postalCode, String phone, String country, Date createDate, String createdBy, Date lastUpdate, String lastUpdateBy) {
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    // Getters
    public String getCustomerName() {
        return customerName;
    }
    public String getAddress() {
        return address;
    }
    public String getAddress2() {
        return address2;
    }
    public String getCity() {
        return city;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getCountry() {
        return country;
    }
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public Date getLastUpdate() {
        return lastUpdate;
    }
    public int getActive() {
        return active;
    }
    public int getCustomerID() {
        return customerID;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public String getPhone() {
        return phone;
    }
    // Setters

    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setActive(int active) {
        this.active = active;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Deletes selected customer data from customer table and address table.
    // SELECT * FROM address a JOIN customer c ON a.addressId = c.addressId;

    // Deletes customer from address and customer table based on addressId.
    public static void deleteCustomer (int customerId) throws SQLException {
        try {
            System.out.println("Removing customer data from database");
            int customerAddressId = getAddressId(customerId);
            PreparedStatement customerDeleteStatement = dbConnect.prepareStatement("DELETE customer, address FROM " +
                    "customer INNER JOIN address WHERE address.addressId = ? AND customer.customerId = ?;");
            customerDeleteStatement.setInt(1,customerAddressId);
            customerDeleteStatement.setInt(2, customerId);
            customerDeleteStatement.executeUpdate();
            System.out.println("Customer successfully deleted");
        }
        catch (SQLException e) {
            System.out.println("There was an error exectuing your SQL DELETE statement");
        }
    }

    // Gets address ID for input customer ID.
    public static int getAddressId(int customerId) throws SQLException {
        int addressId = -1;
        PreparedStatement statement = dbConnect.prepareStatement("Select addressId FROM customer WHERE customerId = " + customerId +";");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            addressId = resultSet.getInt("addressId");
        }
        return addressId;
    }


    // Get list of all current customers ID, Name, and Phone number to verify customers in customer table.
    public static ObservableList<Customer> getCurrentCustomers () {
        ObservableList<Customer> currentCustomers = FXCollections.observableArrayList();

        String getUserStatement = "SELECT * FROM customer as u JOIN address as a ON u.addressId = a.addressId";
        try {
            PreparedStatement statement = dbConnect.prepareStatement(getUserStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Customer currentCustomer = new Customer();
                currentCustomer.setCustomerID(resultSet.getInt("customerId"));
                currentCustomer.setCustomerName(resultSet.getString("customerName"));
                currentCustomer.setPhone(resultSet.getString("phone"));
                currentCustomers.add(currentCustomer);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return currentCustomers;
    }

    public static boolean checkCustomerExist(int customerId) throws SQLException {
        PreparedStatement statement = dbConnect.prepareStatement("SELECT customerId FROM customer WHERE customerId = " + customerId);
        ResultSet result = statement.executeQuery();
        boolean isCustomer = false;
        if (result.isBeforeFirst()) {
            //System.out.println("Customer Exists");
            isCustomer = true;
        }
        return  isCustomer;
    }

    public String returnCustomerName(Customer customer) {
        String selectedCustomerName = getCustomerName();
        return selectedCustomerName;
    }

}


