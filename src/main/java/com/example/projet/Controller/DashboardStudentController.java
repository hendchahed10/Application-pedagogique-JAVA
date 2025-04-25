package com.example.projet.Controller;
import com.example.projet.Model.*;
import com.example.projet.Dao.*;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import java.io.*;
import java.util.List;
import java.sql.*;
import javafx.scene.control.*;

public class DashboardStudentController {
    private ProgressionDao prog;
    private RecommendationEngine recommendation;

    @FXML
    private Label consultedLabel;
    @FXML
    private Label completedLabel;
    @FXML
    private Label savedLabel;
    @FXML
    private Button dashboardBtn, termineesBtn, favorisBtn, logoutBtn;
    @FXML
    private ChoiceBox<String> filterChoiceBox;

    public double ObtenirStatistiquesProgression(int idEtudiant) throws SQLException {
        int nbEnregistrées=prog.NbRessourcesEnregistrees(idEtudiant);
        int nbTerminées=prog.NbRessourcesTerminees(idEtudiant);
        return (nbTerminées/nbEnregistrées)*100;
    }
    /*public double NbRessourcesConsultées(int idEtudiant)
    {
        return prog.NbRessourcesConsultées(idEtudiant);
    }
    public double NbRessourcesEnregistrées(int idEtudiant)  {
        return prog.NbRessourcesEnregistrees(idEtudiant);
    }
    public double NbRessourcesTerminées(int idEtudiant)  {
        return prog.NbRessourcesTerminees(idEtudiant);
    }*/

    public void EnregistrerRessource (int idEtudiant,int idRessource)
    {
        prog.EnregistrerRessource(idEtudiant, idRessource);
    }
    public void MarquerCommeTerminéeRessource(int idEtudiant,int idRessource)
    {
        prog.TerminerRessource(idEtudiant, idRessource);
    }

    @FXML
    private void initialize() {
        filterChoiceBox.setValue("Filtres");
    }

    @FXML
    private void handleConsulter(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ressource.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    private void handleDashboard(ActionEvent event) {

    }

    @FXML
    private void handleTerminees(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listeTerminees.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    private void handleFavoris(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listeFavoris.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet/acceuil.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
