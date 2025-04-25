package com.example.projet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Connexion réussie");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}