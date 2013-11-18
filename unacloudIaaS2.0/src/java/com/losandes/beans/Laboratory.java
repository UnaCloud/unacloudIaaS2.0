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
@Table(name = "laboratory")
@NamedQueries({
    @NamedQuery(name = "Laboratory.findAll", query = "SELECT l FROM Laboratory l"),
    @NamedQuery(name = "Laboratory.findByLaboratorycode", query = "SELECT l FROM Laboratory l WHERE l.laboratorycode = :laboratorycode"),
    @NamedQuery(name = "Laboratory.findByLaboratorycolumns", query = "SELECT l FROM Laboratory l WHERE l.laboratorycolumns = :laboratorycolumns"),
    @NamedQuery(name = "Laboratory.findByLaboratoryname", query = "SELECT l FROM Laboratory l WHERE l.laboratoryname = :laboratoryname"),
    @NamedQuery(name = "Laboratory.findByLaboratoryrows", query = "SELECT l FROM Laboratory l WHERE l.laboratoryrows = :laboratoryrows")})
public class Laboratory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LABORATORYCODE")
    private Integer laboratorycode;
    @Column(name = "LABORATORYCOLUMNS")
    private Integer laboratorycolumns;
    @Column(name = "LABORATORYNAME")
    private String laboratoryname;
    @Column(name = "LABORATORYROWS")
    private Integer laboratoryrows;
    @JoinColumn(name = "LABORATORYTYPE_LABORATORYTYPECODE", referencedColumnName = "LABORATORYTYPECODE")
    @ManyToOne
    private Laboratorytype laboratorytype;
    @OneToMany(mappedBy = "laboratory")
    private Collection<Physicalmachine> physicalmachineCollection;

    public Laboratory() {
    }

    public Laboratory(Integer laboratorycode) {
        this.laboratorycode = laboratorycode;
    }

    public Integer getLaboratorycode() {
        return laboratorycode;
    }

    public void setLaboratorycode(Integer laboratorycode) {
        this.laboratorycode = laboratorycode;
    }

    public Integer getLaboratorycolumns() {
        return laboratorycolumns;
    }

    public void setLaboratorycolumns(Integer laboratorycolumns) {
        this.laboratorycolumns = laboratorycolumns;
    }

    public String getLaboratoryname() {
        return laboratoryname;
    }

    public void setLaboratoryname(String laboratoryname) {
        this.laboratoryname = laboratoryname;
    }

    public Integer getLaboratoryrows() {
        return laboratoryrows;
    }

    public void setLaboratoryrows(Integer laboratoryrows) {
        this.laboratoryrows = laboratoryrows;
    }

    public Laboratorytype getLaboratorytype() {
        return laboratorytype;
    }

    public void setLaboratorytype(Laboratorytype laboratorytype) {
        this.laboratorytype = laboratorytype;
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
        hash += (laboratorycode != null ? laboratorycode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Laboratory)) {
            return false;
        }
        Laboratory other = (Laboratory) object;
        if ((this.laboratorycode == null && other.laboratorycode != null) || (this.laboratorycode != null && !this.laboratorycode.equals(other.laboratorycode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Laboratory[laboratorycode=" + laboratorycode + "]";
    }

}
