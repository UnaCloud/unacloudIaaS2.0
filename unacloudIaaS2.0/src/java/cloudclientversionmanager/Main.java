/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cloudclientversionmanager;

import com.losandes.utils.VariableManager;
import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Clouder
 */
public class Main {

    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(VariableManager.getIntValue("VERSION_MANAGER_PORT"));
            while(true)new Attender(ss.accept());
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }



    

    
}
