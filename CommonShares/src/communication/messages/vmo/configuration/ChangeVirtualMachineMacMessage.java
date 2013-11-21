package communication.messages.vmo.configuration;

import communication.UnaCloudMessage;
import communication.messages.vmo.ConfigurationAbstractMessage;

public class ChangeVirtualMachineMacMessage extends ConfigurationAbstractMessage{
    private static final long serialVersionUID = -1790598953894863802L;
	String hypervisor, destinationMachine, rutaVMRUN;

    public ChangeVirtualMachineMacMessage(String hypervisor, String destinationMachine, String rutaVMRUN) {
        super(VMC_CHANGE_MAC);
        this.hypervisor = hypervisor;
        this.destinationMachine = destinationMachine;
        this.rutaVMRUN = rutaVMRUN;
    }
        
    public ChangeVirtualMachineMacMessage(UnaCloudMessage message){
        super(VMC_CHANGE_MAC,message);
    }

    public String getHypervisor() {
        return hypervisor;
    }

    public String getDestinationMachine() {
        return destinationMachine;
    }

    public String getRutaVMRUN() {
        return rutaVMRUN;
    }
    
    @Override
    public void processMessage(UnaCloudMessage message){
        hypervisor = message.getString(2);
        destinationMachine = message.getString(3);
        rutaVMRUN = message.getString(4);
    }
}