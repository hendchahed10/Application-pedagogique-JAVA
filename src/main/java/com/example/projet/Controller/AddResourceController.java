package com.example.projet.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class AddResourceController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private RadioButton facileRadio;

    @FXML
    private RadioButton moyenneRadio;

    @FXML
    private RadioButton difficileRadio;

    @FXML
    private Label fileLabel;

    private File selectedFile;

    @FXML
    private void handleFileUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        selectedFile = fileChooser.showOpenDialog(getStage());
        if (selectedFile != null) {
            fileLabel.setText(selectedFile.getName());
        } else {
            fileLabel.setText("Aucun fichier sélectionné");
        }
    }

    @FXML
    private void handlePublier(ActionEvent event) {
        String titre = titreField.getText().trim();
        String description = descriptionField.getText().trim();
        String niveau = "";

        if (facileRadio.isSelected()) {
            niveau = "Facile";
        } else if (moyenneRadio.isSelected()) {
            niveau = "Moyenne";
        } else if (difficileRadio.isSelected()) {
            niveau = "Difficile";
        }

        if (titre.isEmpty() || description.isEmpty() || niveau.isEmpty() || selectedFile == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        System.out.println("Titre: " + titre);
        System.out.println("Description: " + description);
        System.out.println("Niveau: " + niveau);
        System.out.println("Fichier: " + selectedFile.getAbsolutePath());

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource ajoutée avec succès !");
    }

    private Stage getStage() {
        return (Stage) titreField.getScene().getWindow();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



