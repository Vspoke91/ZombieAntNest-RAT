package Intro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ConnectionConfigFrame {

    public ConnectionConfigFrame() {

        IntroClassHolder.setConnectionConfigFrame(this);
    }

    @FXML Label ConnectionType_label;

    //public void initialize(){}zzzzxz

    public void buttonPressed(ActionEvent event) {
        Main.primaryStage.show();
        IntroClassHolder.connectionTypeFrame.configStage.hide();
    }
}
