package virtualmachine;

/**
 * Exception to be used by hypervisors to notify hypervisor operations errors.
 * @author Clouder
 */
public class HypervisorOperationException extends Exception{
    protected HypervisorOperationException(String message) {
        super(message);
    }
}
