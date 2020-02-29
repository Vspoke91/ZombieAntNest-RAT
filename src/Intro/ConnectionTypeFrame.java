package Intro;

import Controller.ControllerCommandFrame;
import Extras.LogText;
import Host.Server.ConnectionThread;
import Intro.Controller.ControllerConfigFrame;
import Intro.Host.HostConfigFrame;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static java.lang.Thread.sleep;

public class ConnectionTypeFrame {

    public static ConnectionTypeFrame me;
    public static Stage stage;

    static String ip;
    static String connectionType;

    @FXML Label ip_label;
    @FXML Label error_label;
    @FXML Button host_button;
    @FXML Button controller_button;

    public ConnectionTypeFrame(){
        me = this;
    }

    public void initialize() throws IOException {

        showErrorMessage("testing", 0);

        ip = new BufferedReader(new InputStreamReader(new URL("http://myexternalip.com/raw").openStream())).readLine(); //reads line from a website to get ip

        //sets ip for label and disables buttons if not found
        if(ip == null) {

            ip = "Not found!";
            showErrorMessage("IP not found!", 5);
            host_button.setDisable(true);
        }

        ip_label.setText("My IP: " + ip);

        ControllerConfigFrame.makeFrame(FXMLLoader.load(getClass().getResource("Controller/ControllerConfigFrame.fxml")), new Stage());
        HostConfigFrame.makeFrame(FXMLLoader.load(getClass().getResource("Host/HostConfigFrame.fxml")), new Stage());

        ControllerConfigFrame.stage.hide();
        HostConfigFrame.stage.hide();
    }

    public void buttonPressed(ActionEvent event)  {

        if(controller_button.equals(event.getSource()))
            ControllerConfigFrame.stage.show();
        else if (host_button.equals(event.getSource()))
            HostConfigFrame.stage.show();
        stage.hide();

        /*
        //makes a frame if configuration frame is null
        if(ConnectionConfigFrame.stage == null) {

            ConnectionConfigFrame.stage = new Stage();
            ConnectionConfigFrame.makeFrame(FXMLLoader.load(getClass().getResource("ConnectionConfigFrame.fxml")));
        }

        ConnectionConfigFrame.me.connectionType_label.setText(connectionType);//sets type  of connection to label
        ConnectionConfigFrame.me.resetFields(); //resets field in case of labels changing

        ConnectionConfigFrame.stage.show();
        stage.hide();

         */
    }

    public void showErrorMessage(String message, int seconds){

        new Thread(() -> {
            Platform.runLater(() -> error_label.setText(message));
            Platform.runLater(() -> error_label.setVisible(true));

            try {
                synchronized (this) { sleep(seconds * 1000); }
            } catch (InterruptedException e) { e.printStackTrace(); }

            Platform.runLater(() -> error_label.setVisible(false));
        }).start();
    }

    static void makeFrame(Parent load, Stage stage){

        ConnectionTypeFrame.stage = stage;
        stage.setTitle("ZAN - Welcome");
        stage.setScene(new Scene(load, 300, 275));
        stage.show();
    }
}
