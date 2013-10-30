/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bi;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author nayzo
 */
public class ConnexionXML {
    
    String[] brch = {"mathemathique", "informatique", "science", "technique", "economie", "lettre", "sport"};
    
    public void writeIntoXML(int nbreLigne) {
        try {
 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// element racine
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("BDtransactionnelle");
		doc.appendChild(rootElement);
 
                // element etudiant
                Element etudiant = doc.createElement("etudiant");
                rootElement.appendChild(etudiant);
                
                // element etudiant
                Element notes = doc.createElement("notes");
                rootElement.appendChild(notes);
                
                // element branche
                Element branche = doc.createElement("branche");
                rootElement.appendChild(branche);

                    // element etnd
                    for(int i=1; i<=nbreLigne; i++) {
                        Element etnd = doc.createElement("etnd");
                        //etnd.appendChild(doc.createTextNode("valeur txt"));                
                        etnd.setAttribute("id", ""+i+"");
                        etnd.setAttribute("nom", "Riahi");
                        etnd.setAttribute("prenom", "Walid"); 
                        etnd.setAttribute("date_naissance", ""+Constante.getRandomNumber(1950, 2000)+"/"+Constante.getRandomNumber(1, 12)+"/"+Constante.getRandomNumber(1, 28)+"");
                        etnd.setAttribute("branche_id", ""+Constante.getRandomNumber(1, 7)+"");
                        etudiant.appendChild(etnd);
                    }
            
                
                // element nt
                    for(int i=1; i<=nbreLigne; i++) {
                        Element nt = doc.createElement("nt");              
                        nt.setAttribute("id", ""+i+"");
                        nt.setAttribute("trimestre1", ""+Constante.getRandomNumber(0,20)+"");
                        nt.setAttribute("trimestre2", ""+Constante.getRandomNumber(0,20)+"");
                        nt.setAttribute("trimestre3", ""+Constante.getRandomNumber(0,20)+"");
                        nt.setAttribute("etudiant_id", ""+i+"");
                        notes.appendChild(nt);
                    }
                    
                 // element bre
                    for(int i=1; i<=7; i++) {
                        Element bre = doc.createElement("bre");   
                        bre.setAttribute("id", ""+i+"");
                        bre.setAttribute("branche", ""+brch[i-1]+""); 
                        branche.appendChild(bre);
                    }
                        
		// l'enregistrement dans le fichier XML
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("BDtransactionnelle.xml"));
 
		transformer.transform(source, result);
 
		System.out.println("Fichier XML enregistré avec succès");
 
	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
    }
    
}
