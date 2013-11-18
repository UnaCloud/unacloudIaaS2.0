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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Clouder
 */
@Entity
@Table(name = "application")
@NamedQueries({
    @NamedQuery(name = "Application.findAll", query = "SELECT a FROM Application a"),
    @NamedQuery(name = "Application.findByApplicationcode", query = "SELECT a FROM Application a WHERE a.applicationcode = :applicationcode"),
    @NamedQuery(name = "Application.findByApplicationname", query = "SELECT a FROM Application a WHERE a.applicationname = :applicationname")})
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "APPLICATIONCODE")
    private Integer applicationcode;
    @Column(name = "APPLICATIONNAME")
    private String applicationname;
    @ManyToMany(mappedBy = "applicationCollection")
    private Collection<Template> templateCollection;

    public Application() {
    }

    public Application(Integer applicationcode) {
        this.applicationcode = applicationcode;
    }

    public Integer getApplicationcode() {
        return applicationcode;
    }

    public void setApplicationcode(Integer applicationcode) {
        this.applicationcode = applicationcode;
    }

    public String getApplicationname() {
        return applicationname;
    }

    public void setApplicationname(String applicationname) {
        this.applicationname = applicationname;
    }

    public Collection<Template> getTemplateCollection() {
        return templateCollection;
    }

    public void setTemplateCollection(Collection<Template> templateCollection) {
        this.templateCollection = templateCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (applicationcode != null ? applicationcode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Application)) {
            return false;
        }
        Application other = (Application) object;
        if ((this.applicationcode == null && other.applicationcode != null) || (this.applicationcode != null && !this.applicationcode.equals(other.applicationcode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Application[applicationcode=" + applicationcode + "]";
    }

}
