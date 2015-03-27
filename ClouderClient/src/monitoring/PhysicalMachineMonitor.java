package monitoring;

import java.util.ArrayList;

import unacloudEnums.MonitoringStatus;

import com.losandes.utils.VariableManager;

public class PhysicalMachineMonitor extends Thread {
	
	
	public static MonitoringStatus status = MonitoringStatus.OFF;
	private static PhysicalMachineMonitor instance;
	public static synchronized PhysicalMachineMonitor getInstance(){
		if(instance==null)instance=new PhysicalMachineMonitor();
		return instance;
	}
	private static final ArrayList<AbstractMonitor> services = new ArrayList<AbstractMonitor>();
    //"C:\\Agentes\\UnaCloud\\logCPU.txt"
	//"C:\\Agentes\\UnaCloud\\report.txt"
	
	public PhysicalMachineMonitor() {}	
	
	@Override
	public void run() {	
		try { for (AbstractMonitor abstractMonitor : services) abstractMonitor.doInitial();
		} catch (Exception e) {e.printStackTrace();}	   
		while(status == MonitoringStatus.RUNNING){
			try {
				for (AbstractMonitor abstractMonitor : services) abstractMonitor.run();
				for (AbstractMonitor abstractMonitor : services) abstractMonitor.join();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		status=MonitoringStatus.RESUME;
	}
	
	public void initService() {	
		try {
			if(VariableManager.local.getBooleanValue("MONITORING_ENABLE")){			
				if(status == MonitoringStatus.OFF){
					services.clear();
					int frE = VariableManager.global.getIntValue("FRECUENCY_ENERGY");
					int frC = VariableManager.global.getIntValue("FRECUENCY_CPU");
					int wsCpu = VariableManager.global.getIntValue("MONITOR_REGISTER_FREQUENCY_CPU");
					int wsEnergy = VariableManager.global.getIntValue("MONITOR_REGISTER_FREQUENCY_ENERGY");
//					int frE = 10;
//					int frC = 10;
//					int wsCpu = 60;
//					int wsEnergy =60;
					if(frE>0&&frC>0&&wsCpu>0&&wsEnergy>0&&frE<wsEnergy&&frC<wsCpu){
						String path = VariableManager.local.getStringValue("PATH_POWERLOG");
						int time  = (int)(Math.random()*60*60);					
						if(frC>0){
							MonitorCPUAgent m = new MonitorCPUAgent(VariableManager.local.getStringValue("LOG_CPU_PATH"));						
							m.setFrecuency(frC); 
							m.setWindowSizeTime(wsCpu);
							m.setReduce(time);
							services.add(m);
						}					
						if(path!=null&&frE>0){
							MonitorEnergyAgent me = new MonitorEnergyAgent(VariableManager.local.getStringValue("LOG_ENERGY_PATH"));
							me.setPowerlogPath(path);
							me.setFrecuency(frE);			
							me.setWindowSizeTime(wsEnergy);
							me.setReduce(time);
							services.add(me);
						}
						if(services.size()>0){
							status = MonitoringStatus.RUNNING;
							this.run();									
						}		
					}					
				}
				else if(status == MonitoringStatus.RESUME){
					if(services.size()>0){
						status = MonitoringStatus.RUNNING;
						this.run();									
					}else status = MonitoringStatus.OFF;
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			status = MonitoringStatus.OFF;
		}
			
	}	
	public void stopService(){		
		VariableManager.local.setBooleanValue("MONITORING_ENABLE", false);
		status = MonitoringStatus.STOPPED;
	}
	
	public void enabledService(){
		VariableManager.local.setBooleanValue("MONITORING_ENABLE", true);
	}
	
	public void updateService(int frE, int frC, int wsCpu, int wsEnergy){
		
		if(frE>0&&frC>0&&wsCpu>0&&wsEnergy>0&&frE<wsEnergy&&frC<wsCpu){
			VariableManager.global.setIntValue("FRECUENCY_ENERGY",frE);
			VariableManager.global.setIntValue("FRECUENCY_CPU",frC);
			VariableManager.global.setIntValue("MONITOR_REGISTER_FREQUENCY_CPU",wsCpu);
			VariableManager.global.setIntValue("MONITOR_REGISTER_FREQUENCY_ENERGY",wsEnergy);
			String path = VariableManager.local.getStringValue("PATH_POWERLOG");
			String logCpu = VariableManager.local.getStringValue("LOG_CPU_PATH");
			String logEnergy = VariableManager.local.getStringValue("LOG_ENERGY_PATH");
			
			int time  = (int)(Math.random()*60*60);		
			for (AbstractMonitor abstractMonitor : services) {
				if(abstractMonitor instanceof MonitorEnergyAgent){
					abstractMonitor.setFrecuency(frE);
					abstractMonitor.setWindowSizeTime(wsEnergy);
					abstractMonitor.setRecordPath(logEnergy);				
					((MonitorEnergyAgent) abstractMonitor).setPowerlogPath(path);
				}else if(abstractMonitor instanceof MonitorCPUAgent){
					abstractMonitor.setFrecuency(frC);
					abstractMonitor.setWindowSizeTime(wsCpu);
					abstractMonitor.setRecordPath(logCpu);
				}
				abstractMonitor.setReduce(time);
			}
		}
		
	}
	//To testing
	
	//MonitorCPUAgent m = new MonitorCPUAgent("logCPU.txt");
	//private static int LIMIT_TIME = 60;
	//	int frE = 10;
	//	int frC = 10;
	//	String path = "C:\\Program Files\\Intel\\Power Gadget 3.0\\PowerLog3.0.exe";
}
