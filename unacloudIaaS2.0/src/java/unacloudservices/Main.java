/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unacloudservices;

import ServerUpdateListener.ServerListener;
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
        new Thread(){
            @Override
            public void run() {
                domain.Main.main(args);
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                cloudclientversionmanager.Main.main(args);
            }
        }.start();
        new ServerListener().start();
    }
}