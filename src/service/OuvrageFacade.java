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

    public List<Ouvrage> getAllOuvrages() {
        try {
            List<Ouvrage> list = new ArrayList();
            ResultSet rs = c.loadData("SELECT * FROM ouvrages ");

            while (rs.next()) {
                Ouvrage ouvrage = new Ouvrage();
                ouvrage.setId(rs.getInt(1));
                ouvrage.setTitre(rs.getString(2));
                ouvrage.setEditeur(rs.getString(3));
                ouvrage.setAnnee(rs.getString(4));
                ouvrage.setStock(rs.getInt(5));
                Universite universite = universiteFacade.findUniversiteById(rs.getInt(6));
                ouvrage.setUniversite(universite);
                Auteur auteur = auteurFacade.findAuteurById(rs.getInt(7));
                ouvrage.setAuteur(auteur);
                Domaine domaine = domaineFacade.findDomaineById(rs.getInt(8));
                ouvrage.setDomaine(domaine);
                ouvrage.setNb_ruptures(rs.getInt(9));
                list.add(ouvrage);
            }
            return list;
        } catch (SQLException ex) {
            System.out.println("SQLEXCEPTION " + ex);
            return null;
        }
    }

    public List<Ouvrage> findOuvrages(String titre) {
        try {
            List<Ouvrage> list = new ArrayList();

            ResultSet rs = c.loadData(
                    "SELECT * "
                    + "FROM ouvrages "
                    + "WHERE LOWER(titre) like LOWER('%" + titre + "%') "
            );

            while (rs.next()) {
                Ouvrage ouvrage = new Ouvrage();
                ouvrage.setId(rs.getInt(1));
                ouvrage.setTitre(rs.getString(2));
                ouvrage.setEditeur(rs.getString(3));
                ouvrage.setAnnee(rs.getString(4));
                ouvrage.setStock(rs.getInt(5));
                Universite universite = universiteFacade.findUniversiteById(rs.getInt(6));
                ouvrage.setUniversite(universite);
                Auteur auteur = auteurFacade.findAuteurById(rs.getInt(7));
                ouvrage.setAuteur(auteur);
                Domaine domaine = domaineFacade.findDomaineById(rs.getInt(8));
                ouvrage.setDomaine(domaine);
                ouvrage.setNb_ruptures(rs.getInt(9));
                list.add(ouvrage);
            }
            rs.close();
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(OuvrageFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void insertDb(String titre, String editeur, String annee, int stock, int auteur, int domaine) {
        String query = "insert into ouvrages (id, titre, editeur, annee, stock, auteur_id, domaine_id, nb_ruptures) "
                + "values (seq_ouvrages.nextval, '" + titre + "', '" + editeur + "', '" + annee + "', " + stock + ", "
                + auteur + ", " + domaine + ", 0)";
        c.execQuery(query);
    }

    public void updateDb(int id, String titre, String editeur, String annee, int stock, int auteur, int domaine) {
        String query = "update ouvrages set titre = '" + titre + "', editeur = '" + editeur
                + "', annee = '" + annee + "', stock = " + stock + ", auteur_id = " + auteur
                + ", domaine_id = " + domaine + " where id = " + id;
        c.execQuery(query);
    }

    public void deleteDb(int id) {
        String query = "delete from ouvrages where id = " + id;
        c.execQuery(query);
    }
}
