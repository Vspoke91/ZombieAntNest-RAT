package Intro.Java.Controller;

import Controller.Java.ControllerCommandFrame;
import Intro.Java.ConnectionTypeFrame;
import Intro.Java.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class ControllerConfigFrame {

    public static ControllerConfigFrame me;
    public static Stage stage;

    @FXML Label error_label;
    @FXML TextField hostIP_textField;
    @FXML TextField port_textField;
    @FXML CheckBox save_checkBox;
    @FXML Button connect_button;

    public ControllerConfigFrame(){
        me = this;
    }

    public void initialize() throws FileNotFoundException {

            showErrorMessage("testing",0);

            ArrayList<String> settings = readSettings("src/Intro/Java/Controller/ControllerSettings.txt");

            if(Boolean.parseBoolean(settings.get(2).split(" ")[1])) {

                hostIP_textField.setText(settings.get(0).split(" ")[1]);
                port_textField.setText(settings.get(1).split(" ")[1]);
                save_checkBox.setSelected(true);
            }
    }

    public void startConnection() {

        Main.ip = hostIP_textField.getText();
        Main.port = Integer.parseInt(port_textField.getText());

        connect_button.setDisable(true);
        connect_button.setText("Checking host");

        new Thread(() -> {

            try {

                //sets a limit to the sockets so it does not take long to see if there is a server that it can connect.
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(hostIP_textField.getText(), Integer.parseInt(port_textField.getText())), 1000);

                ControllerCommandFrame.connection = socket;

                Platform.runLater(() -> {
                    stage.hide();

                    try {
                        ControllerConfigFrame.makeFrame(FXMLLoader.load(getClass().getResource("../../../Controller/FX/ControllerCommandFrame.fxml")));
                    } catch (IOException e) { e.printStackTrace(); }
                });

            } catch (IOException e) {
                showErrorMessage("Host not found!", 5);
                Platform.runLater(() -> connect_button.setText("Connect"));
                connect_button.setDisable(false);
            }
        }).start();

    }

    public void backAction(){

        stage.hide();
        ConnectionTypeFrame.stage.show();
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

    public ArrayList<String> readSettings(String file) throws FileNotFoundException {

        ArrayList <String> settings = new ArrayList<>();
        Scanner scanner = new Scanner(new File(file));

        while(scanner.hasNextLine()){

            settings.add(scanner.nextLine());
        }

        return settings;
    }

    public static void makeFrame(Parent load){

        stage = new Stage();
        stage.setTitle("ZAN - Controller Config");
        stage.setScene(new Scene(load, 300, 275));
        stage.show();
    }
}
