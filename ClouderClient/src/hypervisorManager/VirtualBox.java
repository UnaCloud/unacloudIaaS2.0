package hypervisorManager;

import static com.losandes.utils.Constants.ERROR_MESSAGE;

import java.io.File;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.losandes.utils.Constants;

import utils.AddressUtility;
import virtualMachineManager.LocalProcessExecutor;

/**
 * Implementation of hypervisor abstract class to give support for VMwarePlayer
 * hypervisor.
 */
public class VirtualBox extends Hypervisor {
	public static final String HYPERVISOR_ID=Constants.VIRTUAL_BOX;
    public VirtualBox(String path) {
		super(path);
	}

    @Override
    public void stopVirtualMachine(ImageCopy image){
		LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "controlvm", image.getVirtualMachineName(), "poweroff");
        sleep(2000);
    }
    @Override
	public void registerVirtualMachine(ImageCopy image){
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "registervm", image.getMainFile().getPath());
        sleep(15000);
    }
    @Override
	public void unregisterVirtualMachine(ImageCopy image){
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "unregistervm", image.getVirtualMachineName());
        sleep(15000);
    }
    @Override
    public void restartVirtualMachine(ImageCopy image) throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "controlvm", image.getVirtualMachineName(), "reset");
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(30000);
    }
    @Override
	public void startVirtualMachine(ImageCopy image) throws HypervisorOperationException {
        String h;
        if((h=LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "startvm", image.getVirtualMachineName(), "--type", "headless")).contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(30000);
        LocalProcessExecutor.executeCommandOutput("wmic","process","where","name=\"VBoxHeadless.exe\"","CALL","setpriority","64");
        sleep(1000);
    }
    @Override
    public void configureVirtualMachineHardware(int cores, int ram, ImageCopy image) throws HypervisorOperationException {
    	if(cores!=0&&ram!=0){
            LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "modifyvm", image.getVirtualMachineName(),"--memory",""+ram,"--cpus",""+cores);
            sleep(20000);
        }
    }
    @Override
    public void executeCommandOnMachine(ImageCopy image,String command, String... args) throws HypervisorOperationException {
        List<String> com = new ArrayList<>();
        Collections.addAll(com, getExecutablePath(), "--nologo", "guestcontrol", image.getVirtualMachineName(), "execute", "--image", command, "--username", image.getImage().getUsername(), "--password", image.getImage().getPassword(), "--wait-exit", "--");
        Collections.addAll(com, args);
        String h = LocalProcessExecutor.executeCommandOutput(com.toArray(new String[0]));
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(10000);
    }

    @Override
    public void copyFileOnVirtualMachine(ImageCopy image, String destinationRoute, File sourceFile) throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "guestcontrol", image.getVirtualMachineName(), "copyto", sourceFile.getAbsolutePath(), destinationRoute, "--username", image.getImage().getUsername(), "--password", image.getImage().getPassword());
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(10000);
    }

    @Override
    public void takeVirtualMachineSnapshot(ImageCopy image,String snapshotname){
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"snapshot",image.getVirtualMachineName(),"take",snapshotname);
        sleep(20000);
    }

    @Override
    public void changeVirtualMachineMac(ImageCopy image) throws HypervisorOperationException {
    	NetworkInterface ninterface=AddressUtility.getDefaultNetworkInterface();
    	LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "modifyvm", image.getVirtualMachineName(),"--bridgeadapter1",ninterface.getDisplayName(),"--macaddress1","auto");
        sleep(20000);
        
    }

    

	@Override
	public void restoreVirtualMachineSnapshot(ImageCopy image, String snapshotname) throws HypervisorOperationException {
		LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "snapshot", image.getVirtualMachineName(), "restorecurrent");
        sleep(20000);
	}

	@Override
	public boolean existsVirtualMachineSnapshot(ImageCopy image, String snapshotname) throws HypervisorOperationException {
		String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "snapshot", image.getVirtualMachineName(), "list");
        sleep(20000);
        return h != null && !h.contains("does not");
	}
	public void unregisterAllVms(){
		String[] h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "list","vms").split("\n|\r");
		for(String vm:h){
			LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "unregistervm", vm.split(" ")[1]);
	        sleep(15000);
		}
	}

	@Override
	public void cloneVirtualMachine(ImageCopy source, ImageCopy dest) {
		LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"clonevm",source.getVirtualMachineName(),"--snapshot","unacloudbase","--name",dest.getVirtualMachineName(),"--basefolder",dest.getMainFile().getParentFile().getParentFile().getAbsolutePath(),"--register");
		sleep(20000);
		takeVirtualMachineSnapshot(dest,"unacloudbase");
        unregisterVirtualMachine(dest);
	}
}