package com.losandes.persistence.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Application entity
 */
@Entity
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applicationCode;
    private String applicationName;
    @ManyToMany(mappedBy = "applications")
    private List<Template> templates;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getApplicationCode() != null ? getApplicationCode().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.Application[id=" + getApplicationCode() + "]";
    }

    /**
     * @return the applicationCode
     */
    public Integer getApplicationCode() {
        return applicationCode;
    }

    /**
     * @param applicationCode the applicationCode to set
     */
    public void setApplicationCode(Integer applicationCode) {
        this.applicationCode = applicationCode;
    }

    /**
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @param applicationName the applicationName to set
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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
}// end of Application

