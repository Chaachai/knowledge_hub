package service;

import beans.Cursus;
import beans.Statut;

import java.sql.ResultSet;

public class CursusFacade {

    Config c = new Config();
    public Cursus findCursusById(int id) {
        ResultSet rs = c.loadData("SELECT * FROM cursus WHERE id = '" + id + "' ");
        Cursus cursus = new Cursus();
        if (rs != null) {
            try {
                while (rs.next()) {
                    cursus.setId(rs.getInt(1));
                    cursus.setNom(rs.getString(2));
                }
                rs.close();
                return cursus;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        } else {
            return null;
        }
    }
}
