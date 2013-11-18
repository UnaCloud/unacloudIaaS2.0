/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "physicalmachine")
@NamedQueries({
    @NamedQuery(name = "Physicalmachine.findAll", query = "SELECT p FROM Physicalmachine p"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinename", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinename = :physicalmachinename"),
    @NamedQuery(name = "Physicalmachine.findByAverageavailability", query = "SELECT p FROM Physicalmachine p WHERE p.averageavailability = :averageavailability"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachineip", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachineip = :physicalmachineip"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinevirtualmachineson", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinevirtualmachineson = :physicalmachinevirtualmachineson"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinearchitecture", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinearchitecture = :physicalmachinearchitecture"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinevirtualip", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinevirtualip = :physicalmachinevirtualip"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinecolumn", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinecolumn = :physicalmachinecolumn"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinevirtualnetmask", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinevirtualnetmask = :physicalmachinevirtualnetmask"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinevirtualmac", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinevirtualmac = :physicalmachinevirtualmac"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinerammemory", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinerammemory = :physicalmachinerammemory"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinestate", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinestate = :physicalmachinestate"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinecores", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinecores = :physicalmachinecores"),
    @NamedQuery(name = "Physicalmachine.findByMaxvirtualmachineson", query = "SELECT p FROM Physicalmachine p WHERE p.maxvirtualmachineson = :maxvirtualmachineson"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachineuser", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachineuser = :physicalmachineuser"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinedisk", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinedisk = :physicalmachinedisk"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinehypervisorpath", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinehypervisorpath = :physicalmachinehypervisorpath"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinerow", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinerow = :physicalmachinerow"),
    @NamedQuery(name = "Physicalmachine.findByExpectedfailures", query = "SELECT p FROM Physicalmachine p WHERE p.expectedfailures = :expectedfailures"),
    @NamedQuery(name = "Physicalmachine.findByPhysicalmachinemac", query = "SELECT p FROM Physicalmachine p WHERE p.physicalmachinemac = :physicalmachinemac")})
