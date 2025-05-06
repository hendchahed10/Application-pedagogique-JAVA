package com.example.projet.service;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;

public class CSVExporter {

    public static void exportAvisToCSV(String dbPath, String outputCSVPath) throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println(" Connecting to database: " + dbPath);
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            Statement stmt = conn.createStatement();
            System.out.println(" Executing query...");

            ResultSet rs = stmt.executeQuery("SELECT etudiant_id, ressource_id, note FROM ResourceInteraction");

            File csvFile = new File(outputCSVPath);
            System.out.println(" Writing to CSV: " + csvFile.getAbsolutePath());

            try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
                int rowCount = 0;
                while (rs.next()) {
                    writer.println(rs.getLong("etudiant_id") + "," + rs.getLong("ressource_id") + "," + rs.getDouble("note"));
                    rowCount++;
                }
                System.out.println("✔ Wrote " + rowCount + " rows to CSV.");
            }
            // Close resources...
        } catch (Exception e) {
            System.err.println(" Export failed: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to handle in caller
        }
    }
    public static String getRessourceTitre(long ressourceId) throws Exception {
        String dbPath = "jdbc:sqlite:C:\\Users\\damla\\IdeaProjects\\javads22\\database.db";
        String titre = "Resource ID: " + ressourceId; // Default value

        try (Connection conn = DriverManager.getConnection(dbPath);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT titre FROM Ressource WHERE id = ?")) {

            stmt.setLong(1, ressourceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                titre = rs.getString("titre");
            }
        }
        return titre;
    }
}
