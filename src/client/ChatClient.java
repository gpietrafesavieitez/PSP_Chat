package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatClient {

    private String nickName;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }
    
    public void start() throws InterruptedException {
        Client client = new Client();
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter hostname: ");
            client.setHostname(sc.nextLine());
            System.out.print("Enter port: ");
            client.setPort(sc.nextInt());
            System.out.println("Trying to connect to server...");
            Socket socket = new Socket();
            InetSocketAddress isa = new InetSocketAddress(client.getHostname(), client.getPort());
            socket.connect(isa);
            System.out.println("Connected!");
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
        } catch (SocketException se) {
            System.out.println("[ ! ]\tDisconnected.");
        } catch (IOException ioe) {
            System.out.println("[ ! ]\tUps! an error ocurred (I/O)");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }
}
