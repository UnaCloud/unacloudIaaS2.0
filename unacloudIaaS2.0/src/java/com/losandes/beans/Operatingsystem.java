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
@Table(name = "operatingsystem")
@NamedQueries({
    @NamedQuery(name = "Operatingsystem.findAll", query = "SELECT o FROM Operatingsystem o"),
    @NamedQuery(name = "Operatingsystem.findByOperatingsystemcode", query = "SELECT o FROM Operatingsystem o WHERE o.operatingsystemcode = :operatingsystemcode"),
    @NamedQuery(name = "Operatingsystem.findByOperatingsystemname", query = "SELECT o FROM Operatingsystem o WHERE o.operatingsystemname = :operatingsystemname"),
    @NamedQuery(name = "Operatingsystem.findByConfigurationclass", query = "SELECT o FROM Operatingsystem o WHERE o.configurationclass = :configurationclass")})
public class Operatingsystem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "OPERATINGSYSTEMCODE")
    private Integer operatingsystemcode;
    @Column(name = "OPERATINGSYSTEMNAME")
    private String operatingsystemname;
    @Column(name = "CONFIGURATIONCLASS")
    private String configurationclass;
    @OneToMany(mappedBy = "operatingsystem")
    private Collection<Template> templateCollection;
    @JoinColumn(name = "OPERATINGSYSTEMTYPE_OPERATINGSYSTEMTYPECODE", referencedColumnName = "OPERATINGSYSTEMTYPECODE")
    @ManyToOne
    private Operatingsystemtype operatingsystemtype;
    @OneToMany(mappedBy = "operatingsystem")
    private Collection<Physicalmachine> physicalmachineCollection;

    public Operatingsystem() {
    }

    public Operatingsystem(Integer operatingsystemcode) {
        this.operatingsystemcode = operatingsystemcode;
    }

    public Integer getOperatingsystemcode() {
        return operatingsystemcode;
    }

    public void setOperatingsystemcode(Integer operatingsystemcode) {
        this.operatingsystemcode = operatingsystemcode;
    }

    public String getOperatingsystemname() {
        return operatingsystemname;
    }

    public void setOperatingsystemname(String operatingsystemname) {
        this.operatingsystemname = operatingsystemname;
    }

    public String getConfigurationclass() {
        return configurationclass;
    }

    public void setConfigurationclass(String configurationclass) {
        this.configurationclass = configurationclass;
    }

    public Collection<Template> getTemplateCollection() {
        return templateCollection;
    }

    public void setTemplateCollection(Collection<Template> templateCollection) {
        this.templateCollection = templateCollection;
    }

    public Operatingsystemtype getOperatingsystemtype() {
        return operatingsystemtype;
    }

    public void setOperatingsystemtype(Operatingsystemtype operatingsystemtype) {
        this.operatingsystemtype = operatingsystemtype;
    }

    public Collection<Physicalmachine> getPhysicalmachineCollection() {
        return physicalmachineCollection;
    }

    public void setPhysicalmachineCollection(Collection<Physicalmachine> physicalmachineCollection) {
        this.physicalmachineCollection = physicalmachineCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (operatingsystemcode != null ? operatingsystemcode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operatingsystem)) {
            return false;
        }
        Operatingsystem other = (Operatingsystem) object;
        if ((this.operatingsystemcode == null && other.operatingsystemcode != null) || (this.operatingsystemcode != null && !this.operatingsystemcode.equals(other.operatingsystemcode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Operatingsystem[operatingsystemcode=" + operatingsystemcode + "]";
    }

}
