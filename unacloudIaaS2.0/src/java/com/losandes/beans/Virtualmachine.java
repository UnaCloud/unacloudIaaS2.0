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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "virtualmachine")
@NamedQueries({
    @NamedQuery(name = "Virtualmachine.findAll", query = "SELECT v FROM Virtualmachine v"),
    @NamedQuery(name = "Virtualmachine.findByVirtualmachinecode", query = "SELECT v FROM Virtualmachine v WHERE v.virtualmachinecode = :virtualmachinecode"),
    @NamedQuery(name = "Virtualmachine.findBySnapshotid", query = "SELECT v FROM Virtualmachine v WHERE v.snapshotid = :snapshotid"),
    @NamedQuery(name = "Virtualmachine.findByVirtualmachineip", query = "SELECT v FROM Virtualmachine v WHERE v.virtualmachineip = :virtualmachineip"),
    @NamedQuery(name = "Virtualmachine.findByCpustate", query = "SELECT v FROM Virtualmachine v WHERE v.cpustate = :cpustate"),
    @NamedQuery(name = "Virtualmachine.findByConfigured", query = "SELECT v FROM Virtualmachine v WHERE v.configured = :configured"),
    @NamedQuery(name = "Virtualmachine.findByVirtualmachinepath", query = "SELECT v FROM Virtualmachine v WHERE v.virtualmachinepath = :virtualmachinepath"),
    @NamedQuery(name = "Virtualmachine.findByVirtualmachinename", query = "SELECT v FROM Virtualmachine v WHERE v.virtualmachinename = :virtualmachinename"),
    @NamedQuery(name = "Virtualmachine.findByLocallystored", query = "SELECT v FROM Virtualmachine v WHERE v.locallystored = :locallystored"),
    @NamedQuery(name = "Virtualmachine.findByAutoprotect", query = "SELECT v FROM Virtualmachine v WHERE v.autoprotect = :autoprotect"),
    @NamedQuery(name = "Virtualmachine.findByIppolicy", query = "SELECT v FROM Virtualmachine v WHERE v.ippolicy = :ippolicy"),
    @NamedQuery(name = "Virtualmachine.findByVirtualmachinerammemory", query = "SELECT v FROM Virtualmachine v WHERE v.virtualmachinerammemory = :virtualmachinerammemory"),
    @NamedQuery(name = "Virtualmachine.findByVirtualmachineharddisk", query = "SELECT v FROM Virtualmachine v WHERE v.virtualmachineharddisk = :virtualmachineharddisk"),
    @NamedQuery(name = "Virtualmachine.findByVirtualmachinecores", query = "SELECT v FROM Virtualmachine v WHERE v.virtualmachinecores = :virtualmachinecores"),
    @NamedQuery(name = "Virtualmachine.findByPersistent", query = "SELECT v FROM Virtualmachine v WHERE v.persistent = :persistent"),
    @NamedQuery(name = "Virtualmachine.findByMacpolicy", query = "SELECT v FROM Virtualmachine v WHERE v.macpolicy = :macpolicy"),
    @NamedQuery(name = "Virtualmachine.findByVirtualmachinestate", query = "SELECT v FROM Virtualmachine v WHERE v.virtualmachinestate = :virtualmachinestate"),
    @NamedQuery(name = "Virtualmachine.findByVirtualmachinemac", query = "SELECT v FROM Virtualmachine v WHERE v.virtualmachinemac = :virtualmachinemac")})
