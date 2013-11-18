package com.losandes.hypervisor;

import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.Hypervisor;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the Hypervisor persistence services
 */
@Stateless
public class HypervisorServices implements IHypervisorServices {

    @EJB
    private IPersistenceServices persistenceServices;

    /**
     * Responsible for exposing the Hypervisor create persistence services
     */
    public boolean createHypervisor(Hypervisor hyp) {
        return persistenceServices.create(hyp);
    }

    /**
     * Responsible for exposing the Hypervisor update persistence services
     */
    public boolean updateHypervisor(Hypervisor hyp) {
        return persistenceServices.update(hyp)!=null;
    }

    /**
     * Responsible for exposing the Hypervisor delete persistence services
     */
    public boolean deleteHypervisor(int code) {
        Hypervisor hyp = getHypervisorByID(code);
        return persistenceServices.delete(hyp);
    }

    /**
     * Responsible for exposing the Hypervisor query by id
     */
    public Hypervisor getHypervisorByID(int systemUserID) {
        return (Hypervisor) persistenceServices.findById(Hypervisor.class, systemUserID);
    }

    /**
     * Responsible for exposing all the Hypervisors available
     */
    public List getHypervisors() {
        return persistenceServices.findAll(Hypervisor.class);
    }
}//end of HypervisorServices
