package com.honeypot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreatIntelEngine {

    private static final Map<String, Integer> ipScore = new HashMap<>();

    public static void updateThreatProfile(String ip, String password, List<String> commands) {

        int score = ipScore.getOrDefault(ip, 0);

        if (commands.size() > 5)
            score += 10;

        if (commands.stream().anyMatch(c -> c.toLowerCase().contains("password"))) {
            score += 50;
        }

        ipScore.put(ip, score);
    }

    public static String getThreatLevel(String ip) {

        int score = ipScore.getOrDefault(ip, 0);

        if (score > 50)
            return "HIGH";
        if (score > 20)
            return "MEDIUM";
        return "LOW";
    }
}