/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bi;

/**
 *
 * @author Hamza
 */
public class Constante {
    
    // Les Constantes
    public static final String mysql_host = "jdbc:mysql://localhost/";
    public static final String oracle_host = "jdbc:oracle:thin:@localhost:1521:XE";
    
    public static final String db_trans = "bi_transactionnelle";
    public static final String db_dwh = "bi_dwh";
    
    public static final String mysql_driver = "com.mysql.jdbc.Driver";
    public static final String oracle_driver = "oracle.jdbc.driver.OracleDriver";
    
    public static final String mysql_username = "root";
    public static final String oracle_username = "";
    
    public static final String mysql_password = "";
    public static final String oracle_password = "test";
    
    // Les Fonctions
    public static int getRandomNumber(int min, int max) {
        //retourne un entier aléatoire compris entre min et max
        return (int)(Math.random() * (max - min + 1)) + min;
    }
    
}
