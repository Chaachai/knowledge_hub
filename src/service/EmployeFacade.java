/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.ResultSet;

/**
 *
 * @author Youssef
 */
public class EmployeFacade {

    Config c = new Config();

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

}
