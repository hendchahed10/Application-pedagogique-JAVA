package com.example.projet.Controller;

import com.example.projet.Model.*;
import com.example.projet.Dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.*;
import java.util.List;
import java.sql.*;

public class DashboardStudentController {
    private RessourceDao ressourceDao;
    private RecommendationEngine recommendation;
    private RessourceController ressourceController;
    private UtilisateurDao utilisateurDao;
    private int currentStudentId;

    @FXML
    private Label statistiquesLabel;
    @FXML
    private Label completedLabel;
    @FXML
    private Label savedLabel;
    @FXML
    private Label subtitleLabel;
    @FXML
    private Button filterButton;
    @FXML
    private ListView<Ressource> ressourcesrec;
    @FXML
    private ListView<Ressource> termineesList;
    @FXML
    private ListView<Ressource> favorisList;
    private ObservableList<Ressource> ressources = FXCollections.observableArrayList();

    public DashboardStudentController() {
        ressourceDao = new RessourceDao();
        utilisateurDao = new UtilisateurDao();
    }

    @FXML
    public void initialize() throws SQLException {
        setupMainListView();

        UserModel user = LoginController.getCurrentuser();
        currentStudentId = user.getId();

        updateStatistics();

    }

    @FXML
    private void updateStatistics() throws SQLException {
        int nb_terminées = ressourceDao.NbRessourcesTerminees(currentStudentId);
        int nb_favorites = ressourceDao.NbRessourcesFavories(currentStudentId);
        int total_ressources = ressourceDao.getTotalRessources();
        double statprog = total_ressources != 0 ?
                Math.floor(((double) nb_terminées / total_ressources) * 100) : 0;

        statistiquesLabel.setText(statprog + "%");
        completedLabel.setText(String.valueOf(nb_terminées));
        savedLabel.setText(String.valueOf(nb_favorites));
    }

    private void setupMainListView() throws SQLException {
        ressources.addAll(ressourceDao.getToutesLesRessources());
        ressourcesrec.setItems(ressources);
        ressourcesrec.setStyle("-fx-background-color: transparent; -fx-control-inner-background: #d4f3ff;");
        ressourcesrec.setCellFactory(lv -> new ListCell<>() {
            private final HBox content;
            private final Label titleLabel;
            private final Button consultButton;

            {
                titleLabel = new Label();
                consultButton = new Button("Consulter");
                consultButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                consultButton.setOnAction(event -> {
                    Ressource currentRessource = getItem(); //lors du clic sur le bouton "Consulter", récupération de la ressource en question
                    openResourceDetails(currentRessource);
                });

                content = new HBox(10, titleLabel, consultButton);
                content.setAlignment(Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(Ressource item, boolean empty) { //fonction qui gère l'affichage de chacune des ressources de la listView
                super.updateItem(item, empty); //re-fixe l'ancien état visuel de la cellule avant de l'utiliser pour de nouvelles doonées
                if (empty || item == null) {
                    setGraphic(null); //efface l'ancien contenu si la ressource est vide
                } else {
                    titleLabel.setText(item.getTitre()); //montre le titre de la ressource
                    setGraphic(content); //affiche le titre de la ressource et le bouton "Consulter" si elle n'est pas vide
                }
            }
        });
    }

    private void configurateCellFactory(ListView<Ressource> listView) {
        listView.setStyle("-fx-background-color: transparent; -fx-control-inner-background: #d4f3ff;");
        listView.setCellFactory(lv -> new ListCell<>() {
            private final HBox content;
            private final Label titleLabel;

            {
                titleLabel = new Label();

                content = new HBox(10, titleLabel);
                content.setAlignment(Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(Ressource item, boolean empty) { //fonction qui gère l'affichage de chacune des ressources de la listView
                super.updateItem(item, empty); //re-fixe l'ancien état visuel de la cellule avant de l'utiliser pour de nouvelles doonées
                if (empty || item == null) {
                    setGraphic(null); //efface l'ancien contenu si la ressource est vide
                } else {
                    titleLabel.setText(item.getTitre()); //montre le titre de la ressource
                    setGraphic(content); //affiche le titre de la ressource et le bouton "Consulter" si elle n'est pas vide
                }
            }
        });
    }

    private void openResourceDetails(Ressource resource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet/ressource.fxml"));
            Parent root = loader.load();
            RessourceController controller = loader.getController(); //identifie le cont^rleur associé à la vue ressource

            controller.setId(resource.getId());
            controller.setTitre(resource.getTitre());
            controller.setCategorie(resource.getCategorie());
            controller.setDifficulté(resource.getDifficulte());
            controller.setDescription(resource.getDescription());
            controller.setDocument(resource.getDocument());
            controller.setEnseignant(utilisateurDao.getNameById(resource.getId_enseignant()));
            System.out.println(resource.getId_enseignant());
            controller.initialize(); //essentiel pour que controller met à jour l'interface en y insérant les données qu'il a récupérées

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboard(ActionEvent event) throws SQLException {
        showListView(ressourcesrec);
    }
    @FXML
    private void handleTerminees(ActionEvent event) {
        try {
            ObservableList<Ressource> terminees = FXCollections.observableArrayList(
                    ressourceDao.getResourcesByStatus(currentStudentId, "Terminee")
            );
            termineesList.setItems(terminees);
            configurateCellFactory(termineesList);
            showListView(termineesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFavoris(ActionEvent event) {
        try {
            ObservableList<Ressource> favoris = FXCollections.observableArrayList(
                    ressourceDao.getResourcesByStatus(currentStudentId, "Favoris")
            );
            favorisList.setItems(favoris);
            configurateCellFactory(favorisList);
            showListView(favorisList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showListView(ListView<Ressource> listView) {
        // Cacher initialement les 3 listes
        ressourcesrec.setVisible(false);
        ressourcesrec.setManaged(false);
        termineesList.setVisible(false);
        termineesList.setManaged(false);
        favorisList.setVisible(false);
        favorisList.setManaged(false);

        // Changer le contenu affiché selon la liste
        if (listView == ressourcesrec) {
            subtitleLabel.setText("Ressources recommandées");
            filterButton.setVisible(true);  // Show filter button
        }
        else if (listView == termineesList) {
            subtitleLabel.setText("Ressources terminées");
            filterButton.setVisible(false); // Hide filter button
        }
        else if (listView == favorisList) {
            subtitleLabel.setText("Ressources favorites");
            filterButton.setVisible(false); // Hide filter button
        }

        // Montrer la liste passée en paramètres
        listView.setVisible(true);
        listView.setManaged(true);
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet/acceuil.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void handleFilterButton(ActionEvent actionEvent) {
        // À implémenter
    }
}