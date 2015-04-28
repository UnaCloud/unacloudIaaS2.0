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

import unacloudEnums.MonitoringStatus;

import com.losandes.connectionDb.MongoConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;


/** 
 * @author Cesar
 * 
 * This class represent a process to monitoring CPU. To monitoring cpu, it uses the library Sigar by Hyperic, and has three process;
 * Do initial: to validate if there are files to record in database and delete those files, Do Monitoring: to sense cpu and record in a file, and Do final: to record en db
 * This class only work in Windows
 */

public class MonitorCPUAgent extends AbstractMonitor {
	
	private File f;
	public MonitorCPUAgent(String path) throws Exception {
		super(path);
		f = new File(recordPath);
	}
	    
	@Override
	protected void doInitial() throws Exception{
		if(status==MonitoringStatus.INIT){
			 PrintWriter pw = new PrintWriter(new FileOutputStream(f,true),true);
		     pw.println(MonitorReportGenerator.getInstance().generateInitialReport());
		     pw.close();
		}	     
	}
	
	@Override
	protected void doMonitoring() throws Exception {
		 checkFile();
	     int localFrecuency = 1000*frecuency;  
	     Date d = new Date();
	     if(reduce>windowSizeTime)reduce= 0;
	     d.setTime(d.getTime()+(windowSizeTime*1000)-(reduce*1000));	     
	     PrintWriter pw = new PrintWriter(new FileOutputStream(f,true),true);
	     while(d.after(new Date())){  
	    	pw.println(MonitorReportGenerator.getInstance().generateStateReport());
	        Thread.sleep(localFrecuency);
	     }
	     if(reduce>0)reduce = 0;
	     pw.close();
	}
	
	@Override
	protected void doFinal() throws Exception{
		recordData();
		cleanFile();
		System.out.println(new Date()+"Termine cpu");
	}

	@Override
	protected void sendError(Exception e) {
		// TODO Send error message to server
		
	}	
	
	private void checkFile() throws Exception{
		if(!f.exists())f.createNewFile();
		else{
			if(f.length()>0){
				recordData();
				cleanFile();
			}
		}
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
		if(reports.size()>0){
			MongoConnection con = MonitorDatabaseConnection.generateConnection();
			if(initial!=null)saveInitialReport(initial, con);
			saveReports(reports, con);
			con.close();
		}	  	
	}
	
	 private void saveInitialReport(MonitorInitialReport initialReport, MongoConnection db) throws UnknownHostException { 
		    BasicDBObject doc = (BasicDBObject) db.infrastructureCollection().findOne(new BasicDBObject("Hostname",initialReport.getHostname()));	
		    if(doc!=null&&compareInitialReport(initialReport, doc))doc = null; 		  
		    if(doc == null){
		    	doc = new BasicDBObject("Hostname",initialReport.getHostname())
				.append("Timestamp",initialReport.getTimest())
				.append("TimeMilli",initialReport.getTimeLong())
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
			    System.out.println(db.infrastructureCollection().insert(doc).getN());
		    }				
	 }
	 
	 private void saveReports(ArrayList<MonitorReport>reports, MongoConnection db){
		 BulkWriteOperation builder = db.cpuCollection().initializeOrderedBulkOperation();
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
			.append("TimeMilli",statusReport.getTimeLong())
			.append("Username", statusReport.getUserName())
			.append("UpTime", statusReport.getUptime())
			.append("MFlops", statusReport.getMflops())
			.append("CPUTimeInSeconds", statusReport.getTimeinSecs())
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
			builder.insert(doc);
        }		
		
//		BasicDBObject[] array = new BasicDBObject[listReports.size()];
//		for (int i = 0; i < array.length; i++) array[i] = listReports.get(i);
		System.out.println("Insert: "+builder.execute().getInsertedCount());
	 }
	 
	 private void cleanFile() throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(f);
		writer.print("");
		writer.close();
	 }
	 /**
	  * Method to compare the current machine infrastructure with the information in db
	  * @param m1 current machine infrastructure
	  * @param object last machine infrastructure 
	  * @return
	  */
	 private boolean compareInitialReport(MonitorInitialReport m1, BasicDBObject object){		
		 if(!object.get("OSName").equals(m1.getOperatingSystemName()))return true;
		 else if(!object.get("OSVersion").equals(m1.getOperatingSystemVersion()))return true;
		 else if(!object.get("OSArquitecture").equals(m1.getOperatingSystemArchitect()))return true;
		 else if(!object.get("CPUModel").equals(m1.getcPUModel()))return true;
		 else if(!object.get("CPUVendor").equals(m1.getcPUVendor()))return true;
		 else if(!object.get("CPUCores").equals(m1.getcPUCores()))return true;
		 else if(!object.get("CPUSockets").equals(m1.getTotalSockets()))return true;
		 else if(!object.get("CpuMhz").equals(m1.getcPUMhz()))return true;
		 else if(!object.get("CoresXSocket").equals(m1.getCoresPerSocket()))return true;
		 else if(!object.get("RAMMemorySize").equals(m1.getrAMMemorySize()))return true;
		 else if(!object.get("SwapMemorySize").equals(m1.getSwapMemorySize()))return true;
		 else if(!object.get("HDSpace").equals(m1.getHardDiskSpace()))return true;
		 else if(!object.get("HDFileSystem").equals(m1.getHardDiskFileSystem()))return true;
		 else if(!object.get("MACAddress").equals(m1.getNetworkMACAddress()))return true;
		 return false;
	 }
}