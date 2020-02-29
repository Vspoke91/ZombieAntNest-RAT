package Intro.Controller;

import Intro.ConnectionConfigFrame;
import Intro.ConnectionTypeFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class ControllerConfigFrame {

    public static ControllerConfigFrame me;
    public static boolean foundHost;
    public static Stage stage;

    @FXML Label error_label;
    @FXML TextField hostIP_textField;
    @FXML TextField port_textField;
    @FXML CheckBox save_checkBox;

    public ControllerConfigFrame(){
        me = this;
    }

    public void initialize() throws FileNotFoundException {

            showErrorMessage("testing",0);

            ArrayList<String> settings = readSettings("src/Intro/Controller/ControllerSettings.txt");

            if(Boolean.parseBoolean(settings.get(2).split(" ")[1])) {

                hostIP_textField.setText(settings.get(0).split(" ")[1]);
                port_textField.setText(settings.get(1).split(" ")[1]);
                save_checkBox.setSelected(true);
            }else{

                hostIP_textField.setText("");
                port_textField.setText("");
                save_checkBox.setSelected(false);
            }
    }

    public void startConnection(){

        if(hostConnectionCheck(hostIP_textField.getText(), Integer.parseInt(port_textField.getText()))){

        }else{
            showErrorMessage("Host not found!",1);
        }
    }

    public void backAction(){

        ConnectionTypeFrame.stage.show();
        stage.hide();
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

    public boolean hostConnectionCheck (String ip, int port){

        new Thread(() -> {
            try {
                new Socket(ip, port);
                foundHost = true;
            } catch (IOException e) {
                foundHost = false;
            }
        }).start();

        return foundHost;
    }

    public ArrayList<String> readSettings(String file) throws FileNotFoundException {

        ArrayList <String> settings = new ArrayList<>();
        Scanner scanner = new Scanner(new File(file));

        while(scanner.hasNextLine()){

            settings.add(scanner.nextLine());
        }

        return settings;
    }

    public static void makeFrame(Parent load, Stage stage){

        ControllerConfigFrame.stage = stage;
        stage.setTitle("ZAN - Controller Config");
        stage.setScene(new Scene(load, 300, 275));
        stage.show();
    }
}
