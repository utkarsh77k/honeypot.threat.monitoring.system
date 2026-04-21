package com.honeypot;

import java.net.ServerSocket;
import java.net.Socket;

public class HoneypotServer {

    public static void main(String[] args) {
        int port = 2323;

        System.out.println("🔥 Honeypot running on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}