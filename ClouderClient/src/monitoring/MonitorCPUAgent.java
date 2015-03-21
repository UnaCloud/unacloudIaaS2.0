package monitoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;

import communication.messages.monitoring.MonitorInitialReport;
import communication.messages.monitoring.MonitorReport;


/** 
 * @author Cesar
 * This class represent a process to monitoring CPU. To monitoring cpu, it uses the library Sigar by Hyperic, and has three process;
 * Do initial: to validate if there are files to record in database and delete those files, Do Monitoring: to sense cpu and record in a file, and Do final: to record en db
 * This class only work in Windows
 */

public class MonitorCPUAgent extends AbstractMonitor {
	
	private File f;
	public MonitorCPUAgent(String path) {
		recordPath = path;
		f = new File(recordPath);
	}
	    
	@Override
	protected void doInitial() throws Exception{
		if(!f.exists())f.createNewFile();
		else{
			if(f.length()>0){
				recordData();
				cleanFile();
			}
		}
	}
	
	@Override
	protected void doMonitoring() throws Exception {
	     int localFrecuency = 1000*frecuency;  
	     Date d = new Date();
	     if(reduce>windowSizeTime)reduce= 0;
	     d.setTime(d.getTime()+(windowSizeTime*1000)-(reduce*1000));	     
	     PrintWriter pw = new PrintWriter(new FileOutputStream(f,true),true);
	     pw.println(MonitorReportGenerator.generateInitialReport());
	     while(d.after(new Date())){  
	    	pw.println(MonitorReportGenerator.generateStateReport());
	        Thread.sleep(localFrecuency);
	     }
	     if(reduce>0)reduce = 0;
	     pw.close();
	}
	
	@Override
	protected void doFinal() throws Exception{
		recordData();
		cleanFile();
	}

	@Override
	protected void sendError(Exception e) {
		// TODO Send error message to server
		
	}	
	
	private void recordData() throws Exception{		
		BufferedReader bf = new BufferedReader(new FileReader(f));
		String line = null;
		MonitorInitialReport initial = null; 
		ArrayList<MonitorReport> reports = new ArrayList<MonitorReport>();
		while((line=bf.readLine())!=null&&!line.isEmpty()){
			if(line.startsWith("MonitorInitialReport")){
				initial = new MonitorInitialReport(line);
			}else if(line.startsWith("MonitorReport")){
				reports.add(new MonitorReport(line));
			}
		}
		bf.close();
		if(initial!=null&&reports.size()>0){
			MongoConnection con = MonitorDatabaseConnection.generateConnection();	    	 	
			saveInitialReport(initial, con);
			saveReports(reports, con);
			con.close();
		}	  	
	}
	
	 private void saveInitialReport(MonitorInitialReport initialReport, MongoConnection db) throws UnknownHostException { 
			BasicDBObject doc = new BasicDBObject("Hostname",initialReport.getHostname())
			.append("Timestamp",initialReport.getTimest())
			.append("OSName", initialReport.getOperatingSystemName())
			.append("OSVersion", initialReport.getOperatingSystemVersion())
			.append("OSArquitecture", initialReport.getOperatingSystemArchitect())
			.append("CPUModel", initialReport.getcPUModel())
			.append("CPUVendor", initialReport.getcPUVendor())
			.append("CPUCores", initialReport.getcPUCores())
			.append("CPUSockets", initialReport.getTotalSockets())
			.append("CpuMhz", initialReport.getcPUMhz())
			.append("CoresXSocket", initialReport.getCoresPerSocket())
			.append("RAMMemorySize", initialReport.getrAMMemorySize())
			.append("SwapMemorySize", initialReport.getSwapMemorySize())
			.append("HDSpace", initialReport.getHardDiskSpace())
			.append("HDFileSystem", initialReport.getHardDiskFileSystem())
			.append("MACAddress", initialReport.getNetworkMACAddress());    		
			System.out.println(db.infrastructureCollection().insert(doc));
	 }
	 
