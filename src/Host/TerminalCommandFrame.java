package Host;

import Host.Server.HostConnection;
import Intro.ConnectionConfigFrame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TerminalCommandFrame {

    public static Stage stage;

    @FXML Label ip_label;
    @FXML Label port_label;
    @FXML Label connectionCount_label;
    @FXML TextFlow log_textFlow;
    @FXML TextField command_textField;

    public TerminalCommandFrame(){

        HostClassHolder.setTerminalCommandFrame(this);
    }

    public void initialize(){

        ip_label.setText("IP: "+ConnectionConfigFrame.ip);
        port_label.setText("Port: "+ConnectionConfigFrame.port);

        HostConnection hostConnection = new HostConnection();
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
