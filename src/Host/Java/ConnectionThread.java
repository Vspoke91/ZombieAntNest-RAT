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

    public String internet;
    public boolean flashlightState;

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

                                    if (checkCount == 10) {
                                        checkCount = 0;
                                        HostCommandFrame.me.checkChange(getName());
                                    }

                                    checkCount++;
                                    break;

                                case "ct": //connection type
                                    if (message[2].equals("controller")) {

                                        connectionType = "controller";
                                        sendAllTargetIPs();

                                    } else if (message[2].equals("target")) {

                                        connectionType = "target";
                                        HostConnection.sendMessageToAllControllers("you-adt-" + getName());
                                    } else
                                        connectionType = "null";
                                        HostCommandFrame.me.logText("[" + getName() + "] was set to " + connectionType, "0943aa");
                                    break;

                                case "dt": //device type
                                    if (connectionType.equals("target")) {

                                        deviceType = message[2];
                                        HostConnection.sendMessageToAllControllers("you-adti-" + getName() + "-dt-" + deviceType);
                                    }
                                    break;

                                case "fls": //flashLight State
                                    if(connectionType.equals("target")) {

                                        flashlightState = Boolean.valueOf(message[2]);
                                        HostConnection.sendMessageToAllControllers("you-adti-" + getName() + "-fls-" + flashlightState);
                                    }
                                    break;
                            }

                    }

                    else {// when get a message not for host find the ip and send to that ip
                        HostConnection.sendMessageTo(message[0], message);
                        HostCommandFrame.me.logText("[" + getName() + "] send \"" + message[1]+"-"+message[2] + "\" to "+ message[0], "0943aa");

                    }

                }
            } catch (java.net.SocketException e) {
                stopThread = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        HostCommandFrame.me.deleteConnection(getName());

        HostConnection.connectionThreadList.remove(this);
        HostConnection.sendMessageToAllControllers("you-det-"+getName());

        HostCommandFrame.me.logText("["+getName()+"] has disconnected! ;(","aa4409");

        isRunning = false;
    }

    public String toString(){

        return getName();
    }

    public void sendAllTargetIPs(){

        for (ConnectionThread connection: new ArrayList<>(HostConnection.connectionThreadList)) {

            if (connection.connectionType.equals("target"))
                output.println("you-adt-"+connection.getName()); //addTarget
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
