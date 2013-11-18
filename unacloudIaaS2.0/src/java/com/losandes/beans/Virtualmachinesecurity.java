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
@Table(name = "virtualmachinesecurity")
@NamedQueries({
    @NamedQuery(name = "Virtualmachinesecurity.findAll", query = "SELECT v FROM Virtualmachinesecurity v"),
    @NamedQuery(name = "Virtualmachinesecurity.findByVirtualmachinesecuritycode", query = "SELECT v FROM Virtualmachinesecurity v WHERE v.virtualmachinesecuritycode = :virtualmachinesecuritycode"),
    @NamedQuery(name = "Virtualmachinesecurity.findByVirtualmachinesecurityuser", query = "SELECT v FROM Virtualmachinesecurity v WHERE v.virtualmachinesecurityuser = :virtualmachinesecurityuser"),
    @NamedQuery(name = "Virtualmachinesecurity.findByVirtualmachinesecurityport", query = "SELECT v FROM Virtualmachinesecurity v WHERE v.virtualmachinesecurityport = :virtualmachinesecurityport"),
    @NamedQuery(name = "Virtualmachinesecurity.findByVirtualmachinesecurityaccess", query = "SELECT v FROM Virtualmachinesecurity v WHERE v.virtualmachinesecurityaccess = :virtualmachinesecurityaccess"),
    @NamedQuery(name = "Virtualmachinesecurity.findByVirtualmachinesecuritypassword", query = "SELECT v FROM Virtualmachinesecurity v WHERE v.virtualmachinesecuritypassword = :virtualmachinesecuritypassword"),
    @NamedQuery(name = "Virtualmachinesecurity.findByVirtualmachinesecurityname", query = "SELECT v FROM Virtualmachinesecurity v WHERE v.virtualmachinesecurityname = :virtualmachinesecurityname")})
public class Virtualmachinesecurity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "VIRTUALMACHINESECURITYCODE")
    private Integer virtualmachinesecuritycode;
    @Column(name = "VIRTUALMACHINESECURITYUSER")
    private String virtualmachinesecurityuser;
    @Column(name = "VIRTUALMACHINESECURITYPORT")
    private Integer virtualmachinesecurityport;
    @Column(name = "VIRTUALMACHINESECURITYACCESS")
    private String virtualmachinesecurityaccess;
    @Column(name = "VIRTUALMACHINESECURITYPASSWORD")
    private String virtualmachinesecuritypassword;
    @Column(name = "VIRTUALMACHINESECURITYNAME")
    private String virtualmachinesecurityname;
    @OneToMany(mappedBy = "virtualmachinesecurity")
    private Collection<Virtualmachine> virtualmachineCollection;

    public Virtualmachinesecurity() {
    }

    public Virtualmachinesecurity(Integer virtualmachinesecuritycode) {
        this.virtualmachinesecuritycode = virtualmachinesecuritycode;
    }

    public Integer getVirtualmachinesecuritycode() {
        return virtualmachinesecuritycode;
    }

    public void setVirtualmachinesecuritycode(Integer virtualmachinesecuritycode) {
        this.virtualmachinesecuritycode = virtualmachinesecuritycode;
    }

    public String getVirtualmachinesecurityuser() {
        return virtualmachinesecurityuser;
    }

    public void setVirtualmachinesecurityuser(String virtualmachinesecurityuser) {
        this.virtualmachinesecurityuser = virtualmachinesecurityuser;
    }

    public Integer getVirtualmachinesecurityport() {
        return virtualmachinesecurityport;
    }

    public void setVirtualmachinesecurityport(Integer virtualmachinesecurityport) {
        this.virtualmachinesecurityport = virtualmachinesecurityport;
    }

    public String getVirtualmachinesecurityaccess() {
        return virtualmachinesecurityaccess;
    }

    public void setVirtualmachinesecurityaccess(String virtualmachinesecurityaccess) {
        this.virtualmachinesecurityaccess = virtualmachinesecurityaccess;
    }

    public String getVirtualmachinesecuritypassword() {
        return virtualmachinesecuritypassword;
    }

    public void setVirtualmachinesecuritypassword(String virtualmachinesecuritypassword) {
        this.virtualmachinesecuritypassword = virtualmachinesecuritypassword;
    }

    public String getVirtualmachinesecurityname() {
        return virtualmachinesecurityname;
    }

    public void setVirtualmachinesecurityname(String virtualmachinesecurityname) {
        this.virtualmachinesecurityname = virtualmachinesecurityname;
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
        hash += (virtualmachinesecuritycode != null ? virtualmachinesecuritycode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Virtualmachinesecurity)) {
            return false;
        }
        Virtualmachinesecurity other = (Virtualmachinesecurity) object;
        if ((this.virtualmachinesecuritycode == null && other.virtualmachinesecuritycode != null) || (this.virtualmachinesecuritycode != null && !this.virtualmachinesecuritycode.equals(other.virtualmachinesecuritycode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Virtualmachinesecurity[virtualmachinesecuritycode=" + virtualmachinesecuritycode + "]";
    }

}
