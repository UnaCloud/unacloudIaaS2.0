/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.utils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Clouder
 */
public class Log {

    public static void print(String msg){
        try{
            /*Socket s = new Socket("unacloud.uniandes.edu.co",VariableManager.getIntValue("LOG_SOCKET"));
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println(executeCommandOutput("hostname", true).trim()+" : "+msg);
            pw.flush();
            pw.close();
            s.close();*/
        }catch(Exception e){

        }
    }

    public static void print2(String msg){
        try{
            PrintWriter pw = new PrintWriter(new FileWriter("LocalExecutionsLog.txt",true));
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            pw.println(dateFormat.format(date)+" "+msg);
            pw.close();
        }catch(Throwable a){}

    }
}
