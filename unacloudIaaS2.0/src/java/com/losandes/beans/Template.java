/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.beans;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Clouder
 */
@Entity
@Table(name = "template")
@NamedQueries({
    @NamedQuery(name = "Template.findAll", query = "SELECT t FROM Template t"),
    @NamedQuery(name = "Template.findByTemplatecode", query = "SELECT t FROM Template t WHERE t.templatecode = :templatecode"),
    @NamedQuery(name = "Template.findByCustomizable", query = "SELECT t FROM Template t WHERE t.customizable = :customizable"),
    @NamedQuery(name = "Template.findByVmxfilelocation", query = "SELECT t FROM Template t WHERE t.vmxfilelocation = :vmxfilelocation"),
    @NamedQuery(name = "Template.findByTemplatename", query = "SELECT t FROM Template t WHERE t.templatename = :templatename"),
    @NamedQuery(name = "Template.findByHighavailability", query = "SELECT t FROM Template t WHERE t.highavailability = :highavailability")})
public class Template implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TEMPLATECODE")
    private Integer templatecode;
    @Column(name = "CUSTOMIZABLE")
    private Boolean customizable;
    @Column(name = "VMXFILELOCATION")
    private String vmxfilelocation;
    @Column(name = "TEMPLATENAME")
    private String templatename;
    @Column(name = "HIGHAVAILABILITY")
    private Boolean highavailability;
    @JoinTable(name = "template_application", joinColumns = {
        @JoinColumn(name = "templates_TEMPLATECODE", referencedColumnName = "TEMPLATECODE")}, inverseJoinColumns = {
        @JoinColumn(name = "applications_APPLICATIONCODE", referencedColumnName = "APPLICATIONCODE")})
    @ManyToMany
    private Collection<Application> applicationCollection;
    @ManyToMany(mappedBy = "templateCollection")
    private Collection<Systemuser> systemuserCollection;
    @OneToMany(mappedBy = "template")
    private Collection<Virtualmachine> virtualmachineCollection;
    @JoinColumn(name = "TEMPLATETYPE_TEMPLATETYPECODE", referencedColumnName = "TEMPLATETYPECODE")
    @ManyToOne
    private Templatetype templatetype;
    @JoinColumn(name = "OPERATINGSYSTEM_OPERATINGSYSTEMCODE", referencedColumnName = "OPERATINGSYSTEMCODE")
    @ManyToOne
    private Operatingsystem operatingsystem;
    @JoinColumn(name = "ELASTICRULE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Elasticrule elasticrule;
    @OneToMany(mappedBy = "template")
    private Collection<Virtualmachineexecution> virtualmachineexecutionCollection;

    public Template() {
    }

    public Template(Integer templatecode) {
        this.templatecode = templatecode;
    }

    public Integer getTemplatecode() {
        return templatecode;
    }

    public void setTemplatecode(Integer templatecode) {
        this.templatecode = templatecode;
    }

    public Boolean getCustomizable() {
        return customizable;
    }

    public void setCustomizable(Boolean customizable) {
        this.customizable = customizable;
    }

    public String getVmxfilelocation() {
        return vmxfilelocation;
    }

    public void setVmxfilelocation(String vmxfilelocation) {
        this.vmxfilelocation = vmxfilelocation;
    }

    public String getTemplatename() {
        return templatename;
    }

    public void setTemplatename(String templatename) {
        this.templatename = templatename;
    }

    public Boolean getHighavailability() {
        return highavailability;
    }

    public void setHighavailability(Boolean highavailability) {
        this.highavailability = highavailability;
    }

    public Collection<Application> getApplicationCollection() {
        return applicationCollection;
    }

    public void setApplicationCollection(Collection<Application> applicationCollection) {
        this.applicationCollection = applicationCollection;
    }

    public Collection<Systemuser> getSystemuserCollection() {
        return systemuserCollection;
    }

    public void setSystemuserCollection(Collection<Systemuser> systemuserCollection) {
        this.systemuserCollection = systemuserCollection;
    }

    public Collection<Virtualmachine> getVirtualmachineCollection() {
        return virtualmachineCollection;
    }

    public void setVirtualmachineCollection(Collection<Virtualmachine> virtualmachineCollection) {
        this.virtualmachineCollection = virtualmachineCollection;
    }

    public Templatetype getTemplatetype() {
        return templatetype;
    }

    public void setTemplatetype(Templatetype templatetype) {
        this.templatetype = templatetype;
    }

    public Operatingsystem getOperatingsystem() {
        return operatingsystem;
    }

    public void setOperatingsystem(Operatingsystem operatingsystem) {
        this.operatingsystem = operatingsystem;
    }

    public Elasticrule getElasticrule() {
        return elasticrule;
    }

    public void setElasticrule(Elasticrule elasticrule) {
        this.elasticrule = elasticrule;
    }

    public Collection<Virtualmachineexecution> getVirtualmachineexecutionCollection() {
        return virtualmachineexecutionCollection;
    }

    public void setVirtualmachineexecutionCollection(Collection<Virtualmachineexecution> virtualmachineexecutionCollection) {
        this.virtualmachineexecutionCollection = virtualmachineexecutionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (templatecode != null ? templatecode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Template)) {
            return false;
        }
        Template other = (Template) object;
        if ((this.templatecode == null && other.templatecode != null) || (this.templatecode != null && !this.templatecode.equals(other.templatecode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Template[templatecode=" + templatecode + "]";
    }

}
