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
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

// import du SAX
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author nayzo
 */
public class TraitementXML extends DefaultHandler {
    
    // Definition des variables
        Map<String, String> etn = new HashMap<String, String>();
        List<HashMap<String, String>> etudiant = new ArrayList<HashMap<String, String>>();
        Map<String, String> branche = new HashMap<String, String>();
        Statement st_MySql;
        ResultSet rs;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        float moy;
        Date d1;
        Calendar cal;
        int age=0;
        
    private Connection conn_MySql;    
    
    public TraitementXML(Connection conn_MySql) {
        
        this.conn_MySql = conn_MySql;
    }
    
//***********************************************    
public int getAge(String age) {
      // Calculer age
                try {
                    d1 = dateFormat.parse(age);
                    cal = Calendar.getInstance();
                    cal.setTime(d1);
                    int by = cal.get(Calendar.YEAR);// year of birth
                    int cy = Calendar.getInstance().get(Calendar.YEAR);// current year
                    this.age = cy - by;
                }
                catch(ParseException pe) {
                    System.out.println("Erreur parse: " + pe.getCause() + "\n" + pe.getMessage());
                }
                return this.age;
  }

 public void LoadXML(){
  try {
   // obtain and configure a SAX based parser
   SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

   // obtain object for SAX parser
   SAXParser saxParser = saxParserFactory.newSAXParser();

   // default handler for SAX handler class
   // all three methods are written in handler's body
   DefaultHandler defaultHandler = new DefaultHandler(){
    
    // this method is called every time the parser gets an open tag '<'
    // identifies which tag is being open at time by assigning an open flag
    public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
   
     if (qName.equalsIgnoreCase("BRE")) {
         branche.put(""+attributes.getValue("id")+"", attributes.getValue("branche"));
     }   
     if (qName.equalsIgnoreCase("ETN")) {
         etn.put("nom", attributes.getValue("nom"));
         etn.put("prenom", attributes.getValue("prenom"));
         etn.put("branche", branche.get(""+attributes.getValue("branche_id")+""));
         etn.put("age", ""+getAge(attributes.getValue("date_naissance"))+"");
         etudiant.add( Integer.parseInt(attributes.getValue("id")), (HashMap<String, String>) etn);
     }
     if (qName.equalsIgnoreCase("NT")) {
         
         moy = ((Float.parseFloat(attributes.getValue("trimestre1")) + Float.parseFloat(attributes.getValue("trimestre2")) + Float.parseFloat(attributes.getValue("trimestre3")))/3);
         etudiant.get(Integer.parseInt(attributes.getValue("etudiant_id"))).put("moyenne", ""+moy+"");
     }     
    }

    // calls by the parser whenever '>' end tag is found in xml 
    // makes tags flag to 'close'
    public void endElement(String uri, String localName, String qName)
      throws SAXException {

    }
   };
   
   // parse the XML specified in the given path and uses supplied
   // handler to parse the document
   // this calls startElement(), endElement() and character() methods
   // accordingly
   saxParser.parse("src/bi/BDtransactionnelle.xml", defaultHandler);
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

  
//****************************************************    
    
    
    public boolean transformXML() {
        // Verification des connexions
        if(this.conn_MySql == null) {
            System.out.println("La connexions est null. Vérifier la connexion à les bases de données");
            return false;
        }
        
        // Traitement
        try {
            // Initialisation
            st_MySql = this.conn_MySql.createStatement();
            
            // Parcours du resultat de la requete
            for(int i=1; i<=etudiant.size(); i++) {        
                
                // Insertion
                    st_MySql.executeUpdate("INSERT INTO etudiant VALUES(null, '" + etudiant.get(i).get("nom") + "', '" + etudiant.get(i).get("prenom") + "', " + etudiant.get(i).get("age") + ", '" + etudiant.get(i).get("branche") + "', " + etudiant.get(i).get("moyenne") + ")");
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
