package Utilities;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public abstract class FrameUtilities {

    public double xOffSet = 0.0;
    public double yOffSet = 0.0;

    public void startFrameUtilities(Stage stage, HBox header_pane, Button exit_button, Button hide_button) {


        header_pane.setOnMousePressed(event -> {
            xOffSet = stage.getX() - event.getScreenX();
            yOffSet = stage.getY() - event.getScreenY();
        });

        header_pane.setOnMouseDragged(event -> {

            stage.setX(event.getScreenX() + xOffSet);
            stage.setY(event.getScreenY() + yOffSet);
        });

        exit_button.setOnAction(actionEvent -> System.exit(0));
        hide_button.setOnAction(actionEvent -> stage.setIconified(true));

    }
}
