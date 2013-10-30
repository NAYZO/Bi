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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Ouverture des connexion
        Connection conn1 = ConnexionBD.openConnectionForMySql(Constante.db_trans);
        Connection conn2 = ConnexionBD.openConnectionForMySql(Constante.db_dwh);
        
        // Clearing all tables
        ConnexionBD.clearEtudiant(conn1);
        ConnexionBD.clearNotes(conn1);
        ConnexionBD.clearDWH(conn2);
        
        // Remplissage des tables de la base transactionnelle
        ConnexionBD.remplirTables(conn1, 20);
        
        //remplire Fichier depuis Mysql
        
        
        // Initialisation du Timer
        Timer timer = new Timer();
        timer.startTimer();
        
        // Transformation des données et remplissage de la base DWH
        Traitement tr = new Traitement(conn1, conn2);
        tr.transform();
        
        // Affichage du temps d'execution
        timer.showTimer();
        
        // Fermeture des connexion
        ConnexionBD.closeConnection(conn1);
        ConnexionBD.closeConnection(conn2);        
        
    }
}
