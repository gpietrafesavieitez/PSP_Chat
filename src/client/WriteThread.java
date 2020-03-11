package client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {

    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.print("[ > ]\tEnter your nickname: ");
        String nickName = sc.nextLine();
        client.setNickName(nickName);
        writer.println(nickName);
        String text;
        System.out.println("[ # ]\tWelcome " + nickName + "! Remember you can exit anytime just typing '/bye'.");
        do {
            System.out.print("[ > ]\t" + nickName + ": ");
            text = sc.nextLine();
            writer.println(text);
        } while (!text.equals("/bye"));
        try {
            socket.close();
        } catch (IOException ioe) {
            System.out.println("[ ! ]\tUps! an error ocurred (I/O)");
        }

    }
}
