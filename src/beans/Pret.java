/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.sql.Date;

/**
 *
 * @author Youssef
 */
public class Pret {

    private int id;
    private Etudiant etudiant;
    private Ouvrage ouvrage;
    private Date date_emprunt;
    private Date date_retour_prevue;
    private Date date_retour_effective;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Ouvrage getOuvrage() {
        return ouvrage;
    }

    public void setOuvrage(Ouvrage ouvrage) {
        this.ouvrage = ouvrage;
    }

    public Date getDate_emprunt() {
        return date_emprunt;
    }

    public void setDate_emprunt(Date date_emprunt) {
        this.date_emprunt = date_emprunt;
    }

    public Date getDate_retour_prevue() {
        return date_retour_prevue;
    }

    public void setDate_retour_prevue(Date date_retour_prevue) {
        this.date_retour_prevue = date_retour_prevue;
    }

    public Date getDate_retour_effective() {
        return date_retour_effective;
    }

    public void setDate_retour_effective(Date date_retour_effective) {
        this.date_retour_effective = date_retour_effective;
    }

    @Override
    public String toString() {
        return "Pret{" + "id=" + id + ", date_emprunt=" + date_emprunt + ", date_retour_prevue=" + date_retour_prevue + ", date_retour_effective=" + date_retour_effective + '}';
    }

}
