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
@Table(name = "elasticrule")
@NamedQueries({
    @NamedQuery(name = "Elasticrule.findAll", query = "SELECT e FROM Elasticrule e"),
    @NamedQuery(name = "Elasticrule.findById", query = "SELECT e FROM Elasticrule e WHERE e.id = :id"),
    @NamedQuery(name = "Elasticrule.findByUpperlimit", query = "SELECT e FROM Elasticrule e WHERE e.upperlimit = :upperlimit"),
    @NamedQuery(name = "Elasticrule.findByLowerlimit", query = "SELECT e FROM Elasticrule e WHERE e.lowerlimit = :lowerlimit"),
    @NamedQuery(name = "Elasticrule.findByActive", query = "SELECT e FROM Elasticrule e WHERE e.active = :active"),
    @NamedQuery(name = "Elasticrule.findByMultiply", query = "SELECT e FROM Elasticrule e WHERE e.multiply = :multiply"),
    @NamedQuery(name = "Elasticrule.findByFactor", query = "SELECT e FROM Elasticrule e WHERE e.factor = :factor"),
    @NamedQuery(name = "Elasticrule.findByAddition", query = "SELECT e FROM Elasticrule e WHERE e.addition = :addition")})
public class Elasticrule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "UPPERLIMIT")
    private Integer upperlimit;
    @Column(name = "LOWERLIMIT")
    private Integer lowerlimit;
    @Column(name = "ACTIVE")
    private Boolean active;
    @Column(name = "MULTIPLY")
    private Boolean multiply;
    @Column(name = "FACTOR")
    private Double factor;
    @Column(name = "ADDITION")
    private Boolean addition;
    @OneToMany(mappedBy = "elasticrule")
    private Collection<Template> templateCollection;

    public Elasticrule() {
    }

    public Elasticrule(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUpperlimit() {
        return upperlimit;
    }

    public void setUpperlimit(Integer upperlimit) {
        this.upperlimit = upperlimit;
    }

    public Integer getLowerlimit() {
        return lowerlimit;
    }

    public void setLowerlimit(Integer lowerlimit) {
        this.lowerlimit = lowerlimit;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getMultiply() {
        return multiply;
    }

    public void setMultiply(Boolean multiply) {
        this.multiply = multiply;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public Boolean getAddition() {
        return addition;
    }

    public void setAddition(Boolean addition) {
        this.addition = addition;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Elasticrule)) {
            return false;
        }
        Elasticrule other = (Elasticrule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Elasticrule[id=" + id + "]";
    }

}
