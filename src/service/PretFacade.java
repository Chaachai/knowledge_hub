/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Employe;
import beans.Etudiant;
import beans.Ouvrage;
import beans.Pret;
import beans.Statut;
import beans.Universite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Youssef
 */
public class PretFacade {

    Config c = new Config();

    EtudiantFacade etudiantFacade = new EtudiantFacade();
    OuvrageFacade ouvrageFacade = new OuvrageFacade();

    public List<Pret> getPretsByResultSet(ResultSet result) {
        if (result != null) {
            List<Pret> list = new ArrayList();
            try {
                while (result.next()) {
                    Pret pret = new Pret();
                    pret.setId(result.getInt(1));
                    Etudiant etudiant = etudiantFacade.getEtudiantById(result.getInt(3));
                    pret.setEtudiant(etudiant);
                    Ouvrage ouvrage = ouvrageFacade.getOuvrageById(result.getInt(2));
                    pret.setOuvrage(ouvrage);
                    pret.setDate_emprunt(result.getDate(4));
                    pret.setDate_retour_prevue(result.getDate(5));
                    pret.setDate_retour_effective(result.getDate(6));
                    list.add(pret);
                }
                result.close();
            } catch (SQLException ex) {
                Logger.getLogger(PretFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
            return list;
        } else {
            return null;
        }
    }

    public void insertDb(int etudiant_id, int ouvrage_id, LocalDate date_emprunt, LocalDate date_retour_prevue) {
        String query = "insert into prets (id, ouvrage_id, etudiant_id, date_emprunt, date_retour_prevue) "
                + "values (seq_prets.nextval, " + ouvrage_id + ", " + etudiant_id + ", "
                + "TO_DATE('" + date_emprunt + "', 'yyyy/MM/dd'),  TO_DATE('" + date_retour_prevue + "', 'yyyy/MM/dd') )";
        c.execQuery(query);

    }

    public List<Pret> getPendingPrets(int universite_id) {
        return getPretsByResultSet(c.loadData("SELECT * FROM prets WHERE date_retour_effective IS null "
                + "AND ouvrage_id IN (SELECT id from ouvrages where universite_id = " + universite_id + " )"));
    }

    public List<Pret> filterPretsByStudent(String name) {
        return getPretsByResultSet(c.loadData("SELECT * FROM prets "
                + "WHERE date_retour_effective IS null "
                + "AND (etudiant_id IN (SELECT id from etudiants "
                + "WHERE LOWER(nom) like '%" + name + "%' OR LOWER(prenom) LIKE '%" + name + "%' ) )"));
    }

    public void updateDb(Pret pret) {
        SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = mdyFormat.format(pret.getDate_retour_effective());
//        System.out.println("Date emprunt == " + pret.getDate_emprunt());
//        System.out.println("Date retour prevu == " + pret.getDate_retour_prevue());
//        System.out.println("Date retour effective == " + sDate);
        String query = "update prets set ouvrage_id = '" + pret.getOuvrage().getId() + "', "
                + "etudiant_id = '" + pret.getEtudiant().getId() + "', "
                + "date_emprunt = TO_DATE('" + pret.getDate_emprunt() + "', 'yyyy-MM-dd'), "
                + "date_retour_prevue =  TO_DATE('" + pret.getDate_retour_prevue() + "', 'yyyy-MM-dd'), "
                + "date_retour_effective =  TO_DATE('" + sDate + "', 'yyyy-MM-dd') "
                + " where id = " + pret.getId();
//        System.out.println("REQUEST == " + query);
        c.execQuery(query);
    }
}
