package com.example.projet.Dao;

import com.example.projet.Model.Ressource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RessourceDao {

    private Connection con;
    {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:C:/IdeaProjects/projet_java/database.db");
            System.out.println("Connexion réussie");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RessourceDao(Connection con) {
        this.con = con;
    }
    public RessourceDao(){}
    public int getIdByTitle(String nom_res)
    {
        String sql = "SELECT id FROM Ressource WHERE titre= ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nom_res);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
        return 0; //retourne 0 si quelque chose tourne mal
    }
    //méthodes spécifiques pour l'enseignant
    public void ajouterRessource(Ressource ressource) throws SQLException {
        String query = "INSERT INTO Ressource (titre, description, difficulte, document, enseignant_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, ressource.getTitre());
            statement.setString(2, ressource.getDescription());
            statement.setString(3, ressource.getDifficulte());
            statement.setString(4, ressource.getDocument());
            statement.setInt(5, ressource.getId_enseignant()); // Add the teacher's ID
            statement.executeUpdate();
            System.out.println("Ressource ajoutée avec succès!");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la ressource: " + e.getMessage());
        }
    }
    public void supprimerRessource(int ressourceId) throws SQLException {
        String query = "DELETE FROM Ressource WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, ressourceId);  // Pass the resource ID to delete
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Ressource supprimée avec succès!");
            } else {
                System.out.println("Aucune ressource trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la ressource: " + e.getMessage());
        }
    }
    public List<Ressource> getAllResourcesByTeacherId(int teacherId) throws SQLException {
        List<Ressource> resources = new ArrayList<>();
        String sql = "SELECT * FROM Ressource WHERE enseignant_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ressource resource = new Ressource(null, null, null, null, 0);
                resource.setTitre(rs.getString("titre"));
                resource.setDescription(rs.getString("description"));
                resource.setDifficulte(rs.getString("difficulte"));
                resource.setDocument(rs.getString("document"));
                resources.add(resource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERREUR DANS L'INSERTION : " + e.getMessage());

        }
        return resources;
    }

    //méthodes spécifiques pour l'étudiant

    public void addFavorite(int studentId, int resourceId) throws SQLException {
        String sql = "INSERT INTO ResourceInteraction (etudiant_id, ressource_id, date_consultation, date_enregistrement, date_completion, statut) VALUES (?, ?, DATETIME('now'), DATETIME('now'), DATETIME('now'), ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, resourceId);
            stmt.setString(3, "Enregistrée");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERREUR DANS L'INSERTION : " + e.getMessage());
        }
    }
    public void addCompleted(int studentId, int resourceId) throws SQLException {
        String sql = "INSERT INTO ResourceInteraction (etudiant_id, ressource_id, date_consultation, date_enregistrement, date_completion, statut) VALUES (?, ?, DATETIME('now'), DATETIME('now'), DATETIME('now'), ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, resourceId);
            stmt.setString(3, "Terminée");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERREUR DANS L'INSERTION : " + e.getMessage());
        }
    }
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
                Ressource resource = new Ressource(null,null,null,null,0);
                resource.setTitre(rs.getString("titre"));
                resource.setDescription(rs.getString("description"));
                resource.setDifficulte(rs.getString("difficulte"));
                resource.setDocument(rs.getString("document"));
                resources.add(resource);
            }
        }
        return resources;
    }
    public int NbRessourcesFavories(int idEtudiant) throws SQLException {
        String sql = "SELECT COUNT(ressource_id) AS Total FROM ResourceInteraction WHERE etudiant_id= ? AND statut='Enregistrée'";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Total");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; //retourne 0 si quelque chose tourne mal
    }
    public int NbRessourcesTerminees(int idEtudiant) throws SQLException {
        String sql = "SELECT COUNT(ressource_id) AS Total FROM ResourceInteraction WHERE etudiant_id= ? AND statut='Terminée'";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Total");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; //retourne 0 si quelque chose tourne mal
    }
    }

