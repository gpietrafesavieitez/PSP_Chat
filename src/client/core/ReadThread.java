package client.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ReadThread extends Thread {

    private BufferedReader reader;
    private Socket socket;
    private SocketStreamClient client;

    public ReadThread(Socket socket, SocketStreamClient client) throws IOException {
        this.socket = socket;
        this.client = client;
        InputStream input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public void run() {
        String response;
        try {
            while (!(response = reader.readLine()).equals(null)) {
                System.out.println("\n" + response);
                if (client.getNickName() != null) {
                    System.out.print("[ > ]\t" + client.getNickName() + ": ");
                }
            }
        } catch (SocketException se) {
            System.out.println("\n[ ! ]\tDisconnected from server.");
        } catch (NullPointerException npe) {
            System.out.println("\n[ ! ]\tServer got disconnected.");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("\n[ ! ]\tUps! an error ocurred.");
        }
    }
}
