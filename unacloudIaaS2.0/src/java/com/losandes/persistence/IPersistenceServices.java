package com.losandes.persistence;

import java.security.KeyPair;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Interface responsible for exposing all the Persistence services
 */
@Local
public interface IPersistenceServices {

    public boolean createorUpdate(Object obj);

    public boolean create(java.lang.Object obj);

    public Object update(java.lang.Object obj);

    public boolean delete(java.lang.Object obj);

    public java.util.List findAll(Class c);

    public java.lang.Object findById(java.lang.Class c, java.lang.Object id);

    public java.lang.Object findByName(Class c,java.lang.Object name);

    public List executeNativeSQLList(java.lang.String sentence, java.lang.Class c);

    public String executeSQLString(java.lang.String sentence);

    public String getProperty(String key);

    public void setProperty(String key,String value);

    public void setProperties(Map<String,String> properties);

    public String getStringValue(String key);

    public int getIntValue(String key);

}//end of IPersistenceServices

