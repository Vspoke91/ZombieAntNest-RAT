package Intro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectionTypeFrame {

    public ConnectionTypeFrame(){

        IntroClassHolder.setConnectionTypeFrame(this);
    }

    @FXML Button host_button;
    @FXML Button controller_button;
    @FXML Button hostAndController_button;

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
