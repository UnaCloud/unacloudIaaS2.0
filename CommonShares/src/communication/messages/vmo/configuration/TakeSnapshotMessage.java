package communication.messages.vmo.configuration;

import communication.UnaCloudMessage;
import communication.messages.vmo.ConfigurationAbstractMessage;

/**
 *
 * @author Clouder
 */
public class TakeSnapshotMessage extends ConfigurationAbstractMessage{
    private static final long serialVersionUID = 6574606218582664239L;
	String hypervisor, destinationMachine, rutaVMRUN, snapshotname;

    public TakeSnapshotMessage(String hypervisor, String destinationMachine, String rutaVMRUN, String snapshotname) {
        super(VMC_TAKE_SNAPSHOT);
        this.hypervisor = hypervisor;
        this.destinationMachine = destinationMachine;
        this.rutaVMRUN = rutaVMRUN;
        this.snapshotname = snapshotname;
    }

    public TakeSnapshotMessage(UnaCloudMessage message) {
        super(VMC_TAKE_SNAPSHOT, message);
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

    public String getSnapshotname() {
        return snapshotname;
    }

    @Override
    protected void processMessage(UnaCloudMessage message) {
        hypervisor = message.getString(2);
        destinationMachine = message.getString(3);
        rutaVMRUN = message.getString(4);
        snapshotname=message.getString(5);
    }
}