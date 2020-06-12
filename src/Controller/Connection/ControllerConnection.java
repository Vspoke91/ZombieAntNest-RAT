package Controller.Connection;

import Controller.Terminal.ControllerTerminalFrame;
import Main.Main;
import Utilities.Child.Controller;
import Utilities.Child.Target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ControllerConnection extends Thread{

    public static ControllerConnection me;

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public static List<Target> targetList = new ArrayList<>();

    public ControllerConnection(boolean makeLocalConnection, Socket socket) {
        //"68.98.164.176"
        me = this;

        try {
            if (socket == null) {
                if (makeLocalConnection)
                    socket = new Socket("localhost", Main.port);
                else
                    socket = new Socket(Main.ip, Main.port);
            } else
                this.socket = socket;

            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //reads message
            output = new PrintWriter(socket.getOutputStream(), true); //sends message

        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }

    public void run() {
        super.run();

        output.println("host-childType-controller");
        output.println("host-addControllerInfo-os-"+System.getProperty("os.name"));//TODO checking this later

        //starts a check thread to make sure it does not delay
        new Thread(() -> {

            while (true) {

                output.println("host-check");

                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        while(true) {

            try {
                if(input.ready()){

                    String[] message = input.readLine().split("-");

                    if(message[0].equals("you")){//to who

                        switch(message[1]) {//command

                            case "addTarget": //add Target

                                addTarget(message[2]);
                                break;

                            case "removeTarget": //remove Target

                                deleteTarget(message[2]);
                                break;

                            case "addTargetInfo": //add target info

                                getTargetInList(message[2]).setInfo(message);

                                if(ControllerTerminalFrame.target != null && message[2].equals(ControllerTerminalFrame.target.ip))
                                    ControllerTerminalFrame.me.updateTargetInfo();

                                break;
                        }

                    } else {

                        System.out.println("message was not for me");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void addTarget(String ip){

        targetList.add(new Target(ip));
        ControllerTerminalFrame.me.addTarget(ip);
    }

    public void deleteTarget(String ip){

        targetList.remove(getTargetInList(ip));
        ControllerTerminalFrame.me.deleteTarget(ip);
    }

    public static Target getTargetInList(String ip){

        for(Target target : targetList){

            if(target.ip.equals(ip))
                return target;
        }
        return null;
    }

    public void sendMessageTo(String name, String command, String message){

        output.println(name+"-"+command+"-"+message);
    }
}
