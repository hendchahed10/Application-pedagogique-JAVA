package com.example.projet.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AcceuilController {

    @FXML
    private Button signInButton;

    @FXML
    private void goToLogin() {
        try {
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet/login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot, 1100, 700);
            currentStage.setScene(loginScene);
            currentStage.setTitle("Connexion");
            currentStage.centerOnScreen();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ouverture de la vue login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}