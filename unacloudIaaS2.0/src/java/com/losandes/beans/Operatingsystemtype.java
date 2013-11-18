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
@Table(name = "operatingsystemtype")
@NamedQueries({
    @NamedQuery(name = "Operatingsystemtype.findAll", query = "SELECT o FROM Operatingsystemtype o"),
    @NamedQuery(name = "Operatingsystemtype.findByOperatingsystemtypecode", query = "SELECT o FROM Operatingsystemtype o WHERE o.operatingsystemtypecode = :operatingsystemtypecode"),
    @NamedQuery(name = "Operatingsystemtype.findByOperatingsystemtypename", query = "SELECT o FROM Operatingsystemtype o WHERE o.operatingsystemtypename = :operatingsystemtypename")})
public class Operatingsystemtype implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "OPERATINGSYSTEMTYPECODE")
    private Integer operatingsystemtypecode;
    @Column(name = "OPERATINGSYSTEMTYPENAME")
    private String operatingsystemtypename;
    @OneToMany(mappedBy = "operatingsystemtype")
    private Collection<Operatingsystem> operatingsystemCollection;

    public Operatingsystemtype() {
    }

    public Operatingsystemtype(Integer operatingsystemtypecode) {
        this.operatingsystemtypecode = operatingsystemtypecode;
    }

    public Integer getOperatingsystemtypecode() {
        return operatingsystemtypecode;
    }

    public void setOperatingsystemtypecode(Integer operatingsystemtypecode) {
        this.operatingsystemtypecode = operatingsystemtypecode;
    }

    public String getOperatingsystemtypename() {
        return operatingsystemtypename;
    }

    public void setOperatingsystemtypename(String operatingsystemtypename) {
        this.operatingsystemtypename = operatingsystemtypename;
    }

    public Collection<Operatingsystem> getOperatingsystemCollection() {
        return operatingsystemCollection;
    }

    public void setOperatingsystemCollection(Collection<Operatingsystem> operatingsystemCollection) {
        this.operatingsystemCollection = operatingsystemCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (operatingsystemtypecode != null ? operatingsystemtypecode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operatingsystemtype)) {
            return false;
        }
        Operatingsystemtype other = (Operatingsystemtype) object;
        if ((this.operatingsystemtypecode == null && other.operatingsystemtypecode != null) || (this.operatingsystemtypecode != null && !this.operatingsystemtypecode.equals(other.operatingsystemtypecode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Operatingsystemtype[operatingsystemtypecode=" + operatingsystemtypecode + "]";
    }

}
