package Controller.Java;

import Intro.Java.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.Socket;

public class ControllerCommandFrame {

    public static ControllerCommandFrame me;
    public static Stage stage;
    public static Socket connection;
    public static ClientConnection clientConnection;

    @FXML Label target_label;
    @FXML Label myIP_label;
    @FXML Label hostIP_label;
    @FXML Label port_label;
    @FXML ListView target_listView;

    public ControllerCommandFrame(){
        me = this;
    }

    public void initialize(){

        if(connection == null)
            clientConnection = new ClientConnection();
        else
            clientConnection = new ClientConnection(connection);

        target_label.setText("Target: None :c");
        hostIP_label.setText("Host IP: " + Main.ip);
        port_label.setText("Port: " + Main.port);
        myIP_label.setText(Main.ip);

    }

    public void flashlight_onAction(){


    }

    public void addTarget(String target){

        Platform.runLater(() -> target_listView.getItems().add(target));
    }

    public void deleteTarget(String target){

        Platform.runLater(() -> target_listView.getItems().remove(target));
    }

    public static void makeFrame(Parent root){

        stage = new Stage();

        stage.setTitle("Controller");
        stage.setScene(new Scene(root, 630, 400));
        stage.show();
    }

}
