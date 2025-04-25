package com.example.projet.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/IdeaProjects/projet/database.db";
        String[] sqlStatements = {
                "CREATE TABLE IF NOT EXISTS Utilisateur (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nom TEXT NOT NULL, " +
                        "email TEXT UNIQUE NOT NULL CHECK(email LIKE '%@%.%'), " +
                        "motDePasse TEXT NOT NULL, " +
                        "role TEXT CHECK(role IN ('etudiant', 'enseignant')) NOT NULL" +
                        ")",

                "CREATE TABLE IF NOT EXISTS Enseignant (" +
                        "id_enseignant INTEGER PRIMARY KEY, " +
                        "modules TEXT, " +
                        "FOREIGN KEY (id_enseignant) REFERENCES Utilisateur(id) ON DELETE CASCADE" +
                        ")",

                "CREATE TABLE IF NOT EXISTS Ressource (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "titre TEXT NOT NULL, " +
                        "description TEXT, " +
                        "difficulte TEXT CHECK(difficulte IN ('Facile', 'Moyen', 'Difficile')), " +
                        "document TEXT, " +
                        "enseignant_id INTEGER NOT NULL, " +
                        "FOREIGN KEY (enseignant_id) REFERENCES Enseignant(id_enseignant)" +
                        ")",

                "CREATE TABLE IF NOT EXISTS Avis (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "etudiant_id INTEGER NOT NULL, " +
                        "ressource_id INTEGER NOT NULL, " +
                        "note REAL CHECK (note BETWEEN 0 AND 5), " +
                        "commentaire TEXT, " +
                        "FOREIGN KEY (etudiant_id) REFERENCES Etudiant(id_etudiant), " +
                        "FOREIGN KEY (ressource_id) REFERENCES Ressource(id), " +
                        "UNIQUE(etudiant_id, ressource_id)" +
                        ")",

                "CREATE TABLE IF NOT EXISTS ResourceInteraction (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "etudiant_id INTEGER NOT NULL, " +
                        "ressource_id INTEGER NOT NULL, " +
                        "date_consultation TEXT, " +
                        "date_enregistrement TEXT, " +
                        "date_completion TEXT, " +
                        "statut TEXT CHECK(statut IN ('Consultée', 'Enregistrée', 'Terminée')), " +
                        "FOREIGN KEY (etudiant_id) REFERENCES Etudiant(id_etudiant), " +
                        "FOREIGN KEY (ressource_id) REFERENCES Ressource(id), " +
                        "UNIQUE(etudiant_id, ressource_id, statut)" +
                        ")",

                "CREATE INDEX IF NOT EXISTS idx_ressource_enseignant ON Ressource(enseignant_id)",
                "CREATE INDEX IF NOT EXISTS idx_avis_etudiant ON Avis(etudiant_id)",
                "CREATE INDEX IF NOT EXISTS idx_interaction_ressource ON ResourceInteraction(ressource_id)"
        };


        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Exécution de toutes les commandes SQL
            for (String sql : sqlStatements) {
                stmt.execute(sql);
            }
            System.out.println("[SUCCÈS] Base de données initialisée avec les tables :");
            System.out.println("- Utilisateur, Etudiant, Enseignant");
            System.out.println("- Ressource, Avis, ResourceInteraction");

        } catch (SQLException e) {
            System.err.println("[ERREUR] Initialisation de la BDD : " + e.getMessage());
        }
    }
}