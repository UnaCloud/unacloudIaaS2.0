package domain;

import static com.losandes.utils.Constants.LOGIN_DB;
import static com.losandes.utils.Constants.TURN_ON_DB;
import hypervisorManager.HypervisorFactory;

import monitoring.PhysicalMachineMonitor;
import monitoring.PhysicalMachineState;
import monitoring.PhysicalMachineStateReporter;
import physicalmachine.OperatingSystem;
import virtualMachineManager.PersistentExecutionManager;

import com.losandes.dataChannel.DataServerSocket;
import com.losandes.utils.VariableManager;

import communication.ClouderClientAttention;
import fileTransfer.TreeDistributionChannelManager;

/**
 * Responsible for starting the Clouder Client
 *
 */
public class Main {
	
    /**
     * Responsible for sorting and starting the Clouder Client
     * @param args[0] = {0 = TURN_OFF_DB, 1 = TURN_ON_DB , 2 = LOGIN_DB, 3 = LOGOUT_DB}
     */
    public static void main(String[] args){
        VariableManager.init();
        HypervisorFactory.registerHypervisors();
        int mainCase = 1;
        if (args != null && args.length>0 && !args[0].matches("[0-9]+"))mainCase = Integer.parseInt(args[0]);
        
        if(mainCase==LOGIN_DB) PhysicalMachineState.reportPhyisicalMachineLoggin(OperatingSystem.getUserName());
        else PhysicalMachineState.reportPhyisicalMachineState(mainCase);
        
        if(mainCase==TURN_ON_DB){
        	PhysicalMachineMonitor.restart();
            DataServerSocket.init();
            new PhysicalMachineStateReporter().start();
            new Thread(){
            @Override
            public void run() {
                super.run();
                    PersistentExecutionManager.loadData();
                }
            }.start();
            new TreeDistributionChannelManager();
            System.out.println("paso reporter4");
            ClouderClientAttention.getInstance().connect();
        }
    }
}

