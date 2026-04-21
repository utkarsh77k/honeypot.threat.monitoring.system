package com.honeypot;

import java.util.List;

public class DetectionEngine {

    public static int calculateRisk(List<String> commands, int attempts) {

        int score = 0;

        // Brute force attempts
        if (attempts > 5)
            score += 40;

        for (String cmd : commands) {

            cmd = cmd.toLowerCase();

            // Credential access
            if (cmd.contains("password") || cmd.contains("passwd") || cmd.contains("shadow")) {
                score += 40;
            }

            // Recon
            if (cmd.contains("ls") || cmd.contains("whoami") || cmd.contains("pwd")) {
                score += 10;
            }

            // Dangerous commands
            if (cmd.contains("rm") || cmd.contains("shutdown") || cmd.contains("reboot")) {
                score += 50;
            }
        }

        return Math.min(score, 100);
    }

    public static String classify(List<String> commands) {

        for (String cmd : commands) {
            cmd = cmd.toLowerCase();

            if (cmd.contains("password") || cmd.contains("passwd") || cmd.contains("shadow")) {
                return "Credential Access";
            }
        }

        if (commands.size() > 5) {
            return "Reconnaissance";
        }

        return "Normal";
    }
}