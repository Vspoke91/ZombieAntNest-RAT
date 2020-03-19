package Host.Server;

import Host.TerminalCommandFrame;

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

        TerminalCommandFrame.me.logText("Welcome!\nServer is starting....","19b386");
        try { ss = new ServerSocket(9191); } catch (IOException e) { e.printStackTrace(); }
        TerminalCommandFrame.me.logText("Server Ready and listening :)","19b386");

        //testing connection adding UI
        TerminalCommandFrame.me.addConnection(null);
        TerminalCommandFrame.me.deleteConnection(null);

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

    public void validateConnection(Socket s){

        for (ConnectionThread connection: new ArrayList<>(connectionThreadList)) {

            if(connection.getName().equals(s.getInetAddress().toString().split("/")[1])){

                TerminalCommandFrame.me.logText("["+connection+"] already connected!","aa4409");
                connection.stopThread = true;

                while(connection.isRunning){

                    try { sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }
    }


}
