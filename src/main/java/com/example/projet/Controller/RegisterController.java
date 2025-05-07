package com.example.projet.Controller;

import com.example.projet.Model.StudentModel;
import com.example.projet.Dao.UtilisateurDao;
import com.example.projet.MainApp;
import com.example.projet.Model.TeacherModel;
import com.example.projet.Model.UserModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private RadioButton studentRadio;
    @FXML private RadioButton teacherRadio;
    @FXML private ToggleGroup roleGroup;
    @FXML private Label registerMessage;

    @FXML
    public void initialize() {
        // Initialisation du ToggleGroup
        roleGroup = new ToggleGroup();
        studentRadio.setToggleGroup(roleGroup);
        teacherRadio.setToggleGroup(roleGroup);
    }

    @FXML
    public void handleRegister() {
        // Récupération des valeurs
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
        String role = selectedRole != null ? selectedRole.getText().toLowerCase() : null;

        // Validation des champs
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
            registerMessage.setText("Tous les champs doivent être remplis !");
            return;
        }

        // Validation du rôle
        if (!role.equals("etudiant") && !role.equals("enseignant")) {
            registerMessage.setText("Rôle invalide !");
            return;
        }

        // Vérification de l'email
        if (UtilisateurDao.isEmailExist(email)) {
            registerMessage.setText("Erreur : cet email est déjà utilisé !");
            return;
        }

        try {
            // Processus d'inscription
            UserModel newUser;
            if (role.equals("etudiant")) {
                newUser = new StudentModel(0, fullName, email, password, role);
            } else {
                newUser = new TeacherModel(0, fullName, email, password, role, null);
            }

            boolean success = UtilisateurDao.ajouterUtilisateur(newUser);

            if (success) {
                registerMessage.setText("Inscription réussie pour " + fullName + " !");

                Stage currentStage = (Stage) fullNameField.getScene().getWindow();
                currentStage.close();

                if (role.equals("etudiant")) {
                    MainApp.showStudentDashboard();
                } else {
                    MainApp.showTeacherDashboard();
                }
            } else {
                registerMessage.setText("Erreur lors de l'inscription !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            registerMessage.setText("Erreur système lors de l'inscription");
        }
    }


    @FXML
    public void goToLogin() {
        try {
            Stage currentStage = (Stage) registerMessage.getScene().getWindow();
            currentStage.close();
            MainApp.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
            registerMessage.setText("Erreur lors de l'ouverture de la connexion");
        }
    }

}



