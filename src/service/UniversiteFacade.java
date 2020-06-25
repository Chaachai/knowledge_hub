/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Universite;
import java.sql.ResultSet;
import static oracle.net.aso.b.c;

/**
 *
 * @author Youssef
 */
public class UniversiteFacade {

    Config c = new Config();

    public Universite findUniversiteById(int id) {
        ResultSet rs = c.loadData("SELECT * FROM universites WHERE id = '" + id + "' ");
        Universite universite = new Universite();
        if (rs != null) {
            try {
                while (rs.next()) {
                    universite.setId(rs.getInt(1));
                    universite.setNom(rs.getString(2));
                }
                return universite;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        } else {
            return null;
        }
    }
}
