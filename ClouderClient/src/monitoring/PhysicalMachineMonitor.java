package monitoring;


import unacloudEnums.MonitoringStatus;

import com.losandes.utils.VariableManager;

public class PhysicalMachineMonitor extends Thread {	

	private static PhysicalMachineMonitor instance;
	public static synchronized PhysicalMachineMonitor getInstance(){
		if(instance==null)instance=new PhysicalMachineMonitor();
		return instance;
	}
	//private static ArrayList<AbstractMonitor> services;
	//ArrayList<AbstractMonitor> services = new ArrayList<AbstractMonitor>();
	private MonitorCPUAgent mc;
	private MonitorEnergyAgent me;
	private boolean isRunning = false;
	
	
	private void monitorError() {
		VariableManager.local.setBooleanValue("MONITORING_ENABLE_CPU", false);
		VariableManager.local.setBooleanValue("MONITORING_ENABLE_ENERGY", false);
	}

	@Override
	public void run() {				
		while(me.isRunning()||mc.isRunning()){
			try { 
				mc.doInitial();
				me.doInitial();
			} catch (Exception e) {e.printStackTrace();}	
			try {
				if(mc.isRunning())mc.run();
				if(me.isRunning())me.run();
				if(me.isRunning()&&mc.isRunning()){mc.join();me.join();}			
			} catch (Exception e) {
				e.printStackTrace();
			}			
			if(mc.isStopped())mc.turnOff();
			if(me.isStopped())mc.turnOff();
		}	
	}
	
	public void initService() {			
		try {
			mc = new MonitorCPUAgent(VariableManager.local.getStringValue("LOG_CPU_PATH"));	
			me  = new MonitorEnergyAgent(VariableManager.local.getStringValue("LOG_ENERGY_PATH"));
		} catch (Exception e) {
			e.printStackTrace();
			monitorError();
		}
	}	
	public void startService(boolean energy, boolean cpu){
		try {
			int time  = (int)(Math.random()*60*60);	
			if(cpu)
				if(VariableManager.local.getBooleanValue("MONITORING_ENABLE_CPU")
					&&mc.getStatus()==MonitoringStatus.OFF){	
					int wsCpu = VariableManager.global.getIntValue("MONITOR_REGISTER_FREQUENCY_CPU");
					int frC = VariableManager.global.getIntValue("MONITOR_FREQUENCY_CPU");
					if(frC>0&&wsCpu>0&&frC<wsCpu){	
						mc.setFrecuency(frC); 
						mc.setWindowSizeTime(wsCpu);
						mc.setReduce(time);
						mc.setStatus(MonitoringStatus.INIT);	
					}						
			}
		    if(energy)
		    	if(VariableManager.local.getBooleanValue("MONITORING_ENABLE_ENERGY")
					&&me.getStatus()==MonitoringStatus.OFF){	
		    		int wsEnergy = VariableManager.global.getIntValue("MONITOR_REGISTER_FREQUENCY_ENERGY");
		    		int frE = VariableManager.global.getIntValue("MONITOR_FREQUENCY_ENERGY");
		    		if(frE>0&&wsEnergy>0&&frE<wsEnergy){
		    			String path = VariableManager.local.getStringValue("PATH_POWERLOG");		    													
		    			if(path!=null&&frE>0){
		    				me.setPowerlogPath(path);
							me.setFrecuency(frE);			
							me.setWindowSizeTime(wsEnergy);
							me.setReduce(time);		
							me.setStatus(MonitoringStatus.INIT);
						}						
					}	
				}
			if(!isRunning){isRunning = true;	this.run();}
		} catch (Exception e) {
			e.printStackTrace();
			monitorError();
		}
	}

	public void stopService(boolean energy, boolean cpu){
		if(energy)me.stopMonitor();
		if(cpu)me.stopMonitor();				
	}
	
	public void enabledService(boolean energy, boolean cpu){
		if(cpu&&mc.isDisable()){
			VariableManager.local.setBooleanValue("MONITORING_ENABLE_CPU", true);
			mc.setStatus(MonitoringStatus.OFF);
		}if(energy&&me.isDisable()){
			VariableManager.local.setBooleanValue("MONITORING_ENABLE_ENERGY", true);
			me.setStatus(MonitoringStatus.OFF);
		}		
	}
	
	public void disableService(boolean energy, boolean cpu){
		if(cpu)if(mc.getStatus()==MonitoringStatus.OFF){
			VariableManager.local.setBooleanValue("MONITORING_ENABLE_CPU", false);
			mc.setStatus(MonitoringStatus.DISABLE);
		}if(energy)if(me.getStatus()==MonitoringStatus.OFF){
			VariableManager.local.setBooleanValue("MONITORING_ENABLE_ENERGY", false);
			me.setStatus(MonitoringStatus.DISABLE);
		}	
	}
	
	public void updateService(int frE, int frC, int wsCpu, int wsEnergy, boolean energy, boolean cpu){
		int time  = (int)(Math.random()*60*60);	
		if(frE>0&&frC>0&&wsCpu>0&&wsEnergy>0&&frE<wsEnergy&&frC<wsCpu){
			if(energy){
				VariableManager.global.setIntValue("FRECUENCY_ENERGY",frE);
				VariableManager.global.setIntValue("MONITOR_REGISTER_FREQUENCY_ENERGY",wsEnergy);
				String logEnergy = VariableManager.local.getStringValue("LOG_ENERGY_PATH");
				String path = VariableManager.local.getStringValue("PATH_POWERLOG");
				me.setFrecuency(frE);
				me.setWindowSizeTime(wsEnergy);
				me.setRecordPath(logEnergy);				
				me.setPowerlogPath(path);
				me.setReduce(time);
			}
			if(cpu){
				VariableManager.global.setIntValue("FRECUENCY_CPU",frC);
				VariableManager.global.setIntValue("MONITOR_REGISTER_FREQUENCY_CPU",wsCpu);				
				String logCpu = VariableManager.local.getStringValue("LOG_CPU_PATH");
				mc.setFrecuency(frC);
				mc.setWindowSizeTime(wsCpu);
				mc.setRecordPath(logCpu);
				mc.setReduce(time);
			}
		}		
	}
	public MonitoringStatus getStatusEnergy(){
		return me==null?MonitoringStatus.DISABLE:me.getStatus();
	}
	public MonitoringStatus getStatusCpu(){
		return mc==null?MonitoringStatus.DISABLE:mc.getStatus();
	}
	//To testing
	
	//MonitorCPUAgent m = new MonitorCPUAgent("logCPU.txt");
	//private static int LIMIT_TIME = 60;
	//	int frE = 10;
	//	int frC = 10;
	//	String path = "C:\\Program Files\\Intel\\Power Gadget 3.0\\PowerLog3.0.exe";
}
