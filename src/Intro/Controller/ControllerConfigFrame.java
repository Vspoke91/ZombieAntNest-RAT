package Intro.Controller;

import Intro.ConnectionConfigFrame;
import Intro.ConnectionTypeFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
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
    @FXML Button connect_button;

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

    public void startConnection() {

       connect_button.setDisable(true);
       connect_button.setText("Checking host");

        new Thread(() -> {

            try {

                new Socket(hostIP_textField.getText(), Integer.parseInt(port_textField.getText()));
                return ;
            } catch (IOException e) {

                showErrorMessage("Host not found!", 5);
                Platform.runLater(() -> connect_button.setText("Connect"));
                connect_button.setDisable(false);
            }
        }).start();
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
