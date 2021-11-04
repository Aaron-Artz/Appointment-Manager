package C195Master.Controller;

import C195Master.Model.Appointment;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import C195Master.Model.User;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class LoginScreenController implements Initializable {

    @FXML private Label loginLable;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private TextField usernameTxtField;
    @FXML private PasswordField passwordTxtField;
    @FXML private Button exitButton;
    @FXML private Button loginButton;
    Logger logger = Logger.getLogger("UserLog.txt");
    ResourceBundle resources;
    Locale locale;


    // LOGIN screen loads in English or Spanish depending on locale.
    // All spanish translations were done with google translate, translations may not be perfectly accurate.
    // In a real life situation I would reach out to someone who speaks the language for validation.


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.locale = Locale.getDefault();
            this.resources = ResourceBundle.getBundle("C195Master.LanguageResources.MessagesBundle", this.locale);
            //resources = ResourceBundle.getBundle("C195Master.LanguageResources.MessagesBundle", Locale.getDefault());
            System.out.println("Resource bundle loaded");
            this.loginLable.setText(this.resources.getString("login"));
            this.usernameLabel.setText(this.resources.getString("username"));
            this.passwordLabel.setText(this.resources.getString("password"));
            this.exitButton.setText(this.resources.getString("exitBtn"));
            this.loginButton.setText(this.resources.getString("loginBtn"));
        } catch (MissingResourceException e) {
            System.out.println("Missing resource bundle");
        }
    }


// Exit Button Terminates database connection with warning prompt

    public void exitButtonAction (ActionEvent exitButtonAction) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("exitTitle"));
        alert.setHeaderText(resources.getString("exitHeader"));
        alert.setContentText(resources.getString("exitContent"));
        alert.showAndWait()
                .filter(responce -> responce == ButtonType.OK) //LAMBDA used to make code easily understood.
                .ifPresent(responce -> System.exit(0));



    }


// Log In button directs to Main Screen.

    @FXML
    private void loginButtonAction(ActionEvent event) throws IOException, SQLException {
        String username = usernameTxtField.getText();
        String password = passwordTxtField.getText();
        User user = new User();
        FileHandler fileHandler = new FileHandler("UserLog.txt", true);
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        int loginID = User.currentUserID(username);

        Boolean validation = User.validateLogin(username, password);

        // Verifies correct Username and Password are entered
        // If not, gives Warning Alert.
        if (validation) {
            user.setUsername(username);
            user.setPassword(password);
            user.setUserID(loginID);

            //Loges username and user ID into UserLog file
            logger.log(Level.INFO, "User ID: " + user.getUserID() + " User: " + user.getUsername());

            // Alerts user of upcoming appointments after login button press.
            if(Appointment.getUpcomingAppointment()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle(resources.getString("appointmentTitle"));
                alert.setHeaderText(resources.getString("appointmentHeader"));
                alert.setContentText(resources.getString("appointmentContent"));
                alert.showAndWait();
            }

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
        else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle(resources.getString("loginErrorTitle"));
            alert.setHeaderText(resources.getString("loginErrorHeader"));
            alert.setContentText(resources.getString("loginErrorContent"));
            alert.showAndWait().filter(response -> response == ButtonType.OK); // Lambdas used in Alerts, makes code more easily understood.
            System.out.println("NOT A VALID USER!");
        }
    }
}
