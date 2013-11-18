package com.losandes.persistence.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Virtual Machine Security entity
 */
@Entity
public class VirtualMachineSecurity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int virtualMachineSecurityCode;
    private String virtualMachineSecurityName;
    private String virtualMachineSecurityUser;
    private String virtualMachineSecurityPassword;
    private int virtualMachineSecurityPort;
    private String virtualMachineSecurityAccess;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getVirtualMachineSecurityCode() != null ? getVirtualMachineSecurityCode().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.VirtualMachineSecurity[id=" + getVirtualMachineSecurityCode() + "]";
    }

    /**
     * @return the virtualMachineSecurityCode
     */
    public Integer getVirtualMachineSecurityCode() {
        return virtualMachineSecurityCode;
    }

    /**
     * @param virtualMachineSecurityCode the virtualMachineSecurityCode to set
     */
    public void setVirtualMachineSecurityCode(Integer virtualMachineSecurityCode) {
        this.virtualMachineSecurityCode = virtualMachineSecurityCode;
    }

    /**
     * @return the virtualMachineSecurityUser
     */
    public String getVirtualMachineSecurityUser() {
        return virtualMachineSecurityUser;
    }

    /**
     * @param virtualMachineSecurityUser the virtualMachineSecurityUser to set
     */
    public void setVirtualMachineSecurityUser(String virtualMachineSecurityUser) {
        this.virtualMachineSecurityUser = virtualMachineSecurityUser;
    }

    /**
     * @return the virtualMachineSecurityPassword
     */
    public String getVirtualMachineSecurityPassword() {
        return virtualMachineSecurityPassword;
    }

    /**
     * @param virtualMachineSecurityPassword the virtualMachineSecurityPassword to set
     */
    public void setVirtualMachineSecurityPassword(String virtualMachineSecurityPassword) {
        this.virtualMachineSecurityPassword = virtualMachineSecurityPassword;
    }

    /**
     * @return the virtualMachineSecurityPort
     */
    public int getVirtualMachineSecurityPort() {
        return virtualMachineSecurityPort;
    }

    /**
     * @param virtualMachineSecurityPort the virtualMachineSecurityPort to set
     */
    public void setVirtualMachineSecurityPort(int virtualMachineSecurityPort) {
        this.virtualMachineSecurityPort = virtualMachineSecurityPort;
    }

    /**
     * @return the virtualMachineSecurityAccess
     */
    public String getVirtualMachineSecurityAccess() {
        return virtualMachineSecurityAccess;
    }

    /**
     * @param virtualMachineSecurityAccess the virtualMachineSecurityAccess to set
     */
    public void setVirtualMachineSecurityAccess(String virtualMachineSecurityAccess) {
        this.virtualMachineSecurityAccess = virtualMachineSecurityAccess;
    }

    /**
     * @return the virtualMachineSecurityName
     */
    public String getVirtualMachineSecurityName() {
        return virtualMachineSecurityName;
    }

    /**
     * @param virtualMachineSecurityName the virtualMachineSecurityName to set
     */
    public void setVirtualMachineSecurityName(String virtualMachineSecurityName) {
        this.virtualMachineSecurityName = virtualMachineSecurityName;
    }
}// end of VirtualMachineSecurity
