package C195Master;

import C195Master.Utilities.DatabaseConnect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class C195Master extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Controller/LoginScreen.fxml"));
        primaryStage.setTitle("C195 Scheduling System");
        primaryStage.setScene(new Scene(root, 700, 300));
        primaryStage.show();
        System.out.println("Application loaded");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DatabaseConnect.dbConnect();
        launch(args);
        DatabaseConnect.dbDisconnect();
    }
}



