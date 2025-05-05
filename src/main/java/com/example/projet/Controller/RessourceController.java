package com.example.projet.Controller;
import com.example.projet.Dao.RessourceDao;
import com.example.projet.Model.Ressource;
import com.example.projet.Model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import java.io.*;
import java.sql.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class RessourceController {
    private RessourceDao ressourceDAO;
    private int currentStudentId;
    UserModel user = LoginController.getCurrentuser();

    @FXML
    private TextField titreField;
    @FXML
    private TextField categorieField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField difficulteField;
    @FXML
    private TextField documentField;
    @FXML
    private TextField enseignantField;
    @FXML
    private Spinner<Double> noteSpinner;
    public RessourceController()
    {
        ressourceDAO=new RessourceDao();
    }
      public void AjouterRessource(Ressource r) throws SQLException {
        ressourceDAO.ajouterRessource(r);
    }
    public void SupprimerRessource(Ressource r) throws SQLException {
        ressourceDAO.supprimerRessource(r.getId());
    }
    /*public void ModifierRessource(Ressource r) {
        ressourceDAO.modifierRessource(r);
}*/
    @FXML
    public void initialize() {
        UserModel user = LoginController.getCurrentuser();
        currentStudentId = user.getId();
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 5.0, 0.0, 0.5);
        noteSpinner.setValueFactory(valueFactory);
    }

    @FXML
    public void handleFavoris(ActionEvent actionEvent) {
        try {
            // 1. Récupération de toutes les données
            String titre = titreField.getText().trim();
            String categorie = categorieField.getText().trim();
            String description = descriptionField.getText().trim();
            String difficulte = difficulteField.getText().trim();
            String enseignant = enseignantField.getText().trim();
            Double note = noteSpinner.getValue();
            int id_res= ressourceDAO.getIdByTitle(titre);
            ressourceDAO.addFavorite(currentStudentId,id_res);
            // Fermer la fenêtre
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }catch (SQLException e) {
             e.getMessage();
            e.printStackTrace();
        }
    }


    public void handleTermine(ActionEvent actionEvent) {
        try {
            // 1. Récupération de toutes les données
            String titre = titreField.getText().trim();
            String categorie = categorieField.getText().trim();
            String description = descriptionField.getText().trim();
            String difficulte = difficulteField.getText().trim();
            String enseignant = enseignantField.getText().trim();
            Double note = noteSpinner.getValue();
            int id_res= ressourceDAO.getIdByTitle(titre);
            ressourceDAO.addCompleted(currentStudentId,id_res);
        }catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
    public void setTitre(String titre) {
        titreField.setText(titre);
    }
    public void setDescription(String desc) {
        descriptionField.setText(desc);
    }
    public void setDifficulté(String diff) {
        difficulteField.setText(diff);
    }
    public void setDocument(String doc) {
        documentField.setText(doc);
    }
    public void setEnseignant(String enseignant) {
        enseignantField.setText(enseignant);
    }
}
