/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.beans;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "ipdirection")
@NamedQueries({
    @NamedQuery(name = "Ipdirection.findAll", query = "SELECT i FROM Ipdirection i"),
    @NamedQuery(name = "Ipdirection.findById", query = "SELECT i FROM Ipdirection i WHERE i.id = :id")})
public class Ipdirection implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @JoinTable(name = "ippool_ipdirection", joinColumns = {
        @JoinColumn(name = "directions_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "IPPool_INITIALIP_ID", referencedColumnName = "INITIALIP_ID")})
    @ManyToMany
    private Collection<Ippool> ippoolCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ipdirection")
    private Ippool ippool;

    public Ipdirection() {
    }

    public Ipdirection(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Ippool> getIppoolCollection() {
        return ippoolCollection;
    }

    public void setIppoolCollection(Collection<Ippool> ippoolCollection) {
        this.ippoolCollection = ippoolCollection;
    }

    public Ippool getIppool() {
        return ippool;
    }

    public void setIppool(Ippool ippool) {
        this.ippool = ippool;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ipdirection)) {
            return false;
        }
        Ipdirection other = (Ipdirection) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Ipdirection[id=" + id + "]";
    }

}
