package com.example.projet.Dao;

import com.example.projet.Model.TeacherModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnseignantDao extends UtilisateurDao {

    public List<TeacherModel> getAllEnseignants() {
        List<TeacherModel> enseignants = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur WHERE role = 'enseignant'";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String modulesString = rs.getString("modules");

                enseignants.add(new TeacherModel(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"), // nom de colonne corrigé
                        rs.getString("role"),
                        modulesString // transmis tel quel en String
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération enseignants : " + e.getMessage());
        }

        return enseignants;
    }

    // D'autres méthodes utiles peuvent être ajoutées ici
}
