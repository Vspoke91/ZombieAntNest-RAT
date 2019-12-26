package Intro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectionConfigFrame {

    public ConnectionConfigFrame() {

        IntroClassHolder.setConnectionConfigFrame(this);
    }

    public void configure(){

        ip_textField.setText(ConnectionTypeFrame.ip);

    }

    @FXML Label connectionType_label;
    @FXML Label ip_label;
    @FXML TextField ip_textField;
    @FXML TextField port_textField;

    public void buttonPressed(ActionEvent event) {

        Main.primaryStage.show();
        ConnectionTypeFrame.configStage.hide();
    }
}
