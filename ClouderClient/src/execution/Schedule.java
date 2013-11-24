package execution;

import java.util.TimerTask;
import virtualmachine.HypervisorFactory;

/**
 * @author Eduardo Rosales
 * Responsible for undeploying a virtual machine execution
 */
public class Schedule extends TimerTask {

    private int hypervisorName;
    private String hypervisorPath;
    private String hypervisorOperation;
    private String virtualMachinePath;

    /**
     * Constructor Method
     * @param execTime
     * @param hypName
     * @param hypPath
     * @param hypOpe
     * @param vmPath
     */
    public Schedule(int hypName, String hypPath, String hypOpe, String vmPath) {
        hypervisorName = hypName;
        hypervisorPath = hypPath;
        hypervisorOperation = hypOpe;
        virtualMachinePath = vmPath;
    }

    /**
     * Responsible for undeploying a virtual machine execution in an executionTime
     */
    public void run() {
    	HypervisorFactory.getHypervisor(getHypervisorName(), getHypervisorPath(), getVirtualMachinePath()).stopVirtualMachine();
    }

    /**
     * @return the hypervisorName
     */
    public int getHypervisorName() {
        return hypervisorName;
    }

    /**
     * @return the hypervisorPath
     */
    public String getHypervisorPath() {
        return hypervisorPath;
    }

    /**
     * @return the hypervisorOperation
     */
    public String getHypervisorOperation() {
        return hypervisorOperation;
    }

    /**
     * @return the virtualMachinePath
     */
    public String getVirtualMachinePath() {
        return virtualMachinePath;
    }
}//end of ExecutionTime
