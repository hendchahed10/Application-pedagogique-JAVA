package com.example.projet.Dao;

import com.example.projet.Model.Ressource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RessourceDao {
    private Connection con;
    // Initialisation de la connexion à la base de données
    {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:C:/IdeaProjects/Javads2/database.db");
            System.out.println("Connexion réussie");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Constructeur avec injection de dépendance pour la connexion
    public RessourceDao(Connection con) {
        this.con = con;
    }
    // Constructeur par défaut
    public RessourceDao() {
    }

    //Récupère l'identifiant d'une ressource à partir de son titre
    public int getIdByTitle(String nom_res) {
        String sql = "SELECT id FROM Ressource WHERE titre= ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nom_res);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'ID : " + e.getMessage());
        }
        return 0;
    }

    //Récupère toutes les ressources créées par un enseignant spécifique

    public List<Ressource> getAllResourcesByTeacherId(int teacherId) throws SQLException {
        List<Ressource> resources = new ArrayList<>();
        String sql = "SELECT * FROM Ressource WHERE enseignant_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ressource resource = new Ressource(0,null,null, null, null, null, 0);
                resource.setId(rs.getInt("id"));
                resource.setTitre(rs.getString("titre"));
                resource.setCategorie(rs.getString("categorie"));
                resource.setDescription(rs.getString("description"));
                resource.setDifficulte(rs.getString("difficulte"));
                resource.setDocument(rs.getString("document"));
                resource.setId_enseignant(rs.getInt("enseignant_id"));
                resources.add(resource);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des ressources : " + e.getMessage());
            throw e;
        }
        return resources;
    }

    // ============== Méthodes spécifiques pour les étudiants ==============

    public void addFavorite(int studentId, int resourceId,double note) throws SQLException {
        String sql = "INSERT INTO ResourceInteraction (etudiant_id, ressource_id, note, date_consultation, date_enregistrement, date_completion, statut) VALUES (?, ?, ?, DATETIME('now'), DATETIME('now'), DATETIME('now'), ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, resourceId);
            stmt.setDouble(3, note);
            stmt.setString(4, "Favoris");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERREUR DANS L'INSERTION : " + e.getMessage());
        }
    }
    public void addCompleted(int studentId, int resourceId,double note) throws SQLException {
        String sql = "INSERT INTO ResourceInteraction (etudiant_id, ressource_id, note, date_consultation, date_enregistrement, date_completion, statut) VALUES (?, ?, ?, DATETIME('now'), DATETIME('now'), DATETIME('now'), ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, resourceId);
            stmt.setDouble(3, note);
            stmt.setString(4, "Terminee");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERREUR DANS L'INSERTION : " + e.getMessage());
        }
    }

    //Récupère les ressources d'un étudiant selon un statut spécifique (Terminée/Favoris)
    public List<Ressource> getResourcesByStatus(int studentId, String status) throws SQLException {
        List<Ressource> resources = new ArrayList<>();
        String sql = "SELECT r.* FROM Ressource r " +
                "JOIN ResourceInteraction ri ON r.id = ri.ressource_id " +
                "WHERE ri.etudiant_id = ? AND ri.statut = ?";


        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setString(2, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ressource resource = new Ressource(0,null, null,null,null,null,0);
                resource.setTitre(rs.getString("titre"));
                resource.setDescription(rs.getString("description"));
                resource.setDifficulte(rs.getString("difficulte"));
                resource.setDocument(rs.getString("document"));
                resources.add(resource);
            }
        }
        return resources;
    }


    //Compte le nombre de ressources favorites d'un étudiant
    public int NbRessourcesFavories(int idEtudiant) throws SQLException {
        String sql = "SELECT COUNT(ressource_id) AS Total FROM ResourceInteraction WHERE etudiant_id= ? AND statut='Favoris'";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Total");
            }
        }
        return 0;
    }

    public int getTotalRessources() {
        String sql = "SELECT COUNT(*) FROM Ressource";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // Ou lancez une exception personnalisée
        }
    }

    //Compte le nombre de ressources terminées par un étudiant
    public int NbRessourcesTerminees(int idEtudiant) throws SQLException {
        String sql = "SELECT COUNT(ressource_id) AS Total FROM ResourceInteraction WHERE etudiant_id= ? AND statut='Terminee'";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Total");
            }
        }
        return 0;
    }

    // ============== Méthodes pour la gestion des ressources ==============

    public List<Ressource> getToutesLesRessources() {
        List<Ressource> ressources = new ArrayList<>();
        String sql = "SELECT * FROM Ressource";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ressource r = new Ressource(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("categorie"),
                        rs.getString("description"),
                        rs.getString("difficulte"),
                        rs.getString("document"),
                        rs.getInt("enseignant_id")
                );
                ressources.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ressources;
    }

    //Récupère toutes les ressources d'un enseignant

    public List<Ressource> getRessourcesByEnseignantId(int enseignantId) {
        List<Ressource> ressources = new ArrayList<>();
        String sql = "SELECT * FROM Ressource WHERE enseignant_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ressource ressource = new Ressource(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("categorie"),
                            rs.getString("description"),
                            rs.getString("difficulte"),
                            rs.getString("document"),
                            rs.getInt("enseignant_id")
                    );
                    ressources.add(ressource);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des ressources : " + e.getMessage());
        }
        return ressources;
    }

    //Met à jour les informations d'une ressource existante

    public void mettreAJourRessource(Ressource ressource) {
        String sql = "UPDATE Ressource SET titre = ?, description = ?, difficulte = ?, document = ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ressource.getTitre());
            stmt.setString(2, ressource.getDescription());
            stmt.setString(3, ressource.getDifficulte());
            stmt.setString(4, ressource.getDocument());
            stmt.setInt(5, ressource.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la ressource : " + e.getMessage());
        }
    }

    //Ajoute une nouvelle ressource dans la base de données

    public void ajouterRessource(Ressource ressource) {
        String sql = "INSERT INTO Ressource (titre, categorie, description, difficulte, document, enseignant_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ressource.getTitre());
            stmt.setString(2, ressource.getCategorie());
            stmt.setString(3, ressource.getDescription());
            stmt.setString(4, ressource.getDifficulte().trim());
            stmt.setString(5, ressource.getDocument());
            stmt.setInt(6, ressource.getId_enseignant());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la ressource : " + e.getMessage());
        }
    }

    //Supprime une ressource de la base de données

    public void supprimerRessource(int id) {
        String sql = "DELETE FROM Ressource WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la ressource : " + e.getMessage());
        }
    }

    // ============== Méthodes statistiques ==============

    //Compte le nombre total d'étudiants dans la base de données
    public int getNombreTotalEtudiants() {
        String sql = "SELECT COUNT(*) AS total FROM Utilisateur WHERE role = 'etudiant'";
        int total = 0;

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du comptage des étudiants : " + e.getMessage());
        }
        return total;
    }

    //Compte le nombre de ressources favorites pour un enseignant
    public int getNombreRessourcesFavorisParEnseignant(int enseignantId) {
        String sql = "SELECT COUNT(DISTINCT ri.ressource_id) AS nb_favoris " +
                "FROM ResourceInteraction ri " +
                "JOIN Ressource r ON ri.ressource_id = r.id " +
                "WHERE r.enseignant_id = ? AND ri.statut = 'Favoris'";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("nb_favoris");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du comptage des favoris : " + e.getMessage());
        }
        return 0;
    }

    //Calcule la note moyenne des ressources d'un enseignant
    public double getNoteMoyenRessourcesParEnseignant(int enseignantId) {
        String sql = "SELECT AVG(ri.note) AS moyenne " +
                "FROM Ressource r " +
                "JOIN ResourceInteraction ri ON r.id = ri.ressource_id " +
                "WHERE r.enseignant_id = ? AND ri.note IS NOT NULL";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("moyenne");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul de la note moyenne : " + e.getMessage());
        }
        return 0.0;
    }

    //Compte le nombre de ressources créées par un enseignant
    public int getNombreRessourcesParEnseignant(int enseignantId) {
        String sql = "SELECT COUNT(*) FROM Ressource WHERE enseignant_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération ressources de l'enseignant : " + e.getMessage());
        }
        return 0;
    }
}

