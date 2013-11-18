package com.losandes.laboratory;

import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.Laboratory;
import com.losandes.persistence.entity.LaboratoryType;
import com.losandes.persistence.entity.PhysicalMachine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the Laboratory persistence services
 */
@Stateless
public class LaboratoryServices implements ILaboratoryServices {

    @EJB
    private IPersistenceServices persistenceServices;
    

    /**
     * Responsible for getting of the physical machines in a Laboratory
     */
    private List<PhysicalMachine> getPhysicalMachines(Laboratory lab, int numRows, int numColumns) {
        ArrayList<PhysicalMachine> physicalMachines = new ArrayList<PhysicalMachine>();
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numColumns; j++) {
                PhysicalMachine physicalMachine = new PhysicalMachine();
                physicalMachine.setPhysicalMachineName("machine " + i + " " + j);
                physicalMachine.setLaboratory(lab);
                physicalMachines.add(physicalMachine);
            }
        }
        return physicalMachines;
    }

    /**
     * Responsible for exposing the Laboratory create persistence services
     */
    public boolean createLaboratory(Laboratory lab) {
        //lab.setPhysicalMachine(getPhysicalMachines(lab, lab.getLaboratoryRows(), lab.getLaboratoryColumns()));
        return persistenceServices.create(lab);
    }

    /**
     * Responsible for exposing the Laboratory update persistence services
     */
    public boolean updateLaboratory(Laboratory lab) {
        return persistenceServices.update(lab)!=null;
    }

    /**
     * Responsible for exposing the Laboratory delete persistence services
     */
    public boolean deleteLaboratory(int code) {
        Laboratory laboratory = getLaboratoryByID(code);
        return persistenceServices.delete(laboratory);
    }

    /**
     * Responsible for exposing the Laboratory query by id
     */
    public Laboratory getLaboratoryByID(int laboratoryCode) {
        Laboratory laboratory = (Laboratory) persistenceServices.findById(Laboratory.class, laboratoryCode);
        Collections.sort(laboratory.getPhysicalMachine(), new Comparator<PhysicalMachine>(){
            @Override
            public int compare(PhysicalMachine o1, PhysicalMachine o2) {
                return o1.getPhysicalMachineName().compareTo(o2.getPhysicalMachineName());
            }

        });
        return laboratory;
    }

    /**
     * Responsible for exposing all Laboratory Types avaliable
     */
    public List<LaboratoryType> getLaboratoryTypes() {
        return persistenceServices.findAll(LaboratoryType.class);
    }

    /**
     * Responsible for exposing all Laboratories avaliable
     */
    public List getLaboratories() {
        return persistenceServices.findAll(Laboratory.class);
    }
}//end of LaboratoryServices

