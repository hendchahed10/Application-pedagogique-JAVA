package com.example.projet.Controller;

import com.example.projet.Model.*;
import com.example.projet.Dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;

public class DashboardTeacherController {
    private RessourceDao ressourceDao;
    private UtilisateurDao utilisateurDao;
    private int currentTeacherId ;
    private final ObservableList<Ressource> ressources = FXCollections.observableArrayList();
    private Ressource selectedRessource = null;

    @FXML private Label nbStudentsLabel;
    @FXML private Label averageRatingLabel;
    @FXML private Label nbFavorisLabel;
    @FXML private Label nbResourcesLabel;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField documentField;
    @FXML private ToggleGroup difficultyGroup;
    @FXML private Button logoutButton;
    @FXML private ListView<Ressource> ressourceListView;
    @FXML private ComboBox<String> categoryComboBox;

    public DashboardTeacherController () {
        ressourceDao = new RessourceDao();
        utilisateurDao= new UtilisateurDao();
    }

    @FXML
    public void initialize() {
        UserModel user = LoginController.getCurrentuser();
        currentTeacherId = user.getId();

        categoryComboBox.getItems().addAll(
                "Informatique", "Science", "Gestion", "Economie", "Littérature", "Mathématiques"
        );

        loadRessources();
        afficherNombreEtudiants();
        afficherNbRessourcesPourCetEnseignant();
        afficherNbFavorisRessources();
        afficherRateRessources();

        ressourceListView.setItems(ressources);
        ressourceListView.setCellFactory(listView -> new ListCell<>() {
            private final HBox content;
            private final Label titleLabel;
            private final Button editButton;
            private final Button deleteButton;

            {
                titleLabel = new Label();
                editButton = new Button("Modifier");
                deleteButton = new Button("Supprimer");

                editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    selectedRessource = getItem();
                    if (selectedRessource != null) {
                        populateFields(selectedRessource);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Ressource ressourceToDelete = getItem();
                    if (ressourceToDelete != null) {
                        ressourceDao.supprimerRessource(ressourceToDelete.getId());
                        ressources.remove(ressourceToDelete);
                    }
                });

                content = new HBox(10, titleLabel, editButton, deleteButton);
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

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void populateFields(Ressource ressource) {
        titleField.setText(ressource.getTitre());
        descriptionArea.setText(ressource.getDescription());
        documentField.setText(ressource.getDocument());
        categoryComboBox.setValue(ressource.getCategorie());

        for (Toggle toggle : difficultyGroup.getToggles()) {
            RadioButton radio = (RadioButton) toggle;
            if (radio.getText().equalsIgnoreCase(ressource.getDifficulte())) {
                difficultyGroup.selectToggle(radio);
                break;
            }
        }
    }


    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projet/acceuil.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la vue d'accueil.");
        }
    }

    @FXML
    private void handleDownload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Télécharger le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            documentField.setText(selectedFile.getAbsolutePath());
            System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String document = documentField.getText().trim();
        RadioButton selectedRadio = (RadioButton) difficultyGroup.getSelectedToggle();
        String categorie = categoryComboBox.getValue();
        String difficulte = selectedRadio != null ? selectedRadio.getText() : "";

        if (title.isEmpty() || difficulte.isEmpty() || categorie == null || categorie.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs obligatoires manquants", "Veuillez remplir tous les champs nécessaires.");
            return;
        }

        if (currentTeacherId == -1) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non connecté", "Veuillez vous reconnecter.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de publication");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Vous êtes sûr de vouloir publier cette ressource ?");

        ButtonType buttonOui = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(buttonOui, buttonNon);

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == buttonOui) {
                if (selectedRessource == null) {
                    Ressource newRessource = new Ressource(0, title, categorie, description, difficulte, document, currentTeacherId);
                    ressourceDao.ajouterRessource(newRessource);
                } else {
                    selectedRessource.setTitre(title);
                    selectedRessource.setDescription(description);
                    selectedRessource.setDifficulte(difficulte);
                    selectedRessource.setDocument(document);
                    selectedRessource.setCategorie(categorie);
                    ressourceDao.mettreAJourRessource(selectedRessource);
                    selectedRessource = null;
                }

                loadRessources();
                clearForm();
            }
        });
    }




    private void loadRessources() {
        ressources.clear();
        ressources.addAll(ressourceDao.getRessourcesByEnseignantId( currentTeacherId));
    }

    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        documentField.clear();
        difficultyGroup.selectToggle(null);
        categoryComboBox.getSelectionModel().clearSelection();
        selectedRessource = null;
    }

    public void afficherNombreEtudiants() {
        int total = ressourceDao.getNombreTotalEtudiants();
        nbStudentsLabel.setText(String.valueOf(total));
    }

    public void afficherNbRessourcesPourCetEnseignant() {
        int count = ressourceDao.getNombreRessourcesParEnseignant(currentTeacherId);
        nbResourcesLabel.setText(String.valueOf(count));
    }

    public void afficherNbFavorisRessources() {
        int count =ressourceDao.getNombreRessourcesFavorisParEnseignant(currentTeacherId);
        nbFavorisLabel.setText(String.valueOf(count));
    }

    public void afficherRateRessources() {
        double count =ressourceDao.getNoteMoyenRessourcesParEnseignant(currentTeacherId);
        averageRatingLabel.setText(String.valueOf(count));
    }



}
