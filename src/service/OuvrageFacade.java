/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Auteur;
import beans.Domaine;
import beans.Ouvrage;
import beans.Universite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Youssef
 */
public class OuvrageFacade {

    Config c = new Config();
    UniversiteFacade universiteFacade = new UniversiteFacade();
    AuteurFacade auteurFacade = new AuteurFacade();
    DomaineFacade domaineFacade = new DomaineFacade();

    public List<Ouvrage> getOuvragesByResultSet(ResultSet result) {
        if (result != null) {
            List<Ouvrage> list = new ArrayList();
            try {
                while (result.next()) {
                    Ouvrage ouvrage = new Ouvrage();
                    ouvrage.setId(result.getInt(1));
                    ouvrage.setTitre(result.getString(2));
                    ouvrage.setEditeur(result.getString(3));
                    ouvrage.setAnnee(result.getString(4));
                    ouvrage.setStock(result.getInt(5));
                    Universite universite = universiteFacade.findUniversiteById(result.getInt(6));
                    ouvrage.setUniversite(universite);
                    Auteur auteur = auteurFacade.findAuteurById(result.getInt(7));
                    ouvrage.setAuteur(auteur);
                    Domaine domaine = domaineFacade.findDomaineById(result.getInt(8));
                    ouvrage.setDomaine(domaine);
                    ouvrage.setNb_ruptures(result.getInt(9));
                    list.add(ouvrage);
                }
                result.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
            return list;
        } else {
            return null;
        }
    }

    public List<Ouvrage> getAllOuvrages(int univ_id) {
        return getOuvragesByResultSet(c.loadData("SELECT * FROM ouvrages where universite_id = " + univ_id));
    }

    public List<Ouvrage> findOuvrages(String titre, int univ_id) {
        ResultSet rs = c.loadData(
                "SELECT * "
                + "FROM ouvrages "
                + "WHERE universite_id = " + univ_id + " AND LOWER(titre) like LOWER('%" + titre + "%') "
        );
        return getOuvragesByResultSet(rs);
    }

    public void insertDb(String titre, String editeur, String annee, int stock, int auteur, int domaine, int universite_id) {
        String query = "insert into ouvrages (id, titre, editeur, annee, stock, auteur_id, domaine_id, nb_ruptures, universite_id) "
                + "values (seq_ouvrages.nextval, '" + titre + "', '" + editeur + "', '" + annee + "', " + stock + ", "
                + auteur + ", " + domaine + ", 0, " + universite_id + ")";
        c.execQuery(query);
    }

    public void updateDb(int id, String titre, String editeur, String annee, int stock, int auteur, int domaine, int universite_id) {
        String query = "update ouvrages set titre = '" + titre + "', editeur = '" + editeur
                + "', annee = '" + annee + "', stock = " + stock + ", auteur_id = " + auteur
                + ", domaine_id = " + domaine + ", universite_id = " + universite_id + " where id = " + id;
        c.execQuery(query);
    }

    public void deleteDb(int id) {
        String query = "delete from ouvrages where id = " + id;
        c.execQuery(query);
    }

    public Ouvrage getOuvrageById(int id) {
        ResultSet rs = c.loadData("SELECT * FROM ouvrages WHERE id = " + id);
        List<Ouvrage> ouvrages = getOuvragesByResultSet(rs);
        if (ouvrages != null && !ouvrages.isEmpty()) {
            return ouvrages.get(0);
        } else {
            return null;
        }
    }
}
