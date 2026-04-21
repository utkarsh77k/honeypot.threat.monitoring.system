package com.honeypot;

import java.util.List;

public class AttackRecord {

    public String ip;
    public String username;
    public String password;
    public List<String> commands;
    public String timestamp;

    public int riskScore;
    public String attackType;
    public long sessionDuration;
    public String threatLevel;

    public String country;
    public String city;
    public String org;

    // 🌍 NEW
    public double lat;
    public double lon;

    public AttackRecord(String ip, String username, String password,
            List<String> commands, String timestamp,
            int riskScore, String attackType,
            long duration, String threatLevel) {

        this.ip = ip;
        this.username = username;
        this.password = password;
        this.commands = commands;
        this.timestamp = timestamp;

        this.riskScore = riskScore;
        this.attackType = attackType;
        this.sessionDuration = duration;
        this.threatLevel = threatLevel;
    }
}