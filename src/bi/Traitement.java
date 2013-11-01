/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Hamza
 */
public class Traitement {
    
    private Connection conn_src;
    private Connection conn_dst;
    
    private final String mysql_request = "SELECT e.nom, e.prenom, e.date_naissance, b.branche, n.trimestre1, n.trimestre2, n.trimestre3 FROM etudiant e JOIN branche b ON e.branche_id = b.id JOIN notes n ON e.id = n.etudiant_id";
    private final String oracle_request = "SELECT e.\"nom\", e.\"prenom\", e.\"date_naissance\", b.\"branche\", n.\"trimestre1\", n.\"trimestre2\", n.\"trimestre3\" FROM \"etudiant\" e JOIN \"branche\" b ON e.\"branche_id\" = b.\"id\" JOIN \"notes\" n ON e.\"id\" = n.\"etudiant_id\"";
    
    public Traitement(Connection src, Connection dst) {
        
        this.conn_src = src;
        this.conn_dst = dst;
    }
    
    public boolean transform() {
        
        // Verification des connexions
        if(this.conn_src == null || this.conn_dst == null) {
            System.out.println("L'une ou les deux connexions sont null. Vérifier les connexion à les bases de données");
            return false;
        }
        
        // Definition des variables
        Statement st_src, st_dst;
        ResultSet rs;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        float moy;
        Date d1;
        Calendar cal;
        int age;
        
        // Traitement
        try {
            // Initialisation
            st_src = this.conn_src.createStatement();
            st_dst = this.conn_dst.createStatement();
            
            // Execution de la requete
            if(conn_src.getMetaData().getURL().contains(Constante.db_trans)) { // Base de données = Mysql
                rs = st_src.executeQuery(this.mysql_request);
            }
            else if(conn_src.getMetaData().getUserName().equals(Constante.db_trans.toUpperCase())) { // Base de données = Oracle
                rs = st_src.executeQuery(this.oracle_request);
            }
            else {
                System.out.println("Transform error message: Mauvaise base de données selectionnée. Vous devez utiliser la base transcationnelle pour cette fonction");
                return false;
            }
            
            // Parcours du resultat de la requete
            while(rs.next()) {
                
                // Calculer moyenne
                moy = ((rs.getFloat("trimestre1") + rs.getFloat("trimestre2") + rs.getFloat("trimestre3"))/3);
                
                // Calculer age
                try {
                    d1 = dateFormat.parse(rs.getString("date_naissance"));
                    cal = Calendar.getInstance();
                    cal.setTime(d1);
                    int by = cal.get(Calendar.YEAR);// year of birth
                    int cy = Calendar.getInstance().get(Calendar.YEAR);// current year
                    age = cy - by;
                }
                catch(ParseException pe) {
                    System.out.println("Erreur parse: " + pe.getCause() + "\n" + pe.getMessage());
                    return false;
                }
                
                // Insertion
                if(conn_dst.getMetaData().getURL().contains(Constante.db_dwh)) { // Base de données = Mysql
                    st_dst.executeUpdate("INSERT INTO etudiant VALUES(null, '" + rs.getString("nom") + "', '" + rs.getString("prenom") + "', " + age + ", '" + rs.getString("branche") + "', " + moy + ")");
                }
                else if(conn_dst.getMetaData().getUserName().equals(Constante.db_dwh.toUpperCase())) { // Base de données = Oracle
                    st_dst.executeUpdate("INSERT INTO \"etudiant\" VALUES(etudiant_iterator.nextval, '" + rs.getString("nom") + "', '" + rs.getString("prenom") + "', " + age + ", '" + rs.getString("branche") + "', " + moy + ")");
                }
                else {
                    System.out.println("Transform error message: Mauvaise base de données selectionnée. Vous devez utiliser la base DWH pour cette fonction");
                    return false;
                }
                
            }
            
            System.out.println("Transformation et rempliassage du DWH réussi");
            return true;
        }
        catch (SQLException ex) {
            System.out.println("Erreur: " + ex.getCause() + "\n" + ex.getMessage());
            return false;
        }
    }

}
