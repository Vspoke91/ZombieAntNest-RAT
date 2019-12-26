package Intro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ConnectionConfigFrame {

    public ConnectionConfigFrame() {

        IntroClassHolder.setConnectionConfigFrame(this);
    }

    @FXML Label ConnectionType_label;

    public void setConnectionType_label(String type){
        ConnectionType_label.setText(type);
    }

    public void buttonPressed(ActionEvent event) {
        Main.primaryStage.show();
        ConnectionTypeFrame.configStage.hide();
    }
}
