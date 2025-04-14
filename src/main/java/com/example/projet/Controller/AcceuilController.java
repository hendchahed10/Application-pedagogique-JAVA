package com.example.projet.Controller;

import com.example.projet.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class AcceuilController {

    @FXML
    private Button signInButton;
    @FXML
    private StackPane rootPane;
    @FXML
    private Label welcomeLabel;

    @FXML
    public void goToLogin(ActionEvent event) {
        try {
            MainApp.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


