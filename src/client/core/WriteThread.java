package client.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteThread extends Thread {

    private PrintWriter writer;
    private Socket socket;
    private SocketStreamClient client;
    private String nickName;

    public WriteThread(Socket socket, String nickName, SocketStreamClient client) throws IOException {
        this.socket = socket;
        this.client = client;
        this.nickName = nickName;
        OutputStream output = socket.getOutputStream();
        writer = new PrintWriter(output, true);
    }

    public void run() {
        try {
            Thread.sleep(1000);
            client.setNickName(this.nickName);
            writer.println(nickName);
            System.out.println("[ # ]\tWelcome " + nickName + "! Remember you can exit anytime just typing '/bye'.");
            Scanner sc = new Scanner(System.in);
            String text;
            do {
                System.out.print("[ > ]\t" + nickName + ": ");
                text = sc.nextLine();
                writer.println(text);
            } while (!text.equals("/bye"));
                socket.close();
        } catch (Exception e) {
            System.out.println("\n[ ! ]\tUps! an error ocurred.");
        }

    }
}
