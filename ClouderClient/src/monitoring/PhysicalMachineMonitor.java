package monitoring;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import com.losandes.monitoring.LoaderDll;
import com.losandes.utils.VariableManager;

import unacloudEnums.MonitoringStatus;

public class PhysicalMachineMonitor {	

	private static PhysicalMachineMonitor instance;
	public static synchronized PhysicalMachineMonitor getInstance(){
		if(instance==null)instance=new PhysicalMachineMonitor();
		return instance;
	}
	private MonitorCPUAgent mc;
	private MonitorEnergyAgent me;
	private Controller c;

	private PhysicalMachineMonitor(){
		
	}
	public void initService() {			
		System.out.println("Config monitoring service");
		try {
			mc = new MonitorCPUAgent();	
			
			if(VariableManager.local.getBooleanValue("MONITORING_ENABLE_CPU"))mc.toEnable(VariableManager.local.getStringValue("LOG_CPU_PATH"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			VariableManager.local.setBooleanValue("MONITORING_ENABLE_CPU", false);
		}
		try {			
			me  = new MonitorEnergyAgent();
			
			if(VariableManager.local.getBooleanValue("MONITORING_ENABLE_ENERGY"))me.toEnable(VariableManager.local.getStringValue("LOG_ENERGY_PATH"));			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			VariableManager.local.setBooleanValue("MONITORING_ENABLE_ENERGY", false);
		}
		startService(true, true);
	}	
	public void startService(boolean energy, boolean cpu){
		try {
			System.out.println("Start monitoring service");
			int time  = (int)(Math.random()*60*60);	
			if(cpu)
				if(VariableManager.local.getBooleanValue("MONITORING_ENABLE_CPU")){
					LoaderDll.getInstance().loadLibrary();
					mc.offToInit(VariableManager.global.getIntValue("MONITOR_FREQUENCY_CPU"),
							VariableManager.global.getIntValue("MONITOR_REGISTER_FREQUENCY_CPU"), time);
					
				}else System.out.println("Monitoring CPU is disable");
		    if(energy)
		    	if(VariableManager.local.getBooleanValue("MONITORING_ENABLE_ENERGY")){
		    		me.offToInit(VariableManager.global.getIntValue("MONITOR_FREQUENCY_ENERGY"),
		    				VariableManager.global.getIntValue("MONITOR_REGISTER_FREQUENCY_ENERGY"), time);
		    		me.addEnergyPath(VariableManager.local.getStringValue("PATH_POWERLOG"));					
		    	}else System.out.println("Monitoring energy is disable");
		    if(c==null||!c.isAlive()){
		    	c = new Controller();
		    	c.addMonitor(mc);
		    	c.addMonitor(me);
		    	c.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mc.toError();
			me.toError();
		}
	}

	public void stopService(boolean energy, boolean cpu){
		System.out.println("Stop monitoring service");
		if(energy)me.toStop();		
		if(cpu)mc.toStop();				
	}
	
	public void enabledService(boolean energy, boolean cpu) {	
		System.out.println("Enable monitoring service");
		if(cpu){			
			try {
				mc.toEnable(VariableManager.local.getStringValue("LOG_CPU_PATH"));
				VariableManager.local.setBooleanValue("MONITORING_ENABLE_CPU", true);
			} catch (Exception e) {
				System.out.println("ERROR: "+e.getMessage());
			}			
		}
		if(energy){
			try {				
				me.toEnable(VariableManager.local.getStringValue("LOG_ENERGY_PATH"));
				VariableManager.local.setBooleanValue("MONITORING_ENABLE_ENERGY", true);
			} catch (Exception e) {
				System.out.println("ERROR: "+e.getMessage());
			}			
		}		
	}
	
	public void disableService(boolean energy, boolean cpu){
//		if(cpu)if(mc.getStatus()==MonitoringStatus.OFF){
//			VariableManager.local.setBooleanValue("MONITORING_ENABLE_CPU", false);
//			mc.setStatus(MonitoringStatus.DISABLE);
//		}if(energy)if(me.getStatus()==MonitoringStatus.OFF){
//			VariableManager.local.setBooleanValue("MONITORING_ENABLE_ENERGY", false);
//			me.setStatus(MonitoringStatus.DISABLE);
//		}	
	}
	
	public void updateService(int frE, int frC, int wsCpu, int wsEnergy, boolean energy, boolean cpu){
		System.out.println("Update monitoring service");
		int time  = (int)(Math.random()*60*60);	
		if(energy){
			VariableManager.global.setIntValue("MONITOR_FREQUENCY_ENERGY",frE);
			VariableManager.global.setIntValue("MONITOR_REGISTER_FREQUENCY_ENERGY",wsEnergy);
			String logEnergy = VariableManager.local.getStringValue("LOG_ENERGY_PATH");
			String path = VariableManager.local.getStringValue("PATH_POWERLOG");
			me.updateVariables(frE, wsEnergy, time);				
			me.setRecordPath(logEnergy);				
			me.setPowerlogPath(path);
		}
		if(cpu){
			VariableManager.global.setIntValue("MONITOR_FREQUENCY_CPU",frC);
			VariableManager.global.setIntValue("MONITOR_REGISTER_FREQUENCY_CPU",wsCpu);				
			String logCpu = VariableManager.local.getStringValue("LOG_CPU_PATH");
			mc.updateVariables(frC, wsCpu, time);				
			mc.setRecordPath(logCpu);
		}	
	}
	public MonitoringStatus getStatusEnergy(){
		return me==null?MonitoringStatus.DISABLE:me.getStatus();
	}
	public MonitoringStatus getStatusCpu(){
		return mc==null?MonitoringStatus.DISABLE:mc.getStatus();
	}
	
	private class Controller extends Thread{

		ArrayList<AbstractMonitor> monitors = new ArrayList<AbstractMonitor>();
		@Override
		public void run() {	
			for (AbstractMonitor monitor : monitors) {
				try {
					monitor.doInitial();
				} catch (Exception e) {
					e.printStackTrace();
					monitor.toError();
				}
			}
			while(isReady()){
				try { 					
					System.out.println(new Date()+" Init monitor processes");
					ArrayList<Thread> processes = new ArrayList<Thread>();
					for (AbstractMonitor monitor : monitors)
						if(monitor.isReady())processes.add(new Thread(monitor));									
				    for (Thread thread : processes) thread.start();
					for (Thread thread : processes) thread.join();
					System.out.println(new Date()+" Finish monitor processes");
				} catch (Exception e) {
					e.printStackTrace();
					for (AbstractMonitor monitor : monitors)
						monitor.toError();					
				}				
			}	
			for (AbstractMonitor monitor : monitors)
				if(monitor.isStopped())monitor.toOff();
		}
		public void addMonitor(AbstractMonitor monitor){
			monitors.add(monitor);
		}
		public boolean isReady(){
			for (AbstractMonitor abstractMonitor : monitors) {
				if(abstractMonitor.isReady())return true;
			}
			return false;
		}
	}
	public long getSpaceDirVMS(){
        try {
			long space = new File(VariableManager.local.getStringValue("DATA_PATH")).getFreeSpace();
			return space;
		} catch (Exception e) {
			return -1;
		}
    }
}
