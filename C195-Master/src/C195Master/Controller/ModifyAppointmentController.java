package C195Master.Controller;

import C195Master.Model.Appointment;
import C195Master.Model.User;
import C195Master.Utilities.Exceptions;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import static C195Master.Model.User.getUserID;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class ModifyAppointmentController implements Initializable {
    @FXML private TextField nameTxtField;
    @FXML private TextField titleTxtField;
    @FXML private TextField descTxtField;
    @FXML private TextField locationTxtField;
    @FXML private TextField contactTxtField;
    @FXML private TextField urlTxtField;
    @FXML private ComboBox typeCombo;
    @FXML private ComboBox startCombo;
    @FXML private ComboBox endCombo;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private DatePicker modifyDatePicker;
    Appointment selectedAppointment = new Appointment();
    ObservableList<String> timeOptions = FXCollections.observableArrayList();
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    private final DateTimeFormatter datetimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int appointmentId = Appointment.getPassAppointmentId();
        Appointment appointment = null;
        try {
            appointment = Appointment.getAppointment(appointmentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Changes String times from database to LocalTime variables and formatted.
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        final String startTime = appointment.getStart();
        final String endTime = appointment.getEnd();
        final LocalTime startLocalTime = LocalTime.parse(startTime, formatter);
        final LocalTime endLocalTime = LocalTime.parse(endTime, formatter);

        // Set all fields to current appointment data.
        nameTxtField.setText(appointment.getCustomerName());
        titleTxtField.setText(appointment.getTitle());
        descTxtField.setText(appointment.getDescription());
        locationTxtField.setText(appointment.getLocation());
        contactTxtField.setText(appointment.getContact());
        urlTxtField.setText(appointment.getUrl());
        typeCombo.setValue(appointment.getType());
        modifyDatePicker.setValue(appointment.getDay());
        startCombo.setValue(startLocalTime);
        endCombo.setValue(endLocalTime);
        selectedAppointment.setCustomerID(appointment.getCustomerID());
        selectedAppointment.setAppointmentID(appointment.getAppointmentID());

        // Populates available times and types of appointments.
        populateTypeCombo();
        populateTimeCombo();
    }

    @FXML
    void cancelButtonAction(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Canceling will erase all unsaved changes and return to the Main Screen.\nWould you like to continue?");

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK); {
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
                }
    }

    @FXML
    private void populateTimeCombo() {
        LocalTime time = LocalTime.of(9, 0, 0);
        for (int i = 0; i < 15; i++){
            timeOptions.add(time.format(timeFormat));
            time = time.plusMinutes(30);
        }
        endCombo.setItems(timeOptions);
        startCombo.setItems(timeOptions);
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


    @FXML
    void saveButtonAction(ActionEvent event) throws ParseException, SQLException, IOException {
        int appointmentId = selectedAppointment.getAppointmentID();
        String customerName = nameTxtField.getText();
        String userName = User.getUsername();
        String title = titleTxtField.getText();
        String desc = descTxtField.getText();
        String location = locationTxtField.getText();
        String contact = contactTxtField.getText();
        String type = typeCombo.getValue().toString();
        String date = modifyDatePicker.getValue().toString();
        String startDate = modifyDatePicker.getValue().toString() + " " + startCombo.getValue().toString();
        String endDate = modifyDatePicker.getValue().toString() + " " + endCombo.getValue().toString();
        String url = urlTxtField.getText();
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

        try {
            Appointment.updateAppointment(appointmentId, userId, title, desc, location, contact, type, url, startDate, endDate, userName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Prompts user with success screen then returns user to main screen when OK button is pressed.
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Modify Appointment Confirmation");
        alert.setHeaderText("You've modified an appointment for " + customerName);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK); {

                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
        }
    }
}
