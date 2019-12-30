package Intro;

import Host.HostClassHolder;
import Host.TerminalCommandFrame;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectionConfigFrame {

    public static String ip;
    public static int port;
    static Stage stage;


    @FXML Label connectionType_label;
    @FXML Label ip_label;
    @FXML TextField ip_textField;
    @FXML TextField port_textField;

    public ConnectionConfigFrame() {

        IntroClassHolder.setConnectionConfigFrame(this);
    }

    public void initialize() {

        ip_textField.setText(ConnectionTypeFrame.ip);
    }

    public void backButtonPressed() {

        ConnectionTypeFrame.stage.show();
        stage.hide();
    }

    public void connectButtonPressed() throws IOException {

        ip = ip_textField.getText();
        port = Integer.parseInt(port_textField.getText());

        if(ConnectionTypeFrame.connectionType.equals("Host")) {

            TerminalCommandFrame.stage = new Stage();
            TerminalCommandFrame.makeFrame(FXMLLoader.load(getClass().getResource("/Host/TerminalCommandFrame.fxml")));

            stage.hide();
            HostClassHolder.terminalCommandFrame.initialize();
        }
    }

    void resetFields(){

        ip_label.setText("IP:");
        ip_textField.setText(ConnectionTypeFrame.ip);
        ip_textField.setEditable(false);

        if(ConnectionTypeFrame.connectionType.equals("Controller")) {
            ip_label.setText("Host IP:");
            ip_textField.setText("");
            ip_textField.setEditable(true);
        }
    }

    static void makeFrame(Parent root){

        stage.setTitle("Configuration");
        stage.setScene(new Scene(root, 300, 300));
    }
}
