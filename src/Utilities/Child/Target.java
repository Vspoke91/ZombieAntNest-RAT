package Utilities.Child;

import Mother.Connection.MotherConnection;

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
                break;

            case "os":

                os = message[4];
                break;
        }
    }

    public void sendAllInfo(PrintWriter output){

        output.println("you-addTargetInfo-"+ip+"-os-"+os);
        output.println("you-addTargetInfo-"+ip+"-flashLightState-"+isFlashlightOn);
    }
}
