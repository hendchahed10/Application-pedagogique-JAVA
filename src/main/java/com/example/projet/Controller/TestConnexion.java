package com.example.projet.Controller;

import java.sql.*;

public class TestConnexion {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:database.db";

        String[] createTables = {
                "CREATE TABLE IF NOT EXISTS Utilisateur (" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    nom TEXT NOT NULL," +
                        "    email TEXT UNIQUE NOT NULL," +
                        "    motDePasse TEXT NOT NULL," +
                        "    role TEXT NOT NULL CHECK (role IN ('etudiant', 'enseignant'))" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Etudiant (" +
                        "   id_etudiant INTEGER PRIMARY KEY," +
                        "   FOREIGN KEY (id_etudiant) REFERENCES Utilisateur(id) ON DELETE CASCADE" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Enseignant (" +
                        "   id_enseignant INTEGER PRIMARY KEY," +
                        "   FOREIGN KEY (id_enseignant) REFERENCES Utilisateur(id) ON DELETE CASCADE" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Ressource (" +
                        "   id_ressource INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "   titre TEXT NOT NULL," +
                        "   description TEXT," +
                        "   niveauDifficulte TEXT NOT NULL CHECK(niveauDifficulte IN ('Facile', 'Moyenne', 'Difficile'))," +
                        "   urlPdf TEXT," +
                        "   enseignant_id INTEGER NOT NULL," +
                        "   FOREIGN KEY (enseignant_id) REFERENCES Enseignant(id_enseignant) ON DELETE CASCADE" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Avis (" +
                        "   id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "   etudiant_id INTEGER NOT NULL," +
                        "   ressource_id INTEGER NOT NULL," +
                        "   note REAL CHECK (note BETWEEN 0 AND 5)," +
                        "   commentaire TEXT," +
                        "   FOREIGN KEY (etudiant_id) REFERENCES Etudiant(id_etudiant) ON DELETE CASCADE," +
                        "   FOREIGN KEY (ressource_id) REFERENCES Ressource(id) ON DELETE CASCADE" +
                        ");",

                "CREATE TABLE IF NOT EXISTS Etudiant_Ressource (" +
                        "   id_etudiant INTEGER NOT NULL," +
                        "   id_ressource INTEGER NOT NULL," +
                        "   type TEXT NOT NULL CHECK(type IN ('Consultée', 'Enregistrée', 'Terminée'))," +
                        "   PRIMARY KEY (id_etudiant, id_ressource, type)," +
                        "   FOREIGN KEY (id_etudiant) REFERENCES Etudiant(id_etudiant) ON DELETE CASCADE," +
                        "   FOREIGN KEY (id_ressource) REFERENCES Ressource(id) ON DELETE CASCADE" +
                        ");"
        };

        String[] insertData = {
                "INSERT OR IGNORE INTO Utilisateur (nom, email, motDePasse, role) VALUES " +
                        "('Mohamed Ben Ali', 'mohamed.benali@esprit.tn', 'prof123', 'enseignant');",

                "INSERT OR IGNORE INTO Utilisateur (nom, email, motDePasse, role) VALUES " +
                        "('Fatma Zouari', 'fatma.zouari@ensi.tn', 'enseignant456', 'enseignant');",

                "INSERT OR IGNORE INTO Utilisateur (nom, email, motDePasse, role) VALUES " +
                        "('Amine Trabelsi', 'amine.trabelsi@student.tn', 'etudiant789', 'etudiant');",

                "INSERT OR IGNORE INTO Utilisateur (nom, email, motDePasse, role) VALUES " +
                        "('Salma Abid', 'salma.abid@student.tn', 'salma2023', 'etudiant');",

                "INSERT OR IGNORE INTO Utilisateur (nom, email, motDePasse, role) VALUES " +
                        "('Youssef Ksouri', 'youssef.ksouri@student.tn', 'youssef123', 'etudiant');",

                "INSERT OR IGNORE INTO Enseignant (id_enseignant) VALUES (1);",
                "INSERT OR IGNORE INTO Enseignant (id_enseignant) VALUES (2);",
                "INSERT OR IGNORE INTO Etudiant (id_etudiant) VALUES (3);",
                "INSERT OR IGNORE INTO Etudiant (id_etudiant) VALUES (4);",
                "INSERT OR IGNORE INTO Etudiant (id_etudiant) VALUES (5);",

                "INSERT INTO Ressource (titre, description, niveauDifficulte, enseignant_id) " +
                        "VALUES ('Algèbre linéaire', 'Cours complet sur les matrices', 'Moyenne', 1);",

                "INSERT OR IGNORE  INTO Ressource (titre, description, niveauDifficulte, enseignant_id) " +
                        "VALUES ('Programmation Java', 'Introduction à Java SE', 'Facile', 2);",

                "INSERT OR IGNORE INTO Ressource (titre, description, niveauDifficulte, enseignant_id) " +
                        "VALUES ('Systèmes dexploitation', 'Processus, threads et gestion mémoire', 'Difficile', 1);",

                "INSERT OR IGNORE INTO Avis (etudiant_id, ressource_id, note, commentaire) VALUES " +
                        "(3, 1, 4.5, 'Explications très claires, mais quelques exercices supplémentaires seraient utiles');",

                "INSERT OR IGNORE INTO Avis (etudiant_id, ressource_id, note, commentaire) VALUES " +
                        "(4, 1, 5.0, 'Parfait! Les exemples concrets m''ont beaucoup aidé à comprendre');",

                "INSERT OR IGNORE INTO Avis (etudiant_id, ressource_id, note, commentaire) VALUES " +
                        "(5, 1, 3.5, 'Bon contenu mais le PDF serait plus lisible avec une meilleure mise en page');"
        };

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            for (String sql : createTables) {
                stmt.executeUpdate(sql);
            }
            System.out.println("Tables créées avec succès!");

            for (String sql : insertData) {
                try {
                    stmt.executeUpdate(sql);
                    System.out.println("Requête exécutée avec succès");
                } catch (SQLException e) {
                    System.err.println("Erreur sur la requête: " + sql);
                    System.err.println("Message d'erreur: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
        }
    }
}