/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Domaine;
import beans.Employe;
import beans.Statut;
import beans.Universite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.relation.Role;

/**
 *
 * @author Youssef
 */
public class EmployeFacade {

    Config c = new Config();

    UniversiteFacade universiteFacade = new UniversiteFacade();
    StatutFacade statutFacade = new StatutFacade();

    public int login(String username, String password, int universite_id) {
        ResultSet rs = c.loadData("SELECT * FROM employes "
                + "WHERE username = '" + username + "' "
                + "AND password = '" + password + "' "
                + "AND universite_id = " + universite_id);
        if (rs != null) {
            try {
                while (rs.next()) {
                    return 1;
                }
                return -1;
            } catch (Exception e) {
                System.out.println(e);
                return -3;
            }
        }
        return -2;
    }

    public Employe getEmployeByLogin(String login) {
        ResultSet rs = c.loadData("SELECT * FROM employes WHERE username = '" + login + "' ");
        Employe employe = new Employe();
        if (rs != null) {
            try {
                while (rs.next()) {
                    employe.setId(rs.getInt(1));
                    employe.setNom(rs.getString(2));
                    employe.setPrenom(rs.getString(3));
                    employe.setAdresse(rs.getString(4));
                    employe.setUsername(rs.getString(5));
                    employe.setPassword(rs.getString(6));
                    Statut statut = statutFacade.findStatutById(rs.getInt(7));
                    employe.setStatut(statut);
                    Universite universite = universiteFacade.findUniversiteById(rs.getInt(8));
                    employe.setUniversite(universite);
                }
                return employe;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Employe> getAllEmployes() {
        try {
            List<Employe> list = new ArrayList();
            ResultSet rs = c.loadData("SELECT * FROM employes ");
            while (rs.next()) {
                Employe employe = new Employe();
                employe.setId(rs.getInt(1));
                employe.setNom(rs.getString(2));
                employe.setPrenom(rs.getString(3));
                employe.setAdresse(rs.getString(4));
                employe.setUsername(rs.getString(5));
                employe.setPassword(rs.getString(6));
                Statut statut = statutFacade.findStatutById(rs.getInt(7));
                employe.setStatut(statut);
                Universite universite = universiteFacade.findUniversiteById(rs.getInt(8));
                employe.setUniversite(universite);
                list.add(employe);
            }
            return list;
        } catch (SQLException ex) {
            System.out.println("SQLEXCEPTION " + ex);
            return null;
        }

    }

    public List<Employe> findEmployes(String nom) {
        try {
            List<Employe> list = new ArrayList();
            ResultSet rs = c.loadData(
                    "SELECT * "
                    + "FROM employes "
                    + "WHERE LOWER(nom) like LOWER('%" + nom + "%') "
                    + "OR LOWER(prenom) like LOWER('%" + nom + "%') "
            );
            while (rs.next()) {
                Employe employe = new Employe();
                employe.setId(rs.getInt(1));
                employe.setNom(rs.getString(2));
                employe.setPrenom(rs.getString(3));
                employe.setAdresse(rs.getString(4));
                employe.setUsername(rs.getString(5));
                employe.setPassword(rs.getString(6));
                Statut statut = statutFacade.findStatutById(rs.getInt(7));
                employe.setStatut(statut);
                Universite universite = universiteFacade.findUniversiteById(rs.getInt(8));
                employe.setUniversite(universite);
                list.add(employe);
            }
            rs.close();
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(OuvrageFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
