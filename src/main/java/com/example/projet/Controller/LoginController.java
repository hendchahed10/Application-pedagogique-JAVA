package com.example.projet.Controller;

import com.example.projet.MainApp;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Hyperlink registerLink;

    @FXML
    public void handleLogin() {
        String user = username.getText();
        if (user.isEmpty()) {
            messageLabel.setText("Veuillez entrer votre nom d'utilisateur !");
        } else {
            messageLabel.setText("Bienvenue " + user + " !");
        }
    }

    @FXML
    public void onRegisterLinkClicked(ActionEvent event) {
        try {
            MainApp.showRegisterView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

