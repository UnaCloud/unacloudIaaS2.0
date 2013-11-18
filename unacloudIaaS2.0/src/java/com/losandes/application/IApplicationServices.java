package com.losandes.application;

import javax.ejb.Local;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Interface responsible for exposing the Application services
 */
@Local
public interface IApplicationServices {

    /**
     * Creates a new application entity
     * @param application
     * @return
     */
    public boolean createApplication(com.losandes.persistence.entity.Application application);

    /**
     * Updates the given entity into the system
     * @param application
     * @return
     */
    public boolean updateApplication(com.losandes.persistence.entity.Application application);

    /**
     * Deletes the application with the given id
     * @param code
     * @return
     */
    public boolean deleteApplication(int code);

    /**
     * Search an application given its id code
     * @param applicationID
     * @return
     */
    public com.losandes.persistence.entity.Application getApplicationByID(int applicationID);

    /**
     * Returns all applications
     * @return
     */
    public java.util.List getApplications();

    /**
     * Return the applications of a template
     * @param templateSelected
     * @return
     */
    public java.util.List getAvailableApplications(int templateSelected);
}// end of IApplicationServices

