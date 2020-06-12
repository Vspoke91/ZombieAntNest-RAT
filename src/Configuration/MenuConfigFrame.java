package Configuration;

import Configuration.Controller.ControllerConfigFrame;
import Configuration.Mother.MotherConfigFrame;
import Main.Main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static java.lang.Thread.sleep;

public class MenuConfigFrame {

    public static MenuConfigFrame me;
    public static Stage stage;

    @FXML Label ip_label;
    @FXML Label errorWifi_label;
    @FXML Label error_label;
    @FXML Button mother_button;
    @FXML Button controller_button;

    public MenuConfigFrame(){
        me = this;
    }

    public void initialize() throws IOException {

        errorWifi_label.setVisible(false);
        error_label.setVisible(false);

        ConfigIP(); //sets stuff related to ip

        ip_label.setText("My IP: " + Main.ip);
    }

    private void ConfigIP(){

        try {

            Main.ip = new BufferedReader(new InputStreamReader(new URL("http://myexternalip.com/raw").openStream())).readLine(); //reads line from a website to get ip
        } catch (IOException e) {
            errorWifi_label.setVisible(true);//not the error, it shows if you have internet
            controller_button.setDisable(true);
        }


        //sets ip for label and disables buttons if not found
        if(Main.ip == null) {

            Main.ip = "Not found!";

            showErrorMessage("IP not found!", 5);

            mother_button.setDisable(true);
        }

    }

    public void buttonPressed(ActionEvent event) throws IOException {

        stage.hide();

        if(controller_button.equals(event.getSource()))
            ControllerConfigFrame.makeFrame();

        else if (mother_button.equals(event.getSource()))
            MotherConfigFrame.makeFrame();

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

    public static void makeFrame() throws IOException {

        stage = new Stage();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("ZAN-RAT Menu");
        stage.setScene(new Scene(FXMLLoader.load(MenuConfigFrame.class.getResource("MenuConfigFrame.fxml")), 300, 300));

        stage.show();
    }
}
