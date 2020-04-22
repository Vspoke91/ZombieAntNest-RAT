package Main;

import Configuration.MenuConfigFrame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {

    public static Main me;

    public static String ip;  //this is used to store mothers ip if you are mother then it stores yours.
    public static int port;

    public Main(){ me = this; }

    @Override
    public void start(Stage stage) throws Exception{
        MenuConfigFrame.makeFrame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
