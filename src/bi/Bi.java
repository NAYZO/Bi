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
        Connection db_trans = ConnexionBD.openConnectionForMySql(Constante.db_trans);
        Connection db_dwh = ConnexionBD.openConnectionForMySql(Constante.db_dwh);
        
        // Clearing all tables
        ConnexionBD.clearEtudiant(db_trans);
        ConnexionBD.clearNotes(db_trans);
        ConnexionBD.clearDWH(db_dwh);
        
// ############################ FICHIER XML ######################################        
        //remplire le XML
        ConnexionXML connexionxml = new ConnexionXML();
        connexionxml.deleteXML();
        connexionxml.writeIntoXML(7);        
        
        // Initialisation du Timer
        Timer timer1 = new Timer();
        timer1.startTimer();
        
        // Traitement sur XML
        TraitementXML traitementxml = new TraitementXML(db_dwh);
        traitementxml.LoadXML();
        traitementxml.transformXML();
        
        // Affichage du temps d'execution
        timer1.showTimer();

 // ############################ MySql -> MySql ######################################
 
   /*     
        // Remplissage des tables de la base transactionnelle
        ConnexionBD.remplirTables(db_trans, 20);

        // Initialisation du Timer
        Timer timer2 = new Timer();
        timer2.startTimer();
        
        // Transformation des données et remplissage de la base DWH
        Traitement tr = new Traitement(db_trans, db_dwh);
        tr.transform();
        
        // Affichage du temps d'execution
        timer2.showTimer();
        
        // Fermeture des connexion
        ConnexionBD.closeConnection(db_trans);
        ConnexionBD.closeConnection(db_dwh);        
    */   
    }
}
