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
 * Responsible for implementing the Laboratory Type entity
 */
@Entity
@NamedQueries( {@NamedQuery(name = "LaboratoryType.findByLaboratoryTypeName", query = "SELECT ln FROM LaboratoryType ln WHERE ln.laboratoryTypeName = :laboratoryTypeName")})
public class LaboratoryType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int laboratoryTypeCode;
    private String laboratoryTypeName;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getLaboratoryTypeCode() != null ? getLaboratoryTypeCode().hashCode() : 0);
        return hash;
    }


    @Override
    public String toString() {
        return "com.losandes.persistence.entity.LaboratoryType[id=" + getLaboratoryTypeCode() + "]";
    }

    /**
     * @return the laboratoryTypeCode
     */
    public Integer getLaboratoryTypeCode() {
        return laboratoryTypeCode;
    }

    /**
     * @param laboratoryTypeCode the laboratoryTypeCode to set
     */
    public void setLaboratoryTypeCode(Integer LaboratoryTypeCode) {
        this.laboratoryTypeCode = LaboratoryTypeCode;
    }

    /**
     * @return the laboratoryTypeName
     */
    public String getLaboratoryTypeName() {
        return laboratoryTypeName;
    }

    /**
     * @param laboratoryTypeName the laboratoryTypeName to set
     */
    public void setLaboratoryTypeName(String LaboratoryTypeName) {
        this.laboratoryTypeName = LaboratoryTypeName;
    }

}//end of LaboratoryType
