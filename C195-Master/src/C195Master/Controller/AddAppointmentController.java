package C195Master.Controller;

import C195Master.Model.Appointment;
import C195Master.Model.Customer;
import C195Master.Model.User;
import C195Master.Utilities.Exceptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import static C195Master.Model.User.dbConnect;
import static C195Master.Model.User.getUserID;


/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/


public class AddAppointmentController implements Initializable{
    @FXML private Button newCustomerButton;
    @FXML private Button cancelButton;
    @FXML private Button selectCustomerButton;
    @FXML private Button saveButton;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> idCol;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> phoneCol;
    @FXML private TextField nameTxtField;
    @FXML private TextField titleTxtField;
    @FXML private TextField descTxTField;
    @FXML private TextField locationTxtField;
    @FXML private TextField contactTxtField;
    @FXML private ComboBox typeCombo;
    @FXML private DatePicker apptDatePicker;
    @FXML private ComboBox startCombo;
    @FXML private ComboBox endCombo;
    @FXML private TextField urlTxtField;
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    private final DateTimeFormatter datetimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    ObservableList<String> timeOptions = FXCollections.observableArrayList();
    private int selectedCustomerId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize Customers table using Lambdas
        PropertyValueFactory<Customer, Integer> customerIdValue = new PropertyValueFactory<>("CustomerID");
        idCol.setCellValueFactory(customerIdValue);
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        phoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        customerTable.setItems(Customer.getCurrentCustomers());

        populateTimeCombo();
        populateTypeCombo();
        apptDatePicker.setValue(LocalDate.now());
        nameTxtField.setDisable(true);
        typeCombo.setValue(""); // Allows empty fields exception to properly check the combo box.
    }


    // Saves information to appointments table.
    @FXML
    public void saveButtonAction (ActionEvent event) throws SQLException, IOException, ParseException {
        int appointmentId = -1;
        String customerName = nameTxtField.getText();
        String userName = User.getUsername();
        String title = titleTxtField.getText();
        String desc = descTxTField.getText();
        String location = locationTxtField.getText();
        String contact = contactTxtField.getText();
        String type = typeCombo.getValue().toString();
        String date = apptDatePicker.getValue().toString();
        String startDate = apptDatePicker.getValue().toString() + " " + startCombo.getValue().toString();
        String endDate = apptDatePicker.getValue().toString() + " " + endCombo.getValue().toString();
        String url = urlTxtField.getText();
        int customerId = selectedCustomerId;
        int userId = getUserID();
        String errorMsg = Exceptions.emptyAppFields(customerName, title, desc, location, contact, type, url, date, startDate, endDate); //Checks for empty fields.
        String errorMsgTimes = Exceptions.invalidTimeFields(startDate, endDate, appointmentId);


        if(errorMsg.length() > 1) {  //Checks for empty fields and returns custom error message prompt based on empty fields.
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Empty Fields Error");
            alert.setHeaderText("You may not have empty fields.");
            alert.setContentText(errorMsg);
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return;
        }

        if(errorMsgTimes.length() > 1) { // Checks to see if there are any scheduling conflicts.
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Scheduliung Conflict");
            alert.setHeaderText("There was a scheduling conflict");
            alert.setContentText(errorMsgTimes);
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return;
        }



        Appointment.addAppointment(customerId, userId, title, desc, location, contact, type, url, startDate, endDate, userName);

        // Prompts user with success screen then returns user to main screen when OK button is pressed.
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Add Appointment Confirmation");
        alert.setHeaderText("You've added a new appointment for " + customerName);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }


    }

    @FXML
    private void populateTypeCombo() {
        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
        appointmentTypes.add("Consultation");
        appointmentTypes.add("Check-Up");
        appointmentTypes.add("Cancelation");
        appointmentTypes.add("Other");
        typeCombo.setItems(appointmentTypes);

    }

        // Populates Start and End time combo boxes within business hours and set defaults.
        // Business Hours: 8:00 am - 5:00 pm
        // No appointments first or last hour of day for set up and closing.
    @FXML
    private void populateTimeCombo() {
        LocalTime time = LocalTime.of(9, 0, 0);
        for (int i = 0; i < 15; i++){
            timeOptions.add(time.format(timeFormat));
            time = time.plusMinutes(30);
        }
        endCombo.setItems(timeOptions);
        startCombo.setItems(timeOptions);
        startCombo.setValue(LocalTime.of(9,0,0));
        endCombo.setValue(LocalTime.of(9,30,0));
    }



    @FXML
    void selectCustButtonAction() throws SQLException {
        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            int customerId = customer.getCustomerID();
            String customerName = "";

            // Retrieves customer name from database.
            PreparedStatement statement = dbConnect.prepareStatement("SELECT customerId, customerName FROM customer " +
                    "WHERE customerId =" + customerId + ";");

            // Sets customer name text field and stores customer ID variable for later INSERT.
            ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    customerName = resultSet.getString("customerName");
                    selectedCustomerId = resultSet.getInt("customerId");

                }
            nameTxtField.setText(customerName);
        }
    }

    @FXML
    void cancelButtonAction(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Canceling will erase all unsaved changes and return to the Main Screen.\nWould you like to continue?");

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK) // Lambdas used in Alerts, makes code more easily understood.
                .ifPresent(response -> {
                    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    Object scene = null;
                    try {
                        scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setScene(new Scene((Parent) scene));
                    stage.show();
                });
    }

    @FXML
    void newCustomerButtonAction(ActionEvent event) throws IOException{
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


}
