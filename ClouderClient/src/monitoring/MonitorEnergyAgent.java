package monitoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import physicalmachine.Network;

import com.losandes.connectionDb.MongoConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;

import unacloudEnums.MonitoringStatus;
import virtualMachineManager.LocalProcessExecutor;


/** 
 * @author Cesar
 * This class represent a process to monitoring Energy in CPU. To monitoring energy, it uses the application Power Gadget by Intel in particular the application PowerLog3.0, and it has three process;
 * Do initial: to validate if there are files to record in database and delete those files, also stop the power log process in case it's running. Do Monitoring: execute the process PowerLog3.0.exe to sense energy and record it in a file, and Do final: to record en db
 * This class only work in Windows
 */

public class MonitorEnergyAgent extends AbstractMonitor {
	
	private String powerlogPath;
	
	public MonitorEnergyAgent(String path) throws Exception {
		super(path);
	}

	@Override
	protected void doInitial() throws Exception {
		if(status==MonitoringStatus.INIT){
			LocalProcessExecutor.executeCommand("taskkill /IMF PowerLog3.0.exe");
		}		
	}

	@Override
	protected void doMonitoring() throws Exception {
		//C:\\Program Files\\Intel\\Power Gadget 3.0\\PowerLog3.0.exe
		checkFile();
		if(reduce>windowSizeTime)reduce= 0;
		LocalProcessExecutor.executeCommand(powerlogPath+" -resolution "+(frecuency*1000)+" -duration "+(windowSizeTime-reduce)+" -file "+recordPath);
		if(reduce>0)reduce=0;
	}

	@Override
	protected void doFinal() throws Exception{
		recordData();
		cleanFile(new File(recordPath));
		System.out.println(new Date()+"Termine energia");
	}
	
	private void checkFile() throws Exception{
		File f = new File(recordPath);
		if(f.exists()){
			if(f.length()>0){
				recordData();
				cleanFile(f);
			}
		}		
	}
	private void recordData() throws Exception{	
		BufferedReader bf = new BufferedReader(new FileReader(new File(recordPath)));
		String line = null;
		ArrayList<MonitorEnergy> reports = new ArrayList<MonitorEnergy>();
		while((line=bf.readLine())!=null&&!line.isEmpty()){
			if(!line.startsWith("System")){
				String[] data = line.split(",");
				MonitorEnergy m = new MonitorEnergy();
				m.Time = data[0];
				m.RDTSC = data[1];
				m.elapsedTime = data[2];
				m.CPUFrequency = data[3];
				m.processorPower = data[4];
				m.cumulativeProcessorEnergyJoules = data[5];
				m.cumulativeProcessorEnergyMhz = data[6];
				m.IAPower = data[7];
				m.cumulativeIAEnergy = data[8];
				m.cumulativeIA = data[9];
				m.packageTemperature = data[10];
				m.packageHot = data[11];
				m.packagePowerLimit = data[12];
				reports.add(m);
			}
		}
		bf.close();
		if(reports.size()>0){
			MongoConnection con = MonitorDatabaseConnection.generateConnection();	 
			saveReports(reports, con);
			con.close();
		}	  	
	}
	
	
	private void saveReports(ArrayList<MonitorEnergy> reports, MongoConnection db) {
		BulkWriteOperation builder = db.energyCollection().initializeOrderedBulkOperation();		
		String hostname = Network.getHostname();
		 for (MonitorEnergy statusReport : reports)if(statusReport!=null){			
			BasicDBObject doc = new BasicDBObject("Hostname",hostname)
			.append("Timestamp", statusReport.Time)
			.append("RDTSC", statusReport.RDTSC)
			.append("elapsedTime", statusReport.elapsedTime)
			.append("CPUFrequency", statusReport.CPUFrequency)
			.append("processorPower", statusReport.processorPower)
			.append("cumulativeProcessorEnergyJoules", statusReport.cumulativeProcessorEnergyJoules)
			.append("cumulativeProcessorEnergyMhz ", statusReport.cumulativeProcessorEnergyMhz)
			.append("IAPower", statusReport.IAPower)
			.append("cumulativeIAEnergy", statusReport.cumulativeIAEnergy)
			.append("cumulativeIA", statusReport.cumulativeIA)
			.append("packageTemperature", statusReport.packageTemperature)
			.append("packageHot", statusReport.packageHot)
			.append("packagePowerLimit",statusReport.packagePowerLimit);
			builder.insert(doc);
       }				
//		BasicDBObject[] array = new BasicDBObject[listReports.size()];
//		for (int i = 0; i < array.length; i++) array[i] = listReports.get(i);
		System.out.println("Insert energy: "+builder.execute().getInsertedCount());
	}

	public String getPowerlogPath() {
		return powerlogPath;
	}
	
	public void setPowerlogPath(String powerlogPath) {
		this.powerlogPath = powerlogPath;
	}

	@Override
	protected void sendError(Exception e) {
		// TODO Auto-generated method stub
		
	}
	
	private void cleanFile(File f) throws FileNotFoundException{
		if(f.exists()){
			PrintWriter writer = new PrintWriter(f);
			writer.print("");
			writer.close();			
		}
    }
	
	private class MonitorEnergy{
		String Time,RDTSC,elapsedTime,CPUFrequency,processorPower,
		cumulativeProcessorEnergyJoules,cumulativeProcessorEnergyMhz,
		IAPower,cumulativeIAEnergy,cumulativeIA,packageTemperature,
		packageHot,packagePowerLimit;		
	}

}
