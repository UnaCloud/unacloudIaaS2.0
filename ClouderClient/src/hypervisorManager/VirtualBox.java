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
        /*if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }*/
    }
    @Override
	public void registerVirtualMachine(VirtualMachineImage image){
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "registervm", image.getMainFile().getPath());
        sleep(15000);
    }
    @Override
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
	public void startVirtualMachine(VirtualMachineImage image) throws HypervisorOperationException {
        String h;
        if((h=LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "startvm", image.getVirtualMachineName(), "--type", "headless")).contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(30000);
        LocalProcessExecutor.executeCommandOutput("wmic","process","where","name=\"VBoxHeadless.exe\"","CALL","setpriority","64");
    }
    @Override
    public void configureVirtualMachineHardware(int cores, int ram, VirtualMachineImage image) throws HypervisorOperationException {
    	if(cores!=0&&ram!=0){
            LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "modifyvm", image.getVirtualMachineName(),"--memory",""+ram,"--cpus",""+cores);
            sleep(10000);
        }
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
    public void takeVirtualMachineSnapshot(VirtualMachineImage image,String snapshotname) throws HypervisorOperationException {
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"snapshot",image.getVirtualMachineName(),"take",snapshotname);
    }

    @Override
    public void changeVirtualMachineMac(VirtualMachineImage image) throws HypervisorOperationException {
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "modifyvm", image.getVirtualMachineName(),"--macaddress1","auto");
    }

    private void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException ex) {
            Logger.getLogger(VirtualBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	@Override
	public void restoreVirtualMachineSnapshot(VirtualMachineImage image, String snapshotname) throws HypervisorOperationException {
		LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "snapshot", image.getVirtualMachineName(), "restorecurrent");
        sleep(10000);
	}

	@Override
	public boolean existsVirtualMachineSnapshot(VirtualMachineImage image, String snapshotname) throws HypervisorOperationException {
		String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "snapshot", image.getVirtualMachineName(), "list");
        return h != null && !h.contains("does not");
	}

}