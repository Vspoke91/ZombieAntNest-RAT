package Extras;

import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class LogText{

   public enum TextColor {

        ERROR("FF0000"),
        Server(""),
        System("00BBB8");

        private String color;

        TextColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }

    public static Text getTextObject(String message, TextColor color){

        Text text = new Text(message);

        text.setFill(Color.web(color.getColor()));

        String c = color.toString();

        return text;
    }

}