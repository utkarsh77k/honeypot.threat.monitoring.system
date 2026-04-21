package com.honeypot;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * JavaFX Dashboard for attack analysis and visualization.
 */
public class AnalysisDashboard extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Honeypot Attack Dashboard");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Load recent logs
        TextArea logView = new TextArea("Logs: " + loadLogsPreview());
        logView.setEditable(false);
        logView.setPrefHeight(200);

        // Attack stats pie chart (static example)
        PieChart.Data loginAttempts = new PieChart.Data("Failed Logins", 75);
        PieChart.Data shellCmds = new PieChart.Data("Shell Commands", 25);
        PieChart chart = new PieChart(FXCollections.observableArrayList(loginAttempts, shellCmds));
        chart.setTitle("Attack Distribution");

        // Recent IPs
        ListView<String> ipList = new ListView<>();
        ipList.getItems().addAll("127.0.0.1 (5 attempts)", "AttackerIP (12 cmds)");

        root.getChildren().addAll(title, chart, new Label("Recent IPs:"), ipList,
                new Label("Log Preview:"), logView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Honeypot Analysis");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String loadLogsPreview() {
        try {
            return Files.exists(Paths.get("logs/attacks.json")) ? "Logs loaded (check file). Recent attacks captured."
                    : "No logs yet. Run server and test.";
        } catch (Exception e) {
            return "Log load error.";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
