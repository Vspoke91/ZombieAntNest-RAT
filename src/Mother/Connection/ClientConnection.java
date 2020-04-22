package Mother.Connection;

import Mother.Terminal.MotherTerminalFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection extends Thread {

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

    public ClientConnection(Socket socket){

        stopThread = false;
        isRunning = true;
        isOnline = true;

        setName(socket.getRemoteSocketAddress().toString().split("/")[1]);

        MotherTerminalFrame.me.logText("["+getName()+"] has connected!","2daa09");
        MotherTerminalFrame.me.addConnection(getName());

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //reads message
            output = new PrintWriter(socket.getOutputStream(), true); //sends message
        } catch (IOException e) {
            e.printStackTrace();
        }

        MotherTerminalFrame.me.logText("["+getName()+"] ready to command .(+_+)>","0943aa");

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
                                        MotherTerminalFrame.me.checkChange(getName());
                                    }

                                    checkCount++;
                                    break;

                                case "ct": //connection type
                                    if (message[2].equals("controller")) {

                                        connectionType = "controller";
                                        sendAllTargetIPs();

                                    } else if (message[2].equals("target")) {

                                        connectionType = "target";
                                        MotherConnection.sendMessageToAllControllers("you-adt-" + getName());
                                    } else
                                        connectionType = "null";
                                        MotherTerminalFrame.me.logText("[" + getName() + "] was set to " + connectionType, "0943aa");
                                    break;

                                case "dt": //device type
                                    if (connectionType.equals("target")) {

                                        deviceType = message[2];
                                        MotherConnection.sendMessageToAllControllers("you-adti-" + getName() + "-dt-" + deviceType);
                                    }
                                    break;

                                case "fls": //flashLight State
                                    if(connectionType.equals("target")) {

                                        flashlightState = Boolean.valueOf(message[2]);
                                        MotherConnection.sendMessageToAllControllers("you-adti-" + getName() + "-fls-" + flashlightState);
                                    }
                                    break;
                            }

                    }

                    else {// when get a message not for host find the ip and send to that ip
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
        MotherConnection.sendMessageToAllControllers("you-det-"+getName());

        MotherTerminalFrame.me.logText("["+getName()+"] has disconnected! ;(","aa4409");

        isRunning = false;
    }

    public String toString(){

        return getName();
    }

    public void sendAllTargetIPs(){

        for (ClientConnection connection: new ArrayList<>(MotherConnection.connectionThreadList)) {

            if (connection.connectionType.equals("target")) {
                output.println("you-adt-" + connection.getName()); //addTarget
                output.println("you-adti-" + connection.getName() + "-dt-" + connection.deviceType);
                output.println("you-adti-" + connection.getName() + "-fls-" + connection.flashlightState);
            }
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