public class Virtualmachine implements Serializable {
    @Column(name = "TURNONCOUNT")
    private Integer turnoncount;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "VIRTUALMACHINECODE")
    private Integer virtualmachinecode;
    @Column(name = "SNAPSHOTID")
    private String snapshotid;
    @Column(name = "VIRTUALMACHINEIP")
    private String virtualmachineip;
    @Column(name = "CPUSTATE")
    private Integer cpustate;
    @Column(name = "CONFIGURED")
    private Boolean configured;
    @Column(name = "VIRTUALMACHINEPATH")
    private String virtualmachinepath;
    @Column(name = "VIRTUALMACHINENAME")
    private String virtualmachinename;
    @Column(name = "LOCALLYSTORED")
    private Boolean locallystored;
    @Column(name = "AUTOPROTECT")
    private Boolean autoprotect;
    @Column(name = "IPPOLICY")
    private Integer ippolicy;
    @Column(name = "VIRTUALMACHINERAMMEMORY")
    private Integer virtualmachinerammemory;
    @Column(name = "VIRTUALMACHINEHARDDISK")
    private BigInteger virtualmachineharddisk;
    @Column(name = "VIRTUALMACHINECORES")
    private Integer virtualmachinecores;
    @Column(name = "PERSISTENT")
    private Boolean persistent;
    @Column(name = "MACPOLICY")
    private Integer macpolicy;
    @Column(name = "VIRTUALMACHINESTATE")
    private Integer virtualmachinestate;
    @Column(name = "VIRTUALMACHINEMAC")
    private String virtualmachinemac;
    @JoinColumn(name = "VIRTUALMACHINESECURITY_VIRTUALMACHINESECURITYCODE", referencedColumnName = "VIRTUALMACHINESECURITYCODE")
    @ManyToOne
    private Virtualmachinesecurity virtualmachinesecurity;
    @JoinColumn(name = "PHYSICALMACHINE_PHYSICALMACHINENAME", referencedColumnName = "PHYSICALMACHINENAME")
    @ManyToOne
    private Physicalmachine physicalmachine;
    @JoinColumn(name = "TEMPLATE_TEMPLATECODE", referencedColumnName = "TEMPLATECODE")
    @ManyToOne
    private Template template;
    @JoinColumn(name = "HYPERVISOR_HYPERVISORCODE", referencedColumnName = "HYPERVISORCODE")
    @ManyToOne
    private Hypervisor hypervisor;
    @OneToMany(mappedBy = "virtualmachine")
    private Collection<Virtualmachineexecution> virtualmachineexecutionCollection;

    public Virtualmachine() {
    }

    public Virtualmachine(Integer virtualmachinecode) {
        this.virtualmachinecode = virtualmachinecode;
    }

    public Integer getVirtualmachinecode() {
        return virtualmachinecode;
    }

    public void setVirtualmachinecode(Integer virtualmachinecode) {
        this.virtualmachinecode = virtualmachinecode;
    }

    public String getSnapshotid() {
        return snapshotid;
    }

    public void setSnapshotid(String snapshotid) {
        this.snapshotid = snapshotid;
    }

    public String getVirtualmachineip() {
        return virtualmachineip;
    }

    public void setVirtualmachineip(String virtualmachineip) {
        this.virtualmachineip = virtualmachineip;
    }

    public Integer getCpustate() {
        return cpustate;
    }

    public void setCpustate(Integer cpustate) {
        this.cpustate = cpustate;
    }

    public Boolean getConfigured() {
        return configured;
    }

    public void setConfigured(Boolean configured) {
        this.configured = configured;
    }

    public String getVirtualmachinepath() {
        return virtualmachinepath;
    }

    public void setVirtualmachinepath(String virtualmachinepath) {
        this.virtualmachinepath = virtualmachinepath;
    }

    public String getVirtualmachinename() {
        return virtualmachinename;
    }

    public void setVirtualmachinename(String virtualmachinename) {
        this.virtualmachinename = virtualmachinename;
    }

    public Boolean getLocallystored() {
        return locallystored;
    }

    public void setLocallystored(Boolean locallystored) {
        this.locallystored = locallystored;
    }

    public Boolean getAutoprotect() {
        return autoprotect;
    }

    public void setAutoprotect(Boolean autoprotect) {
        this.autoprotect = autoprotect;
    }

    public Integer getIppolicy() {
        return ippolicy;
    }

    public void setIppolicy(Integer ippolicy) {
        this.ippolicy = ippolicy;
    }

    public Integer getVirtualmachinerammemory() {
        return virtualmachinerammemory;
    }

    public void setVirtualmachinerammemory(Integer virtualmachinerammemory) {
        this.virtualmachinerammemory = virtualmachinerammemory;
    }

    public BigInteger getVirtualmachineharddisk() {
        return virtualmachineharddisk;
    }

    public void setVirtualmachineharddisk(BigInteger virtualmachineharddisk) {
        this.virtualmachineharddisk = virtualmachineharddisk;
    }

    public Integer getVirtualmachinecores() {
        return virtualmachinecores;
    }

    public void setVirtualmachinecores(Integer virtualmachinecores) {
        this.virtualmachinecores = virtualmachinecores;
    }

    public Boolean getPersistent() {
        return persistent;
    }

    public void setPersistent(Boolean persistent) {
        this.persistent = persistent;
    }

    public Integer getMacpolicy() {
        return macpolicy;
    }

    public void setMacpolicy(Integer macpolicy) {
        this.macpolicy = macpolicy;
    }

    public Integer getVirtualmachinestate() {
        return virtualmachinestate;
    }

    public void setVirtualmachinestate(Integer virtualmachinestate) {
        this.virtualmachinestate = virtualmachinestate;
    }

    public String getVirtualmachinemac() {
        return virtualmachinemac;
    }

    public void setVirtualmachinemac(String virtualmachinemac) {
        this.virtualmachinemac = virtualmachinemac;
    }

    public Virtualmachinesecurity getVirtualmachinesecurity() {
        return virtualmachinesecurity;
    }

    public void setVirtualmachinesecurity(Virtualmachinesecurity virtualmachinesecurity) {
        this.virtualmachinesecurity = virtualmachinesecurity;
    }

    public Physicalmachine getPhysicalmachine() {
        return physicalmachine;
    }

    public void setPhysicalmachine(Physicalmachine physicalmachine) {
        this.physicalmachine = physicalmachine;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Hypervisor getHypervisor() {
        return hypervisor;
    }

    public void setHypervisor(Hypervisor hypervisor) {
        this.hypervisor = hypervisor;
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
        hash += (virtualmachinecode != null ? virtualmachinecode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Virtualmachine)) {
            return false;
        }
        Virtualmachine other = (Virtualmachine) object;
        if ((this.virtualmachinecode == null && other.virtualmachinecode != null) || (this.virtualmachinecode != null && !this.virtualmachinecode.equals(other.virtualmachinecode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Virtualmachine[virtualmachinecode=" + virtualmachinecode + "]";
    }

    public Integer getTurnoncount() {
        return turnoncount;
    }

    public void setTurnoncount(Integer turnoncount) {
        this.turnoncount = turnoncount;
    }

}
