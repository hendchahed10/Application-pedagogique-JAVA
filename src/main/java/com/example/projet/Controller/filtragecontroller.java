package com.example.projet.Controller;

import javafx.scene.control.RadioButton;
import com.example.projet.Model.*;
import com.example.projet.Dao.*;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import java.io.*;
import java.util.List;
import java.sql.*;
import java.util.Optional;

import javafx.scene.control.*;


public class filtragecontroller {
    private DashboardStudentController dashboardController; // Reference
    @FXML
    private RadioButton meilleureNotee;

    @FXML
    private RadioButton easyRadioButton;

    @FXML
    private RadioButton mediumRadioButton;

    @FXML
    private RadioButton hardRadioButton;
    private RessourceDao resd;
    @FXML
    private VBox filteredResourcesList;
    @FXML
    private ToggleGroup categorieGroup;
    @FXML private RadioButton radioInformatique;
    @FXML private RadioButton radioScience;
    @FXML private RadioButton radioGestion;
    @FXML private RadioButton radioEconomie;
    @FXML private RadioButton radioLittérature;
    @FXML private RadioButton radioMathématiques;



    @FXML
    private void initialize() {
        this.resd = new RessourceDao();
        radioInformatique.setUserData("Informatique");
        radioScience.setUserData("Science");
        radioGestion.setUserData("Gestion");
        radioEconomie.setUserData("Economie");
        radioLittérature.setUserData("Littérature");
        radioMathématiques.setUserData("Mathématiques");

    }
    public void setDashboardController(DashboardStudentController dashboardController) {
        this.dashboardController = dashboardController;
    }
    @FXML
    private void applyFilters() {
        String difficulty = getSelectedDifficulty();
        boolean sortByPopularity = meilleureNotee.isSelected();
        String category = getSelectedCategory();

        // Handle "Any" selection for category
        if (category == null) category = "";  // Or handle differently

        if (dashboardController != null) {
            dashboardController.filterResources(
                    difficulty != null ? difficulty : "",
                    sortByPopularity,
                    category != null ? category : ""
            );
        }

        // Close the filter window
        ((Stage) easyRadioButton.getScene().getWindow()).close();
    }

    @FXML
    private void handleConsulter(Ressource res) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ressource.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    public String getSelectedDifficulty() {
        if (easyRadioButton.isSelected()) {
            return "Facile";
        } else if (mediumRadioButton.isSelected()) {
            return "Moyen";
        } else if (hardRadioButton.isSelected()) {
            return "Difficile";
        } else {
            return null; // No selection
        }
    }
    @FXML
    private String getSelectedCategory() {
        if (radioInformatique.isSelected()) {
            return "Informatique";
        } else if (radioScience.isSelected()) {
            return "Science";
        } else if (radioGestion.isSelected()) {
            return "Gestion";
        } else if (radioEconomie.isSelected()) {
            return "Economie";
        } else if (radioLittérature.isSelected()) {
            return "Littérature";
        } else if (radioMathématiques.isSelected()) {
            return "Mathématiques";
        } else {
            return null;
        }
    }






}
