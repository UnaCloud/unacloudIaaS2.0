/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author G
 */
public class DatabaseConnection {
    private static DataSource ds;
    public static Connection getConnection()throws SQLException{
        return getConnection(true);
    }
    
    public static Connection getConnection(boolean autoComit)throws SQLException{
        Connection ret=DriverManager.getConnection("jdbc:mysql://localhost:3306/clouder","unacloud","unac10ud");
        ret.setAutoCommit(autoComit);
        return ret;
    }
}
