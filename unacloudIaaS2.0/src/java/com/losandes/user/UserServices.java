package com.losandes.user;

import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.Laboratory;
import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.persistence.entity.SystemUser;
import com.losandes.persistence.entity.SystemUserType;
import com.losandes.physicalmachine.IPhysicalMachineServices;
import static com.losandes.utils.Constants.*;
import com.losandes.utils.Queries;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.EJB;
import javax.ejb.Stateful;


/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the System User persistence services
 */
@Stateful
public class UserServices implements IUserServices {

    @EJB
    private IPersistenceServices persistenceServices;
    @EJB
    private IPhysicalMachineServices physicalMachineServices;

    /**
     * Responsible for exposing the System User create persistence services
     */
    public boolean createUser(SystemUser user) {
        return persistenceServices.create(user);
    }

    /**
     * Responsible for exposing the System User update persistence services
     */
    public boolean updateUser(SystemUser user) {
        return persistenceServices.update(user)!=null;
    }

    /**
     * Responsible for exposing the System User delete persistence services
     */
    public boolean deleteUser(String code) {
        SystemUser user = getUserByID(code);
        return persistenceServices.delete(user);
    }

    /**
     * Responsible for exposing the System User query by id
     */
    public SystemUser getUserByID(String systemUserID) {
        if(systemUserID==null)return null;
        return (SystemUser) persistenceServices.findById(SystemUser.class, systemUserID);
    }

    /**
     * Responsible for exposing all the System User types avaliable
     */
    public List<SystemUserType> getUsersTypes() {
        return persistenceServices.findAll(SystemUserType.class);
    }

    /**
     * Responsible for exposing all the System Uses avaliable
     */
    public List getUsers() {
        return persistenceServices.findAll(SystemUser.class);
    }

    @Override
    public boolean validateUser(String username, String passwd) {
        if(username==null||passwd==null)return false;
        SystemUser us=getUserByID(username);
        return us!=null&&us.getSystemUserPassword()!=null&&us.getSystemUserPassword().equals(passwd);
    }


}//end of UserServices
