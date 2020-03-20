package Controller;

import Controller.Server.ClientConnection;
import Intro.ConnectionTypeFrame;
import Intro.Main;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControllerCommandFrame {

    public static Stage stage;

    @FXML Label hostIP_label;
    @FXML Label port_label;

    public void initialize(){
        hostIP_label.setText("Host IP: " + Main.ip);
        port_label.setText("Port: " + Main.port);
        ClientConnection clientConnection = new ClientConnection();

    }

    public static void makeFrame(Parent root){

        stage.setTitle("Controller");
        stage.setScene(new Scene(root, 630, 400));
        stage.show();
    }

}
