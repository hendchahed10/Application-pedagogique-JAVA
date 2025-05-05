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
import javafx.stage.Stage;
import java.io.*;
import java.sql.*;

public class DashboardStudentController {
    private RessourceDao ressourceDao;
    private RessourceController ressourceController;
    private UtilisateurDao utilisateurDao;
    private int currentStudentId;
    private ListView<Ressource> terminées;
    private ListView<Ressource> Favoris;
    private ObservableList<Ressource> ressources = FXCollections.observableArrayList();


    @FXML private Label statistiquesLabel;
    @FXML private Label completedLabel;
    @FXML private Label savedLabel;
    @FXML private ToggleGroup difficulteGroup;
    @FXML private ToggleGroup nouveauteGroup;
    @FXML private ToggleGroup categorieGroup;
    @FXML private RadioButton meilleureNotee;
    @FXML private Button dashboardBtn, termineesBtn, favorisBtn, logoutBtn;
    @FXML private ChoiceBox<String> filterChoiceBox;
    @FXML private ListView<Ressource> ressourcesrec;

    public DashboardStudentController() {
        ressourceDao = new RessourceDao();
        ressourceController = new RessourceController();
        utilisateurDao= new UtilisateurDao();
    }

    @FXML
    public void initialize() throws SQLException {
        System.out.println("Composants FXML injectés dans DashboardStudentController:");
        System.out.println("ressourcesrec: " + ressourcesrec);
        System.out.println("statistiquesLabel: " + statistiquesLabel);

            UserModel user = LoginController.getCurrentuser();
            currentStudentId = user.getId();

            int nbFavorites = ressourceDao.NbRessourcesTerminees(currentStudentId);
            int nbTerminees = ressourceDao.NbRessourcesFavories(currentStudentId);
            double progression = nbFavorites != 0 ? ((double) nbTerminees / nbFavorites) * 100 : 0;
            statistiquesLabel.setText(String.format("%.1f%%", progression));
            completedLabel.setText(String.valueOf(nbTerminees));
            savedLabel.setText(String.valueOf(nbFavorites));

        ressources.addAll(ressourceDao.getAllResourcesByTeacherId(3));
        ressourcesrec.setItems(ressources);

        ressourcesrec.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        ressourcesrec.setCellFactory(listView -> new ListCell<>() {
            private final HBox content;
            private final Label titleLabel;
            private final Button consultButton;
            private final Button termineesBtn=new Button("Terminées");
            private final Button favButton = new Button("Favoris");

            {
                titleLabel = new Label();
                consultButton = new Button("Consulter");

                consultButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                consultButton.setOnAction(event -> {
                            Ressource currentRessource = getItem(); // ← on récupère la ressource courante
                            int id = currentRessource.getId(); //id de la ressource courante
                            //String catégorie= currentRessource.
                            String titre = currentRessource.getTitre(); //titre de la ressource courante
                            String desc = currentRessource.getDescription(); //description de la ressource courante
                            String difficulté = currentRessource.getDifficulte(); //diffuculté de la ressource courante
                            String url = currentRessource.getDocument(); //url de la ressource courante
                            String enseignant;
                            try {
                                enseignant = utilisateurDao.getNameById(currentRessource.getId_enseignant()); //nom de l'enseignant de la ressource courante
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet/ressource.fxml"));
                            Parent root;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                    RessourceController ressourceController = loader.getController();
                    // Transmission du titre de la ressource en question au champ titre de l'interface ressource
                    ressourceController.setTitre(titre);
                    ressourceController.setDifficulté(difficulté);
                    ressourceController.setDescription(desc);
                    ressourceController.setDocument(url);
                    ressourceController.setEnseignant(enseignant);
                    ressourceController.initialize();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                    ressourceController.handleFavoris(event);
                    ressourceController.handleTermine(event);


                });


                content = new HBox(10, titleLabel, consultButton);
                content.setAlignment(Pos.CENTER_LEFT);
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

    @FXML
    private void handleFilterButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet/filtrage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Filtrage");
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de filtrage", e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }



    @FXML
    private void handleTerminees(ActionEvent event)
    {
        try {
            ObservableList<Ressource> terminees = FXCollections.observableArrayList(
                    ressourceDao.getResourcesByStatus(currentStudentId,"Terminee")
            );
            terminées.setItems(terminees);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleFavoris(ActionEvent event)
    {
        try {
            ObservableList<Ressource> favoris = FXCollections.observableArrayList(
                    ressourceDao.getResourcesByStatus(currentStudentId,"Favoris")
            );
            Favoris.setItems(favoris);
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet/acceuil.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void appliquerFiltres(ActionEvent actionEvent) {
    }
}
