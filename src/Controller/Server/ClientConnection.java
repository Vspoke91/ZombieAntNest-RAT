package Controller.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection extends Thread{

    Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ClientConnection(String hostIP, int port){
        //"68.98.164.176"
        try {
            socket = new Socket(hostIP, port);

            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //reads message
            output = new PrintWriter(socket.getOutputStream(), true); //sends message

        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }

    public void run() {
        super.run();

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
