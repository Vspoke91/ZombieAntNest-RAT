package Controller.Java;

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

        } catch (IOException e) {
            e.printStackTrace();
        }

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

        output.println("host-controller");

        while(true) {
            output.println("host-check");
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
