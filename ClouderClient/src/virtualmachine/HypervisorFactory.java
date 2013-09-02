package virtualmachine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import static com.losandes.utils.Constants.*;

/**
 * Factory resposible for managing hypervisor classes and instances. This class provides methods to dinamically load hypervisor clasees and instantiate them.
 * @author Clouder
 */
public class HypervisorFactory {

    /**
     * All provide services must be statically accesed
     */
    private HypervisorFactory() {
    }
    /**
     * Map that contains a relation between hypervisor names and hypervisor objects
     */
    private static Map<String,Hypervisor> map = new HashMap<String, Hypervisor>();

    /**
     * Uses the map to search for hypervisor instances, if there is not an entry for the given name then it is loaded dinamically using java´s reflection API. If there is an associated object, then a new instance is returned by using the method getInstance from Hypervisor abstract class.
     * @param hypervisorName The hypervisor name to be instantiated
     * @param executablePath The executable route that represents the given hypervisor.
     * @param virtualMachinePath The virtual machine path to be associated with the hypervisor.
     * @return A managed hypervisor for the given name
     */
    public static Hypervisor getHypervisor(final String hypervisorName, String executablePath, String virtualMachinePath){
        Hypervisor c = map.get(hypervisorName);
        if(c==null)try {
            Object b=Class.forName("virtualmachine."+hypervisorName).newInstance();
            if(b instanceof Hypervisor)map.put(hypervisorName, c=(Hypervisor)b);
        } catch (Exception ex) {
        }
        if(c!=null){
            Hypervisor h=c.getInstance();
            h.setExecutablePath(executablePath);
            h.setVirtualMachinePath(virtualMachinePath);
            return h;
        }
        return new VoidHypervisor(hypervisorName);
    }

    /**
     * This method is used for compatibilty purposes. It recieves an integer representing the hypervisor name and not the name itself. This method checks which name is associated with the given number and calls getHypervisor with the appropiated name.
     * @param hypervisorName The hypervisor integer representing the hypervisor to be instantiated
     * @param executablePath The executable route that represents the given hypervisor.
     * @param virtualMachinePath The virtual machine path to be associated with the hypervisor.
     * @return A managed hypervisor for the given integer
     */
    public static Hypervisor getHypervisor(final int hypervisorName, String executablePath, String virtualMachinePath){
        if(hypervisorName==VMW)return getHypervisor("VMwareWorkstation",executablePath, virtualMachinePath);
        else if(hypervisorName == PLAYER)return getHypervisor("VMwarePlayer", executablePath, virtualMachinePath);
        return new VoidHypervisor(executablePath+"");
    }

    /**
     * Void hypervisor to be returned when a hypervisor is not found. All the methods of this class throws a HypervisorOperationException.
     */
    private static class VoidHypervisor extends Hypervisor{
        String hypervisorName;
        public VoidHypervisor(String hypervisorName) {
            this.hypervisorName = hypervisorName;
        }
        @Override
        public void turnOnVirtualMachine() throws HypervisorOperationException {
            throw new HypervisorOperationException("Hypervisor "+hypervisorName+" not found");
        }
        @Override
        public void turnOffVirtualMachine() throws HypervisorOperationException {
            throw new HypervisorOperationException("Hypervisor "+hypervisorName+" not found");
        }
        @Override
        public void restartVirtualMachine() throws HypervisorOperationException {
            throw new HypervisorOperationException("Hypervisor "+hypervisorName+" not found");
        }
        @Override
        public Hypervisor getInstance() {
            return this;
        }
        @Override
        public void preconfigureVirtualMachine(int coreNumber, int ramSize,String persitant) throws HypervisorOperationException {
            throw new HypervisorOperationException("Hypervisor "+hypervisorName+" not found");
        }

        @Override
        public void executeCommandOnMachine(String user, String pass, String command) throws HypervisorOperationException {
            throw new HypervisorOperationException("Hypervisor "+hypervisorName+" not found");
        }

        @Override
        public void copyFileOnVirtualMachine(String user, String pass, String destinationRoute, File sourceFile) throws HypervisorOperationException {
            throw new HypervisorOperationException("Hypervisor "+hypervisorName+" not found");
        }

        @Override
        public void takeSnapshotOnMachine(String snapshotname) throws HypervisorOperationException {
            throw new HypervisorOperationException("Hypervisor "+hypervisorName+" not found");
        }
    }

}
