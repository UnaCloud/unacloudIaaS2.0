package domain;

import static com.losandes.utils.Constants.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import com.losandes.monitoring.MonitorReportGenerator;
import com.losandes.utils.VariableManager;

import physicalmachine.OperatingSystem;
import hypervisorManager.HypervisorFactory;
import monitoring.LoadDll;
import monitoring.PhysicalMachineMonitor;
import monitoring.PhysicalMachineState;
import monitoring.PhysicalMachineStateReporter;
import virtualMachineManager.PersistentExecutionManager;
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
    	System.out.println(MonitorReportGenerator.getInstance().generateStateReport());
    	PhysicalMachineMonitor.getInstance().initService();
    	HypervisorFactory.registerHypervisors();
        int mainCase = 1;
        if (args != null && args.length>0 && !args[0].matches("[0-9]+"))mainCase = Integer.parseInt(args[0]);
        
        if(!VariableManager.local.getBooleanValue("REGISTERED")){
        	PhysicalMachineState.registerPhysicalMachine();
        	VariableManager.local.setBooleanValue("REGISTERED",true);
        }
        if(mainCase==LOGIN_DB) PhysicalMachineState.reportPhyisicalMachineUserLogin();
        else if(mainCase==TURN_ON_DB){
        	{
        		//Validate if the user that is executing agent is system user
        		String user=OperatingSystem.getWhoAmI();
            	if(user!=null&&!user.toLowerCase().contains("system")){
            		System.out.println("You can't execute the agent as "+user);
            		System.exit(0);
            	}
        	}
        	//Load Library to use Sigar
        	new LoadDll().loadLibrary();
        	String dataPath = VariableManager.local.getStringValue("DATA_PATH");
        	if(dataPath==null||dataPath.isEmpty()){
        		System.out.println("DATA_PATH in local file is empty");
        		System.exit(0);
        	}
        	try {
        		//Create agent log file
            	PrintStream ps=new PrintStream(new FileOutputStream(VariableManager.local.getStringValue("DATA_PATH")+"log.txt",true),true){
            		@Override
            		public void println(String x) {
            			super.println(new Date()+" "+x);
            		}
            		@Override
            		public void println(Object x) {
            			super.println(new Date()+" "+x);
            		}
            	};
    			System.setOut(ps);
    			System.setErr(ps);
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
        	PhysicalMachineState.reportPhyisicalMachineStart();    	
     	    PhysicalMachineMonitor.getInstance().initService();
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

