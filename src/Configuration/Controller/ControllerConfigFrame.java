package Configuration.Controller;

import Controller.Terminal.ControllerTerminalFrame;
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
import javafx.stage.StageStyle;

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

        error_label.setVisible(false);

        ArrayList<String> settings = readSettings();

        if(Boolean.parseBoolean(settings.get(2))) {

            hostIP_textField.setText(settings.get(0));
            port_textField.setText(settings.get(1));
            save_checkBox.setSelected(true);
        }
    }

    public void startConnection() {

        connect_button.setDisable(true);
        connect_button.setText("Connecting...");

        new Thread(() -> {

            try {

                //sets a limit to the sockets so it does not take long to see if there is a server that it can connect.
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(hostIP_textField.getText(), Integer.parseInt(port_textField.getText())), 1000);

                Main.ip = hostIP_textField.getText();//TODO get ip from socket like port did.
                Main.port = socket.getPort();

                Platform.runLater(() -> {
                    stage.hide();

                    try {
                        ControllerTerminalFrame.makeFrame(false, socket);
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
        Scanner scanner = new Scanner(new File("src/Configuration/Controller/SaveSettings.txt"));

        while(scanner.hasNextLine()){

            settings.add(scanner.nextLine().split("=")[1].trim());
        }

        return settings;
    }

    public static void makeFrame() throws IOException {

        stage = new Stage();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Controller Config");
        stage.setScene(new Scene(FXMLLoader.load(ControllerConfigFrame.class.getResource("ControllerConfigFrame.fxml")), 300, 275));

        stage.show();
    }
}
