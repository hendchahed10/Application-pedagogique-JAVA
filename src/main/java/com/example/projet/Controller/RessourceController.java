package com.example.projet.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class RessourceController {
    @FXML
    private Spinner<Double> noteSpinner;

    @FXML
    public void initialize() {
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 5.0, 0.0, 0.5);
        noteSpinner.setValueFactory(valueFactory);
    }

    @FXML
    private void handleFavoris() {
        // Récupérer la valeur actuelle du Spinner
        Double note = noteSpinner.getValue();
        System.out.println("Note sélectionnée : " + note);
    }
    public void AddResource(ActionEvent actionEvent) {
    }

    public void handleFavoris(ActionEvent actionEvent) {
    }

    public void handleTermine(ActionEvent actionEvent) {
    }
}
