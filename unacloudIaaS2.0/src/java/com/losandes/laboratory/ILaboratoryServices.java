package com.losandes.laboratory;

import javax.ejb.Local;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Interface responsible for exposing the Laboratory services
 */
@Local
public interface ILaboratoryServices {

    public boolean createLaboratory(com.losandes.persistence.entity.Laboratory lab);

    public boolean updateLaboratory(com.losandes.persistence.entity.Laboratory lab);

    public boolean deleteLaboratory(int code);

    public com.losandes.persistence.entity.Laboratory getLaboratoryByID(int laboratoryCode);

    public java.util.List<com.losandes.persistence.entity.LaboratoryType> getLaboratoryTypes();

    public java.util.List getLaboratories();
}// end of ILaboratoryServices

