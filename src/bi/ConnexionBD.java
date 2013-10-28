/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    
    public void remplirMySql(int nbreLigne) {
        
        try {
            Statement st = this.mysql_conn.createStatement();
            for(int i=0 ; i<nbreLigne ; i++) {
                st.executeUpdate("INSERT INTO etudiant VALUES(null, 'walid', 'riahi', '2012/12/12', "+this.getRandomNote(1,7)+")", Statement.RETURN_GENERATED_KEYS);
                ResultSet keys = st.getGeneratedKeys();    
                keys.next();  
                Integer lastId = keys.getInt(1);
                st.executeUpdate("INSERT INTO notes VALUES(null, "+this.getRandomNote(0,20)+", "+this.getRandomNote(0,20)+", "+this.getRandomNote(0,20)+", "+lastId+")");
            }
        }
        catch (SQLException ex) {
            System.out.println("Erreur: " + ex.getCause() + ex.getMessage());
        }
    }
    
    public int getRandomNote(int min, int max)
    {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
    
    public void loadMysqlDwh()
    {
        try {
            Statement st = this.mysql_conn.createStatement();
            String query = "SELECT * FROM etudiant e JOIN branche b ON e.branche_id = b.id JOIN notes n ON e.id = n.etudiant_id";
            ResultSet rs = st.executeQuery(query);
            this.openConnectionForMySql(Constante.db_dwh);
            Statement stdwh = this.mysql_conn.createStatement();
            while (rs.next()) {
                // calculer Moyenne
                Float moy = (rs.getFloat("trimestre1") + rs.getFloat("trimestre2") + rs.getFloat("trimestre3"))/3;                
                try {
                    // calculer age
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();                
                    long d1=dateFormat.parse(rs.getString("date_naissance")).getTime();
                    long d2=dateFormat.parse(dateFormat.format(date)).getTime();
                    int age = (int)Math.abs((d1-d2)/(1000*60*60*24*365));
                    stdwh.executeUpdate("INSERT INTO etudiant VALUES(null, '"+rs.getString("nom")+"', '"+rs.getString("prenom")+"',"+age+" , '"+rs.getString("branche")+"', "+moy+")");
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
                
            }  
        }
        catch (SQLException ex) {
            System.out.println("Erreur: " + ex.getCause() + ex.getMessage());
        }
    }
       

    
//    public void test() {
//        try {
//            Statement st = this.mysql_conn.createStatement();
//            ResultSet obj = st.executeQuery("SELECT * FROM branche;");
//            ResultSetMetaData resultMeta = obj.getMetaData();
//            
//            System.out.println("\n**********************************");
//            //On affiche le nom des colonnes
//            for(int i = 1; i <= resultMeta.getColumnCount(); i++) {
//                System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");
//            }
//
//            System.out.println("\n**********************************");
//
//            while(obj.next()){         
//              for(int i = 1; i <= resultMeta.getColumnCount(); i++) {
//                    System.out.print("\t" + obj.getObject(i).toString() + "\t |");
//                }
//
//              System.out.println("\n---------------------------------");
//            }
//        }
//        catch (SQLException ex) {
//            System.out.println(ex.getCause() + ex.getMessage());
//        }
//    }
    
}