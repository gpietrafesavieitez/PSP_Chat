package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class UserThread extends Thread {

    private Socket clientSocket;
    private ChatServer chatServer;
    private PrintWriter writer;

    public UserThread(Socket clientSocket, ChatServer chatServer) {
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;
    }

    public void showUsers() {
        if (chatServer.hasUsers()) {
            writer.println("[ # ]\tConnected users: " + chatServer.getListOfNickNames());
        } else {
            writer.println("[ # ]\tChatroom is empty.");
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            OutputStream outputStream = clientSocket.getOutputStream();
            writer = new PrintWriter(outputStream, true);
            showUsers();
            String nickName = reader.readLine();
            chatServer.addNickName(nickName);
            String serverMessage;
            serverMessage = "[ ! ]\tUser '" + nickName + "' has joined the chat.";
            chatServer.broadcast(serverMessage, this);
            String clientMessage;
            do {
                clientMessage = reader.readLine();
                serverMessage = "[ < ]\t" + nickName + " says: " + clientMessage;
                chatServer.broadcast(serverMessage, this);
            } while (!clientMessage.equals("/bye"));
            chatServer.removeUser(nickName, this);
            clientSocket.close();
            serverMessage = "[ ! ]\tUser '" + nickName + "' has left the chat.";
            chatServer.broadcast(serverMessage, this);
        } catch (IOException ioe) {
            System.out.println("[ error ]\tI/O Exception, problems detailed below." + ioe.getMessage());
        }
    }

}
