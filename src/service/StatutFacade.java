/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Employe;
import beans.Statut;
import beans.Universite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Youssef
 */
public class StatutFacade {

    Config c = new Config();

    public Statut findStatutById(int id) {
        ResultSet rs = c.loadData("SELECT * FROM statuts WHERE id = '" + id + "' ");
        Statut statut = new Statut();
        if (rs != null) {
            try {
                while (rs.next()) {
                    statut.setId(rs.getInt(1));
                    statut.setNom(rs.getString(2));
                }
                return statut;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Statut> getAllStatuts() {
        try {
            List<Statut> list = new ArrayList();
            ResultSet rs = c.loadData("SELECT * FROM statuts ");
            while (rs.next()) {
                Statut statut = new Statut();
                statut.setId(rs.getInt(1));
                statut.setNom(rs.getString(2));
                list.add(statut);
            }
            return list;
        } catch (SQLException ex) {
            System.out.println("SQLEXCEPTION " + ex);
            return null;
        }

    }
}
