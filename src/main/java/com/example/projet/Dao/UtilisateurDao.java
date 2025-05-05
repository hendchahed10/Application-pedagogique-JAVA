package com.example.projet.Dao;
import java.util.List;

import com.example.projet.Model.StudentModel;
import com.example.projet.Model.UserModel;
import com.example.projet.Model.TeacherModel;
import java.sql.*;

public class UtilisateurDao {
    private static final String URL = "jdbc:sqlite:C:/IdeaProjects/Javads2/database.db";

    public UtilisateurDao() {
    }

    public UtilisateurDao(String fullName, String email, String password, String role) {
    }


    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
            return null;
        }
    }

    public UserModel getUtilisateurByEmailAndPassword(String email, String motDePasse) {
        String sql = "SELECT u.*, e.modules FROM Utilisateur u " +
                "LEFT JOIN Enseignant e ON u.id = e.id_enseignant " +
                "WHERE u.email = ? AND u.motDePasse = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, motDePasse);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                int userId = rs.getInt("id");

                if ("etudiant".equalsIgnoreCase(role)) {
                    return new StudentModel(
                            userId,
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("motDePasse"),
                            role
                    );
                } else if ("enseignant".equalsIgnoreCase(role)) {
                    String modules = rs.getString("modules");
                    return new TeacherModel(
                            userId,
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("motDePasse"),
                            role,
                            modules != null ? modules : ""
                    );
                } else {
                    return new UserModel(
                            userId,
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("motDePasse"),
                            role
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    public static boolean ajouterUtilisateur(UserModel utilisateur) {
        String sql;

        // Vérifier si c'est un étudiant ou un enseignant pour définir la requête SQL
        if (utilisateur instanceof StudentModel) {
            if (utilisateur instanceof StudentModel) {
                StudentModel student = (StudentModel) utilisateur;
                sql = "INSERT INTO Utilisateur(nom, email, motDePasse, role) VALUES (?, ?, ?, ?)";

                try (Connection conn = connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, student.getNom());
                    pstmt.setString(2, student.getEmail());
                    pstmt.setString(3, student.getMotDePasse());
                    pstmt.setString(4, student.getRole());
                    pstmt.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    System.out.println("Erreur ajout étudiant : " + e.getMessage());
                    return false;
                }

            } else if (utilisateur instanceof TeacherModel) {
                TeacherModel teacher = (TeacherModel) utilisateur;
                sql = "INSERT INTO Utilisateur(nom, email, motDePasse, role) VALUES (?, ?, ?, ?)";

                try (Connection conn = connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, teacher.getNom());
                    pstmt.setString(2, teacher.getEmail());
                    pstmt.setString(3, teacher.getMotDePasse());
                    pstmt.setString(4, teacher.getRole());
                    pstmt.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    System.out.println("Erreur ajout enseignant : " + e.getMessage());
                    return false;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean isEmailExist(String email) {
        String sql = "SELECT COUNT(*) FROM Utilisateur WHERE email = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;  // Si le résultat est supérieur à 0, l'email existe déjà
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'email : " + e.getMessage());
        }
        return false; // L'email n'existe pas
    }

    public String getNameById(int id) throws SQLException {
        String sql = "SELECT nom FROM Utilisateur WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // e.getMessage(); ← Cette ligne ne fait rien ici
        }
        return "erreur"; // Valeur par défaut si rien trouvé ou exception levée
    }
}
