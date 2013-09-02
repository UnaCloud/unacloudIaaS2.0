/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class responsible for manage system variables and stores its values
 * @author Clouder
 */
public class VariableManager {

    /**
     * map used to save variables values. It is used to improve response time
     */
    private final static Map<String, Object> map = new TreeMap<String, Object>() {
        @Override
        public Object put(String key, Object value) {
            key = key.replace("=", "");
            Object c = super.put(key, value);
            saveChanges();
            return c;
        }
    };
    /**
     * File used to persist variable values
     */
    private static File fileVars;

    /**
     * Return the value of the string variable with the given key.
     * @param key The key that identify the variable
     * @return The value of the variable or null if there is no value for the given key
     */
    public static synchronized String getStringValue(String key) {
        return (String) map.get("String." + key);
    }

    public static synchronized void setStringValue(String key, String v) {
        map.put("String." + key, v);
    }

    /**
     * Return the value of the int variable with the given key.
     * @param key The key that identify the variable
     * @return The value of the variable or null if there is no value for the given key
     */
    public static synchronized int getIntValue(String key) {
        return (Integer) map.get("Integer." + key);
    }

    /**
     *
     * @param key
     * @param v
     */
    public static synchronized void setIntValue(String key, int v) {
        map.put("Integer." + key, v);
    }

    /**
     * Merges the state of this variable manager with the given map
     * @param temp
     */
    public static synchronized void mergeValues(Map<String, String> temp) {
        for (Map.Entry<String, String> e : temp.entrySet()) {
            map.put(e.getKey(), e.getValue());
        }
        saveChanges();
    }

    /**
     * Stores the variables on the storage file.
     */
    private static void saveChanges() {
        try {
            PrintWriter pw = new PrintWriter(fileVars);
            for (Map.Entry<String, Object> e : map.entrySet()) {
                pw.println(e.getKey() + "=" + e.getValue());
            }
            pw.close();
        } catch (Exception e) {
        }
    }

    /**
     * Inits this variable manager using the given file to manage its variables.
     * @param varsPath
     */
    public static void init(String varsPath) {
        if (fileVars == null) {
            fileVars = new File(varsPath);
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileVars));
                for (String h, j[]; (h = br.readLine()) != null;) {
                    System.out.println(h);
                    j = h.split("=");
                    if (j[0].startsWith("String.")) {
                        map.put(j[0], j[1]);
                    } else if (j[0].startsWith("Integer.")) {
                        map.put(j[0], Integer.parseInt(j[1]));
                    }
                }
                br.close();
            } catch (Exception e) {
                System.err.println("El archivo vars no existe");
            }
        }

    }
}
