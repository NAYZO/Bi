/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bi;

import java.sql.*;

/**
 *
 * @author nayzo
 */
public class Bi {

    /**
     * @param args the command line arguments
     */
    
    String url = "jdbc:mysql://localhost/";
    String dbR = "bi_transactionnelle";
    String dwh = "bi_dwh";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "root";
    String password = "root";
    Connection conn;
    
    public void remplireBdR() {
        try {
            Class.forName(driver).newInstance();
            this.conn = DriverManager.getConnection(this.url+this.dbR,this.userName,this.password);
            Statement st = this.conn.createStatement();            
            //***
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String formatDate = dateFormat.format(date);                
            //****
            for(int i=1; i<=10; i++) {
                mydate = java.sql.Date.valueOf(formatDate);
                st.executeUpdate("INSERT into etudiant VALUES('walid', 'riahi', '"+mydate+"', 1)");
            }
            
           } catch (Exception e) {
            e.printStackTrace();
          }
        
    }

    public static void main(String[] args) {
        // remplire la base relationnelle
        this.remplireBdR();
        this.conn.close();
    }
}
