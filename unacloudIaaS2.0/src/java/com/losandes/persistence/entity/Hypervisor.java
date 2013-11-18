package com.losandes.persistence.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Hypervisor entity
 */
@Entity
public class Hypervisor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hypervisorCode;
    private String hypervisorName;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getHypervisorCode() != null ? getHypervisorCode().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.Hypervisor[id=" + getHypervisorCode() + "]";
    }

    /**
     * @return the hypervisorName
     */
    public String getHypervisorName() {
        return hypervisorName;
    }

    /**
     * @param hypervisorName the hypervisorName to set
     */
    public void setHypervisorName(String hypervisorName) {
        this.hypervisorName = hypervisorName;
    }

    /**
     * @return the hypervisorCode
     */
    public Integer getHypervisorCode() {
        return hypervisorCode;
    }

    /**
     * @param hypervisorCode the hypervisorCode to set
     */
    public void setHypervisorCode(Integer hypervisorCode) {
        this.hypervisorCode = hypervisorCode;
    }
}//end of Hypervisor

