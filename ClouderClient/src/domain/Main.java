package domain;

import static com.losandes.utils.Constants.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import physicalmachine.OperatingSystem;
import hypervisorManager.HypervisorFactory;
import monitoring.PhysicalMachineMonitor;
import monitoring.PhysicalMachineState;
import monitoring.PhysicalMachineStateReporter;
import virtualMachineManager.PersistentExecutionManager;

import com.losandes.utils.VariableManager;

import communication.ClouderClientAttention;

/**
 * Responsible for starting the Clouder Client
 *
 */
public class Main {
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

    /**
     * Responsible for sorting and starting the Clouder Client
     * @param args[0] = {0 = TURN_OFF_DB, 1 = TURN_ON_DB , 2 = LOGIN_DB, 3 = LOGOUT_DB}
     */
    public static void main(String[] args){
    	HypervisorFactory.registerHypervisors();
        int mainCase = 1;
        if (args != null && args.length>0 && !args[0].matches("[0-9]+"))mainCase = Integer.parseInt(args[0]);
        
        if(!VariableManager.local.getBooleanValue("REGISTERED")){
        	PhysicalMachineState.registerPhysicalMachine();
        	VariableManager.local.setBooleanValue("REGISTERED",true);
        }
        if(mainCase==LOGIN_DB) PhysicalMachineState.reportPhyisicalMachineUserLogin();
        else if(mainCase==TURN_ON_DB){
        	//TODO quitar esto
        	{
        		String user=OperatingSystem.getWhoAmI();
            	if(user!=null&&!user.toLowerCase().contains("system")){
            		System.out.println("No se puede ejecutar el agente como "+user);
            		System.exit(0);
            	}
        	}
        	try {
            	PrintStream ps=new PrintStream(new FileOutputStream("log.txt"),true);
    			System.setOut(ps);
    			System.setErr(ps);
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
        	PhysicalMachineState.reportPhyisicalMachineStart();
        	PhysicalMachineMonitor.restart();
            //DataServerSocket.init();
            PhysicalMachineStateReporter.getInstance().start();
            PersistentExecutionManager.loadData();
            //new TreeDistributionChannelManager();
            ClouderClientAttention.getInstance().connect();
        }
        else if(mainCase==TURN_OFF_DB) PhysicalMachineState.reportPhyisicalMachineStop();
        else if(mainCase==LOGOUT_DB) PhysicalMachineState.reportPhyisicalMachineUserLogoff();
    }
}

