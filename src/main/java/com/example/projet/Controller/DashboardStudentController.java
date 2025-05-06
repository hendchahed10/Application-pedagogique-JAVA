package com.example.projet.Controller;

import com.example.projet.Model.*;
import com.example.projet.Dao.*;
import com.example.projet.service.CSVExporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.example.projet.service.RecommendationService;
import com.example.projet.service.CSVExporter;

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
    @FXML
    private VBox recom;
    @FXML
    private ListView<Ressource> recommendationListView;


    @FXML
    private VBox filteredResourcesList;
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
        try {
            this.ressourceDao = new RessourceDao();
            RecommendationService service = new RecommendationService();
            List<RecommendedItem> recommendations = service.getRecommendations(currentStudentId, 5);
            showRecommendations(recommendations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //recommendation system
    private void showRecommendations(List<RecommendedItem> recommendations) {
        List<Ressource> recommendedRessources = new ArrayList<>();

        for (RecommendedItem item : recommendations) {
            try {
                Ressource ressource = ressourceDao.getRessourceById((int) item.getItemID());

                recommendedRessources.add(ressource);
            } catch (Exception e) {
                System.err.println("Error fetching full ressource for itemID " + item.getItemID());
                e.printStackTrace();
            }
        }

        ObservableList<Ressource> observableList = FXCollections.observableArrayList(recommendedRessources);
        recommendationListView.setItems(observableList);
        recommendationListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Ressource ressource, boolean empty) {
                super.updateItem(ressource, empty);
                if (empty || ressource == null) {
                    setGraphic(null);
                } else {
                    HBox resourceBox = new HBox(10);
                    resourceBox.setAlignment(Pos.CENTER_LEFT);
                    resourceBox.setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 15; -fx-padding: 10;");
                    Label resLabel = new Label(ressource.getTitre()
                             );
                    resLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1A237E;");
                    Button detailsBtn = new Button("Consulter");
                    detailsBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand;");
                    detailsBtn.setOnAction(e -> openResourceDetails(ressource));
                    resourceBox.getChildren().addAll(resLabel, detailsBtn);
                    setGraphic(resourceBox);
                }
            }
        });
    }
    //recommendation system end here

    @FXML
    private void handleConsulter(Ressource res) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ressource.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
    /// filtrage start here

    @FXML
    private void handleFilterButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet/filtrage.fxml"));
        Parent root = loader.load();
        filtragecontroller filterController = loader.getController();
        filterController.setDashboardController(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Filtrage");
        stage.show();
    }
    private void displayFilteredResources(List<Ressource> resources) {
        ObservableList<Ressource> observableResources = FXCollections.observableArrayList(resources);
        ressourcesrec.setItems(observableResources);
    }

    public void filterResources(String difficulty, boolean sortByPopularity, String category) {
        List<Ressource> filteredResources = ressourceDao.getFilteredResources(
                difficulty,
                sortByPopularity,
                category
        );

        ressourcesrec.getItems().clear();
        displayFilteredResources(filteredResources);
        showListView(ressourcesrec);
    }
    //filtrage end here







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

        // This cell factory should handle BOTH initial display and filtered results
        ressourcesrec.setCellFactory(lv -> new ListCell<>() {
            private final HBox content = new HBox(10);
            private final Label titleLabel = new Label();
            private final Button consultButton = new Button("Consulter");

            {
                consultButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                consultButton.setOnAction(event -> {
                    Ressource currentRessource = getItem();
                    openResourceDetails(currentRessource);
                });

                content.setAlignment(Pos.CENTER_LEFT);
                content.getChildren().addAll(titleLabel, consultButton);
            }

            @Override
            protected void updateItem(Ressource item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    titleLabel.setText(item.getTitre());
                    setGraphic(content);
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


}