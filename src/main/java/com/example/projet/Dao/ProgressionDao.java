package com.example.projet.Dao;
import java.sql.*;
import java.util.*;

public class ProgressionDao {
    private Connection conn;

    public ProgressionDao(Connection conn) {
        this.conn = conn;
    }
    public int NbRessourcesEnregistrees(int idEtudiant) throws SQLException {
        String sql = "SELECT COUNT(ressource_id) FROM Progression WHERE etudiant_id= ? AND statut='enregistrée' GROUP BY etudiant_id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("COUNT(ressource_id)");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; //retourne 0 si quelque chose tourne mal
    }
    public int NbRessourcesTerminees(int idEtudiant) throws SQLException {
        String sql = "SELECT COUNT(ressource_id) FROM Progression WHERE etudiant_id= ? AND statut='terminée' GROUP BY etudiant_id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("COUNT(ressource_id)");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; //retourne 0 si quelque chose tourne mal
    }
    public void EnregistrerRessource(int idEtudiant, int idRessource)
    {
        String sql = "INSERT INTO Progression VALUES (?, ?,'enregistreée')";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(idEtudiant,idRessource);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void TerminerRessource(int idEtudiant,int idRessource)
    {
        String sql = "UPDATE users SET statut='terminée' WHERE etudiant_id = ? AND ressource_id= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);   // set the first placeholder (etudiant_id)
            stmt.setInt(2, idRessource);  // set the second placeholder (ressource_id)

            int rowsAffected = stmt.executeUpdate();  // execute the update
            if (rowsAffected > 0) {
                System.out.println("✅ Ressource marquée comme terminée avec succès.");
            } else {
                System.out.println("⚠️ Aucun enregistrement mis à jour (vérifiez les IDs).");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de la mise à jour du statut de la ressource.");
        }
    }
}