package Controller.Java;

import Intro.Java.Main;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.Socket;

public class ControllerCommandFrame {

    public static Stage stage;
    public static Socket connetion;

    @FXML Label target_label;
    @FXML Label myIP_label;
    @FXML Label hostIP_label;
    @FXML Label port_label;

    public void initialize(){

        if(connetion == null)
            new ClientConnection();
        else
            new ClientConnection(connetion);

        target_label.setText("Target: None :c");
        hostIP_label.setText("Host IP: " + Main.ip);
        port_label.setText("Port: " + Main.port);
        myIP_label.setText(Main.ip);

    }

    public static void makeFrame(Parent root){
        stage = new Stage();

        stage.setTitle("Controller");
        stage.setScene(new Scene(root, 630, 400));
        stage.show();
    }

}
