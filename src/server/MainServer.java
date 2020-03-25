package server;

import server.core.SocketStreamServer;

public class MainServer {

    public static void main(String[] args) {
        System.out.println("[ ChatServer ]");
        SocketStreamServer chatServer = new SocketStreamServer();
        chatServer.start();
    }
}
