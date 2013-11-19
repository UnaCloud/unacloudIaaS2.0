/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerUpdateListener;

import cloudclientversionmanager.VersionManager;
import com.losandes.utils.VariableManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Clouder
 */
public class ServerListener extends Thread {

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(2563);
            while (true) {
                Socket s = ss.accept();
                System.out.println("Aceptó");
                try {
                    System.out.println(""+s.getInetAddress().getHostAddress());
                    if (s.getInetAddress().isLoopbackAddress()) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        PrintWriter pw = new PrintWriter(s.getOutputStream(),true);
                        String h=br.readLine();
                        System.out.println(h);
                        if(h!=null&&h.equals("UPDATE_VARIABLES")){
                            Map<String,String> vars = new TreeMap<String, String>();
                            for(String j[];(h=br.readLine())!=null;){
                                System.out.println(h);
                                if(h.equals("-1"))break;
                                j=h.split("=");
                                vars.put(j[0],j[1]);
                            }
                            br.close();
                            VariableManager.mergeValues(vars);
                            VersionManager.updateVersion();
                            s.close();
                            System.out.println("exit(0)");
                            System.exit(0);
                        }else if(h!=null&&h.equals("UPDATE_KEYS")){
                            String pubMod=br.readLine();
                            String pubExp=br.readLine();
                            String priMod=br.readLine();
                            String priExp=br.readLine();
                            if(pubMod!=null&&pubExp!=null&&priExp!=null&&priMod!=null){
                            	//TODO rehacer el esquema de seguridad
                                /*SecureServerStream.setKeys(priMod,priExp);
                                SecureClientStream.setKeys(pubMod,pubExp);*/
                                pw.println("true");
                                System.out.println("exit(0)");
                                System.exit(0);
                            }else{
                                pw.println("false");
                            }
                        }
                    }
                    s.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}