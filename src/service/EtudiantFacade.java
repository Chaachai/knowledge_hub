package service;

import beans.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EtudiantFacade {

    Config c = new Config();
    CursusFacade cursusFacade = new CursusFacade();
    UniversiteFacade universiteFacade = new UniversiteFacade();

    public List<Etudiant> getEtudiantByResultSet(ResultSet result) {
        if (result != null) {
            List<Etudiant> list = new ArrayList();
            try {
                while (result.next()) {
                    Etudiant etudiant = new Etudiant();
                    etudiant.setId(result.getInt(1));
                    etudiant.setCne(result.getString(2));
                    etudiant.setNom(result.getString(3));
                    etudiant.setPrenom(result.getString(4));
                    etudiant.setAdresse(result.getString(5));
                    etudiant.setNb_emprunts(result.getInt(6));
                    etudiant.setNb_retards(result.getInt(7));
                    etudiant.setBloque(result.getInt(8));
                    Cursus cursus = cursusFacade.findCursusById(result.getInt(9));
                    etudiant.setCursus(cursus);
                    Universite universite = universiteFacade.findUniversiteById(result.getInt(10));
                    etudiant.setUniversite(universite);
                    list.add(etudiant);
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

    public Etudiant getEtudiantByCne(String cne) {
        ResultSet rs = c.loadData("SELECT * FROM etudiants WHERE cne = '" + cne + "' ");
        List<Etudiant> etudiants = getEtudiantByResultSet(rs);
        if (etudiants != null && !etudiants.isEmpty()) {
            return etudiants.get(0);
        } else {
            return null;
        }
    }

    public Etudiant getEtudiantById(int id) {
        ResultSet rs = c.loadData("SELECT * FROM etudiants WHERE id = " + id);
        List<Etudiant> etudiants = getEtudiantByResultSet(rs);
        if (etudiants != null && !etudiants.isEmpty()) {
            return etudiants.get(0);
        } else {
            return null;
        }
    }

    public void updateDb(Etudiant etudiant) {
        String query = "update etudiants set cne = '" + etudiant.getCne() + "', nom = '" + etudiant.getNom() + "', "
                + "prenom = '" + etudiant.getPrenom() + "', adresse = '" + etudiant.getAdresse() + "', "
                + "nb_emprunts = " + etudiant.getNb_emprunts() + ", nb_retards = " + etudiant.getNb_retards() + ", "
                + "bloque = " + etudiant.getBloque() + ", cursus_id = " + etudiant.getCursus().getId() + ",  "
                + "universite_id = " + etudiant.getUniversite().getId()
                + " where id = " + etudiant.getId();
        c.execQuery(query);
    }

}
