package Configuration.Mother;

import Mother.Terminal.MotherTerminalFrame;
import Configuration.MenuConfigFrame;
import Main.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class MotherConfigFrame {

    public static Stage stage;

    @FXML Label error_label;
    @FXML TextField port_textField;
    @FXML Label ip_label;
    @FXML CheckBox save_checkBox;
    @FXML Button create_button;

    public void initialize() throws IOException {

        error_label.setVisible(false);

        ip_label.setText("My IP: " + Main.ip);

        ArrayList<String> settings = readSettings();

        if(Boolean.parseBoolean(settings.get(1))) {

            port_textField.setText(settings.get(0));
            save_checkBox.setSelected(true);
        }
    }

    public void createConnection() throws IOException {
        //TODO add a way to see if host can be made else print error message

        Main.port = Integer.parseInt(port_textField.getText());

        stage.hide();
        MotherTerminalFrame.makeFrame();
    }

    public void backAction(){

        stage.hide();
        MenuConfigFrame.stage.show();
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

    public ArrayList<String> readSettings() throws FileNotFoundException {

        ArrayList <String> settings = new ArrayList<>();
        Scanner scanner = new Scanner(new File("src/Configuration/Mother/SaveSettings.txt"));

        while(scanner.hasNextLine()){

            settings.add(scanner.nextLine().split("=")[1].trim());
        }

        return settings;
    }

    public static void makeFrame() throws IOException {

        stage = new Stage();

        stage.setTitle("Mother Config");
        stage.setScene(new Scene(FXMLLoader.load(MotherConfigFrame.class.getResource("MotherConfigFrame.fxml")),300, 275));

        stage.show();
    }
}
