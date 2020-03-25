package client.core;

import client.data.Client;
import client.core.ReadThread;
import client.core.WriteThread;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.regex.Pattern;
import client.data.ClientSocket;
public class SocketStreamClient {

    private String nickName;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void start() {
        Client client = new Client();
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("\nEnter host: ");
            client.setHostname(sc.nextLine());
            System.out.print("\nEnter port: ");
            client.setPort(sc.nextInt());
            System.out.println("\nTrying to connect to server...");
            Socket socket = new Socket();
            InetSocketAddress isa = new InetSocketAddress(client.getHostname(), client.getPort());
            socket.connect(isa, ClientSocket.TIMEOUT);
            System.out.println("\nConnected!");
            Scanner sc2 = new Scanner(System.in);
            String nickName;
            boolean exit = false;
            do {
                System.out.print("[ > ]\tEnter your nickname: ");
                nickName = sc2.nextLine();
                if (nickName.equals(null) || nickName.isEmpty() || nickName.length() < 2 || nickName.length() > 10 || !Pattern.matches("[a-zA-Z]+", nickName)) {
                    System.out.println("\n[ ! ]\tPlease, use a valid nickname.");
                } else {
                    exit = true;
                }
            } while (!exit);
            new ReadThread(socket, this).start();
            new WriteThread(socket, nickName, this).start();
        } catch (SocketException se) {
            System.out.println("\n[ ! ]\tServer not found.");
        } catch (SocketTimeoutException ste) {
            System.out.println("\n[ ! ]\tConnection timeout was reached.");
        } catch (IOException ioe) {
            System.out.println("\n[ ! ]\tBad server format.");
        } catch (Exception e) {
            System.out.println("\n[ ! ]\tUps! an error ocurred.");
        }
    }
}
