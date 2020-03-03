package Intro.Host;

import Host.TerminalCommandFrame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HostConfigFrame {

    public static Stage stage;

    public void initialize() throws IOException {

    }

    public void backAction(){

    }

    public static void makeFrame(Parent load, Stage stage){

        HostConfigFrame.stage = stage;

        stage.setTitle("ZAN - Host Config");
        stage.setScene(new Scene(load, 300, 275));
        stage.show();
    }
}
