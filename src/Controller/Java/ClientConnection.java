package Controller.Java;

import Host.Java.HostCommandFrame;
import Intro.Java.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection extends Thread{

    public static boolean isLocal = false;

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;


    public ClientConnection(){
        //"68.98.164.176"

        try {
            if(isLocal)
                socket = new Socket("localhost", Main.port);
            else
                socket = new Socket(Main.ip, Main.port);

            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //reads message
            output = new PrintWriter(socket.getOutputStream(), true); //sends message

        } catch (IOException e) { e.printStackTrace(); }

        start();
    }

    public ClientConnection(Socket connection) {

        socket = connection;

        try {

            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //reads message
            output = new PrintWriter(socket.getOutputStream(), true); //sends message

        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }

    public void run() {
        super.run();

        output.println("host-ct-controller");
        output.println("host-dt-"+System.getProperty("os.name"));

        //starts a check thread to make sure it does not delay
        new Thread(() -> {

            while (true) {

                output.println("host-check");

                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        while(true) {

            try {
                if(input.ready()){

                    String[] message = input.readLine().split("-");

                    if(message.equals("you")){//to who

                        switch(message[1]) {//what command

                            case "at": //add Target
                                ControllerCommandFrame.me.addTarget(message[2]);
                                break;
                        }

                    } else {

                        System.out.println("message was not for me");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void sendMessageTo(String name, String command){

        output.println(name+"-"+command);
    }

    public void sendMessageTo(String name, String command, String message){

        output.println(name+"-"+command+"-"+message);
    }
}
