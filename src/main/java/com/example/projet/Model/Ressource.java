package com.example.projet.Model;

public class Ressource {
    private String titre;
    private String description;
    private String difficulte;
    private String document;

    public Ressource(String titre, String description, String difficulte, String document) {
        this.titre = titre;
        this.description = description;
        this.difficulte = difficulte;
        this.document = document;
    }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulte() { return difficulte; }
    public void setDifficulte(String difficulte) { this.difficulte = difficulte; }

    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }

    @Override
    public String toString() {
        return titre;
    }
}
