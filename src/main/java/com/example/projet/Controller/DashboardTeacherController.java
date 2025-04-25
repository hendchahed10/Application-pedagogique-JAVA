package com.example.projet.Controller;

import com.example.projet.Model.Ressource;
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

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField documentField;
    @FXML private RadioButton facileRadio;
    @FXML private RadioButton moyenneRadio;
    @FXML private RadioButton difficileRadio;
    @FXML private ToggleGroup difficultyGroup;
    @FXML private Button logoutButton;
    @FXML private Button AjoutButton;
    @FXML private ListView<Ressource> ressourceListView;

    private ObservableList<Ressource> ressources = FXCollections.observableArrayList();
    private Ressource selectedRessource = null;

    @FXML
    public void initialize() {
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
                        titleField.setText(selectedRessource.getTitre());
                        descriptionArea.setText(selectedRessource.getDescription());
                        documentField.setText(selectedRessource.getDocument());
                        switch (selectedRessource.getDifficulte()) {
                            case "Facile" -> difficultyGroup.selectToggle(facileRadio);
                            case "Moyenne" -> difficultyGroup.selectToggle(moyenneRadio);
                            case "Difficile" -> difficultyGroup.selectToggle(difficileRadio);
                        }
                    }
                });

                deleteButton.setOnAction(event -> {
                    ressources.remove(getItem());
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
                    System.out.println(item.getTitre());
                    setGraphic(content);
                }
            }

        });
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
            System.err.println("Erreur lors du chargement de la vue d'accueil");
        }
    }

    @FXML
    private void handleDownload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Télécharger le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            documentField.setText(selectedFile.getAbsolutePath());
            System.out.println("Fichier sélectionné: " + selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String document = documentField.getText().trim();

        RadioButton selectedRadio = (RadioButton) difficultyGroup.getSelectedToggle();
        String difficulte = selectedRadio != null ? selectedRadio.getText() : "";

        if (title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs vides");
            alert.setContentText("Veuillez entrer le titre.");
            alert.showAndWait();
            return;
        }

        if (selectedRessource != null) {
            selectedRessource.setTitre(title);
            selectedRessource.setDescription(description);
            selectedRessource.setDocument(document);
            selectedRessource.setDifficulte(difficulte);
            ressourceListView.refresh();
            selectedRessource = null;
        } else {
            Ressource newRessource = new Ressource(title, description, difficulte, document);
            ressources.add(newRessource);
        }

        titleField.clear();
        descriptionArea.clear();
        documentField.clear();
        difficultyGroup.selectToggle(null);
    }
}
