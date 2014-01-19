package hypervisorManager;

import java.io.File;

import virtualMachineManager.Image;

/**
 * This class is an abstract class to be implemented by each hypervisor. It must be only instantiated by the hyperviso factory
 * @author Clouder
 */
public abstract class Hypervisor {

    /**
     * Path to this hypervisor executable
     */
    private String executablePath;
    
    public Hypervisor(String path){
    	this.executablePath=path;
    }

    public String getExecutablePath() {
        return executablePath;
    }

    /**
     * Configures the managed virtual machine with the given core number, ram size and persistent properties. Then it starts it.
     * @param coreNumber the core number to configure the virtual machine.
     * @param ramSize the ram size to configure the virtual machine
     * @param persistent The persistent property
     * @throws HypervisorOperationException if there is an error configuring the virtual machine settings
     */
    public abstract void startVirtualMachine(Image image) throws HypervisorOperationException;
    
    public abstract void configureVirtualMachineHardware(int cores,int ram,Image image) throws HypervisorOperationException;
    /**
     * turns off the managed virtual machine
     * @throws HypervisorOperationException If there is an error stoping the virtual machine
     */
    public abstract void stopVirtualMachine(Image image);

    /**
     * Restarts the managed virtual machine
     * @throws HypervisorOperationException If there is an error restating the virtual machine
     */
    public abstract void restartVirtualMachine(Image image) throws HypervisorOperationException;

    /**
     * Executes a command on the managed virtual machine
     * @param user the operating system privileged user name that is going to execute the command
     * @param pass the operating system privileged user password that is going to execute the command
     * @param command The command to be executed
     * @throws HypervisorOperationException If there is an error executing the command
     */
    public abstract void executeCommandOnMachine(Image image, String command,String...args) throws HypervisorOperationException;

    public abstract void takeVirtualMachineSnapshot(Image image,String snapshotname) throws HypervisorOperationException;

    public abstract void restoreVirtualMachineSnapshot(Image image,String snapshotname)throws HypervisorOperationException;
    
    public abstract boolean existsVirtualMachineSnapshot(Image image,String snapshotname)throws HypervisorOperationException;
    
    /**
     * writes a file on the virtual machine file system
     * @param user the operating system privileged user name that is going to write the file
     * @param pass the operating system privileged user password that is going to write the file
     * @param destinationRoute the route on the virtual machine file system where the file is going to be writen
     * @param sourceFile The local file that contains the content of the copied file
     * @throws HypervisorOperationException If there is an error copying the file
     */
    public abstract void copyFileOnVirtualMachine(Image image,String destinationRoute, File sourceFile) throws HypervisorOperationException;
    
    public abstract void changeVirtualMachineMac(Image image) throws HypervisorOperationException;
    
    public abstract void registerVirtualMachine(Image image);
    public abstract void unregisterVirtualMachine(Image image);
}
