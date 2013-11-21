/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unacloudservices;

import com.losandes.utils.VariableManager;

/**
 *
 * @author Clouder
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        VariableManager.init("./version/vars");
        domain.Main.main(args);
    }
}