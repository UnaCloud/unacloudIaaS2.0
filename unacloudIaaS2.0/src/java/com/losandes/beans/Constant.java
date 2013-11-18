/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Clouder
 */
@Entity
@Table(name = "constant")
@NamedQueries({
    @NamedQuery(name = "Constant.findAll", query = "SELECT c FROM Constant c"),
    @NamedQuery(name = "Constant.findByKeystring", query = "SELECT c FROM Constant c WHERE c.keystring = :keystring"),
    @NamedQuery(name = "Constant.findByValue", query = "SELECT c FROM Constant c WHERE c.value = :value")})
public class Constant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "KEYSTRING")
    private String keystring;
    @Column(name = "VALUE")
    private String value;

    public Constant() {
    }

    public Constant(String keystring) {
        this.keystring = keystring;
    }

    public String getKeystring() {
        return keystring;
    }

    public void setKeystring(String keystring) {
        this.keystring = keystring;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (keystring != null ? keystring.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Constant)) {
            return false;
        }
        Constant other = (Constant) object;
        if ((this.keystring == null && other.keystring != null) || (this.keystring != null && !this.keystring.equals(other.keystring))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Constant[keystring=" + keystring + "]";
    }

}
