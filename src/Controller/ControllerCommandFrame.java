package Controller;

import Controller.Server.ClientConnection;
import Intro.ConnectionConfigFrame;
import Intro.ConnectionTypeFrame;
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
        hostIP_label.setText("Host IP: " + ConnectionConfigFrame.ip);
        port_label.setText("Port: " + ConnectionConfigFrame.port);
        ClientConnection clientConnection = new ClientConnection(ConnectionConfigFrame.ip, ConnectionConfigFrame.port);

    }

    public static void makeFrame(Parent root){

        stage.setTitle("Controller");
        stage.setScene(new Scene(root, 630, 400));
        stage.show();
    }

}
