package com.losandes.user;

import javax.ejb.Local;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Interface responsible for exposing the System User services
 */
@Local
public interface IUserServices {

    public boolean updateUser(com.losandes.persistence.entity.SystemUser user);

    public com.losandes.persistence.entity.SystemUser getUserByID(String systemUserID);

    public java.util.List getUsersTypes();

    public java.util.List getUsers();

    public boolean createUser(com.losandes.persistence.entity.SystemUser user);

    public boolean deleteUser(String code);

    public boolean validateUser(String username, String passwd);

}// end of IUserServices
