package com.honeypot;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        Session session = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String ip = socket.getInetAddress().getHostAddress();

            // Fix IPv6 localhost
            if ("0:0:0:0:0:0:0:1".equals(ip)) {
                ip = "127.0.0.1";
            }

            session = new Session(ip);

            out.println("Last login: Mon Apr 13 from 192.168.1.5");
            out.println("Welcome to Secure Server");

            //  INPUT (CLEANED)
            out.print("Username: ");
            out.flush();
            String username = cleanInput(in.readLine());

            out.print("Password: ");
            out.flush();
            String password = cleanInput(in.readLine());

            session.setCredentials(username, password);

            out.println("Access Granted...\n");

            //  COMMAND LOOP
            while (true) {
                out.print("root@server:~$ ");
                out.flush();

                String command = in.readLine();

                if (command == null)
                    break;

                command = cleanInput(command);

                if (command.equalsIgnoreCase("exit")) {
                    out.println("Session terminated.");
                    break;
                }

                session.addCommand(command);
                handleCommand(command);
            }

            session.endSession();

            // DETECTION
            int risk = DetectionEngine.calculateRisk(session.getCommands(), 1);
            String type = DetectionEngine.classify(session.getCommands());

            // HREAT INTEL
            ThreatIntelEngine.updateThreatProfile(ip, password, session.getCommands());
            String threatLevel = ThreatIntelEngine.getThreatLevel(ip);

            // GEO-IP
            JsonNode geo = GeoIPService.getIPDetails(ip);

            String country = "Unknown";
            String city = "Unknown";
            String org = "Unknown";
            double lat = 0;
            double lon = 0;

            if (geo != null) {
                country = geo.has("country") ? geo.get("country").asText() : "Unknown";
                city = geo.has("city") ? geo.get("city").asText() : "Unknown";
                org = geo.has("org") ? geo.get("org").asText() : "Unknown";

                lat = geo.has("lat") ? geo.get("lat").asDouble() : 0;
                lon = geo.has("lon") ? geo.get("lon").asDouble() : 0;
            }

            // CREATE RECORD
            AttackRecord record = new AttackRecord(
                    ip,
                    username,
                    password,
                    session.getCommands(),
                    LocalDateTime.now().toString(),
                    risk,
                    type,
                    session.getDuration(),
                    threatLevel);

            record.country = country;
            record.city = city;
            record.org = org;
            record.lat = lat;
            record.lon = lon;

            //log
            AttackLogger.log(record);

            // cl
            System.out.println(
                    "[ATTACK] IP=" + ip +
                            " | Risk=" + risk +
                            " | Type=" + type +
                            " | Threat=" + threatLevel);

        } catch (Exception e) {
            System.out.println("[ERROR] Client handling failed");
            e.printStackTrace();
        } finally {
            close();
        }
    }

    // CLEAN INPUT
    private String cleanInput(String input) {
        if (input == null)
            return "";
        return input.replaceAll("\\p{C}", "").trim();
    }

    private void handleCommand(String cmd) {

        switch (cmd) {
            case "ls":
                out.println("admin.txt  passwords.db  secrets.txt  logs/");
                break;
            case "whoami":
                out.println("root");
                break;
            case "pwd":
                out.println("/root");
                break;
            case "uname":
                out.println("Linux honeypot 5.15.0-ubuntu");
                break;
            case "cat passwords.db":
                out.println("Permission denied");
                break;
            default:
                out.println("command not found");
        }
    }

    private void close() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}