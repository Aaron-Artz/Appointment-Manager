package C195Master.Controller;
import C195Master.Model.City;
import C195Master.Model.Country;
import C195Master.Model.Customer;
import C195Master.Model.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import C195Master.Utilities.Exceptions;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;



import static C195Master.Model.Customer.getAddressId;
import static C195Master.Model.User.dbConnect;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class CustomerScreenController implements Initializable {
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button modifyButton;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn <Customer, Integer> idCol;
    @FXML private TableColumn <Customer, String> nameCol;
    @FXML private TableColumn <Customer, String> phoneCol;
    @FXML private TextField idTxtField;
    @FXML private TextField nameTxtField;
    @FXML private TextField addressTxtField;
    @FXML private TextField addressTwoTxtField;
    @FXML private ComboBox countryCombo;
    @FXML private ComboBox cityCombo;
    @FXML private TextField postalTxtField;
    @FXML private TextField phoneTxtField;
    @FXML private RadioButton activeRadio;
    @FXML private RadioButton inactiveRadio;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    ObservableList<String> countryOptions = FXCollections.observableArrayList();
    ObservableList<String> cityOptions = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize Customers table
        //Lambda in use to set name and phone column.
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        phoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        PropertyValueFactory<Customer, Integer> customerIdValue = new PropertyValueFactory<>("CustomerID");
        idCol.setCellValueFactory(customerIdValue);
        customerTable.setItems(Customer.getCurrentCustomers());

        try {
            fillCountryComboBox();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fillCityComboBox();
        } catch (Exception e) {
            e.printStackTrace();
        }
        activeRadio.setSelected(true);
        idTxtField.setDisable(true);
        idTxtField.setText(null);
        countryCombo.setValue(""); // Default to "" for empty fields exception
        cityCombo.setValue(""); // Default to "" for empty fields exception
    }


    // Passes selected customer information into text fields for editing.
    public void modifyButtonAction() throws SQLException {
        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            int customerId = customer.getCustomerID();
            System.out.println("customer ID to modify: " +customerId);
            int customerAddressId = getAddressId(customerId);
            System.out.println("customer address ID to modify : " + customerAddressId);

            PreparedStatement getStatement = dbConnect.prepareStatement("SELECT * FROM customer c INNER JOIN " +
                    "address a ON c.addressId = a.addressId INNER JOIN city ci ON a.cityId = ci.cityId INNER JOIN " +
                    "country co ON ci.countryId = co.countryId WHERE c.addressId = " + customerAddressId + ";" );

            // Sets txt fields and combo boxes to selected customers information.
            ResultSet getInfoResult = getStatement.executeQuery();
            while (getInfoResult.next()) {
                idTxtField.setText(getInfoResult.getString("customerId"));
                nameTxtField.setText(getInfoResult.getString("customerName"));
                addressTxtField.setText(getInfoResult.getString("address"));
                addressTwoTxtField.setText(getInfoResult.getString("address2"));
                countryCombo.getSelectionModel().select(getInfoResult.getString("country"));
                cityCombo.getSelectionModel().select(getInfoResult.getString("city"));
                postalTxtField.setText(getInfoResult.getString("postalCode"));
                phoneTxtField.setText(getInfoResult.getString("phone"));
                if (getInfoResult.getInt("active") == 1){
                    activeRadio.setSelected(true);
                    inactiveRadio.setSelected(false);
                }
                else {
                    activeRadio.setSelected(false);
                    inactiveRadio.setSelected(true);
                }
            }
        }
    }

    // DELETES selected customer in customerTable view from database as well as address.
    // delete customer method in customer model.
    public void deleteButtonAction() throws SQLException {

        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("There is no customer Selected.");
            alert.setContentText("Please select a customer from the table first");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer Confirmation");
        alert.setHeaderText("Are you sure you want to delete this customer?");
        alert.setContentText("Press OK to Remove the customer. \nPress Cancel to return to the Customer screen.");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK);
        {
            if (customerTable.getSelectionModel().getSelectedItem() != null) {
                Customer customer = customerTable.getSelectionModel().getSelectedItem();
                int customerId = customer.getCustomerID();
                System.out.println("Customer ID to delete is : " + customerId);
                Customer.deleteCustomer(customerId);
                customerTable.setItems(Customer.getCurrentCustomers());
            }
            else {
                alert.close();
            }
        }
    }


    // Inserts customer data into Address and Customer table using auto-generated ID's
    public void saveButtonAction() throws SQLException {
        String address = addressTxtField.getText();
        String postalCode = postalTxtField.getText();
        String phone = phoneTxtField.getText();
        String customerName = nameTxtField.getText();
        String city = cityCombo.getValue().toString();
        String country = countryCombo.getValue().toString();
        String emptyFields = Exceptions.emptyFieldsException(customerName, address, city, country, postalCode, phone);
        String validFields = Exceptions.validFieldsException(customerName);
        String test = "test";
        if (emptyFields.length() < 1) {

            //Statement variables
            String idText = idTxtField.getText();
            String addressTwo = addressTwoTxtField.getText();
            int cityId = City.getCityId(cityCombo.getValue().toString());
            String createDate = "sysdate()";
            String createBy = User.getUsername();
            String lastUpdateBy = User.getUsername();
            int active;
            if (activeRadio.isSelected()) {
                active = 1;
            } else {
                active = 0;
            }
            // ***** Insert statement used for NEW customers
            // Updates both address and customer table
            // uses Auto Generated ID's
            if (idText == null) {
                System.out.println("***** NEW Customer Save Action *****");
                try {
                    int generatedAddressId = -1;

                    // Prepared statement with new generated addressId returned.
                    PreparedStatement addressStatement = dbConnect.prepareStatement("INSERT INTO address (address, address2, cityId, " +
                            "postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, sysdate()," +
                            " ?, sysdate(), ? )", Statement.RETURN_GENERATED_KEYS);

                    addressStatement.setString(1, address);
                    addressStatement.setString(2, addressTwo);
                    addressStatement.setInt(3, cityId);
                    addressStatement.setString(4, postalCode);
                    addressStatement.setString(5, phone);
                    addressStatement.setString(6, createBy);
                    addressStatement.setString(7, lastUpdateBy);

                    // Test for INSERT statement formatting.
                    // System.out.println(statement);

                    // Executes address table INSERT
                    addressStatement.executeUpdate();

                    ResultSet resultSet = addressStatement.getGeneratedKeys();
                    if (resultSet.next()) {
                        generatedAddressId = resultSet.getInt(1);
                        System.out.println("New address generated ID :" + generatedAddressId);
                    }


                    PreparedStatement customerStatement = dbConnect.prepareStatement("INSERT INTO customer (customerName, " +
                            "addressId, active, createDate, createdBy,lastUpdate, lastUpdateBy) VALUES (?, ?,?, sysdate(), ?, " +
                            "sysdate(), ?); ");

                    System.out.println("Customer INSERT SQL statement is : " + customerStatement);

                    customerStatement.setString(1, customerName);
                    customerStatement.setInt(2, generatedAddressId);
                    customerStatement.setInt(3, active);
                    customerStatement.setString(4, createBy);
                    customerStatement.setString(5, lastUpdateBy);

                    // Executes customer table insert.
                    customerStatement.executeUpdate();
                    clearButtonAction();
                    customerTable.setItems(Customer.getCurrentCustomers());
                } catch (SQLException e) {
                    System.out.println("There was an error exectuing your SQL statement");
                }
            }


            // ***** Update Statment for EXISTING customer changes
            // Executes update statment in address and customer table
            // Uses existing generated address and customer ID's
            else if (idText != null) {
                System.out.println("***** EXISTING Customer Save Action *****");
                int customerId = Integer.parseInt(idTxtField.getText());

                // Ensures customer being modified exists in the database.
                if (Customer.checkCustomerExist(customerId)) {
                    int customerAddressId = getAddressId(customerId);

                    // UPDATE statment for existing customers.
                    PreparedStatement statement = dbConnect.prepareStatement("UPDATE customer, address SET " +
                            "customerName = ?, address = ?, address2 = ?, cityId = ?, postalCode = ?, active = ?, " +
                            "phone = ? WHERE customerId = ? AND address.addressId = ?;");

                    statement.setString(1, customerName);
                    statement.setString(2, address);
                    statement.setString(3, addressTwo);
                    statement.setInt(4, cityId);
                    statement.setString(5, postalCode);
                    statement.setInt(6, active);
                    statement.setString(7, phone);
                    statement.setInt(8, customerId);
                    statement.setInt(9, customerAddressId);

                    statement.executeUpdate();
                    clearButtonAction();
                    customerTable.setItems(Customer.getCurrentCustomers());
                    System.out.println("Successfully updated existing user.");

                } else {
                    System.out.println("Error updating Existing customer: could not verify customer ID");
                }
            }
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Empty fields Alert");
            alert.setHeaderText("Empty Fields Error");
            alert.setContentText(emptyFields);
            alert.showAndWait();
        }
    }


    //populates countryCombo box.
    public void fillCountryComboBox() throws SQLException, Exception {
        Statement stmt = dbConnect.createStatement();
        String sqlStatement = "SELECT country FROM country";
        ResultSet result = stmt.executeQuery(sqlStatement);

        while (result.next()) {
            Customer cust = new Customer();
            cust.setCountry(result.getString("country"));
            countryOptions.add(cust.getCountry());
            countryCombo.setItems(countryOptions);
        }
        stmt.close();
        result.close();
    }

    // Ensures the city is in the correct country
    public void countryComboAction () throws SQLException {
        if (countryCombo.getValue() == null) {
            System.out.println("Clearning country combo box");
        }
        else {
            String selectedCountry = countryCombo.getValue().toString();
            int selectedCountryId = Country.getCountryId(selectedCountry);
            System.out.println("Selected Country: " + selectedCountry + "\nSelected ID: " + selectedCountryId);

            PreparedStatement statement = dbConnect.prepareStatement("SELECT city FROM city WHERE countryId = " + selectedCountryId);
            ResultSet result = statement.executeQuery();
            cityCombo.getItems().clear(); //Clears current ComboBox values.

            // Populates city combo box with matching country.
            while (result.next()) {
                Customer cust = new Customer();
                cust.setCity(result.getString("city"));
                cityOptions.add(cust.getCity());
                cityCombo.setItems(cityOptions);
            }
            statement.close();
            result.close();
        }
    }


    //populate CityCombo box will all available cities.
    public void fillCityComboBox() throws SQLException, Exception {
        String sqlStatement = "SELECT city FROM city";
        PreparedStatement pst = dbConnect.prepareStatement(sqlStatement);
        ResultSet result = pst.executeQuery(sqlStatement);

        while (result.next()) {
            Customer cust = new Customer();
            cust.setCity(result.getString("city"));
            cityOptions.add(cust.getCity());
            cityCombo.setItems(cityOptions);
        }
        pst.close();
        result.close();
    }


    // Sets all text fields, combo boxes and radio buttons to default.
    public void clearButtonAction () {
        idTxtField.setText(null);
        nameTxtField.setText(null);
        addressTxtField.setText(null);
        addressTwoTxtField.setText(null);
        countryCombo.setValue(null);
        cityCombo.setValue(null);
        postalTxtField.setText(null);
        activeRadio.setSelected(true);
        phoneTxtField.setText(null);
    }

    // Returns customer to main screen, does not save data.
    @FXML
    void cancelButtonAction(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Canceling will erase all unsaved changes and return to the Main Screen.\nWould you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }
    }
}
