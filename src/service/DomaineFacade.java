/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Auteur;
import beans.Domaine;
import beans.Statut;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Youssef
 */
public class DomaineFacade {

    Config c = new Config();

    public Domaine findDomaineById(int id) {
        ResultSet rs = c.loadData("SELECT * FROM domaines WHERE id = '" + id + "' ");
        Domaine domaine = new Domaine();
        if (rs != null) {
            try {
                while (rs.next()) {
                    domaine.setId(rs.getInt(1));
                    domaine.setNom(rs.getString(2));
                }
                rs.close();
                return domaine;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Domaine> getAllFields() {
        try {
            List<Domaine> list = new ArrayList();
            ResultSet rs = c.loadData("SELECT * FROM domaines ");

            while (rs.next()) {
                Domaine domaine = new Domaine();
                domaine.setId(rs.getInt(1));
                domaine.setNom(rs.getString(2));
                list.add(domaine);
            }
            rs.close();
            return list;
        } catch (SQLException ex) {
            System.out.println("SQLEXCEPTION " + ex);
            return null;
        }
    }

    public void insertDb(String nom) {
        String query = "insert into domaines (id, nom) values "
                + "(seq_domaines.nextval, '" + nom + "')";
        c.execQuery(query);
    }
}
