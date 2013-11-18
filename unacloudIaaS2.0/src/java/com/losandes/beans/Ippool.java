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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Clouder
 */
@Entity
@Table(name = "ippool")
@NamedQueries({
    @NamedQuery(name = "Ippool.findAll", query = "SELECT i FROM Ippool i"),
    @NamedQuery(name = "Ippool.findByPoolsize", query = "SELECT i FROM Ippool i WHERE i.poolsize = :poolsize"),
    @NamedQuery(name = "Ippool.findByInitialipId", query = "SELECT i FROM Ippool i WHERE i.initialipId = :initialipId")})
public class Ippool implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "POOLSIZE")
    private Integer poolsize;
    @Id
    @Basic(optional = false)
    @Column(name = "INITIALIP_ID")
    private Long initialipId;
    @ManyToMany(mappedBy = "ippoolCollection")
    private Collection<Ipdirection> ipdirectionCollection;
    @JoinColumn(name = "INITIALIP_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Ipdirection ipdirection;

    public Ippool() {
    }

    public Ippool(Long initialipId) {
        this.initialipId = initialipId;
    }

    public Integer getPoolsize() {
        return poolsize;
    }

    public void setPoolsize(Integer poolsize) {
        this.poolsize = poolsize;
    }

    public Long getInitialipId() {
        return initialipId;
    }

    public void setInitialipId(Long initialipId) {
        this.initialipId = initialipId;
    }

    public Collection<Ipdirection> getIpdirectionCollection() {
        return ipdirectionCollection;
    }

    public void setIpdirectionCollection(Collection<Ipdirection> ipdirectionCollection) {
        this.ipdirectionCollection = ipdirectionCollection;
    }

    public Ipdirection getIpdirection() {
        return ipdirection;
    }

    public void setIpdirection(Ipdirection ipdirection) {
        this.ipdirection = ipdirection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (initialipId != null ? initialipId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ippool)) {
            return false;
        }
        Ippool other = (Ippool) object;
        if ((this.initialipId == null && other.initialipId != null) || (this.initialipId != null && !this.initialipId.equals(other.initialipId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Ippool[initialipId=" + initialipId + "]";
    }

}
