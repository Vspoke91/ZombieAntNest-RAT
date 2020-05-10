package Utilities.Child;

import Controller.Terminal.ControllerTerminalFrame;
import Mother.Connection.MotherConnection;
import javafx.application.Platform;

import java.io.PrintWriter;

public class Target extends Child {

    public String internet;
    public boolean isFlashlightOn;

    public Target(String ip){

        this.ip = ip;
        this.type = Type.TARGET;
    }

    public void setInfo(String[] message){

        switch (message[3]){

            case "internet": //internet

                internet = message[4];
                break;

            case "flashLightState": //flashlightState

                isFlashlightOn = Boolean.valueOf(message[4]);
                MotherConnection.sendMessageToAllControllers("you-"+ ip +"-addTargetInfo-flashLightState-" + isFlashlightOn);
                break;

            case "os":
                os = message[4];
                break;
        }
    }

    public void sendAllInfo(PrintWriter output){

        output.println("you-" + ip + "-addTargetInfo-os-" + os);
        output.println("you-" + ip + "-addTargetInfo-flashLightState-" + isFlashlightOn);
    }
}
