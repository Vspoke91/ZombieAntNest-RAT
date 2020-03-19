package Intro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        ConnectionTypeFrame.makeFrame(FXMLLoader.load(getClass().getResource("ConnectionTypeFrame.fxml")));
    }

    public static void main(String[] args) {

        launch(args);
    }
}
