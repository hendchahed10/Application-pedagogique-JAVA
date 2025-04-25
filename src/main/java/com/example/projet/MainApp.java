package com.example.projet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApp extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projet/acceuil.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 700);
            primaryStage.setTitle("Accueil");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showLoginView() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/projet/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        applyStylesheet(scene);
        primaryStage.setTitle("Connexion");
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showDstudent() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/projet/dashboardStudent.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        applyStylesheet(scene);
        primaryStage.setTitle("Connexion");
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showRegisterView() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/projet/register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        applyStylesheet(scene);
        primaryStage.setTitle("Inscription");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showAddResourceView() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/projet/addResource.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        applyStylesheet(scene);
        primaryStage.setTitle("Ajout Ressource");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showView(String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/views/" + fxmlFile));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }



    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void showDashboardView() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/projet/dashboardTeacher.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        applyStylesheet(scene);
        primaryStage.setTitle("Tableau de bord");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void applyStylesheet(Scene scene) {
        var css = MainApp.class.getResource("/com/example/projet/style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
