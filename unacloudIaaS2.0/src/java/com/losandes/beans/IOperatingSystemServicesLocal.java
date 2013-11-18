/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.beans;

import java.util.*;
import com.losandes.persistence.entity.*;
import javax.ejb.Local;

/**
 *
 * @author Clouder
 */
@Local
public interface IOperatingSystemServicesLocal {

    public boolean createOperatingSystem(OperatingSystem operatingSystem);

    public boolean updateOperatingSystem(OperatingSystem operatingSystem);

    public boolean deleteOperatingSystem(int code);

    public List getOperatingSystems();

    public List<OperatingSystemType> getOperatingSystemTypes();

    public List getAvailableOperatingSystemTypes();

    public List getAvailableOperatingSystems(int selectedOperatingSystemType);

    public OperatingSystemType getOperatingSystemTypeByID(int operatingSystemTypeID);

    public OperatingSystem getOperatingSystemByID(int operatingSystemID);

    public List getGridAvailableOperatingSystemTypes(String systemUserName);

    public List getGridAvailableOperatingSystems(int selectedOperatingSystemType, String systemUserName);

}
