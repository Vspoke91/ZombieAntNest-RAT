package Intro.Host;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HostConfigFrame {

    public static Stage stage;

    public void backAction(){

    }

    public static void makeFrame(Parent load, Stage stage){

        HostConfigFrame.stage = stage;

        stage.setTitle("ZAN - Host Config");
        stage.setScene(new Scene(load, 300, 275));
        stage.show();
    }
}
