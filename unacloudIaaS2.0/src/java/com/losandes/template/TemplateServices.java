package com.losandes.template;

import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.ElasticRule;
import com.losandes.persistence.entity.Template;
import com.losandes.persistence.entity.TemplateType;
import com.losandes.utils.Queries;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the Template persistence services
 */
@Stateless
public class TemplateServices implements ITemplateServices {

    @EJB
    private IPersistenceServices persistenceServices;
    
    /**
     * Responsible for exposing the Template create persistence services
     */
    public boolean createTemplate(Template template) {
        template.setElasticRule(new ElasticRule());
        return persistenceServices.create(template);
    }

    /**
     * Responsible for exposing the Template update persistence services
     */
    public boolean updateTemplate(Template template) {
        System.out.println("updateTemplate");
        //persistenceServices.update(template.getElasticRule());
        return persistenceServices.update(template)!=null;
    }

    /**
     * Responsible for exposing the Template delete persistence services
     */
    public boolean deleteTemplate(int code) {
        Template template = getTemplateByID(code);
        return persistenceServices.delete(template);
    }

    /**
     * Responsible for exposing the Template query by id
     */
    public Template getTemplateByID(int templateID) {
        return (Template) persistenceServices.findById(Template.class, templateID);
    }

    /**
     * Responsible for exposing all the Template Types available
     */
    public List<TemplateType> getTemplatesTypes() {
        return persistenceServices.findAll(TemplateType.class);
    }

    /**
     * Responsible for exposing all the Templates available
     */
    public List getTemplates() {
        return persistenceServices.findAll(Template.class);
    }

    /**
     * Responsible for exposing all the Templates available for execution
     */
    public List getAvailableTemplates(int operatingSystemSelected) {
        return persistenceServices.executeNativeSQLList(Queries.getTemplatesAvailable(operatingSystemSelected), Template.class);
    }

    /**
     * Responsible for exposing all the Templates available for fast execution by cloud users
     */
    public List getFastAvailableTemplates(String systemUserName) {
        return persistenceServices.executeNativeSQLList(Queries.getFastTemplatesAvailable(systemUserName), Template.class);
    }

    /**
     * Responsible for exposing all the Templates available for execution by grid users
     */
    public List getGridAvailableTemplates(int operatingSystemSelected, String systemUserName) {
        return persistenceServices.executeNativeSQLList(Queries.getGridTemplatesAvailable(operatingSystemSelected, systemUserName), Template.class);
    }

    /**
     * Responsible for exposing all the Templates available for fast execution by grid users
     */
    public List getFastGridAvailableTemplates(String systemUserName) {
        return persistenceServices.executeNativeSQLList(Queries.getFastGridTemplatesAvailable(systemUserName), Template.class);
    }

    /**
     * Responsible for exposing the Template query by name
     */
    public Template getTemplateByName(String templateName) {
        return (Template) persistenceServices.findByName(Template.class,templateName);
    }
}//end od TemplateServices

