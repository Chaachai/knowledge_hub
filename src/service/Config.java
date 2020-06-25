/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Session;

/**
 *
 * @author CHAACHAI Youssef
 */
public class Config {
    
//    Connection con = null;

    public Connection connect(String username, String password, String server, String DBName) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@"+server+":1521:" + DBName, //Default Port = 1521 & DBName = orcl
                    username,
                    password
            );
//            if (con != null) {
//                System.out.println("Connected !");
//            } else {
//                System.out.println("Not connected !!");
//            }

            Session.updateAttribute(con, "connection");

            return con;
        } catch (Exception e) {
            System.out.println(e);
            Session.updateAttribute(e.toString(), "error");
            return null;
        }
    }

    public ResultSet loadData(String query) {
        Connection con = (Connection) Session.getAttribut("connection");
//        Connection con = connect(username, password);
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public int execQuery(String query) {
//        Connection con = connect(username, password);
        Connection con = (Connection) Session.getAttribut("connection");
        try {
            Statement stmt = con.createStatement();
            System.out.println("*****************************");
            System.out.println(query);
            System.out.println("*****************************");
            stmt.executeUpdate(query);
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            return - 1;
        }

    }

//    public Long generateId(String tableName, String idName) {
//        int maxId = 0;
//        String query = " SELECT max(" + idName + ") FROM " + tableName + " ";
//        ResultSet rs = loadData(query);
//        try {
//            while (rs.next()) {
//                maxId = rs.getInt(1);
//            }
//            return maxId + 1L;
//        } catch (SQLException ex) {
//            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
//            return 1L;
//        }        
//    }
}
