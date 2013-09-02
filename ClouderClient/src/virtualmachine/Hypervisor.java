package virtualmachine;

import java.io.File;

/**
 * This class is an abstract class to be implemented by each hypervisor. It must be only instantiated by the hyperviso factory
 * @author Clouder
 */
public abstract class Hypervisor {

    /**
     * Path to this hypervisor executable
     */
    private String executablePath;
    
    /**
     * Path to this hypervisor managed vitual machine
     */
    private String virtualMachinePath;

    protected Hypervisor() {
    }



    protected void setExecutablePath(String executablePath) {
        this.executablePath = executablePath;
    }

    protected void setVirtualMachinePath(String virtualMachinePath) {
        this.virtualMachinePath = virtualMachinePath;
    }

    public String getExecutablePath() {
        return executablePath;
    }

    public String getVirtualMachinePath() {
        return virtualMachinePath;
    }

    /**
     * Configures the managed virtual machine with the given core number, ram size and persistent properties
     * @param coreNumber the core number to configure the virtual machine
     * @param ramSize the ram size to configure the virtual machine
     * @param persistent The persistent property
     * @throws HypervisorOperationException if there is an error configuring the virtual machine settings
     */
    public abstract void preconfigureVirtualMachine(int coreNumber, int ramSize, String persistent) throws HypervisorOperationException;

    /**
     * Turn on the managed virtual machine
     * @throws HypervisorOperationException if there is an error starting up the virtual machine
     */
    public abstract void turnOnVirtualMachine() throws HypervisorOperationException;

    /**
     * Configures the settings for this managed virtual machine and the starts it. It just call preconfigureVirtualMachine method and turnOffVirtualMachine;
     * @param coreNumber the core number to configure the virtual machine
     * @param ramSize the ram size to configure the virtual machine
     * @param persistent The persistent property
     * @throws HypervisorOperationException if there is an error configuring the virtual machine settings or starting it
     */
    public final void turnOnVirtualMachine(int coreNumber, int ramSize, String persistant) throws HypervisorOperationException {
        preconfigureVirtualMachine(coreNumber, ramSize, persistant);
        turnOffVirtualMachine();
    }

    /**
     * turns off the managed virtual machine
     * @throws HypervisorOperationException If there is an error stoping the virtual machine
     */
    public abstract void turnOffVirtualMachine() throws HypervisorOperationException;

    /**
     * Restarts the managed virtual machine
     * @throws HypervisorOperationException If there is an error restating the virtual machine
     */
    public abstract void restartVirtualMachine() throws HypervisorOperationException;

    /**
     * Executes a command on the managed virtual machine
     * @param user the operating system privileged user name that is going to execute the command
     * @param pass the operating system privileged user password that is going to execute the command
     * @param command The command to be executed
     * @throws HypervisorOperationException If there is an error executing the command
     */
    public abstract void executeCommandOnMachine(String user, String pass, String command) throws HypervisorOperationException;

    public abstract void takeSnapshotOnMachine(String snapshotname) throws HypervisorOperationException;

    /**
     * writes a file on the virtual machine file system
     * @param user the operating system privileged user name that is going to write the file
     * @param pass the operating system privileged user password that is going to write the file
     * @param destinationRoute the route on the virtual machine file system where the file is going to be writen
     * @param sourceFile The local file that contains the content of the copied file
     * @throws HypervisorOperationException If there is an error copying the file
     */
    public abstract void copyFileOnVirtualMachine(String user, String pass, String destinationRoute, File sourceFile) throws HypervisorOperationException;

    /**
     * Returns an instance for this hypervisor class
     * @return an instance for this hypervisor class
     */
    protected abstract Hypervisor getInstance();
}