	 private void saveReports(ArrayList<MonitorReport>reports, MongoConnection db){
		 List<BasicDBObject> listReports = new ArrayList<BasicDBObject>();
		 for (MonitorReport statusReport : reports)if(statusReport!=null){
			String[] pros = statusReport.getProcesses().split(",");
			List<BasicDBObject> listProcesses = new ArrayList<BasicDBObject>();
			for (String pro : pros) {
				pro = pro.replace("(", "").replace(")", "").trim();
				if(!pro.isEmpty()){
					BasicDBObject doc = new BasicDBObject();
					String[] values = pro.split(";");
					for (String val : values) {
						String[] cc = val.split(":");
						doc.append(cc[0], cc[1]);
					}
					listProcesses.add(doc);
				}				
			}
			BasicDBObject doc = new BasicDBObject("Hostname",statusReport.getHostName())
			.append("Timestamp", statusReport.getTimest())
			.append("Username", statusReport.getUserName())
			.append("UpTime", statusReport.getUptime())
			.append("MFlops", statusReport.getMflops())
			.append("TimeInSeconds", statusReport.getTimeinSecs())
			.append("CPUIdle", statusReport.getIdle())
			.append("NoCPUIdle ", statusReport.getD())
			.append("CPUuser", statusReport.getCPuser())
			.append("CPUsys", statusReport.getSys())
			.append("CPUNice", statusReport.getNice())
			.append("CPUWait", statusReport.getWait())
			.append("CPUCombined", statusReport.getCombined())
			.append("TotalUserTime",statusReport.getUser())
			.append("TotalSysTime", statusReport.getSys0())
			.append("TotalNiceTime", statusReport.getNice0())
			.append("TotalWaitTime", statusReport.getWait0())
			.append("TotalIdleTime", statusReport.getIdle0())
			.append("RamFree", statusReport.getrAMMemoryFree())
			.append("RamUsed", statusReport.getrAMMemoryUsed())
			.append("MemFreePercent", statusReport.getFreePercent())
			.append("MemUsedPercent", statusReport.getUsedPercent())
			.append("SwapMemoryFree", statusReport.getSwapMemoryFree())
			.append("SwapMemoryPageIn", statusReport.getSwapMemoryPageIn())
			.append("SwapMemoryPageOut", statusReport.getSwapMemoryPageOut())
			.append("SwapMemoryUsed", statusReport.getSwapMemoryUsed())
			.append("HDFreeSpace", statusReport.getHardDiskFreeSpace())
			.append("HDUsedSpace", statusReport.getHardDiskUsedSpace())
			.append("NetworkIpAddress", statusReport.getNetworkIPAddress())
			.append("NetworkInterface", statusReport.getNetworkInterface())
			.append("NetworkNetMask", statusReport.getNetworkNetmask())
			.append("NetworkGateway", statusReport.getNetworkGateway())
			.append("NetRXBytes",statusReport.getRxBytes())
			.append("NetTxBytes", statusReport.getTxBytes())
			.append("NetSpeed", statusReport.getSpeed())
			.append("NetRXErrors", statusReport.getRxErrors())
			.append("NetTxErrors", statusReport.getTxErrors())
			.append("NetRxPackets", statusReport.getRxPackets())
            .append("NetTxPackets", statusReport.getTxPackets())
            .append("Processes",listProcesses);			
			listReports.add(doc);
        }		
		
		BasicDBObject[] array = new BasicDBObject[listReports.size()];
		for (int i = 0; i < array.length; i++) array[i] = listReports.get(i);
		System.out.println(db.cpuCollection().insert(array));
	 }
	 
	 private void cleanFile() throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(f);
		writer.print("");
		writer.close();
	 }
//	 private void recordData() throws Exception{
//		  	MongoClient con = MonitorDatabaseConnection.generateConnection();
//	    	DB db = con.getDB( "local" );
//	    	MonitorExecutionCPU mc = readLocalData();
//	    	if(mc==null)throw new Exception("The file is not in path");
//			saveInitialReport(mc.getInitial(), db);
//			saveReports(mc.getReports(), db);
//			con.close();
//		}
	 
//		@Override
//		protected void doMonitoring() throws Exception {
//			//TODO refactor time delay
//		     int localFrecuency = 1000*frecuency;  
//		     Date d = new Date();
//		     d.setTime(d.getTime()+(windowSizeTime*1000));
//		     MonitorExecutionCPU mc = new MonitorExecutionCPU();
//		     mc.setInitial(MonitorReportGenerator.generateInitialReport());
//		     recordLocalData(mc);
//		     while(d.after(new Date())){  
//		    	MonitorExecutionCPU mcR = readLocalData();
//		    	mcR.getReports().add(MonitorReportGenerator.generateStateReport());
//		    	recordLocalData(mcR);
//		        Thread.sleep(localFrecuency);
//		     }
//		}
//
//	private static void recordLocalData(MonitorExecutionCPU monitor){		
//		try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(f))){
//			oos.writeObject(monitor);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//		
//	private static MonitorExecutionCPU readLocalData(){
//		MonitorExecutionCPU mc = null;
//		try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f))){
//			mc=(MonitorExecutionCPU)ois.readObject();			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return mc;
//	}
}