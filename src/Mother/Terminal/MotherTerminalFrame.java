package Mother.Terminal;

import Configuration.MenuConfigFrame;
import Configuration.Mother.MotherConfigFrame;
import Controller.Connection.ControllerConnection;
import Controller.Terminal.ControllerTerminalFrame;
import Mother.Connection.ClientConnection;
import Mother.Connection.MotherConnection;
import Main.Main;
import Utilities.Child.Child;
import Utilities.Child.Controller;
import Utilities.Child.Target;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MotherTerminalFrame {

    public static MotherTerminalFrame me;
    public static Stage stage;

    public int connectionCounter;
    public ClientConnection selectedConnection;

    @FXML Label pablo_label;
    @FXML Label selectedIP_label;
    @FXML Label selectedDeviceType_label;
    @FXML Label selectedConnectionType_label;
    @FXML Label ip_label;
    @FXML Label port_label;
    @FXML Label connectionCount_label;
    @FXML TextFlow log_textFlow;
    @FXML TextField command_textField;
    @FXML ScrollPane log_ScrollPane;
    @FXML ListView ip_ListView;
    @FXML Button takeControl_Button;

    public MotherTerminalFrame(){
        me = this;
    }

    public void initialize(){

        ip_label.setText("IP: "+ Main.ip);
        port_label.setText("Port: "+Main.port);

        stage.setOnCloseRequest(event -> { System.exit(0); });

        startPabloAnimation();
        selectingTarget_Listener();

        new MotherConnection();
    }

    public void startPabloAnimation(){

        new Thread(() -> {

            while(true){

                Platform.runLater(() -> pablo_label.setText("[o.o]"));

                try { synchronized (this) { wait(6000); }} catch (InterruptedException e) { e.printStackTrace(); }

                Platform.runLater(() -> pablo_label.setText("[-.o]"));

                try { synchronized (this) { wait(1000); }} catch (InterruptedException e) { e.printStackTrace(); }

                Platform.runLater(() -> pablo_label.setText("[o.o]"));

                try { synchronized (this) { wait(6000); }} catch (InterruptedException e) { e.printStackTrace(); }

                Platform.runLater(() -> pablo_label.setText("[o.+]"));

                try { synchronized (this) { wait(1000); }} catch (InterruptedException e) { e.printStackTrace(); }
            }

        }).start();

    }

    public void selectingTarget_Listener(){

        ip_ListView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {

            if (newValue != null){

                if(newValue.startsWith("<") || newValue.startsWith(">"))

                    newValue = newValue.substring(1);

                selectedConnection = MotherConnection.getConnectionInList(newValue);

                selectedIP_label.setText("IP: "+selectedConnection.info.ip);
                selectedDeviceType_label.setText("OS: " + selectedConnection.info.os);
                selectedConnectionType_label.setText("Type: " + selectedConnection.info.type);

                if(selectedConnection.info.type == Child.Type.CONTROLLER){

                    Controller controller = (Controller) selectedConnection.info;

                    //TODO add more infomation to show.

                } else if (selectedConnection.info.type == Child.Type.TARGET){

                    Target target = (Target) selectedConnection.info;
                    //TODO add more infomation to show.
                }
            }
        });

    }

    public void takeControl_OnAction() throws IOException {

        takeControl_Button.setDisable(true);

        ControllerTerminalFrame.makeFrame(true, null);
    }

    public void logText(String text, String color){

        if(text.trim().length() != 0) {

            Text textX = new Text(text + "\n");
            textX.setFill(Color.web(color));

            log_ScrollPane.setVvalue(1);

            Platform.runLater(() -> log_textFlow.getChildren().add(textX));
        }
    }

    public void addConnection(String connection){

        connectionCounter++;

        logText("["+connection+"] has connected!","2daa09");

        Platform.runLater(() -> ip_ListView.getItems().add(connection));
        Platform.runLater(() -> connectionCount_label.setText("Connections: "+connectionCounter));

    }

    public void connectionAnimation(String client){

        final int index;

        if(ip_ListView.getItems().contains(client))
            index = ip_ListView.getItems().indexOf(client);

        else if(ip_ListView.getItems().contains(">"+client))
            index = ip_ListView.getItems().indexOf(">"+client);

        else if(ip_ListView.getItems().contains("<"+client))
            index = ip_ListView.getItems().indexOf("<"+client);
        else
            index = -1;

        if(ip_ListView.getItems().get(index).equals(">"+client))
            Platform.runLater(() -> ip_ListView.getItems().set(index,"<"+client));
        else
            Platform.runLater(() -> ip_ListView.getItems().set(index,">"+client));
    }

    public void deleteConnection(String client){

        connectionCounter--;

        final int index;

        if(ip_ListView.getItems().contains(client))
            index = ip_ListView.getItems().indexOf(client);

        else if(ip_ListView.getItems().contains(">"+client))
            index = ip_ListView.getItems().indexOf(">"+client);

        else if(ip_ListView.getItems().contains("<"+client))
            index = ip_ListView.getItems().indexOf("<"+client);

        else
            index = -1;

        if(selectedConnection != null && selectedConnection.getName().equals(client)){ // checks for null because selected can be null

            Platform.runLater(() -> selectedIP_label.setText("IP: ----"));
            Platform.runLater(() -> selectedDeviceType_label.setText("OS: ----"));
            Platform.runLater(() -> selectedConnectionType_label.setText("Type: ----"));
        }

        Platform.runLater(() -> ip_ListView.getItems().remove(index));
        Platform.runLater(() -> connectionCount_label.setText("Connections: " + connectionCounter));
    }

    //when a command is enter
    public void onEnter(ActionEvent event){

        String command = command_textField.getText();

        command_textField.setText("");

        if(command.trim().length() != 0){

            switch (command.toLowerCase()){

                case("clear"):

                    log_textFlow.getChildren().clear();
                    logText("Logs cleared...","#00b377");

                    break;

                case("exit"):

                    System.exit(0);

                    break;


                 /*   TODO: Restart Does not work.
                case("restart"):

                    stage.hide();

                    MotherConnection.stop = true;
                    MotherConnection.makeFakeConnection();

                    MenuConfigFrame.stage.show();

                    break;

                  */

                default:

                    logText("Command '"+command+"' not found!","#00b377");

                    break;

            }

        }


    }

    public static void makeFrame() throws IOException {

        stage = new Stage();

        stage.setTitle("Terminal - Mother");
        stage.setScene(new Scene(FXMLLoader.load(MotherTerminalFrame.class.getResource("MotherTerminalFrame.fxml")), 630, 400));
        stage.show();
    }
}
