package Utilities.Child;

public class Controller extends Child {

    public Controller(String ip){

        this.ip = ip;
        this.type = Type.CONTROLLER;
    }

    @Override
    public void setInfo(String[] message) {

        switch (message[2]) {

            case "os": //internet

                os = message[3];
                break;
        }
    }
}
