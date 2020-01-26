package Host.Server;

import Host.TerminalCommandFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionThread extends Thread {

    public volatile boolean stopThread;
    public volatile boolean isRunning;
    public volatile boolean isOnline;

    private BufferedReader input;
    private PrintWriter output;

    public ConnectionThread(Socket socket){

    stopThread = false;
    isRunning = true;
    isOnline = true;

    setName(socket.getRemoteSocketAddress().toString().split("/")[1]);

    TerminalCommandFrame.me.logText("["+getName()+"] has connected!","2daa09");
    TerminalCommandFrame.me.addConnection(this);

    try {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //reads message
        output = new PrintWriter(socket.getOutputStream(), true); //sends message
    } catch (IOException e) {
        e.printStackTrace();
    }

    TerminalCommandFrame.me.logText("["+getName()+"] ready to command .(+_+)>","0943aa");

    start();
}

    public void run() {
        super.run();

        startConnectionTimer();

        while(!stopThread){

            try {
                if(input.ready()) {
                    String[] message = input.readLine().split("-");

                    isOnline = true; //is used to reset timer

                    TerminalCommandFrame.me.logText("[" + getName() + "]" + " said \"" + message[0] + "\" to #" + message[1] + "#", "0943aa");
                }
            } catch (java.net.SocketException e) {
                stopThread = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        HostConnection.connectionThreadList.remove(this);
        TerminalCommandFrame.me.logText("["+getName()+"] has disconnected! ;(","aa4409");
        TerminalCommandFrame.me.deleteConnection(this);
        isRunning = false;
    }

    public String toString(){

        return getName();
    }

    //is set to disconnect if no data is going on both ends
    public void startConnectionTimer(){

        new Thread(() -> {
            while(!stopThread) {

                try { synchronized (this) { wait(2000); }} catch (InterruptedException e) { e.printStackTrace(); }

                if (isOnline)

                    isOnline = false;
                else {

                    stopThread = true;
                }
            }
        }).start();

    }

}
