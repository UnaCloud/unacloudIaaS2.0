package com.losandes.persistence.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Operating System Type entity
 */
@Entity
@NamedQueries( {@NamedQuery(name = "OperatingSystemType.findByOperatingSystemTypeName", query = "SELECT ost FROM OperatingSystemType ost WHERE ost.operatingSystemTypeName = :operatingSystemTypeName")})
public class OperatingSystemType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int operatingSystemTypeCode;
    private String operatingSystemTypeName;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getOperatingSystemTypeCode() != null ? getOperatingSystemTypeCode().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.OperatingSystemType[id=" + getOperatingSystemTypeCode() + "]";
    }

    /**
     * @return the operatingSystemTypeCode
     */
    public Integer getOperatingSystemTypeCode() {
        return operatingSystemTypeCode;
    }

    /**
     * @param operatingSystemTypeCode the operatingSystemTypeCode to set
     */
    public void setOperatingSystemTypeCode(Integer operatingSystemTypeCode) {
        this.operatingSystemTypeCode = operatingSystemTypeCode;
    }

    /**
     * @return the operatingSystemTypeName
     */
    public String getOperatingSystemTypeName() {
        return operatingSystemTypeName;
    }

    /**
     * @param operatingSystemTypeName the operatingSystemTypeName to set
     */
    public void setOperatingSystemTypeName(String operatingSystemTypeName) {
        this.operatingSystemTypeName = operatingSystemTypeName;
    }

}//end of OperatingSystemType