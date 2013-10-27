/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Hamza
 */
public class ConnexionBD {
    
    private Connection mysql_conn = null;
    private Connection oracle_conn = null;
    
    public boolean openConnectionForMySql(String bd) {
        
        if(!bd.equals(Constante.db_trans) && !bd.equals(Constante.db_dwh)) {
            System.out.println("Nom de base de données n'est pas valide");
            return false;
        }
        
        try {
            Class.forName(Constante.mysql_driver).newInstance();
            this.mysql_conn = DriverManager.getConnection(Constante.mysql_host + bd, Constante.mysql_username, Constante.mysql_password);
            return true;
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println(ex.getCause() + ex.getMessage());
            return false;
        }
    }
    
    public boolean openConnectionForOracle() {
        
        return true;
    }
    
    public boolean closeConnection() {
        
        try {
            if(this.mysql_conn != null) {
                this.mysql_conn.close();
            }
                
            if(this.oracle_conn != null) {
                this.oracle_conn.close();
            }
            
            return true;
        }
        catch (SQLException ex) {
            System.out.println(ex.getCause() + ex.getMessage());
            return false;
        }
    }
    
    public void remplirEtudiant(int nbreLigne) {
        
        try {
            Statement st = this.mysql_conn.createStatement();
            for(int i=0 ; i<nbreLigne ; i++) {
                st.executeUpdate("INSERT INTO etudiant VALUES(null, 'walid', 'riahi', '2012/12/12', 1)");
            }
        }
        catch (SQLException ex) {
            System.out.println("Erreur: " + ex.getCause() + ex.getMessage());
        }
    }
    
    public void test() {
        try {
            Statement st = this.mysql_conn.createStatement();
            ResultSet obj = st.executeQuery("SELECT * FROM branche;");
            ResultSetMetaData resultMeta = obj.getMetaData();
            
            System.out.println("\n**********************************");
            //On affiche le nom des colonnes
            for(int i = 1; i <= resultMeta.getColumnCount(); i++) {
                System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");
            }

            System.out.println("\n**********************************");

            while(obj.next()){         
              for(int i = 1; i <= resultMeta.getColumnCount(); i++) {
                    System.out.print("\t" + obj.getObject(i).toString() + "\t |");
                }

              System.out.println("\n---------------------------------");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getCause() + ex.getMessage());
        }
    }
    
}

/*Statement st = this.conn.createStatement();
//***
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    String formatDate = dateFormat.format(date);                
//****
for(int i=1; i<=10; i++) {
    Date mydate = java.sql.Date.valueOf(formatDate);
    st.executeUpdate("INSERT into etudiant VALUES('walid', 'riahi', '"+mydate+"', 1)");
}
*/