package Intro.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControllerConfigFrame {

    public static ControllerConfigFrame me;

    public static Stage stage;

    @FXML Label error_label;

    public ControllerConfigFrame(){
        me = this;
    }

    public void ShowErrorMessage(String message, int seconds){

        new Thread(() -> {
            error_label.setText(message);
            error_label.setVisible(true);

            try {
                synchronized (this) { wait(seconds * 1000); }
            } catch (InterruptedException e) { e.printStackTrace(); }

            error_label.setVisible(false);
        }).start();
    }

    public static void makeFrame(Parent load, Stage stage){

        ControllerConfigFrame.stage = stage;
        stage.setTitle("ZAN - Controller Config");
        stage.setScene(new Scene(load, 300, 275));
        stage.show();
    }
}
