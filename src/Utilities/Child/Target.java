package Utilities.Child;

import Controller.Terminal.ControllerTerminalFrame;
import Utilities.Child.Child;
import javafx.application.Platform;

public class Target extends Child {

    public String internet;
    public String deviceType;
    public boolean flashlightState;

    public Target(String name){

        this.ip = name;

    }

    public void addTargetInfo(String field, String state){

        switch (field){

            case "int": //internet
                internet = state;
                break;

            case "fls": //flashlightState
                flashlightState = Boolean.valueOf(state);

                if(flashlightState)
                    Platform.runLater(() -> ControllerTerminalFrame.me.flashLight_button.setText("FlashLight: ON"));

                else
                    Platform.runLater(() -> ControllerTerminalFrame.me.flashLight_button.setText("FlashLight: OFF"));

                break;

            case "dt": //Device Type
                deviceType = state;
                break;

        }

    }
}
