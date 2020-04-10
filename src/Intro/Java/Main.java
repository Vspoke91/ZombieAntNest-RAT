package Intro.Java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {

    public static String ip;
    public static int port;

    @Override
    public void start(Stage stage) throws Exception{

        ConnectionTypeFrame.makeFrame(FXMLLoader.load(getClass().getResource("../FX/ConnectionTypeFrame.fxml")));
    }

    public static void main(String[] args) {

        launch(args);
    }
}
