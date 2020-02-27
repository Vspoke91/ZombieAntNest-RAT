package Host;

import Host.Server.ConnectionThread;
import Host.Server.HostConnection;
import Intro.ConnectionConfigFrame;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TerminalCommandFrame {

    public static TerminalCommandFrame me;
    public static Stage stage;
    public int connectionCounter;

    @FXML Label ip_label;
    @FXML Label port_label;
    @FXML Label connectionCount_label;
    @FXML TextFlow log_textFlow;
    @FXML TextField command_textField;
    @FXML ScrollPane log_ScrollPane;
    @FXML ListView ip_ListView;

    public TerminalCommandFrame(){
        me = this;
    }

    public void initialize(){

        ip_label.setText("IP: "+ConnectionConfigFrame.ip);
        port_label.setText("Port: "+ConnectionConfigFrame.port);

        HostConnection hostConnection = new HostConnection();
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

    public void deleteConnection(ConnectionThread connection){

        connectionCounter--;
        Platform.runLater(() -> ip_ListView.getItems().remove(connection));
        Platform.runLater(() -> connectionCount_label.setText("Connections: "+connectionCounter));
    }

    //when a command is enter
    public void onEnter(ActionEvent event){

        System.out.println("ready to hack");

    }

    public static void makeFrame(Parent root){

        stage.setTitle("Terminal");
        stage.setScene(new Scene(root, 630, 400));
        stage.show();
    }
}
