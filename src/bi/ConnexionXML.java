/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bi;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author nayzo
 */
public class ConnexionXML {
    
    private static String[] branches = {"mathemathique", "informatique", "science", "technique", "economie", "lettre", "sport"};
    
    public static boolean writeIntoXML(int nbreLigne) {
        
        try {    
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // element racine
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("BDtransactionnelle");
            doc.appendChild(rootElement);

            // element branche
            Element branche = doc.createElement("branche");
            rootElement.appendChild(branche);

            // element etudiant
            Element etudiant = doc.createElement("etudiant");
            rootElement.appendChild(etudiant);

            // element notes
            Element notes = doc.createElement("notes");
            rootElement.appendChild(notes);             
            
            // element bre
            for(int i=1 ; i<=7 ; i++) {
                Element bre = doc.createElement("bre");   
                bre.setAttribute("id", "" + i + "");
                bre.setAttribute("branche", "" + branches[i-1] + ""); 
                branche.appendChild(bre);
            }
            
            // element etnd
            for(int i=1 ; i<=nbreLigne ; i++) {
                Element etnd = doc.createElement("etnd");
                //etnd.appendChild(doc.createTextNode("valeur txt"));
                etnd.setAttribute("id", "" + i + "");
                etnd.setAttribute("nom", "Riahi");
                etnd.setAttribute("prenom", "Walid");
                etnd.setAttribute("date_naissance", "" + Constante.getRandomNumber(1950, 2000) + "/" + Constante.getRandomNumber(1, 12) + "/" + Constante.getRandomNumber(1, 28) + "");
                etnd.setAttribute("branche_id", "" + Constante.getRandomNumber(1, 7) + "");
                etudiant.appendChild(etnd);
            }
            
            // element nt
            for(int i=1 ; i<=nbreLigne ; i++) {
                Element nt = doc.createElement("nt");
                nt.setAttribute("id", "" + i + "");
                nt.setAttribute("trimestre1", "" + Constante.getRandomNumber(0,20) + "");
                nt.setAttribute("trimestre2", "" + Constante.getRandomNumber(0,20) + "");
                nt.setAttribute("trimestre3", "" + Constante.getRandomNumber(0,20) + "");
                nt.setAttribute("etudiant_id", "" + i + "");
                notes.appendChild(nt);
            }
            
            // l'enregistrement dans le fichier XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/bi/BDtransactionnelle.xml"));
            
            transformer.transform(source, result);
            System.out.println("Fichier XML enregistré avec succès");
            return true;
        }
        catch (ParserConfigurationException | TransformerException ex) {
            System.out.println("XML Error: " + ex.getClass() + "\nCause: " + ex.getCause() + "\nMEssage: " + ex.getMessage());
            return false;
        }
    }
    
    public static boolean deleteXML() {
        
        try{
            File file = new File("src/bi/BDtransactionnelle.xml");
            if(file.delete()) {
                System.out.println(file.getName() + " est Supprimé avec succès!");
                file.createNewFile();
                return true;
            }
            else {
                System.out.println("Suppression échoué!");
                return false;
            }
    	}
        catch(Exception ex) {
            System.out.println("XML Error: " + ex.getClass() + "\nCause: " + ex.getCause() + "\nMEssage: " + ex.getMessage());
            return false;
        }
    }
}
