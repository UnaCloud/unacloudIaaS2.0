package Exceptions;

/**
 * Exception representing errors on virtual machine operations like configure, start, stop
 * @author Clouder
 */
public class VirtualMachineExecutionException extends Exception{

    public VirtualMachineExecutionException(String message){
        super(message);
        
    }



}
