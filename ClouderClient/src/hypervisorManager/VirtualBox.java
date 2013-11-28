package hypervisorManager;

import static com.losandes.utils.Constants.ERROR_MESSAGE;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.losandes.utils.Constants;

import virtualMachineManager.LocalProcessExecutor;
import virtualMachineManager.VirtualMachineExecution;
import virtualMachineManager.VirtualMachineImage;

/**
 * Implementation of hypervisor abstract class to give support for VMwarePlayer
 * hypervisor.
 */
class VirtualBox extends Hypervisor {
	public static final String HYPERVISOR_ID=Constants.VIRTUAL_BOX;
    public VirtualBox(String path) {
		super(path);
	}

    @Override
    public void stopVirtualMachine(VirtualMachineImage image){
        sleep(2000);
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "controlvm", image.getVirtualMachineName(), "poweroff");
        unregisterVirtualMachine(image);
        /*if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }*/
    }
    public void registerVirtualMachine(VirtualMachineImage image){
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "registervm", image.getMainFile().getPath());
        sleep(15000);
    }
    public void unregisterVirtualMachine(VirtualMachineImage image){
        sleep(15000);
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "unregistervm", image.getVirtualMachineName());
    }
    @Override
    public void restartVirtualMachine(VirtualMachineImage image) throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "controlvm", image.getVirtualMachineName(), "reset");
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }
    @Override
	public void preconfigureAndStartVirtualMachine(VirtualMachineExecution execution) throws HypervisorOperationException {
        registerVirtualMachine(execution.getImage());
        if (hasSnapshot(execution.getImage())) {
            LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "snapshot", execution.getImage().getVirtualMachineName(), "restorecurrent");
            sleep(10000);
        }
        if(execution.getCores()!=0&&execution.getMemory()!=0){
            LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "modifyvm", execution.getImage().getVirtualMachineName(),"--memory",""+execution.getMemory(),"--cpus",""+execution.getCores());
            sleep(10000);
        }
        String h;
        if ((h=LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "startvm", execution.getImage().getVirtualMachineName(), "--type", "headless")).contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(30000);
        LocalProcessExecutor.executeCommandOutput("wmic","process","where","name=\"VBoxHeadless.exe\"","CALL","setpriority","64");
    }
    @Override
    public void executeCommandOnMachine(VirtualMachineImage image,String command, String... args) throws HypervisorOperationException {
        List<String> com = new ArrayList<>();
        Collections.addAll(com, getExecutablePath(), "--nologo", "guestcontrol", image.getVirtualMachineName(), "execute", "--image", command, "--username", image.getUsername(), "--password", image.getPassword(), "--wait-exit", "--");
        Collections.addAll(com, args);
        String h = LocalProcessExecutor.executeCommandOutput(com.toArray(new String[0]));
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(10000);
    }

    @Override
    public void copyFileOnVirtualMachine(VirtualMachineImage image, String destinationRoute, File sourceFile) throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "guestcontrol", image.getVirtualMachineName(), "copyto", sourceFile.getAbsolutePath(), destinationRoute, "--username", image.getUsername(), "--password", image.getPassword());
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(10000);
    }

    @Override
    public void takeSnapshotOnMachine(VirtualMachineImage image,String snapshotname) throws HypervisorOperationException {
        registerVirtualMachine(image);
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"snapshot",image.getVirtualMachineName(),"take",snapshotname);
        unregisterVirtualMachine(image);
    }

    @Override
    public void changeVirtualMachineMac(VirtualMachineImage image) throws HypervisorOperationException {
        registerVirtualMachine(image);
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "modifyvm", image.getVirtualMachineName(),"--macaddress1","auto");
        unregisterVirtualMachine(image);
    }

    
    private boolean hasSnapshot(VirtualMachineImage image){
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "snapshot", image.getVirtualMachineName(), "list");
        return h != null && !h.contains("does not");
    }

    private void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException ex) {
            Logger.getLogger(VirtualBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}