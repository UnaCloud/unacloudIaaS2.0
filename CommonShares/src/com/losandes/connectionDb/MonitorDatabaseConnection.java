/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.connectionDb;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 *
 * @author cesar
 */
public abstract class MonitorDatabaseConnection {	
	
	 protected String ip;
	 protected int port;
	 protected String name;
	 protected String user;
	 protected String password;
	 
	 /**
	 * Class Constructor
	 */
    public MonitorDatabaseConnection() {
    	callVariables();    	
    }
    /**
     * This method should be implemented in each factor to colaborate  
     */
    public abstract void callVariables();

	/**
     * Connects to monitoring database
     * @return connection done
     * @throws UnknownHostException if connection was not possible
     */
    public MongoConnection generateConnection() throws UnknownHostException {
    	MongoClient conexion ;      
        MongoCredential credential = MongoCredential.createCredential(user, name, password.toCharArray());
        ServerAddress address = new ServerAddress(ip, port);
        conexion = new MongoClient(address, Arrays.asList(credential));
        MongoConnection con = new MongoConnection(conexion,name);
        return con;
    }
//    public static void main(String[] args) throws UnknownHostException {
//    	MongoClient conexion ;
////        String ip = VariableManager.global.getStringValue("MONITORING_SERVER_IP");
////        int port = VariableManager.global.getIntValue("MONITORING_SERVER_PORT");
////        String name = VariableManager.global.getStringValue("MONITORING_DATABASE_NAME");
////        String user = VariableManager.global.getStringValue("MONITORING_DATABASE_USER");
////        String password = VariableManager.global.getStringValue("MONITORING_DATABASE_PASSWORD");
//    	  String ip = "172.24.98.119";
//          int port = 27017;
//          String name = "cloudMongo";
//          String user = "cloudmonitoreo";
//          String password = "cloudmonitoreo$#";
//        MongoCredential credential = MongoCredential.createCredential(user, name, password.toCharArray());
//        ServerAddress address = new ServerAddress(ip, port);
//        conexion = new MongoClient(ip,port);
//        System.out.println(conexion);
//       // MongoConnection con = new MongoConnection(conexion,name);
//        DB db = conexion.getDB("otra");
//        System.out.println(db.getCollection("Test").insert(new BasicDBObject("test","uno")));
//        
//        System.out.println(db);
//	}
}
