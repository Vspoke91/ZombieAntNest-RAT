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
import java.net.URL;

public class ConnectionTypeFrame {

    static String ip;
    static String connectionType;
    static Stage stage;

    @FXML Button host_button;
    @FXML Button controller_button;
    @FXML Button hostAndController_button;
    @FXML Label ip_label;

    public ConnectionTypeFrame(){

        IntroClassHolder.setConnectionTypeFrame(this);
    }

    public void initialize(){

        //gets ip for label
        try {
            ip = new BufferedReader(new InputStreamReader(new URL("http://myexternalip.com/raw").openStream())).readLine(); //reads line from a website to get ip
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sets ip for label and disables buttons if not found
        if(ip == null) {
            ip = "Not Found";
            host_button.setDisable(true);
            hostAndController_button.setDisable(true);
        }
        ip_label.setText(ip);
    }

    public void buttonPressed(ActionEvent event) throws IOException {

        Button button = (Button) event.getSource();

        connectionType = button.getText();

        //makes a frame if configuration frame is null
        if(ConnectionConfigFrame.stage == null) {

            ConnectionConfigFrame.stage = new Stage();
            ConnectionConfigFrame.makeFrame(FXMLLoader.load(getClass().getResource("ConnectionConfigFrame.fxml")));
            IntroClassHolder.connectionConfigFrame.initialize();
        }

        IntroClassHolder.connectionConfigFrame.connectionType_label.setText(connectionType);//sets type  of connection to label
        IntroClassHolder.connectionConfigFrame.resetFields(); //resets field in case of labels changing

        ConnectionConfigFrame.stage.show();
        stage.hide();
    }

    static void makeFrame(Parent root){

        stage.setTitle("ZAN");
        stage.setScene(new Scene(root, 300, 275));
        stage.show();
    }
}
