package Controller.Terminal;

import Controller.Connection.ControllerConnection;
import Main.Main;
import Utilities.Child.Target;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class ControllerTerminalFrame {

    public static ControllerTerminalFrame me;
    public static Stage stage;
    public static Socket connection;
    public static ControllerConnection controllerConnection;

    public static Target target;

    @FXML Label target_label;
    @FXML Label hostIP_label;
    @FXML Label port_label;
    @FXML ListView target_listView;

    @FXML Label targetIP_label;
    @FXML Label targetInternet_label;
    @FXML Label targetDeviceType_label;

    @FXML public Button flashLight_button;
    @FXML Tab device_tab;
    @FXML Tab commands_tab;

    public ControllerTerminalFrame(){
        me = this;
    }

    public void initialize(){

        device_tab.setDisable(true);
        commands_tab.setDisable(true);
        target_label.setText("Target: None :c");
        hostIP_label.setText("Mother IP: " + Main.ip);
        port_label.setText("Port: " + Main.port);

        selectTarget_OnAction();

        if(connection == null)
            controllerConnection = new ControllerConnection();
        else
            controllerConnection = new ControllerConnection(connection);

    }

    public void selectTarget_OnAction(){

        target_listView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {

            if (newValue != null){

                device_tab.setDisable(false);
                commands_tab.setDisable(false);

                target = ControllerConnection.getTargetInList(newValue);

                target_label.setText("Target: "+target.ip);
                targetIP_label.setText("IP: "+target.ip);
                targetInternet_label.setText("Internet: "+target.internet);
                targetDeviceType_label.setText("Device Type: "+target.deviceType);

            }
        });

    }

    public void flashlight_ButtonAction(){

        if(target.flashlightState)
            controllerConnection.sendMessageTo(target.ip,"sfst","false");
        else
            controllerConnection.sendMessageTo(target.ip,"sfst","true");
    }

    public void addTarget(String target){

        Platform.runLater(() -> target_listView.getItems().add(target));
    }

    public void deleteTarget(String target){

        Platform.runLater(() -> target_listView.getItems().remove(target));

        if(target.equals(this.target.ip)){

            Platform.runLater(() -> device_tab.setDisable(true));
            Platform.runLater(() -> commands_tab.setDisable(true));
            Platform.runLater(() -> target_label.setText("Target: None :c"));

        }
    }

    public static void makeFrame() throws IOException {

        stage = new Stage();

        stage.setTitle("Controller");
        stage.setScene(new Scene(FXMLLoader.load(ControllerTerminalFrame.class.getResource("ControllerTerminalFrame.fxml")), 630, 400));

        stage.show();
    }

}
