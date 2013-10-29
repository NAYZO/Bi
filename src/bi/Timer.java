/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bi;

/**
 *
 * @author Hamza
 */
public class Timer {

    private long timeBench;
    
    public void startTimer() {
        
        this.timeBench = System.currentTimeMillis();        
    }
    
    public void showTimer() {
        
        System.out.println();
        System.out.println("Temps d'execution totale est : " + (System.currentTimeMillis()-this.timeBench) + " ms");
        System.out.println();
    }
    
}
