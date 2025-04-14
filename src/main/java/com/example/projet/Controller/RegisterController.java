package com.example.projet.Controller;

import com.example.projet.MainApp;
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
        // Débogage : Vérification que la méthode est appelée
        System.out.println("Méthode handleRegister appelée");

        // Récupération des valeurs des champs
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Récupération du rôle sélectionné
        RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
        String role = selectedRole != null ? selectedRole.getText() : "Aucun rôle sélectionné";

        // Validation des champs
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            registerMessage.setText("Tous les champs doivent être remplis !");
            System.out.println("Erreur : Champs vides");
        } else {
            // Logique pour enregistrer l'utilisateur
            registerMessage.setText("Inscription réussie pour " + fullName + " en tant que " + role + " !");
            System.out.println("Nom complet : " + fullName);
            System.out.println("Email : " + email);
            System.out.println("Mot de passe : " + password);
            System.out.println("Rôle : " + role);
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


