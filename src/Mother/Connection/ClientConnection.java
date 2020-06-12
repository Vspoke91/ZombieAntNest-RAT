package Mother.Connection;

import Mother.Terminal.MotherTerminalFrame;
import Utilities.Child.Child;
import Utilities.Child.Controller;
import Utilities.Child.Target;
import javafx.application.Platform;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection extends Thread {

    public volatile boolean stopThread;
    public volatile boolean isOnline;
    public volatile int checkCount;

    public Child info = null;

    private BufferedReader input;
    public PrintWriter output;

    public ClientConnection(Socket socket){

        stopThread = false;
        isOnline = true;

        setName(socket.getRemoteSocketAddress().toString().split("/")[1]);

        MotherTerminalFrame.me.addConnection(getName());

        try {

            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //reads message
            output = new PrintWriter(socket.getOutputStream(), true); //sends message

        } catch (IOException e) { e.printStackTrace(); }

        start();
    }

    public void run() {
        super.run();

        startConnectionTimer(2000);

        while(isOnline || !stopThread){

            try {
                if(input.ready()) {

                    isOnline = true; //is used to reset timer
                    MotherTerminalFrame.me.addDataCount();

                    String[] message = input.readLine().split("-");

                    if(message[0].equals("host")) {//to who

                        switch (message[1]) {//command

                            case "check":

                                if (checkCount == 10) {
                                    checkCount = 0;
                                    MotherTerminalFrame.me.connectionAnimation(getName());
                                }

                                checkCount++;
                                break;

                            case "childType": //connection type
                                if (message[2].equals("controller")) {

                                    info = new Controller(getName());
                                    MotherConnection.sendAllTargetIPs(output);

                                } else if (message[2].equals("target")) {

                                    info = new Target(getName());
                                    MotherConnection.sendMessageToAllControllers("you-addTarget-" + getName());

                                } else
                                    info = null;

                                 MotherTerminalFrame.me.logText("[" + getName() + "] was set to "+ message[2], "0943aa");

                                break;

                            case "addTargetInfo":

                                    info.setInfo(message);

                                    String newMessage = "";

                                    for (String text: message) {

                                        if(text.equals("myIP"))
                                            newMessage += getName()+"-";
                                        else if (text.equals("host"))
                                            newMessage += "you-";
                                        else
                                            newMessage += text+"-";

                                    }

                                    MotherConnection.sendMessageToAllControllers(newMessage);

                                break;

                            case "addControllerInfo":

                                info.setInfo(message);

                                break;
                        }

                    } else {// when get a message not for host find the ip and send to that ip

                        MotherConnection.sendMessageTo(message[0], message);
                        MotherTerminalFrame.me.logText("[" + getName() + "] send \"" + message[1]+"-"+message[2] + "\" to "+ message[0], "0943aa");

                    }

                }
            } catch (java.net.SocketException e) {

                stopThread = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        MotherTerminalFrame.me.deleteConnection(getName());

        MotherConnection.connectionThreadList.remove(this);
        MotherConnection.sendMessageToAllControllers("you-removeTarget-"+getName());

        MotherTerminalFrame.me.logText("["+getName()+"] has disconnected! ;(","aa4409");

        isOnline = false;
    }

    public String toString(){

        return getName();
    }

    //is set to disconnect if no data is going on both ends
    public void startConnectionTimer(int timer){

        new Thread(() -> {
            while(!stopThread) {

                try { synchronized (this) { wait(timer); }} catch (InterruptedException e) { e.printStackTrace(); }

                if (isOnline)
                    isOnline = false;

                else
                    stopThread = true;

            }
        }).start();

    }

}
