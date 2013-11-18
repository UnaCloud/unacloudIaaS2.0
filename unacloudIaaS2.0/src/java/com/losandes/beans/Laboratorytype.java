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
@Table(name = "laboratorytype")
@NamedQueries({
    @NamedQuery(name = "Laboratorytype.findAll", query = "SELECT l FROM Laboratorytype l"),
    @NamedQuery(name = "Laboratorytype.findByLaboratorytypecode", query = "SELECT l FROM Laboratorytype l WHERE l.laboratorytypecode = :laboratorytypecode"),
    @NamedQuery(name = "Laboratorytype.findByLaboratorytypename", query = "SELECT l FROM Laboratorytype l WHERE l.laboratorytypename = :laboratorytypename")})
public class Laboratorytype implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LABORATORYTYPECODE")
    private Integer laboratorytypecode;
    @Column(name = "LABORATORYTYPENAME")
    private String laboratorytypename;
    @OneToMany(mappedBy = "laboratorytype")
    private Collection<Laboratory> laboratoryCollection;

    public Laboratorytype() {
    }

    public Laboratorytype(Integer laboratorytypecode) {
        this.laboratorytypecode = laboratorytypecode;
    }

    public Integer getLaboratorytypecode() {
        return laboratorytypecode;
    }

    public void setLaboratorytypecode(Integer laboratorytypecode) {
        this.laboratorytypecode = laboratorytypecode;
    }

    public String getLaboratorytypename() {
        return laboratorytypename;
    }

    public void setLaboratorytypename(String laboratorytypename) {
        this.laboratorytypename = laboratorytypename;
    }

    public Collection<Laboratory> getLaboratoryCollection() {
        return laboratoryCollection;
    }

    public void setLaboratoryCollection(Collection<Laboratory> laboratoryCollection) {
        this.laboratoryCollection = laboratoryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (laboratorytypecode != null ? laboratorytypecode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Laboratorytype)) {
            return false;
        }
        Laboratorytype other = (Laboratorytype) object;
        if ((this.laboratorytypecode == null && other.laboratorytypecode != null) || (this.laboratorytypecode != null && !this.laboratorytypecode.equals(other.laboratorytypecode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Laboratorytype[laboratorytypecode=" + laboratorytypecode + "]";
    }

}
