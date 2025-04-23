package com.example.projet.Model;

public class AvisModel {
    private int id;
    private StudentModel etudiant;
    private ResourceModel ressource;
    private double note;
    private String commentaire;

    public AvisModel (int id, StudentModel etudiant, ResourceModel ressource, double note, String commentaire) {
        this.id = id;
        this.etudiant = etudiant;
        this.ressource = ressource;
        this.note = note;
        this.commentaire = commentaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StudentModel getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(StudentModel etudiant) {
        this.etudiant = etudiant;
    }

    public ResourceModel getRessource() {
        return ressource;
    }

    public void setRessource(ResourceModel ressource) {
        this.ressource = ressource;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
