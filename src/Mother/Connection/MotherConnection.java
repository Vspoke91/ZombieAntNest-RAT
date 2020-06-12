package Mother.Connection;

import Mother.Terminal.MotherTerminalFrame;
import Main.Main;
import Utilities.Child.Target;
import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static Utilities.Child.Child.Type.CONTROLLER;
import static Utilities.Child.Child.Type.TARGET;

public class MotherConnection extends Thread{

    private Socket s;
    private ServerSocket ss;

    public static volatile boolean stop = false;
    public static ArrayList<ClientConnection> connectionThreadList = new ArrayList<>();

    public MotherConnection(){

        try { ss = new ServerSocket(Main.port); } catch (IOException e) { e.printStackTrace(); }

        start();
    }

    public void run() {
        super.run();

        MotherTerminalFrame.me.logText("Server Up and Ready!","#009933");
        MotherTerminalFrame.me.startDataCounter();

        while(!stop){
            try {

                s = ss.accept();

                new Thread(() -> {

                    //validateConnection(s);
                    connectionThreadList.add(new ClientConnection(s));

                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Platform.runLater(() -> MotherTerminalFrame.me.logText("Server Down!!!", "ff0000"));

        try {
            s.close();
            ss.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static ArrayList<ClientConnection> getControllers(){

        ArrayList<ClientConnection> list = new ArrayList<>();

        for (ClientConnection client : connectionThreadList)
            if (client.info.type == CONTROLLER)
                list.add(client);

        return list;
    }

    public static ArrayList<ClientConnection> getTargets(){

        ArrayList<ClientConnection> list = new ArrayList<>();

        for (ClientConnection client : connectionThreadList)
            if (client.info.type == TARGET)
                list.add(client);

        return list;
    }

    public static void makeFakeConnection(){

        new Thread(() -> {

            try { new Socket("localhost",Main.port); } catch (IOException e) { e.printStackTrace(); }

        }).start();

    }

    public static void sendAllTargetIPs(PrintWriter output){

        for (ClientConnection connection: MotherConnection.getTargets()) {

            Target target = (Target) connection.info;

            output.println("you-addTarget-"+connection.getName()); //addTarget

            target.sendAllInfo(output);
        }
    }

    public static void sendMessageToAllControllers(String message){

        for (ClientConnection client: getControllers()) {

                client.output.println(message);
        }

    }

    public static void sendMessageTo(String name, String[] message){

        String totalMessage = "";

        for (int i = 1; i < message.length; i++)//this has to be done because the message deletes all the '-'
            totalMessage += "-"+message[i];

        getConnectionInList(name).output.println("you"+totalMessage);

    }

    public static ClientConnection getConnectionInList(String name){

        for (ClientConnection connection: connectionThreadList) {

                if (connection.getName().equals(name))
                    return connection;
        }

        return null;
    }

    /* TODO this is not working check for in-net (local ip) and public ip to see if they are different
    public void validateConnection(Socket s){

        for (ClientConnection connection: new ArrayList<>(connectionThreadList)) {

            if(connection.getName().equals(s.getInetAddress().toString().split("/")[1])){

                MotherTerminalFrame.me.logText("["+connection+"] already connected!","aa4409");
                connection.stopThread = true;

                while(connection.isRunning){

                    try { sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }
    }
    */


}
