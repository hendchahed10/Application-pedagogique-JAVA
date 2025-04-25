package com.example.projet.Controller;

import com.example.projet.Model.StudentModel;
import com.example.projet.Dao.UtilisateurDao;
import com.example.projet.MainApp;
import com.example.projet.Model.TeacherModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();

        // Vérifie le texte du RadioButton sélectionné
        String role = selectedRole != null ? selectedRole.getText().toLowerCase() : null;
        System.out.println("Rôle sélectionné : " + role);  // Ajout de l'impression pour debugger

        if (role == null || !(role.equals("etudiant") || role.equals("enseignant"))) {
            registerMessage.setText("Rôle invalide !");
            return;
        }

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || role == null ) {
            registerMessage.setText("Tous les champs doivent être remplis !");
            return;
        }

        // Vérification de l'email avant de procéder à l'inscription
        if (UtilisateurDao.isEmailExist(email)) {
            registerMessage.setText("Erreur : cet email est déjà utilisé !");
            return;
        }

        if (role.equals("etudiant")) {
            // Créer un objet StudentModel avec les valeurs
            StudentModel nouvelEtudiant = new StudentModel(0, fullName, email, password, role);
            boolean success = UtilisateurDao.ajouterUtilisateur(nouvelEtudiant);

            if (success) {
                registerMessage.setText("Inscription réussie pour " + fullName + " !");
            } else {
                registerMessage.setText("Erreur lors de l'inscription !");
            }
        } else {
            // Créer un objet TeacherModel avec les valeurs (id = 0 et modules = null)
            TeacherModel nouvelEnseignant = new TeacherModel(0, fullName, email, password, role, null);
            boolean success = UtilisateurDao.ajouterUtilisateur(nouvelEnseignant);

            if (success) {
                registerMessage.setText("Inscription réussie pour " + fullName + " !");
            } else {
                registerMessage.setText("Erreur lors de l'inscription !");
            }
        }
    }


    @FXML
    public void goToLogin() {
        try {
            MainApp.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



