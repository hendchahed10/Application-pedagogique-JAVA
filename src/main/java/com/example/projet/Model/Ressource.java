package com.example.projet.Model;

public class Ressource {
    private int id;
    private String titre;
    private String categorie;
    private String description;
    private String difficulte;
    private String urlpdf;
    private int id_enseignant;


    public Ressource(int id , String titre, String categorie, String description, String difficulte, String urlpdf, int id_enseignant) {
        this.id = id;
        this.titre = titre;
        this.categorie = categorie;
        this.description = description;
        this.difficulte = difficulte;
        this.urlpdf = urlpdf;
        this.id_enseignant = id_enseignant;
    }

    public int getId() {
        return id;
    }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getCategorie() {return categorie;}
    public void setCategorie(String categorie) {this.categorie = categorie;}

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulte() { return difficulte; }
    public void setDifficulte(String difficulte) { this.difficulte = difficulte; }

    public String getDocument() { return urlpdf; }
    public void setDocument(String urlpdf) { this.urlpdf = urlpdf; }

    @Override
    public String toString() {
        return titre;
    }

    public String getUrlpdf() {
        return urlpdf;
    }

    public void setUrlpdf(String urlpdf) {
        this.urlpdf = urlpdf;
    }

    public int getId_enseignant() {
        return id_enseignant;
    }

    public void setId_enseignant(int id_enseignant) {
        this.id_enseignant = id_enseignant;
    }
}
