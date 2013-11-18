package com.losandes.virtualmachinesecurity;

import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.VirtualMachineSecurity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the VirtualMachineSecurity persistence services
 */
@Stateless
public class VirtualMachineSecurityServices implements IVirtualMachineSecurityServices {

    @EJB
    private IPersistenceServices persistenceServices;

    /**
     * Responsible for exposing the Virtual Machine Security create persistence services
     */
    public boolean createVirtualMachineSecurity(VirtualMachineSecurity virtualMachineSecurity) {
        return persistenceServices.create(virtualMachineSecurity);
    }

    /**
     * Responsible for exposing the Virtual Machine Security update persistence services
     */
    public boolean updateVirtualMachineSecurity(VirtualMachineSecurity virtualMachineSecurity) {
        return persistenceServices.update(virtualMachineSecurity)!=null;
    }

    /**
     * Responsible for exposing the Virtual Machine Security delete persistence services
     */
    public boolean deleteVirtualMachineSecurity(int code) {
        VirtualMachineSecurity virtualMachineSecurity = getVirtualMachineSecurityByID(code);
        return persistenceServices.delete(virtualMachineSecurity);
    }

    /**
     * Responsible for exposing the Virtual Machine Security query by id
     */
    public VirtualMachineSecurity getVirtualMachineSecurityByID(int VirtualMachineSecurityID) {
        return (VirtualMachineSecurity) persistenceServices.findById(VirtualMachineSecurity.class, VirtualMachineSecurityID);
    }

    /**
     * Responsible for exposing all the Virtual Machine Security schemas available
     */
    public List getVirtualMachineSecurities() {
        return persistenceServices.findAll(VirtualMachineSecurity.class);
    }
}// end of VirtualMachineSecurityServices

