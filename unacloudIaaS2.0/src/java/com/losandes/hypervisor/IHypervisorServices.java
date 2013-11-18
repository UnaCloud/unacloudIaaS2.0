package com.losandes.hypervisor;

import javax.ejb.Local;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Interface responsible for exposing the Hypervisor services
 */
@Local
public interface IHypervisorServices {

    public boolean createHypervisor(com.losandes.persistence.entity.Hypervisor hyp);

    public boolean updateHypervisor(com.losandes.persistence.entity.Hypervisor hyp);

    public boolean deleteHypervisor(int code);

    public com.losandes.persistence.entity.Hypervisor getHypervisorByID(int systemUserID);

    public java.util.List getHypervisors();
}//end of IHypervisorServices

