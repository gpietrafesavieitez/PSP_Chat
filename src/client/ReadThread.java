package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ReadThread extends Thread {

    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println("\n" + response);
                if (client.getNickName() != null) {
                    System.out.print("[ > ]\t" + client.getNickName() + ": ");
                }
            } catch (SocketException se) {
                System.out.println("[ ! ]\tDisconnected.");
                break;
            } catch (IOException ioe) {
                System.out.println("[ ! ]\tUps! an error ocurred (I/O)");
                break;
            }
        }
    }
}
