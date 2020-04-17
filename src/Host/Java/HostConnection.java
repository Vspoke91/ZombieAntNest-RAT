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

                System.out.println(s.getRemoteSocketAddress());//TODO: delete this latere

                new Thread(() -> {
                    validateConnection(s);
                    connectionThreadList.add(new ConnectionThread(s));
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static ConnectionThread getConnectionInList(String name){

        for (ConnectionThread connection: new ArrayList<>(connectionThreadList)) {

            if(name.startsWith("<") || name.startsWith(">")) {

                if (connection.getName().equals(name.substring(1)))

                    return connection;

            } else {

                if (connection.getName().equals(name))
                    return  connection;

            }
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
