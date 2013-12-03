package hypervisorManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import virtualMachineManager.VirtualMachineExecution;
import virtualMachineManager.VirtualMachineImage;

/**
 * Factory responsible for managing hypervisor classes and instances. This class provides methods to dinamically load hypervisor clasees and instantiate them.
 * @author Clouder
 */
public class HypervisorFactory {
    /**
     * All provide services must be statically accessed
     */
    private HypervisorFactory() {
    }
    /**
     * Map that contains a relation between hypervisor names and hypervisor objects
     */
    private static Map<String,Hypervisor> map = new HashMap<>();
    
    public static void registerHypervisors(){
    	map.put(VMwareWorkstation.HYPERVISOR_ID,new VMwareWorkstation(""));
    	map.put(VMwarePlayer.HYPERVISOR_ID,new VMwarePlayer(""));
    	map.put(VirtualBox.HYPERVISOR_ID,new VirtualBox(""));
    }

    /**111
     * Uses the map to search for hypervisor instances, if there is not an entry for the given name then it is loaded dinamically using java´s reflection API. If there is an associated object, then a new instance is returned by using the method getInstance from Hypervisor abstract class.
     * @param hypervisorName The hypervisor name to be instantiated
     * @param executablePath The executable route that represents the given hypervisor.
     * @param virtualMachinePath The virtual machine path to be associated with the hypervisor.
     * @return A managed hypervisor for the given name
     */
    public static Hypervisor getHypervisor(final String hypervisorId){
    	Hypervisor c = map.get(hypervisorId);
        if(c==null)return new VoidHypervisor(hypervisorId);
        return c;
    }

    /**
     * Void hypervisor to be returned when a hypervisor is not found. All the methods of this class throws a HypervisorOperationException.
     */
    private static class VoidHypervisor extends Hypervisor{
    	String hypervisorId;
    	public VoidHypervisor(String hypervisorId) {
			super("");
			this.hypervisorId=hypervisorId;
		}
		@Override
		public void preconfigureAndStartVirtualMachine(VirtualMachineExecution execution)throws HypervisorOperationException {
			throw new HypervisorOperationException("Hypervisor "+hypervisorId+" not found");
		}
		@Override
		public void stopVirtualMachine(VirtualMachineImage image) {
		}
		@Override
		public void restartVirtualMachine(VirtualMachineImage image)throws HypervisorOperationException {
			throw new HypervisorOperationException("Hypervisor "+hypervisorId+" not found");			
		}
		@Override
		public void executeCommandOnMachine(VirtualMachineImage image,String command, String... args)throws HypervisorOperationException {
			throw new HypervisorOperationException("Hypervisor "+hypervisorId+" not found");
		}
		@Override
		public void takeSnapshotOnMachine(VirtualMachineImage image,String snapshotname) throws HypervisorOperationException {
			throw new HypervisorOperationException("Hypervisor "+hypervisorId+" not found");
		}
		@Override
		public void copyFileOnVirtualMachine(VirtualMachineImage image,String destinationRoute, File sourceFile)throws HypervisorOperationException {
			throw new HypervisorOperationException("Hypervisor "+hypervisorId+" not found");
		}
		@Override
		public void changeVirtualMachineMac(VirtualMachineImage image)throws HypervisorOperationException {
			throw new HypervisorOperationException("Hypervisor "+hypervisorId+" not found");
		}
		@Override
		public void registerVirtualMachine(VirtualMachineImage image) {
		}
		@Override
		public void unregisterVirtualMachine(VirtualMachineImage image) {
		}
    }
}