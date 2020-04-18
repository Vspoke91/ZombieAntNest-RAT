package Controller.Java;

import Host.Java.ConnectionThread;
import Host.Java.HostConnection;
import Intro.Java.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.Socket;

public class ControllerCommandFrame {

    public static ControllerCommandFrame me;
    public static Stage stage;
    public static Socket connection;
    public static ClientConnection clientConnection;

    public static Target target;

    @FXML Label target_label;
    @FXML Label myIP_label;
    @FXML Label hostIP_label;
    @FXML Label port_label;
    @FXML ListView target_listView;

    @FXML Label targetIP_label;
    @FXML Label targetInternet_label;
    @FXML Label targetDeviceType_label;

    @FXML Button flashLight_button;

    public ControllerCommandFrame(){
        me = this;
    }

    public void initialize(){

        target_label.setText("Target: None :c");
        hostIP_label.setText("Host IP: " + Main.ip);
        port_label.setText("Port: " + Main.port);
        myIP_label.setText(Main.ip);

        selectTarget_OnAction();

        if(connection == null)
            clientConnection = new ClientConnection();
        else
            clientConnection = new ClientConnection(connection);

    }

    public void selectTarget_OnAction(){

        target_listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (newValue != null){

                    target = ClientConnection.getTargetInList(newValue);

                    target_label.setText("Target: "+target.ip);

                    targetIP_label.setText("IP: "+target.ip);
                    targetInternet_label.setText("Internet: "+target.internet);
                    targetDeviceType_label.setText("Device Type: "+target.deviceType);
                }else{


                }
            }
        });

    }

    public void setFlashLightButton(boolean state){

        if(state)
            Platform.runLater(() -> flashLight_button.setText("FlashLight: ON"));

        else
            Platform.runLater(() -> flashLight_button.setText("FlashLight: OFF"));
    }

    public void flashlight_onAction(){

        if(target.flashlightState)
            clientConnection.sendMessageTo(target.ip,"sfst","false");
        else
            clientConnection.sendMessageTo(target.ip,"sfst","true");
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
