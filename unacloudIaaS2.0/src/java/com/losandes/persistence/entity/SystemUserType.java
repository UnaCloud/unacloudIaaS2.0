package com.losandes.persistence.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the System User Type entity
 */
@Entity
@NamedQueries({@NamedQuery(name = "SystemUser.findBySystemUserTypeName", query = "SELECT su FROM SystemUserType su WHERE su.systemUserTypeName = :systemUserTypeName")})
public class SystemUserType implements Serializable {

    @Id
    private String systemUserTypeName;

    /**
     * @return the systemUserTypeName
     */
    public String getSystemUserTypeName() {
        return systemUserTypeName;
    }

    /**
     * @param systemUserTypeName the systemUserTypeName to set
     */
    public void setSystemUserTypeName(String systemUserTypeName) {
        this.systemUserTypeName = systemUserTypeName;
    }
}//end of SystemUserType
