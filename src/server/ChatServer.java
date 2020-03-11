package server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {
    
    private ArrayList<UserThread> listOfUserThreads = new ArrayList<>();
    private ArrayList<String> listOfNickNames = new ArrayList<>();
    
    public ArrayList<String> getListOfNickNames() {
        return listOfNickNames;
    }

    public void broadcast(String msg, UserThread userThread) {
        for (UserThread ut : listOfUserThreads) {
            if (ut != userThread) {
                ut.sendMessage(msg);
            }
        }
    }

    public void addNickName(String nickName) {
        listOfNickNames.add(nickName);
    }

    public void removeUser(String nickName, UserThread aUser) {
        boolean removed = listOfNickNames.remove(nickName);
        if (removed) {
            listOfUserThreads.remove(aUser);
            System.out.println("[ info ]\tUser " + nickName + " got disconnected.");
        }
    }

    public boolean hasUsers() {
        return !this.listOfNickNames.isEmpty();
    }
    
    public void start() throws InterruptedException {
        try {
            Server server = new Server("ChatServer");
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter hostname: ");
            server.setHostname(sc.nextLine());
            System.out.print("Enter port: ");
            server.setPort(sc.nextInt());
            System.out.println("[ info ]\tStarting server...");
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress isa = new InetSocketAddress(server.getHostname(), server.getPort());
            serverSocket.bind(isa);
            System.out.println("[ info ]\tServer started! Listening for new connections...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[ info ]\tNew connection from: " + clientSocket);
                UserThread userThread = new UserThread(clientSocket, this);
                listOfUserThreads.add(userThread);
                userThread.start();
            }
        } catch (BindException be) {
            System.out.println("[ error ]\tServer already running. Only one active instance of the server is allowed.");
        } catch (IOException ioe) {
            System.out.println("[ error ]\tI/O Exception. Errors detailed below:");
            ioe.printStackTrace();
        } catch (Exception e) {
            System.out.println("[ error ]\tSomething went wrong. Errors detailed below:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }
}
