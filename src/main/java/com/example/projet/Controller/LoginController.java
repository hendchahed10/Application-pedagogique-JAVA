package com.example.projet.Controller;

import com.example.projet.Dao.UtilisateurDao;
import com.example.projet.Model.StudentModel;
import com.example.projet.Model.UserModel;
import com.example.projet.Model.TeacherModel;
import com.example.projet.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Label messageLabel;

    private UtilisateurDao utilisateurDao;
    private static UserModel currentuser;

    public LoginController() {
        utilisateurDao=new UtilisateurDao();
        currentuser=new UserModel();
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String email = username.getText();
        String motDePasse = password.getText();

        if (email.isEmpty() || motDePasse.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        UserModel user = utilisateurDao.getUtilisateurByEmailAndPassword(email, motDePasse);

        if (user != null) {
            messageLabel.setText("Bienvenue " + user.getNom() + " (" + user.getRole() + ")");
            currentuser = user;

            Stage currentStage = (Stage) username.getScene().getWindow();
            currentStage.close();

            try {
                if (user instanceof TeacherModel) {
                    MainApp.showTeacherDashboard();
                } else if (user instanceof StudentModel) {
                    MainApp.showStudentDashboard();
                } else {
                    messageLabel.setText("Email ou mot de passe incorrect.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                messageLabel.setText("Erreur lors du chargement du tableau de bord.");
            }

        } else {
            messageLabel.setText("Identifiants invalides.");
        }
    }

    @FXML
    private void onRegisterLinkClicked() {
        Stage currentStage = (Stage) username.getScene().getWindow();
        currentStage.close();
        MainApp.showRegisterView();
    }

    public static UserModel getCurrentuser() {
        return currentuser;
    }
}
