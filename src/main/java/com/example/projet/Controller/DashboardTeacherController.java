package com.example.projet.Controller;

import com.example.projet.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardTeacherController {

    @FXML
    public void onAddRessourceClicked(ActionEvent event) {
        try {
            MainApp.showAddResourceView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

