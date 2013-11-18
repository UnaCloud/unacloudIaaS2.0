/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.beans;

import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.*;
import com.losandes.utils.Queries;
import javax.ejb.Stateless;
import java.util.*;
import javax.ejb.EJB;

/**
 *
 * @author Clouder
 */
@Stateless
public class IOperatingSystemServices implements IOperatingSystemServicesLocal {
    
    @EJB
    private IPersistenceServices persistenceServices;

    /**
     * Responsible for exposing the Operating System create persistence services
     */
    @Override
    public boolean createOperatingSystem(OperatingSystem operatingSystem) {
        return persistenceServices.create(operatingSystem);
    }

    /**
     * Responsible for exposing the Operating System update persistence services
     */
    @Override
    public boolean updateOperatingSystem(OperatingSystem operatingSystem) {
        return persistenceServices.update(operatingSystem)!=null;
    }

    /**
     * Responsible for exposing the Operating System delete persistence services
     */
    @Override
    public boolean deleteOperatingSystem(int code) {
        OperatingSystem operatingSystem = getOperatingSystemByID(code);
        return persistenceServices.delete(operatingSystem);
    }

    /**
     * Responsible for exposing the Operating System query by id
     */
    @Override
    public OperatingSystem getOperatingSystemByID(int operatingSystemID) {
        return (OperatingSystem) persistenceServices.findById(OperatingSystem.class, operatingSystemID);
    }

    /**
     * Responsible for exposing the Operating System Type query by id
     */
    @Override
    public OperatingSystemType getOperatingSystemTypeByID(int operatingSystemTypeID) {
        return (OperatingSystemType) persistenceServices.findById(OperatingSystemType.class, operatingSystemTypeID);
    }

    /**
     * Responsible for exposing all Operating System Types available
     */
    @Override
    public List getOperatingSystemTypes() {
        return persistenceServices.findAll(OperatingSystemType.class);
    }

    /**
     * Responsible for exposing all Operating Systems available
     */
    @Override
    public List getOperatingSystems() {
        return persistenceServices.findAll(OperatingSystem.class);
    }

    /**
     * Responsible for exposing all Operating Systems available for an Operating System Type
     */
    @Override
    public List getAvailableOperatingSystems(int selectedOperatingSystemType) {
        return persistenceServices.executeNativeSQLList(Queries.getOperatingSystemAvailable(selectedOperatingSystemType), OperatingSystem.class);
    }

    /**
     * Responsible for exposing all Operating Systems Types available for execution
     */
    @Override
    public List getAvailableOperatingSystemTypes() {
        return persistenceServices.executeNativeSQLList(Queries.getOperatingSystemTypeAvailable(), OperatingSystemType.class);
    }

    /**
     * Responsible for exposing all Grid Operating Systems available for execution
     */
    @Override
    public List getGridAvailableOperatingSystems(int selectedOperatingSystemType, String systemUserName) {
        return persistenceServices.executeNativeSQLList(Queries.getGridOperatingSystemAvailable(selectedOperatingSystemType, systemUserName), OperatingSystem.class);
    }

    /**
     * Responsible for exposing all Grid Operating System Types available for execution
     */
    @Override
    public List getGridAvailableOperatingSystemTypes(String systemUserName) {
        return persistenceServices.executeNativeSQLList(Queries.getGridOperatingSystemTypeAvailable(systemUserName), OperatingSystemType.class);
    }
 
}
