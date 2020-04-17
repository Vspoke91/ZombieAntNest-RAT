package Host.Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionThread extends Thread {

    public volatile boolean stopThread;
    public volatile boolean isRunning;
    public volatile boolean isOnline;
    public volatile int checkCount;

    public String connectionType;
    public String deviceType;

    private BufferedReader input;
    public PrintWriter output;

    public ConnectionThread(Socket socket){

        stopThread = false;
        isRunning = true;
        isOnline = true;

        setName(socket.getRemoteSocketAddress().toString().split("/")[1]);

        HostCommandFrame.me.logText("["+getName()+"] has connected!","2daa09");
        HostCommandFrame.me.addConnection(getName());

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

                    if(message[0].equals("host")){//to who

                            switch(message[1]) {//what command

                                case "check":

                                    if(checkCount == 10) {
                                        checkCount = 0;
                                        HostCommandFrame.me.checkChange(getName());
                                    }

                                    checkCount++;
                                    break;

                                case "ct": //connection type
                                    if(message[2].equals("controller")) {

                                        connectionType = "controller";
                                        sendAllTargetIPs();

                                    }

                                    else if (message[2].equals("target"))
                                        connectionType = "target";

                                    else
                                        connectionType = "null";

                                    HostCommandFrame.me.logText("[" + getName() + "] was set to " + connectionType, "0943aa");
                                    break;

                                case "dt": //device type

                                    deviceType = message[2];

                                    break;
                            }

                    }

                    else {// when get a message not for host find the ip and send to that ip
                        HostConnection.sendMessageTo(message[0], message[1]);
                        HostCommandFrame.me.logText("[" + getName() + "] send \"" + message[1] + "\" to "+ message[0], "0943aa");

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
        HostCommandFrame.me.deleteConnection(getName());

        isRunning = false;
    }

    public String toString(){

        return getName();
    }

    public void sendAllTargetIPs(){

        for (ConnectionThread connection: new ArrayList<>(HostConnection.connectionThreadList)) {

            if (connection.connectionType.equals("target"))
                output.println("you-at-"+connection.getName()); //at = addTarget
        }
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
