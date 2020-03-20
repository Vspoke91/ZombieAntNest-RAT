package Intro;

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

    @FXML Label ip_label;
    @FXML Label errorWifi_label;
    @FXML Label error_label;
    @FXML Button host_button;
    @FXML Button controller_button;

    public ConnectionTypeFrame(){
        me = this;
    }

    public void initialize() throws IOException {

        errorWifi_label.setVisible(false);

        showErrorMessage("testing", 0);

        try {
            Main.ip = new BufferedReader(new InputStreamReader(new URL("http://myexternalip.com/raw").openStream())).readLine(); //reads line from a website to get ip
        } catch (IOException e) {

            errorWifi_label.setVisible(true);
            controller_button.setDisable(true);
        }


        //sets ip for label and disables buttons if not found
        if(Main.ip == null) {

            Main.ip = "Not found!";
            showErrorMessage("IP not found!", 5);
            host_button.setDisable(true);
        }

        ip_label.setText("My IP: " + Main.ip);

        ControllerConfigFrame.makeFrame(FXMLLoader.load(getClass().getResource("../resources/ControllerConfigFrame.fxml")));
        HostConfigFrame.makeFrame(FXMLLoader.load(getClass().getResource("../resources/HostConfigFrame.fxml")));

        ControllerConfigFrame.stage.hide();
        HostConfigFrame.stage.hide();
    }

    public void buttonPressed(ActionEvent event)  {

        stage.hide();

        if(controller_button.equals(event.getSource()))
            ControllerConfigFrame.stage.show();
        else if (host_button.equals(event.getSource()))
            HostConfigFrame.stage.show();
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

    static void makeFrame(Parent load){

        stage = new Stage();
        stage.setTitle("ZAN - Welcome");
        stage.setScene(new Scene(load, 300, 300));
        stage.show();
    }
}
