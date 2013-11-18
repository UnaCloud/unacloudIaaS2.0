package com.losandes.persistence.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Template entity
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Template.findByTemplateName", query = "SELECT su FROM Template su WHERE su.templateName = :templateName")})
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int templateCode;
    private String templateName;
    @Transient
    private boolean templateEnable;
    @ManyToOne
    private TemplateType templateType;
    @OneToOne
    private OperatingSystem operatingSystem;
    @ManyToMany
    private List<Application> applications;
    @ManyToMany(mappedBy = "templates", cascade = CascadeType.PERSIST)
    private List<SystemUser> systemUser;
    private String vmxFileLocation;
    private boolean customizable;
    private boolean highAvailability;
    @OneToOne(cascade=CascadeType.ALL)
    private ElasticRule elasticRule;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getTemplateCode() != null ? getTemplateCode().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.Template[id=" + getTemplateCode() + "]";
    }

    /**
     * @return the templateCode
     */
    public Integer getTemplateCode() {
        return templateCode;
    }

    /**
     * @param templateCode the templateCode to set
     */
    public void setTemplateCode(Integer TemplateCode) {
        this.templateCode = TemplateCode;
    }

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return the operatingSystem
     */
    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * @param operatingSystem the operatingSystem to set
     */
    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    /**
     * @return the templateType
     */
    public TemplateType getTemplateType() {
        return templateType;
    }

    /**
     * @param templateType the templateType to set
     */
    public void setTemplateType(TemplateType template) {
        this.templateType = template;
    }

    /**
     * @return the applications
     */
    public List<Application> getApplications() {
        return applications;
    }

    /**
     * @param applications the applications to set
     */
    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    /**
     * @return the templateEnable
     */
    public boolean isTemplateEnable() {
        return templateEnable;
    }

    /**
     * @param templateEnable the templateEnable to set
     */
    public void setTemplateEnable(boolean templateEnable) {
        this.templateEnable = templateEnable;
    }

    /**
     * @return the systemUser
     */
    public List<SystemUser> getSystemUser() {
        return systemUser;
    }

    /**
     * @param systemUser the systemUser to set
     */
    public void setSystemUser(List<SystemUser> systemUser) {
        this.systemUser = systemUser;
    }

    public String getVmxFileLocation() {
        return vmxFileLocation;
    }

    public void setVmxFileLocation(String vmxFileLocation) {
        this.vmxFileLocation = vmxFileLocation;
    }

    public boolean isCustomizable() {
        return customizable;
    }

    public void setCustomizable(boolean customizable) {
        this.customizable = customizable;
    }

    public boolean isHighAvailability() {
        return highAvailability;
    }

    public void setHighAvailability(boolean highAvailability) {
        this.highAvailability = highAvailability;
    }

    public ElasticRule getElasticRule() {
        return elasticRule;
    }

    public void setElasticRule(ElasticRule elasticRule) {
        this.elasticRule = elasticRule;
    }

    
}//end of Template

