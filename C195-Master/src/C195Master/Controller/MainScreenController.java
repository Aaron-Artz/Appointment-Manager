package C195Master.Controller;

import C195Master.Model.Appointment;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class MainScreenController implements Initializable{
    @FXML private Label headerLabel;
    @FXML private RadioButton weeklyRadioButton;
    @FXML private RadioButton monthlyRadioButton;
    @FXML private TableView appointmentsTable;
    @FXML private TableColumn<Appointment, String> idCol;
    @FXML private TableColumn<Appointment, String> custCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, String> startCol;
    @FXML private TableColumn<Appointment, String> endCol;
    @FXML private TableColumn<Appointment, String> employeeCol;
    @FXML private Button addButton;
    @FXML private Button modifyButton;
    @FXML private Button deleteButton;
    @FXML private Button custLookupButton;
    @FXML private Button reportsButton;
    @FXML private Button logoutButton;
    private boolean isWeekly = true;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Loads current appointments and sets up view with Lambdas
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        custCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        startCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        endCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        employeeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));


        appointmentsTable.setItems(Appointment.getCurrentAppointments(isWeekly));
        weeklyRadioButton.setSelected(true);
        appointmentsTable.getSortOrder().addAll(startCol);
    }


    //Delete button deletes selected appointment from table.
    public void deleteButtonAction() throws SQLException {

        if (appointmentsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("There is no appointment selected.");
            alert.setContentText("To delete an appointment, first select on from the table.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment Confirmation");
        alert.setHeaderText("You are about to delete an Appointment!");
        alert.setContentText("Press OK to delete appointment \nPress Cancle to go back");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK) // Lambdas used in Alerts, makes code more easily understood.
                .ifPresent(response -> {
                    Appointment appointment = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
                    int appointmentId = appointment.getAppointmentID();
                    System.out.println("Appoingment ID: " + appointmentId);
                    try {
                        Appointment.deleteAppointment(appointmentId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    appointmentsTable.setItems(Appointment.getCurrentAppointments(isWeekly));
                });
    }

    // Populates table with weekly appointments when selected.
    public void weeklyRadioAction() {
        isWeekly = true;
        appointmentsTable.setItems(Appointment.getCurrentAppointments(isWeekly));
    }

    // Populates table with monthly appointments when selected.
    public void monthlyRadioAction() {
        isWeekly = false;
        appointmentsTable.setItems(Appointment.getCurrentAppointments(isWeekly));
    }


    // Logout Button Action,,, Returns user to log-in screen.
    @FXML
    void logoutButtonAction(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about a Log out of the scheduling system.\nWould you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }
    }
    // Redirects to AddAppointmentScreen
    @FXML
    void addButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("AddAppointmentScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


    // Redirects to ReportsScreen
    @FXML
    void reportsButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("ReportsScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    void modifyButtonAction(ActionEvent event) throws IOException {
        if (appointmentsTable.getSelectionModel().getSelectedItem() != null) {
            Appointment appointment = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
            int appointmentId = appointment.getAppointmentID();

            Appointment.setPassAppointmentId(appointmentId); // Passes appointmentId into passAppointmentId to be retrieved in ModifyController.
            System.out.println(Appointment.getPassAppointmentId());

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("ModifyAppointmentScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Modify Appointment Error");
            alert.setHeaderText("Unable to modify appointment!");
            alert.setContentText("Make sure an appointment is selected in the appointment table.");
            alert.showAndWait();
        }

    }
// Direct user to the CustomerScreen
    @FXML
    void custLookupButtonAction(ActionEvent event) throws IOException{
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
}
