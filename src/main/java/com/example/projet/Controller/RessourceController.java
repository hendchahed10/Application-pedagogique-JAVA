package com.example.projet.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class RessourceController {
    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField difficulteField;

    @FXML
    private TextField enseignantField;

    @FXML
    private TextField documentField;

    @FXML
    private Button telechargerButton;

    @FXML
    private TextArea commentairesArea;

    @FXML
    private Button favorisButton;

    @FXML
    private Button termineesButton;

    @FXML
    private Button star1, star2, star3, star4, star5;

    private int evaluation = 0;

    @FXML
    private void initialize() {
        // Associer le téléchargement de document
        telechargerButton.setOnAction(event -> choisirFichier());

        // Associer les étoiles à l’évaluation
        star1.setOnAction(event -> setEvaluation(1));
        star2.setOnAction(event -> setEvaluation(2));
        star3.setOnAction(event -> setEvaluation(3));
        star4.setOnAction(event -> setEvaluation(4));
        star5.setOnAction(event -> setEvaluation(5));

        // Actions boutons Favoris / Terminées
        favorisButton.setOnAction(event -> ajouterAuxFavoris());
        termineesButton.setOnAction(event -> marquerCommeTerminee());
    }

    private void choisirFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un document");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            documentField.setText(file.getName());
        }
    }

    private void setEvaluation(int note) {
        evaluation = note;
        resetStars();
        if (evaluation >= 1) star1.setStyle("-fx-text-fill: gold;");
        if (evaluation >= 2) star2.setStyle("-fx-text-fill: gold;");
        if (evaluation >= 3) star3.setStyle("-fx-text-fill: gold;");
        if (evaluation >= 4) star4.setStyle("-fx-text-fill: gold;");
        if (evaluation >= 5) star5.setStyle("-fx-text-fill: gold;");
    }

    private void resetStars() {
        star1.setStyle("-fx-text-fill: gray;");
        star2.setStyle("-fx-text-fill: gray;");
        star3.setStyle("-fx-text-fill: gray;");
        star4.setStyle("-fx-text-fill: gray;");
        star5.setStyle("-fx-text-fill: gray;");
    }

    private void ajouterAuxFavoris() {
        System.out.println("Ressource ajoutée aux favoris : " + titreField.getText());
    }

    private void marquerCommeTerminee() {
        System.out.println("Ressource marquée comme terminée : " + titreField.getText());
    }
}
