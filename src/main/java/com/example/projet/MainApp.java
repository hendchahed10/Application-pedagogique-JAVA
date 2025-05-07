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
            Scene scene = new Scene(fxmlLoader.load(), 1100, 700);
            primaryStage.setTitle("Accueil");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/example/projet/login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1100, 700));
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur chargement login.fxml: " + e.getMessage());
        }
    }

    public static void showRegisterView() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/example/projet/register.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1100, 700));
            stage.setTitle("Inscription");
            stage.show();

        } catch (IOException e) {
            System.err.println("Erreur chargement register.fxml: " + e.getMessage());
        }
    }


    public static void showTeacherDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/example/projet/dashboardTeacher.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1100, 700));
            stage.setTitle("Tableau de bord Enseignant");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur chargement dashboard enseignant: " + e.getMessage());
        }
    }

    public static void showStudentDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/example/projet/dashboardStudent.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1100, 700));
            stage.setTitle("Tableau de bord Étudiant");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur chargement dashboard étudiant: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
