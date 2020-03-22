package Controller.Server;

import Intro.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection extends Thread{

    Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ClientConnection(){
        //"68.98.164.176"

        start();
    }

    public void run() {
        super.run();

        try {
            socket = new Socket(Main.ip, Main.port);

        input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //reads message
        output = new PrintWriter(socket.getOutputStream(), true); //sends message

        } catch (IOException e) {
            e.printStackTrace();
        }

        output.println("host-type|controller");

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
