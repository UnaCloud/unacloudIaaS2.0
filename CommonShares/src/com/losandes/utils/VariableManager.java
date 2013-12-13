package com.losandes.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class responsible for manage system variables and stores its values
 */
public class VariableManager {
	/**
     * File used to persist variable values
     */
    private static File globalConfig=new File("vars");
    
    /**
     * map used to save variables values. It is used to improve response time
     */
    private final static Map<String, Object> globalMap = new TreeMap<String, Object>();
    
    /**
     * Return the value of the string variable with the given key.
     * @param key The key that identify the variable
     * @return The value of the variable or null if there is no value for the given key
     */
    public static synchronized String getStringValue(String key) {
        return (String) globalMap.get("String." + key);
    }

    public static synchronized void setStringValue(String key, String v) {
    	globalMap.put("String." + key, v);
    	saveChanges();
    }

    /**
     * Return the value of the int variable with the given key.
     * @param key The key that identify the variable
     * @return The value of the variable or null if there is no value for the given key
     */
    public static synchronized int getIntValue(String key) {
        return (Integer) globalMap.get("Integer." + key);
    }

    public static synchronized void setIntValue(String key, int v) {
    	globalMap.put("Integer." + key, v);
    	saveChanges();
    }

    /**
     * Stores the variables on the storage file.
     */
    private static void saveChanges() {
        try(PrintWriter pw = new PrintWriter(globalConfig)){
            for (Map.Entry<String, Object> e : globalMap.entrySet())pw.println(e.getKey() + "=" + e.getValue());
        } catch (Exception e) {
        }
    }

    /**
     * Inits this variable manager using the given file to manage its variables.
     * @param varsPath
     */
    public static void init() {
        try(BufferedReader br = new BufferedReader(new FileReader(globalConfig));) {
            for (String h; (h = br.readLine()) != null;)processLine(globalMap,h);
        } catch (Exception e) {
            System.err.println("El archivo vars no existe");
        }
    }
    private static void processLine(Map<String,Object> map,String line){
    	String[] j = line.split("=");
        if (j[0].startsWith("String."))map.put(j[0], j[1]);
        else if (j[0].startsWith("Integer."))map.put(j[0], Integer.parseInt(j[1]));
    }
}
