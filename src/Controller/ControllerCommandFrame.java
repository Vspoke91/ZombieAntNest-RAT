package Controller;

import Controller.Server.ClientConnection;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerCommandFrame {

    public static Stage stage;


    public void initialize(){

        ClientConnection clientConnection = new ClientConnection();
    }

    public static void makeFrame(Parent root){

        stage.setTitle("Controller");
        stage.setScene(new Scene(root, 630, 400));
        stage.show();
    }

}
