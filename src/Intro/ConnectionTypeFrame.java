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

        try{
            URL ipAdress = new URL("http://myexternalip.com/raw");

            BufferedReader in = new BufferedReader(new InputStreamReader(ipAdress.openStream()));
            ip = in.readLine();
            ip_label.setText(ip);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML Button host_button;
    @FXML Button controller_button;
    @FXML Button hostAndController_button;
    @FXML Label ip_label;

    public static Stage configStage;
    public static String connectionType;

    public void buttonPressed(ActionEvent event) throws IOException {

        Button button = (Button) event.getSource();

        if(configStage == null) {
            configStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("ConnectionConfigFrame.fxml"));
            configStage.setTitle("Configuration");
            configStage.setScene(new Scene(root, 300, 300));
        }
        IntroClassHolder.connectionConfigFrame.setConnectionType_label(button.getText());

        configStage.show();

        Main.primaryStage.hide();
    }
}
