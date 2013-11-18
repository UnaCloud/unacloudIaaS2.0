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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Clouder
 */
@Entity
@Table(name = "templatetype")
@NamedQueries({
    @NamedQuery(name = "Templatetype.findAll", query = "SELECT t FROM Templatetype t"),
    @NamedQuery(name = "Templatetype.findByTemplatetypecode", query = "SELECT t FROM Templatetype t WHERE t.templatetypecode = :templatetypecode"),
    @NamedQuery(name = "Templatetype.findByTemplatetypename", query = "SELECT t FROM Templatetype t WHERE t.templatetypename = :templatetypename")})
public class Templatetype implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TEMPLATETYPECODE")
    private Integer templatetypecode;
    @Column(name = "TEMPLATETYPENAME")
    private String templatetypename;
    @OneToMany(mappedBy = "templatetype")
    private Collection<Template> templateCollection;

    public Templatetype() {
    }

    public Templatetype(Integer templatetypecode) {
        this.templatetypecode = templatetypecode;
    }

    public Integer getTemplatetypecode() {
        return templatetypecode;
    }

    public void setTemplatetypecode(Integer templatetypecode) {
        this.templatetypecode = templatetypecode;
    }

    public String getTemplatetypename() {
        return templatetypename;
    }

    public void setTemplatetypename(String templatetypename) {
        this.templatetypename = templatetypename;
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
        hash += (templatetypecode != null ? templatetypecode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Templatetype)) {
            return false;
        }
        Templatetype other = (Templatetype) object;
        if ((this.templatetypecode == null && other.templatetypecode != null) || (this.templatetypecode != null && !this.templatetypecode.equals(other.templatetypecode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Templatetype[templatetypecode=" + templatetypecode + "]";
    }

}
