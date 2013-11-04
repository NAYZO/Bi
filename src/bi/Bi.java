/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bi;

import java.sql.Connection;

/**
 *
 * @author nayzo
 */
public class Bi {

    /**
     *
    public s @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ConnexionXML.deleteXML();
        ConnexionXML.writeIntoXML(5);
        
        Connection conn = ConnexionBD.openConnectionForOracle(Constante.db_dwh);
        ConnexionBD.clearDWH(conn);
        
        TraitementXML trxml = new TraitementXML(conn);
        trxml.transformXML();
        
        ConnexionBD.closeConnection(conn);
        
        /*
        // Ouverture des connexions
        Connection conn_trans = ConnexionBD.openConnectionForMySql(Constante.db_trans);
        Connection conn_dwh   = ConnexionBD.openConnectionForOracle(Constante.db_dwh);
        
        // Clearing tables
        ConnexionBD.clearEtudiant(conn_trans);
        ConnexionBD.clearNotes(conn_trans);
        ConnexionBD.clearDWH(conn_dwh);
        
        // Remplissage des tables
        ConnexionBD.remplirTables(conn_trans, 10);
        
        // Traitement
        Traitement tr = new Traitement(conn_trans, conn_dwh);
        tr.transform();
        
        // Fermeture des connexions
        ConnexionBD.closeConnection(conn_trans);
        ConnexionBD.closeConnection(conn_dwh);*/
    }
}
