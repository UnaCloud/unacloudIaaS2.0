/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.transfer;

import com.losandes.virtualmachine.PairMachineExecution;
import java.io.File;
import java.util.*;
import javax.ejb.Local;

/**
 * Interface that exposes the services to copy files on the infrastrucutre. This interface is used to copy virtual machines.
 * @author Clouder
 */
@Local
public interface FileSenderLocal {

    public void sendFolder(PairMachineExecution[] receivers, File folder, String remotePath) ;

    public void refreshExecutions(PairMachineExecution[] receivers, String message, int current);

    public void setMachineError(PairMachineExecution receiver, String message);

}
