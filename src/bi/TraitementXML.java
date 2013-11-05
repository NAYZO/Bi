package bi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    List<HashMap<String, String>> etudiant = new ArrayList<>();
    Map<String, String> branche = new HashMap<>();
    Statement st_MySql;
    ResultSet rs;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date d1;
    Calendar cal;
    int age = 0;
    float moy;
    private Connection conn_dst;

    public TraitementXML(Connection conn) {

        this.conn_dst = conn;
    }
    
    public String getAge(String age) {
        // Calculer age
        try {
            d1 = dateFormat.parse(age);
            cal = Calendar.getInstance();
            cal.setTime(d1);
            int by = cal.get(Calendar.YEAR);// year of birth
            int cy = Calendar.getInstance().get(Calendar.YEAR);// current year
            this.age = cy - by;
        }
        catch (ParseException pe) {
            System.out.println("Erreur parse: " + pe.getCause() + "\n" + pe.getMessage());
        }
        return Integer.toString(this.age);
    }

    public void LoadXML() {
        try {
            // obtain and configure a SAX based parser
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

            // obtain object for SAX parser
            SAXParser saxParser = saxParserFactory.newSAXParser();

            // default handler for SAX handler class
            // all three methods are written in handler's body
            DefaultHandler defaultHandler = new DefaultHandler() {
                // this method is called every time the parser gets an open tag '<'
                // identifies which tag is being open at time by assigning an open flag
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

                    if (qName.equalsIgnoreCase("BRE")) {

                        int id = Integer.parseInt(attributes.getValue("id")) - 1;
                        branche.put("" + id + "", attributes.getValue("branche"));
                    }
                    if (qName.equalsIgnoreCase("ETND")) {
                        Map<String, String> etnd = new HashMap<>();
                        etnd.put("nom", attributes.getValue("nom"));
                        etnd.put("prenom", attributes.getValue("prenom"));
                        int id = Integer.parseInt(attributes.getValue("branche_id")) - 1;
                        etnd.put("branche", branche.get("" + id + ""));
                        etnd.put("age", getAge(attributes.getValue("date_naissance")));
                        etudiant.add(Integer.parseInt(attributes.getValue("id")) - 1, (HashMap<String, String>) etnd);
                    }
                    if (qName.equalsIgnoreCase("NT")) {

                        moy = ((Float.parseFloat(attributes.getValue("trimestre1")) + Float.parseFloat(attributes.getValue("trimestre2")) + Float.parseFloat(attributes.getValue("trimestre3"))) / 3);
                        int id = Integer.parseInt(attributes.getValue("etudiant_id")) - 1;
                        etudiant.get(id).put("moyenne", "" + moy + "");
                    }
                }

                // calls by the parser whenever '>' end tag is found in xml 
                // makes tags flag to 'close'
                public void endElement(String uri, String localName, String qName) throws SAXException {
                }
            };

            // parse the XML specified in the given path and uses supplied
            // handler to parse the document
            // this calls startElement(), endElement() and character() methods
            // accordingly
            saxParser.parse("src/bi/BDtransactionnelle.xml", defaultHandler);
        } catch (Exception ex) {
            System.out.println("LoadXML error: " + ex.getClass() + "\nCause: " + ex.getCause() + "\nMessage: " + ex.getMessage());
        }
    }
    
    public boolean transformXML() {
        // Verification des connexions
        if(this.conn_dst == null) {
            System.out.println("La connexion est null. Vérifier la connexion à la base de données");
            return false;
        }
        
        //Load XML
        this.LoadXML();

        // Traitement
        try {
            // Initialisation
            st_MySql = this.conn_dst.createStatement();

            if(conn_dst.getMetaData().getURL().contains(Constante.db_dwh)) { // Base de données = Mysql
                
                // Insertion
                for(int i=0 ; i<=etudiant.size()-1 ; i++) {
                    st_MySql.executeUpdate("INSERT INTO etudiant VALUES(null, '" + etudiant.get(i).get("nom") + "', '" + etudiant.get(i).get("prenom") + "', " + etudiant.get(i).get("age") + ", '" + etudiant.get(i).get("branche") + "', " + etudiant.get(i).get("moyenne") + ")");
                }
            }
            else if(conn_dst.getMetaData().getUserName().equals(Constante.db_dwh.toUpperCase())) { // Base de données = Oracle
                
                // Insertion
                for(int i=0 ; i<=etudiant.size()-1 ; i++) {
                    st_MySql.executeUpdate("INSERT INTO \"etudiant\" VALUES(etudiant_iterator.nextval, '" + etudiant.get(i).get("nom") + "', '" + etudiant.get(i).get("prenom") + "', " + etudiant.get(i).get("age") + ", '" + etudiant.get(i).get("branche") + "', " + etudiant.get(i).get("moyenne") + ")");
                }
            }
            else {
                System.out.println("TransformXML error message: Mauvaise base de données selectionnée. Vous devez utiliser la base DWH pour cette fonction");
                return false;
            }

            System.out.println("Transformation et rempliassage du DWH réussi");
            return true;
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getCause() + "\n" + ex.getMessage());
            return false;
        }
    }
}