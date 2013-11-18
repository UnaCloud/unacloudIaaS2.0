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
@Table(name = "hypervisor")
@NamedQueries({
    @NamedQuery(name = "Hypervisor.findAll", query = "SELECT h FROM Hypervisor h"),
    @NamedQuery(name = "Hypervisor.findByHypervisorcode", query = "SELECT h FROM Hypervisor h WHERE h.hypervisorcode = :hypervisorcode"),
    @NamedQuery(name = "Hypervisor.findByHypervisorname", query = "SELECT h FROM Hypervisor h WHERE h.hypervisorname = :hypervisorname")})
public class Hypervisor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "HYPERVISORCODE")
    private Integer hypervisorcode;
    @Column(name = "HYPERVISORNAME")
    private String hypervisorname;
    @OneToMany(mappedBy = "hypervisor")
    private Collection<Virtualmachine> virtualmachineCollection;

    public Hypervisor() {
    }

    public Hypervisor(Integer hypervisorcode) {
        this.hypervisorcode = hypervisorcode;
    }

    public Integer getHypervisorcode() {
        return hypervisorcode;
    }

    public void setHypervisorcode(Integer hypervisorcode) {
        this.hypervisorcode = hypervisorcode;
    }

    public String getHypervisorname() {
        return hypervisorname;
    }

    public void setHypervisorname(String hypervisorname) {
        this.hypervisorname = hypervisorname;
    }

    public Collection<Virtualmachine> getVirtualmachineCollection() {
        return virtualmachineCollection;
    }

    public void setVirtualmachineCollection(Collection<Virtualmachine> virtualmachineCollection) {
        this.virtualmachineCollection = virtualmachineCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hypervisorcode != null ? hypervisorcode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hypervisor)) {
            return false;
        }
        Hypervisor other = (Hypervisor) object;
        if ((this.hypervisorcode == null && other.hypervisorcode != null) || (this.hypervisorcode != null && !this.hypervisorcode.equals(other.hypervisorcode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Hypervisor[hypervisorcode=" + hypervisorcode + "]";
    }

}
