package Controller.Server;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends Thread{

    Socket socket;

    public ClientConnection(){

        try {
            socket = new Socket("68.98.164.176", 9191);
        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }

    public void run() {
        super.run();



    }
}
