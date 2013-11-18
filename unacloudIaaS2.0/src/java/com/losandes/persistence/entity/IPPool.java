/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.persistence.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Clouder
 */
@Entity
public class IPPool implements Serializable {

    @Id
    private IPDirection initialIP;
    private int poolSize;
    private static final long serialVersionUID = 123L;

    List<IPDirection> directions;

    public IPDirection getInitialIP() {
        return initialIP;
    }

    public void setInitialIP(IPDirection initialIP) {
        this.initialIP = initialIP;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public List<IPDirection> getDirections() {
        return directions;
    }

    public void setDirections(List<IPDirection> directions) {
        this.directions = directions;
    }

    

}
