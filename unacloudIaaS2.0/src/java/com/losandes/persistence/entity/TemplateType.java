package com.losandes.persistence.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Template Type entity
 */
@Entity
public class TemplateType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int templateTypeCode;
    private String templateTypeName;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getTemplateTypeCode() != null ? getTemplateTypeCode().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.TemplateType[id=" + getTemplateTypeCode() + "]";
    }

    /**
     * @return the templateTypeCode
     */
    public Integer getTemplateTypeCode() {
        return templateTypeCode;
    }

    /**
     * @param templateTypeCode the templateTypeCode to set
     */
    public void setTemplateTypeCode(Integer templateCode) {
        this.templateTypeCode = templateCode;
    }

    /**
     * @return the templateTypeName
     */
    public String getTemplateTypeName() {
        return templateTypeName;
    }

    /**
     * @param templateTypeName the templateTypeName to set
     */
    public void setTemplateTypeName(String templateTypeName) {
        this.templateTypeName = templateTypeName;
    }
}//end of TemplateType

