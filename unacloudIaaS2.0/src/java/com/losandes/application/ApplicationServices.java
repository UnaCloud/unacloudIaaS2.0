package com.losandes.application;

import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.Application;
import com.losandes.utils.Queries;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the Application persistence services
 */
@Stateless
public class ApplicationServices implements IApplicationServices {

    @EJB
    private IPersistenceServices persistenceServices;

    /**
     * Responsible for exposing the Application create persistence services
     */
    public boolean createApplication(Application application) {
        return persistenceServices.create(application);
    }

    /**
     * Responsible for exposing the Application update persistence services
     */
    public boolean updateApplication(Application application) {
        return persistenceServices.update(application)!=null;
    }

    /**
     * Responsible for exposing the Application delete persistence services
     */
    public boolean deleteApplication(int code) {
        Application application = getApplicationByID(code);
        return persistenceServices.delete(application);
    }

    /**
     * Responsible for exposing the Application query by id
     */
    public Application getApplicationByID(int applicationID) {
        return (Application) persistenceServices.findById(Application.class, applicationID);
    }

    /**
     * Responsible for exposing all the Application available
     */
    public List getApplications() {
        return persistenceServices.findAll(Application.class);
    }

    /**
     * Responsible for exposing the Application available in a template
     */
    public List getAvailableApplications(int templateSelected) {
        return persistenceServices.executeNativeSQLList(Queries.getApplicationsAvailables(templateSelected), Application.class);
    }
}//end of ApplicationServices

