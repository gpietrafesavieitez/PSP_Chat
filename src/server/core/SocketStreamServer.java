package server.core;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import server.data.Server;

public class SocketStreamServer {

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

    public void start() {
        try {
            Server server = new Server("ChatServer");
            Scanner sc = new Scanner(System.in);
            System.out.print("\nEnter host: ");
            server.setHostname(sc.nextLine());
            System.out.print("\nEnter port: ");
            server.setPort(sc.nextInt());
            System.out.println("\n[ info ]\tStarting server...");
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress isa = new InetSocketAddress(server.getHostname(), server.getPort());
            serverSocket.bind(isa);
            System.out.println("\n[ info ]\tServer started! Listening for new connections...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n[ info ]\tNew connection from: " + clientSocket);
                UserThread userThread = new UserThread(clientSocket, this);
                listOfUserThreads.add(userThread);
                userThread.start();
            }
        } catch (BindException be) {
            System.out.println("\n[ error ]\tAn instance of the server is already being used or another service is using this port. Errors detailed below:");
            be.printStackTrace();
        } catch (SocketException se) {
            System.out.println("\n[ error ]\tClient socket was disconnected wrongly. Errors detailed below:");
            se.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("\n[ error ]\tProblem with input/output streams. Errors detailed below:");
            ioe.printStackTrace();
        } catch (Exception e) {
            System.out.println("\n[ error ]\tSomething went wrong. Errors detailed below:");
            e.printStackTrace();
        }
    }
}
