package client;

import client.core.SocketStreamClient;

public class MainClient {

    public static void main(String[] args) {
        System.out.println("[ ChatClient ]");
        SocketStreamClient chatClient = new SocketStreamClient();
        chatClient.start();
    }
}
