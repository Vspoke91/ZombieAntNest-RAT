package Intro;

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
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionTypeFrame {

    public static String ip;

    public ConnectionTypeFrame(){

        IntroClassHolder.setConnectionTypeFrame(this);
    }

    public void configure(){

        try {
            ip = new BufferedReader(new InputStreamReader(new URL("http://myexternalip.com/raw").openStream())).readLine(); //reads line from a website to get ip
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ip == null) {
            ip = "Not Found";
            host_button.setDisable(true);
            hostAndController_button.setDisable(true);
        }
        ip_label.setText(ip);

    }

    @FXML Button host_button;
    @FXML Button controller_button;
    @FXML Button hostAndController_button;
    @FXML Label ip_label;

    public static Stage configStage;

    public void buttonPressed(ActionEvent event) throws IOException {

        Button button = (Button) event.getSource();

        if(configStage == null) {
            configStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("ConnectionConfigFrame.fxml"));
            configStage.setTitle("Configuration");
            configStage.setScene(new Scene(root, 300, 300));
            IntroClassHolder.connectionConfigFrame.configure();
        }

        //resets fields
        IntroClassHolder.connectionConfigFrame.ip_textField.setDisable(false);
        IntroClassHolder.connectionConfigFrame.ip_label.setDisable(false);

        if(button.getText().equals("Controller")) {
            IntroClassHolder.connectionConfigFrame.ip_textField.setDisable(true);
            IntroClassHolder.connectionConfigFrame.ip_label.setDisable(true);
        }

        //sets type connection to label
        IntroClassHolder.connectionConfigFrame.connectionType_label.setText(button.getText());

        configStage.show();
        Main.primaryStage.hide();
    }
}
