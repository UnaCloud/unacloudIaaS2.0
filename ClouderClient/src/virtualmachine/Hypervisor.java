package virtualmachine;

import com.losandes.communication.messages.configuration.ExecuteCommandRequest;
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
    
    /**
     * This is the virtual machine name
     */
    private String virtualMachineName;

    public abstract int getHypervisorId();
    
    protected Hypervisor() {
    }



    protected void setExecutablePath(String executablePath) {
        this.executablePath = executablePath;
    }

    protected final void setVirtualMachinePath(String virtualMachinePath) {
        this.virtualMachinePath = virtualMachinePath.replace("\"", "");
        String filename=new File(this.virtualMachinePath).getName();
        setVirtualMachineName(filename.substring(0,filename.lastIndexOf(".")));
    }

    public String getExecutablePath() {
        return executablePath;
    }

    public String getVirtualMachinePath() {
        return virtualMachinePath;
    }

    public String getVirtualMachineName() {
        return virtualMachineName;
    }

    public void setVirtualMachineName(String virtualMachineName) {
        this.virtualMachineName = virtualMachineName;
    }
    
    
    //public abstract void preconfigureVirtualMachine(int coreNumber, int ramSize, String persistent) throws HypervisorOperationException;

    /**
     * Configures the managed virtual machine with the given core number, ram size and persistent properties. Then it starts it.
     * @param coreNumber the core number to configure the virtual machine.
     * @param ramSize the ram size to configure the virtual machine
     * @param persistent The persistent property
     * @throws HypervisorOperationException if there is an error configuring the virtual machine settings
     */
    public abstract void preconfigureAndStartVirtualMachine(int coreNumber, int ramSize, String persistent) throws HypervisorOperationException;
    
    /**
     * Turn on the managed virtual machine
     * @throws HypervisorOperationException if there is an error starting up the virtual machine
     */
    public final void turnOnVirtualMachine() throws HypervisorOperationException{
        preconfigureAndStartVirtualMachine(0,0,null);
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
    public abstract void executeCommandOnMachine(String user, String pass, ExecuteCommandRequest command) throws HypervisorOperationException;

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
    
    public abstract void changeVirtualMachineMac() throws HypervisorOperationException;
    /**
     * Returns an instance for this hypervisor class
     * @return an instance for this hypervisor class
     */
    protected abstract Hypervisor getInstance();
}
