package Intro.Java.Host;

import Host.Java.HostCommandFrame;
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
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class HostConfigFrame {

    public static Stage stage;


    @FXML Label error_label;
    @FXML TextField port_textField;
    @FXML Label ip_label;
    @FXML CheckBox save_checkBox;
    @FXML Button create_button;

    public void initialize() throws IOException {

        ip_label.setText("IP: " + Main.ip);

        showErrorMessage("testing",0);

        ArrayList<String> settings = readSettings("src/Intro/Java/Host/HostSettings.txt");

        if(Boolean.parseBoolean(settings.get(1).split(" ")[1])) {

            port_textField.setText(settings.get(0).split(" ")[1]);
            save_checkBox.setSelected(true);
        }
    }

    public void createConnection() throws IOException {

        Main.port = Integer.parseInt(port_textField.getText());

        stage.hide();
        HostCommandFrame.makeFrame(FXMLLoader.load(getClass().getResource("../../../Host/FX/HostCommandFrame.fxml")));
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

        stage.setTitle("ZAN - Host Config");
        stage.setScene(new Scene(load, 300, 275));
        stage.show();
    }
}
