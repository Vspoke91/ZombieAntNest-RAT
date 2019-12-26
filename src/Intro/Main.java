package Intro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        IntroClassHolder.setMain(this);

        Parent root = FXMLLoader.load(getClass().getResource("ConnectionTypeFrame.fxml"));
        primaryStage.setTitle("ZAC");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        this.primaryStage = primaryStage;

        IntroClassHolder.connectionTypeFrame.configure();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
