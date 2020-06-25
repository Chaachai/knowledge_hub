/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author Youssef
 */
public class Etudiant {

    private int id;
    private String cne;
    private String nom;
    private String prenom;
    private String adresse;
    private int nb_emprunts;
    private int nb_retards;
    private int bloque;
    private Cursus cursus;
    private Universite universite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getNb_emprunts() {
        return nb_emprunts;
    }

    public void setNb_emprunts(int nb_emprunts) {
        this.nb_emprunts = nb_emprunts;
    }

    public int getNb_retards() {
        return nb_retards;
    }

    public void setNb_retards(int nb_retards) {
        this.nb_retards = nb_retards;
    }

    public int getBloque() {
        return bloque;
    }

    public void setBloque(int bloque) {
        this.bloque = bloque;
    }

    public Cursus getCursus() {
        return cursus;
    }

    public void setCursus(Cursus cursus) {
        this.cursus = cursus;
    }

    public Universite getUniversite() {
        return universite;
    }

    public void setUniversite(Universite universite) {
        this.universite = universite;
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }

}
