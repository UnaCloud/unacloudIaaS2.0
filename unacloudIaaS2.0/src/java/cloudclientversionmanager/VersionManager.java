/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cloudclientversionmanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 *
 * @author Clouder
 */
public class VersionManager {

    public static void updateVersion(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("version.txt"));
            String v = br.readLine();
            System.out.println(v);
            br.close();
            char[] vArray = v.toCharArray();
            int e=vArray.length-1,i;
            while(!Character.isDigit(vArray[e]))e--;
            i=e;
            while(Character.isDigit(vArray[i]))i--;
            int t=Integer.parseInt(v.substring(i+1,e+1));
            t++;
            PrintWriter pw = new PrintWriter("version.txt");
            pw.println(v.subSequence(0,i+1)+""+t+""+v.substring(e+1));
            pw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
