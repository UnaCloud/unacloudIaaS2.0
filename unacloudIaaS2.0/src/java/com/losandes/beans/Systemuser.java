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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Clouder
 */
@Entity
@Table(name = "systemuser")
@NamedQueries({
    @NamedQuery(name = "Systemuser.findAll", query = "SELECT s FROM Systemuser s"),
    @NamedQuery(name = "Systemuser.findBySystemusername", query = "SELECT s FROM Systemuser s WHERE s.systemusername = :systemusername"),
    @NamedQuery(name = "Systemuser.findBySystemuseremail", query = "SELECT s FROM Systemuser s WHERE s.systemuseremail = :systemuseremail"),
    @NamedQuery(name = "Systemuser.findBySystemuserpassword", query = "SELECT s FROM Systemuser s WHERE s.systemuserpassword = :systemuserpassword")})
public class Systemuser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SYSTEMUSERNAME")
    private String systemusername;
    @Column(name = "SYSTEMUSEREMAIL")
    private String systemuseremail;
    @Column(name = "SYSTEMUSERPASSWORD")
    private String systemuserpassword;
    @ManyToMany(mappedBy = "systemuserCollection")
    private Collection<Systemusertype> systemusertypeCollection;
    @JoinTable(name = "systemuser_template", joinColumns = {
        @JoinColumn(name = "systemUser_SYSTEMUSERNAME", referencedColumnName = "SYSTEMUSERNAME")}, inverseJoinColumns = {
        @JoinColumn(name = "templates_TEMPLATECODE", referencedColumnName = "TEMPLATECODE")})
    @ManyToMany
    private Collection<Template> templateCollection;
    @OneToMany(mappedBy = "systemuser")
    private Collection<Virtualmachineexecution> virtualmachineexecutionCollection;

    public Systemuser() {
    }

    public Systemuser(String systemusername) {
        this.systemusername = systemusername;
    }

    public String getSystemusername() {
        return systemusername;
    }

    public void setSystemusername(String systemusername) {
        this.systemusername = systemusername;
    }

    public String getSystemuseremail() {
        return systemuseremail;
    }

    public void setSystemuseremail(String systemuseremail) {
        this.systemuseremail = systemuseremail;
    }

    public String getSystemuserpassword() {
        return systemuserpassword;
    }

    public void setSystemuserpassword(String systemuserpassword) {
        this.systemuserpassword = systemuserpassword;
    }

    public Collection<Systemusertype> getSystemusertypeCollection() {
        return systemusertypeCollection;
    }

    public void setSystemusertypeCollection(Collection<Systemusertype> systemusertypeCollection) {
        this.systemusertypeCollection = systemusertypeCollection;
    }

    public Collection<Template> getTemplateCollection() {
        return templateCollection;
    }

    public void setTemplateCollection(Collection<Template> templateCollection) {
        this.templateCollection = templateCollection;
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
        hash += (systemusername != null ? systemusername.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Systemuser)) {
            return false;
        }
        Systemuser other = (Systemuser) object;
        if ((this.systemusername == null && other.systemusername != null) || (this.systemusername != null && !this.systemusername.equals(other.systemusername))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Systemuser[systemusername=" + systemusername + "]";
    }

}
