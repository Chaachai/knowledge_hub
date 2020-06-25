/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Statut;
import beans.Universite;
import java.sql.ResultSet;

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
}
