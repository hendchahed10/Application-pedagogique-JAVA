package com.example.projet.Model;

public class ResourceInteraction {
    public class InteractionRessource {
        private int id;
        private String dateConsultation;
        private String dateEnregistrement;
        private String dateCompletion;
        private String statut;
        private int idEtudiant;
        private int idRessource;

        public InteractionRessource(int id, String dateConsultation, String dateEnregistrement, String dateCompletion, String statut, int idEtudiant, int idRessource) {
            this.id = id;
            this.dateConsultation = dateConsultation;
            this.dateEnregistrement = dateEnregistrement;
            this.dateCompletion = dateCompletion;
            this.statut = statut;
            this.idEtudiant = idEtudiant;
            this.idRessource = idRessource;
        }
    }
}
