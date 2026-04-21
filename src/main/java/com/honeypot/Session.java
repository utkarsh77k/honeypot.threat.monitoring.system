package com.honeypot;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private String ip;
    private String username;
    private String password;
    private List<String> commands = new ArrayList<>();

    private long startTime;
    private long endTime;

    public Session(String ip) {
        this.ip = ip;
        this.startTime = System.currentTimeMillis();
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addCommand(String cmd) {
        commands.add(cmd);
    }

    public void endSession() {
        this.endTime = System.currentTimeMillis();
    }

    public long getDuration() {
        return endTime - startTime;
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getIp() {
        return ip;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}