package com.example.projet.Model;

public class TeacherModel extends UserModel {
    private String modules;

    public TeacherModel(int id, String nom, String email, String motDePasse, String role, String modules) {
        super(id, nom, email, motDePasse, role);
        this.modules = modules;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }
}

