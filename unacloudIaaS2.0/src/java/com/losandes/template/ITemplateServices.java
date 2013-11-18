package com.losandes.template;

import com.losandes.persistence.entity.Template;
import javax.ejb.Local;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Interface responsible for exposing the Template services
 */
@Local
public interface ITemplateServices {

    public boolean createTemplate(com.losandes.persistence.entity.Template template);

    public boolean updateTemplate(com.losandes.persistence.entity.Template template);

    public boolean deleteTemplate(int code);

    public com.losandes.persistence.entity.Template getTemplateByID(int templateID);

    public java.util.List<com.losandes.persistence.entity.TemplateType> getTemplatesTypes();

    public java.util.List getTemplates();

    public java.util.List getAvailableTemplates(int operatingSystemSelected);

    public java.util.List getGridAvailableTemplates(int operatingSystemSelected, String systemUserName);

    public java.util.List getFastGridAvailableTemplates(String systemUserName);

    public java.util.List getFastAvailableTemplates(String systemUserName);

    public Template getTemplateByName(String templateName);
}// end of ITemplateServices

