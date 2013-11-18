package com.losandes.persistence.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Operating System entity
 */
@Entity
public class OperatingSystem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int operatingSystemCode;
    private String operatingSystemName;
    @OneToOne
    private OperatingSystemType operatingSystemType;
    private String configurationClass;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getOperatingSystemCode() != null ? getOperatingSystemCode().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.OperatingSystem[id=" + getOperatingSystemCode() + "]";
    }

    /**
     * @return the operatingSystemCode
     */
    public Integer getOperatingSystemCode() {
        return operatingSystemCode;
    }

    /**
     * @param operatingSystemCode the operatingSystemCode to set
     */
    public void setOperatingSystemCode(Integer operatingSystemCode) {
        this.operatingSystemCode = operatingSystemCode;
    }

    /**
     * @return the operatingSystemName
     */
    public String getOperatingSystemName() {
        return operatingSystemName;
    }

    /**
     * @param operatingSystemName the operatingSystemName to set
     */
    public void setOperatingSystemName(String operatingSystemName) {
        this.operatingSystemName = operatingSystemName;
    }

    /**
     * @return the operatingSystemType
     */
    public OperatingSystemType getOperatingSystemType() {
        return operatingSystemType;
    }

    /**
     * @param operatingSystemType the operatingSystemType to set
     */
    public void setOperatingSystemType(OperatingSystemType operatingSystemType) {
        this.operatingSystemType = operatingSystemType;
    }

    public String getConfigurationClass() {
        return configurationClass;
    }

    public void setConfigurationClass(String configurationClass) {
        this.configurationClass = configurationClass;
    }

    
}// end of OperatingSystem
