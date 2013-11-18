package com.losandes.persistence.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the System User entity
 */
@Entity
@NamedQueries({@NamedQuery(name = "SystemUser.findBySystemUserName", query = "SELECT su FROM SystemUser su WHERE su.systemUserName = :systemUserName")})
public class SystemUser implements Serializable {

    @Id
    private String systemUserName;
    private String systemUserEmail;
    private String systemUserPassword;
    @ManyToMany()
    private List<Template> templates;
    @ManyToMany()
    @JoinColumn(table="systemuser_systemusertype",name="systemUserName")
    private List<SystemUserType> userGroups;

    /**
     * @return the systemUserName
     */
    public String getSystemUserName() {
        return systemUserName;
    }

    /**
     * @param systemUserName the systemUserName to set
     */
    public void setSystemUserName(String systemUserName) {
        this.systemUserName = systemUserName;
    }

    /**
     * @return the templates
     */
    public List<Template> getTemplates() {
        return templates;
    }

    /**
     * @param templates the templates to set
     */
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public String getSystemUserEmail() {
        return systemUserEmail;
    }

    public void setSystemUserEmail(String systemUserEmail) {
        this.systemUserEmail = systemUserEmail;
    }

    public String getSystemUserPassword() {
        return systemUserPassword;
    }

    public void setSystemUserPassword(String systemUserPassword) {
        this.systemUserPassword = systemUserPassword;
    }

    public List<SystemUserType> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<SystemUserType> userGroups) {
        this.userGroups = userGroups;
    }

    
    
}//end of SystemUser
