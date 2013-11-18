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
import javax.persistence.Table;

/**
 *
 * @author Clouder
 */
@Entity
@Table(name = "systemusertype")
@NamedQueries({
    @NamedQuery(name = "Systemusertype.findAll", query = "SELECT s FROM Systemusertype s"),
    @NamedQuery(name = "Systemusertype.findBySystemusertypename", query = "SELECT s FROM Systemusertype s WHERE s.systemusertypename = :systemusertypename")})
public class Systemusertype implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SYSTEMUSERTYPENAME")
    private String systemusertypename;
    @JoinTable(name = "systemuser_systemusertype", joinColumns = {
        @JoinColumn(name = "userGroups_SYSTEMUSERTYPENAME", referencedColumnName = "SYSTEMUSERTYPENAME")}, inverseJoinColumns = {
        @JoinColumn(name = "SystemUser_SYSTEMUSERNAME", referencedColumnName = "SYSTEMUSERNAME")})
    @ManyToMany
    private Collection<Systemuser> systemuserCollection;

    public Systemusertype() {
    }

    public Systemusertype(String systemusertypename) {
        this.systemusertypename = systemusertypename;
    }

    public String getSystemusertypename() {
        return systemusertypename;
    }

    public void setSystemusertypename(String systemusertypename) {
        this.systemusertypename = systemusertypename;
    }

    public Collection<Systemuser> getSystemuserCollection() {
        return systemuserCollection;
    }

    public void setSystemuserCollection(Collection<Systemuser> systemuserCollection) {
        this.systemuserCollection = systemuserCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (systemusertypename != null ? systemusertypename.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Systemusertype)) {
            return false;
        }
        Systemusertype other = (Systemusertype) object;
        if ((this.systemusertypename == null && other.systemusertypename != null) || (this.systemusertypename != null && !this.systemusertypename.equals(other.systemusertypename))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Systemusertype[systemusertypename=" + systemusertypename + "]";
    }

}
