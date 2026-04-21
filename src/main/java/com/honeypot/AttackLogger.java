package com.honeypot;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AttackLogger {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File file = new File("src/main/Dashboard/logs/attacks.json");

    private static List<AttackRecord> records = new ArrayList<>();

    static {
        try {
            file.getParentFile().mkdirs();

            if (file.exists()) {
                AttackRecord[] existing = mapper.readValue(file, AttackRecord[].class);
                for (AttackRecord r : existing)
                    records.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void log(AttackRecord record) {
        try {
            records.add(record);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}