public class Physicalmachine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PHYSICALMACHINENAME")
    private String physicalmachinename;
    @Column(name = "AVERAGEAVAILABILITY")
    private Double averageavailability;
    @Column(name = "PHYSICALMACHINEIP")
    private String physicalmachineip;
    @Column(name = "PHYSICALMACHINEVIRTUALMACHINESON")
    private Integer physicalmachinevirtualmachineson;
    @Column(name = "PHYSICALMACHINEARCHITECTURE")
    private String physicalmachinearchitecture;
    @Column(name = "PHYSICALMACHINEVIRTUALIP")
    private String physicalmachinevirtualip;
    @Column(name = "PHYSICALMACHINECOLUMN")
    private Integer physicalmachinecolumn;
    @Column(name = "PHYSICALMACHINEVIRTUALNETMASK")
    private String physicalmachinevirtualnetmask;
    @Column(name = "PHYSICALMACHINEVIRTUALMAC")
    private String physicalmachinevirtualmac;
    @Column(name = "PHYSICALMACHINERAMMEMORY")
    private Integer physicalmachinerammemory;
    @Column(name = "PHYSICALMACHINESTATE")
    private Integer physicalmachinestate;
    @Column(name = "PHYSICALMACHINECORES")
    private Integer physicalmachinecores;
    @Column(name = "MAXVIRTUALMACHINESON")
    private Integer maxvirtualmachineson;
    @Column(name = "PHYSICALMACHINEUSER")
    private String physicalmachineuser;
    @Column(name = "PHYSICALMACHINEDISK")
    private BigInteger physicalmachinedisk;
    @Column(name = "PHYSICALMACHINEHYPERVISORPATH")
    private String physicalmachinehypervisorpath;
    @Column(name = "PHYSICALMACHINEROW")
    private Integer physicalmachinerow;
    @Column(name = "EXPECTEDFAILURES")
    private Double expectedfailures;
    @Column(name = "PHYSICALMACHINEMAC")
    private String physicalmachinemac;
    @OneToMany(mappedBy = "physicalmachine")
    private Collection<Virtualmachine> virtualmachineCollection;
    @JoinColumn(name = "OPERATINGSYSTEM_OPERATINGSYSTEMCODE", referencedColumnName = "OPERATINGSYSTEMCODE")
    @ManyToOne
    private Operatingsystem operatingsystem;
    @JoinColumn(name = "LABORATORY_LABORATORYCODE", referencedColumnName = "LABORATORYCODE")
    @ManyToOne
    private Laboratory laboratory;

    public Physicalmachine() {
    }

    public Physicalmachine(String physicalmachinename) {
        this.physicalmachinename = physicalmachinename;
    }

    public String getPhysicalmachinename() {
        return physicalmachinename;
    }

    public void setPhysicalmachinename(String physicalmachinename) {
        this.physicalmachinename = physicalmachinename;
    }

    public Double getAverageavailability() {
        return averageavailability;
    }

    public void setAverageavailability(Double averageavailability) {
        this.averageavailability = averageavailability;
    }

    public String getPhysicalmachineip() {
        return physicalmachineip;
    }

    public void setPhysicalmachineip(String physicalmachineip) {
        this.physicalmachineip = physicalmachineip;
    }

    public Integer getPhysicalmachinevirtualmachineson() {
        return physicalmachinevirtualmachineson;
    }

    public void setPhysicalmachinevirtualmachineson(Integer physicalmachinevirtualmachineson) {
        this.physicalmachinevirtualmachineson = physicalmachinevirtualmachineson;
    }

    public String getPhysicalmachinearchitecture() {
        return physicalmachinearchitecture;
    }

    public void setPhysicalmachinearchitecture(String physicalmachinearchitecture) {
        this.physicalmachinearchitecture = physicalmachinearchitecture;
    }

    public String getPhysicalmachinevirtualip() {
        return physicalmachinevirtualip;
    }

    public void setPhysicalmachinevirtualip(String physicalmachinevirtualip) {
        this.physicalmachinevirtualip = physicalmachinevirtualip;
    }

    public Integer getPhysicalmachinecolumn() {
        return physicalmachinecolumn;
    }

    public void setPhysicalmachinecolumn(Integer physicalmachinecolumn) {
        this.physicalmachinecolumn = physicalmachinecolumn;
    }

    public String getPhysicalmachinevirtualnetmask() {
        return physicalmachinevirtualnetmask;
    }

    public void setPhysicalmachinevirtualnetmask(String physicalmachinevirtualnetmask) {
        this.physicalmachinevirtualnetmask = physicalmachinevirtualnetmask;
    }

    public String getPhysicalmachinevirtualmac() {
        return physicalmachinevirtualmac;
    }

    public void setPhysicalmachinevirtualmac(String physicalmachinevirtualmac) {
        this.physicalmachinevirtualmac = physicalmachinevirtualmac;
    }

    public Integer getPhysicalmachinerammemory() {
        return physicalmachinerammemory;
    }

    public void setPhysicalmachinerammemory(Integer physicalmachinerammemory) {
        this.physicalmachinerammemory = physicalmachinerammemory;
    }

    public Integer getPhysicalmachinestate() {
        return physicalmachinestate;
    }

    public void setPhysicalmachinestate(Integer physicalmachinestate) {
        this.physicalmachinestate = physicalmachinestate;
    }

    public Integer getPhysicalmachinecores() {
        return physicalmachinecores;
    }

    public void setPhysicalmachinecores(Integer physicalmachinecores) {
        this.physicalmachinecores = physicalmachinecores;
    }

    public Integer getMaxvirtualmachineson() {
        return maxvirtualmachineson;
    }

    public void setMaxvirtualmachineson(Integer maxvirtualmachineson) {
        this.maxvirtualmachineson = maxvirtualmachineson;
    }

    public String getPhysicalmachineuser() {
        return physicalmachineuser;
    }

    public void setPhysicalmachineuser(String physicalmachineuser) {
        this.physicalmachineuser = physicalmachineuser;
    }

    public BigInteger getPhysicalmachinedisk() {
        return physicalmachinedisk;
    }

    public void setPhysicalmachinedisk(BigInteger physicalmachinedisk) {
        this.physicalmachinedisk = physicalmachinedisk;
    }

    public String getPhysicalmachinehypervisorpath() {
        return physicalmachinehypervisorpath;
    }

    public void setPhysicalmachinehypervisorpath(String physicalmachinehypervisorpath) {
        this.physicalmachinehypervisorpath = physicalmachinehypervisorpath;
    }

    public Integer getPhysicalmachinerow() {
        return physicalmachinerow;
    }

    public void setPhysicalmachinerow(Integer physicalmachinerow) {
        this.physicalmachinerow = physicalmachinerow;
    }

    public Double getExpectedfailures() {
        return expectedfailures;
    }

    public void setExpectedfailures(Double expectedfailures) {
        this.expectedfailures = expectedfailures;
    }

    public String getPhysicalmachinemac() {
        return physicalmachinemac;
    }

    public void setPhysicalmachinemac(String physicalmachinemac) {
        this.physicalmachinemac = physicalmachinemac;
    }

    public Collection<Virtualmachine> getVirtualmachineCollection() {
        return virtualmachineCollection;
    }

    public void setVirtualmachineCollection(Collection<Virtualmachine> virtualmachineCollection) {
        this.virtualmachineCollection = virtualmachineCollection;
    }

    public Operatingsystem getOperatingsystem() {
        return operatingsystem;
    }

    public void setOperatingsystem(Operatingsystem operatingsystem) {
        this.operatingsystem = operatingsystem;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (physicalmachinename != null ? physicalmachinename.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Physicalmachine)) {
            return false;
        }
        Physicalmachine other = (Physicalmachine) object;
        if ((this.physicalmachinename == null && other.physicalmachinename != null) || (this.physicalmachinename != null && !this.physicalmachinename.equals(other.physicalmachinename))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Physicalmachine[physicalmachinename=" + physicalmachinename + "]";
    }

}
