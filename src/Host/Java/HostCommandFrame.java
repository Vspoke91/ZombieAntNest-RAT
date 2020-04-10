package Host.Java;

import Controller.Java.ClientConnection;
import Controller.Java.ControllerCommandFrame;
import Intro.Java.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;

public class HostCommandFrame {

    public static HostCommandFrame me;
    public static Stage stage;

    public int connectionCounter;

    @FXML Label ip_label;
    @FXML Label port_label;
    @FXML Label connectionCount_label;
    @FXML TextFlow log_textFlow;
    @FXML TextField command_textField;
    @FXML ScrollPane log_ScrollPane;
    @FXML ListView ip_ListView;
    @FXML Button takeControl_Button;

    public HostCommandFrame(){
        me = this;
    }

    public void initialize(){

        ip_label.setText("IP: "+ Main.ip);
        port_label.setText("Port: "+Main.port);

        HostConnection hostConnection = new HostConnection();
    }

    public void takeControlOnAction() throws IOException {

        takeControl_Button.setDisable(true);
        ClientConnection.isLocal = true;
        ControllerCommandFrame.makeFrame(FXMLLoader.load(getClass().getResource("../../Controller/FX/ControllerCommandFrame.fxml")));
    }

    public void logText(String text, String color){

        if(text.trim().length() != 0) {

            Text textX = new Text(text + "\n");
            textX.setFill(Color.web(color));

            log_ScrollPane.setVvalue(1);
            // try/catch runs so it can be implemented with threads.
            try {

                log_textFlow.getChildren().add(textX);
            }catch (java.lang.IllegalStateException e) {

                Platform.runLater(() -> log_textFlow.getChildren().add(textX));
            }
        }
    }

    public void addConnection(ConnectionThread connection){
        connectionCounter++;

        Platform.runLater(() -> ip_ListView.getItems().add(connection));
        Platform.runLater(() -> connectionCount_label.setText("Connections: "+connectionCounter));
    }

    public void checkChange(ConnectionThread connection){

        int index;

        if(ip_ListView.getItems().indexOf(connection) != -1)
            index = ip_ListView.getItems().indexOf(connection);

        else if(ip_ListView.getItems().indexOf(">"+connection) != -1)
            index = ip_ListView.getItems().indexOf(">"+connection);

        else if(ip_ListView.getItems().indexOf("<"+connection) != -1)
            index = ip_ListView.getItems().indexOf("<"+connection);
        else
            index = -1;

        if(ip_ListView.getItems().get(index).equals(">"+connection))
            Platform.runLater(() -> ip_ListView.getItems().set(index,"<"+connection));
        else
            Platform.runLater(() -> ip_ListView.getItems().set(index,">"+connection));
    }

    public void deleteConnection(ConnectionThread connection){

        connectionCounter--;
        Platform.runLater(() -> ip_ListView.getItems().remove(connection));
        Platform.runLater(() -> connectionCount_label.setText("Connections: "+connectionCounter));
    }

    //when a command is enter
    public void onEnter(ActionEvent event){



    }

    public static void makeFrame(Parent root){

        stage = new Stage();

        stage.setTitle("Terminal");
        stage.setScene(new Scene(root, 630, 400));
        stage.show();
    }
}
