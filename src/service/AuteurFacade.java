/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Auteur;
import beans.Statut;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Youssef
 */
public class AuteurFacade {

    Config c = new Config();

    public Auteur findAuteurById(int id) {
        ResultSet rs = c.loadData("SELECT * FROM auteurs WHERE id = '" + id + "' ");
        Auteur auteur = new Auteur();
        if (rs != null) {
            try {
                while (rs.next()) {
                    auteur.setId(rs.getInt(1));
                    auteur.setNom(rs.getString(2));
                    auteur.setPrenom(rs.getString(3));
                }
                rs.close();
                return auteur;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Auteur> getAllAuteurs() {
        try {
            List<Auteur> list = new ArrayList();
            ResultSet rs = c.loadData("SELECT * FROM auteurs ");

            while (rs.next()) {
                Auteur auteur = new Auteur();
                auteur.setId(rs.getInt(1));
                auteur.setNom(rs.getString(2));
                auteur.setPrenom(rs.getString(3));
                list.add(auteur);
            }
            rs.close();
            return list;
        } catch (SQLException ex) {
            System.out.println("SQLEXCEPTION " + ex);
            return null;
        }
    }

    public void insertDb(String nom, String prenom) {
        String query = "insert into auteurs (id, nom, prenom) "
                + "values (seq_auteurs.nextval, '" + nom + "', '" + prenom + "')";
        c.execQuery(query);
    }
}
