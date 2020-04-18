package Host.Java;

import Intro.Java.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HostConnection extends Thread{

    private Socket s;
    private ServerSocket ss;

    private volatile boolean stop;
    public static ArrayList<ConnectionThread> connectionThreadList = new ArrayList<>();

    public HostConnection(){

        stop = false;

        HostCommandFrame.me.logText("Welcome!\nServer is starting....","19b386");

        try { ss = new ServerSocket(Main.port); } catch (IOException e) { e.printStackTrace(); }

        HostCommandFrame.me.logText("Server Ready and listening :)","19b386");

        start();
    }

    public void run() {
        super.run();

        while(!stop){
            try {

                s = ss.accept();

                new Thread(() -> {
                    validateConnection(s);
                    connectionThreadList.add(new ConnectionThread(s));
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void sendMessageToAllControllers(String message){

        for (ConnectionThread connection: new ArrayList<>(connectionThreadList)) {

            if (connection.connectionType.equals("controller"))
                connection.output.println(message);
        }

    }

    public static void sendMessageTo(String name, String[] message){

        String totalMessage = "";

        for (int i = 1; i < message.length; i++)
            totalMessage += "-"+message[i];

        System.out.println(totalMessage);

        getConnectionInList(name).output.println("you"+totalMessage);

    }

    public static ConnectionThread getConnectionInList(String name){

        for (ConnectionThread connection: new ArrayList<>(connectionThreadList)) {

                if (connection.getName().equals(name))
                    return  connection;
        }

        return null;
    }

    public void validateConnection(Socket s){

        for (ConnectionThread connection: new ArrayList<>(connectionThreadList)) {

            if(connection.getName().equals(s.getInetAddress().toString().split("/")[1])){

                HostCommandFrame.me.logText("["+connection+"] already connected!","aa4409");
                connection.stopThread = true;

                while(connection.isRunning){

                    try { sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }
    }


}
