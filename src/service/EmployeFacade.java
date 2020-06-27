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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.relation.Role;
import util.Session;

/**
 *
 * @author Youssef
 */
public class EmployeFacade {

    Config c = new Config();

    UniversiteFacade universiteFacade = new UniversiteFacade();
    StatutFacade statutFacade = new StatutFacade();

    public List<Employe> getEmployesByResultSet(ResultSet result) {
        if (result != null) {
            List<Employe> list = new ArrayList();
            try {
                while (result.next()) {
                    Employe employe = new Employe();
                    employe.setId(result.getInt(1));
                    employe.setNom(result.getString(2));
                    employe.setPrenom(result.getString(3));
                    employe.setAdresse(result.getString(4));
                    employe.setUsername(result.getString(5));
                    employe.setPassword(result.getString(6));
                    Statut statut = statutFacade.findStatutById(result.getInt(7));
                    employe.setStatut(statut);
                    Universite universite = universiteFacade.findUniversiteById(result.getInt(8));
                    employe.setUniversite(universite);
                    employe.setAccepted(result.getInt(9));
                    list.add(employe);
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
        List<Employe> emps = getEmployesByResultSet(rs);
        if (emps != null && !emps.isEmpty()) {
            return emps.get(0);
        } else {
            return null;
        }

    }

    public List<Employe> getAllEmployes() {
        return getEmployesByResultSet(c.loadData("SELECT * FROM employes WHERE accepted = 1"));
    }

    public List<Employe> getPendingEmployes() {
        return getEmployesByResultSet(c.loadData("SELECT * FROM employes WHERE accepted = 0"));
    }

    public List<Employe> findEmployes(String nom) {
        ResultSet rs = c.loadData(
                "SELECT * FROM employes "
                + "WHERE LOWER(nom) like LOWER('%" + nom + "%') "
                + "OR LOWER(prenom) like LOWER('%" + nom + "%') "
        );
        return getEmployesByResultSet(rs);
    }

    public void insertDb(String nom, String prenom, String adresse, String username, String password, int universite) {
        String query = "insert into employes (id, nom, prenom, adresse, username, password, statut_id, universite_id, accepted) "
                + "values (seq_employes.nextval, '" + nom + "', '" + prenom + "', '" + adresse + "', '" + username + "', '"
                + password + "', 2" + "," + universite + ", 0)";
        c.execQuery(query);
    }

    public void updateDb(Employe emp) {
        String query = "update employes set nom = '" + emp.getNom() + "', prenom = '" + emp.getPrenom()
                + "', adresse = '" + emp.getAdresse() + "', username = '" + emp.getUsername()
                + "', password= '" + emp.getPassword() + "', statut_id = " + emp.getStatut().getId()
                + ", universite_id = " + emp.getUniversite().getId() + ", accepted= " + emp.getAccepted()
                + " where id = " + emp.getId();
        c.execQuery(query);
    }

    public boolean usernameUsed(String username) {
        Employe emp = getEmployeByLogin(username);
        if (emp == null) {
            return false;
        } else if (emp.getUsername() == null) {
            return false;
        } else {
            return true;
        }
    }
}
