package Host.Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionThread extends Thread {

    public volatile boolean stopThread;
    public volatile boolean isRunning;
    public volatile boolean isOnline;
    public volatile int checkCount;

    private BufferedReader input;
    private PrintWriter output;

    public ConnectionThread(Socket socket){

        stopThread = false;
        isRunning = true;
        isOnline = true;

        setName(socket.getRemoteSocketAddress().toString().split("/")[1]);

        HostCommandFrame.me.logText("["+getName()+"] has connected!","2daa09");
        HostCommandFrame.me.addConnection(this);

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //reads message
            output = new PrintWriter(socket.getOutputStream(), true); //sends message
        } catch (IOException e) {
            e.printStackTrace();
        }

        HostCommandFrame.me.logText("["+getName()+"] ready to command .(+_+)>","0943aa");
        start();
    }

    public void run() {
        super.run();

        startConnectionTimer();

        while(!stopThread){

            try {
                if(input.ready()) {

                    isOnline = true; //is used to reset timer

                    String[] message = input.readLine().split("-");

                    switch (message[0]){

                        case("host"):

                            switch(message[1]) {

                                case "check":

                                    if(checkCount == 10) {
                                        checkCount = 0;
                                        HostCommandFrame.me.checkChange(this);
                                    }

                                    checkCount++;
                                    break;

                                case "controller":
                                    HostCommandFrame.me.logText("[" + getName() + "] was set to Controller", "0943aa");
                                    break;

                                case "target":
                                    HostCommandFrame.me.logText("[" + getName() + "] was set to Target", "0943aa");
                                    break;
                            }
                    }
                }
            } catch (java.net.SocketException e) {
                stopThread = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        HostConnection.connectionThreadList.remove(this);
        HostCommandFrame.me.logText("["+getName()+"] has disconnected! ;(","aa4409");
        HostCommandFrame.me.deleteConnection(this);
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
