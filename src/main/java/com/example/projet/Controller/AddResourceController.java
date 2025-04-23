package com.example.projet.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;

public class AddResourceController{
    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private RadioButton facileRadio, moyenneRadio, difficileRadio;
    @FXML
    private Button uploadButton, publierButton;
    private File fichierSelectionne;

    @FXML
    public void initialize() {
        uploadButton.setOnAction(event -> choisirFichier());
        publierButton.setOnAction(event -> publierRessource());
    }

    private void choisirFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un document");
        fichierSelectionne = fileChooser.showOpenDialog(null);

        if (fichierSelectionne != null) {
            uploadButton.setText(fichierSelectionne.getName());
        }
    }

    private void publierRessource() {
        String titre = titreField.getText();
        String description = descriptionArea.getText();
        String difficulte = "";

        if (facileRadio.isSelected()) {
            difficulte = "Facile";
        } else if (moyenneRadio.isSelected()) {
            difficulte = "Moyenne";
        } else if (difficileRadio.isSelected()) {
            difficulte = "Difficile";
        }

        if (titre.isEmpty() || description.isEmpty() || difficulte.isEmpty() || fichierSelectionne == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs et choisir un fichier !");
        } else {
            // Ici tu peux ajouter ton code pour enregistrer la ressource
            System.out.println("Titre : " + titre);
            System.out.println("Description : " + description);
            System.out.println("Difficulté : " + difficulte);
            System.out.println("Fichier : " + fichierSelectionne.getAbsolutePath());

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource publiée avec succès !");
        }
    }

    private void showAlert(Alert.AlertType alertType, String titre, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
