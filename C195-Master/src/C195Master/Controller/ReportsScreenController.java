package C195Master.Controller;

import C195Master.Model.Report;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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

public class ReportsScreenController implements Initializable {

    @FXML private Button cancelButton;
    @FXML private TableView byTypeTable;
    @FXML private TableColumn<Report, String> typeMonthCol;
    @FXML private TableColumn<Report, String> typeTypeCol;
    @FXML private TableColumn<Report, String> typeCountCol;
    @FXML private TableView consultantScheduleTable;
    @FXML private TableColumn<Report, String> consultantCol;
    @FXML private TableColumn<Report, String> idCol;
    @FXML private TableColumn<Report, String> nameCol;
    @FXML private TableColumn<Report, String> startCol;
    @FXML private TableColumn<Report, String> endCol;
    @FXML private TableColumn<Report, String> typeCol;
    @FXML private TableView customerTypeTable;
    @FXML private TableColumn<Report, String> customerTypeName;
    @FXML private TableColumn<Report, String> customerType;
    @FXML private TableColumn<Report, String> customerTypeCount;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Sets up and populates by type table view for number of types by month report.
        typeMonthCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMonth()));
        typeTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        typeCountCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeCount()));
        try {
            byTypeTable.setItems(Report.getReportTypeByMonth());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Sets up and populates consultant schedule report table.
        // Orders by consultant then start time.
        consultantCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        startCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        endCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        try {
            consultantScheduleTable.setItems(Report.getUserSchedule());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Sets up and populates customer appointment types and count table
        customerTypeName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        customerType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        customerTypeCount.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeCount()));
        try {
            customerTypeTable.setItems(Report.getCustomerTypeCount());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Returns the User to MainScreen
    @FXML
    void cancelButtonAction(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to return to the Main Screen?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
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


}
