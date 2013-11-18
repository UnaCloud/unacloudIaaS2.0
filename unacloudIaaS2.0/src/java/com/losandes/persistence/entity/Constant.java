/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.persistence.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Clouder
 */
@Entity
public class Constant implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String keyString;

    private String value;

    public String getKeyString() {
        return keyString;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
