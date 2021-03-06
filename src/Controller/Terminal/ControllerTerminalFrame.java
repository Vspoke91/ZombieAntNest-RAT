package Controller.Terminal;

import Controller.Connection.ControllerConnection;
import Main.Main;
import Utilities.Child.Target;

import Utilities.FrameUtilities;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class ControllerTerminalFrame extends FrameUtilities {

    public static ControllerTerminalFrame me;
    public static Stage stage;
    public static Socket myConnection;
    public static ControllerConnection controllerConnection;
    public static Target target;
    public static boolean isLocalConnection = false;

    @FXML HBox header_pane;
    @FXML Label target_label;
    @FXML Label hostIP_label;
    @FXML Label port_label;
    @FXML ListView target_listView;

    @FXML Label targetIP_label;
    @FXML Label targetInternet_label;
    @FXML Label targetDeviceType_label;

    @FXML Button flashLight_button;
    @FXML Tab device_tab;
    @FXML Tab commands_tab;

    @FXML Button exit_button;
    @FXML Button hide_button;

    public ControllerTerminalFrame(){
        me = this;
    }

    public void initialize(){

        startFrameUtilities(stage, header_pane, exit_button, hide_button);

        device_tab.setDisable(true);
        commands_tab.setDisable(true);
        target_label.setText("Target: None :c");
        hostIP_label.setText("Mother IP: " + Main.ip);
        port_label.setText("Port: " + Main.port);

        selectTarget_OnAction();

        controllerConnection = new ControllerConnection(isLocalConnection, myConnection);

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
                targetDeviceType_label.setText("OS: "+ target.os);

                updateTargetInfo();

            }else{

                Platform.runLater(() -> device_tab.setDisable(true));
                Platform.runLater(() -> commands_tab.setDisable(true));
                Platform.runLater(() -> target_label.setText("Target: None :c"));
            }
        });

    }

    public void flashlight_ButtonAction(){

        if(target.isFlashlightOn)
            controllerConnection.sendMessageTo(target.ip,"setFlashLight","false");
        else
            controllerConnection.sendMessageTo(target.ip,"setFlashLight","true");
    }

    public void addTarget(String target){

        Platform.runLater(() -> target_listView.getItems().add(target));
    }

    public void deleteTarget(String target){

        Platform.runLater(() -> target_listView.getItems().remove(target));
    }

    public void updateTargetInfo(){

        if (target.isFlashlightOn)
            Platform.runLater(() -> flashLight_button.setText("Flashlight: ON"));
        else
            Platform.runLater(() -> flashLight_button.setText("Flashlight: OFF"));

    }

    public static void makeFrame(boolean makeLocalConnection, Socket connection) throws IOException {

        isLocalConnection = makeLocalConnection;
        myConnection = connection;

        stage = new Stage();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Terminal - Controller");
        stage.setScene(new Scene(FXMLLoader.load(ControllerTerminalFrame.class.getResource("ControllerTerminalFrame.fxml"))));

        stage.show();
    }

}
