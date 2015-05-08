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
import com.losandes.connectionDb.MonitorInitialReport;
import com.losandes.connectionDb.MonitorReport;
import com.losandes.connectionDb.enums.ItemCPUMetrics;
import com.losandes.connectionDb.enums.ItemCPUReport;
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
			MongoConnection con = connection.generateConnection();
			if(initial!=null)saveInitialReport(initial, con);
			saveReports(reports, con);
			con.close();
		}	  	
	}
	
	 private void saveInitialReport(MonitorInitialReport initialReport, MongoConnection db) throws UnknownHostException { 
		    BasicDBObject doc = (BasicDBObject) db.infrastructureCollection().findOne(new BasicDBObject(ItemCPUMetrics.HOSTNAME.title(),initialReport.getHostname()));
		    if(doc!=null&&compareInitialReport(initialReport, doc))doc = null; 		  
		    if(doc == null){
		    	doc = new BasicDBObject(ItemCPUMetrics.HOSTNAME.title(),initialReport.getHostname())
				.append(ItemCPUMetrics.TIME.title(),initialReport.getTimest())
				.append(ItemCPUMetrics.TIME_MILLI.title(),initialReport.getTimeLong())
				.append(ItemCPUMetrics.OS_NAME.title(), initialReport.getOperatingSystemName())
				.append(ItemCPUMetrics.OS_VERSION.title(), initialReport.getOperatingSystemVersion())
				.append(ItemCPUMetrics.OS_ARQUITECTURE.title(), initialReport.getOperatingSystemArchitect())
				.append(ItemCPUMetrics.CPU_MODEL.title(), initialReport.getcPUModel())
				.append(ItemCPUMetrics.CPU_VENDOR.title(), initialReport.getcPUVendor())
				.append(ItemCPUMetrics.CPU_CORES.title(), initialReport.getcPUCores())
				.append(ItemCPUMetrics.CPU_SOCKETS.title(), initialReport.getTotalSockets())
				.append(ItemCPUMetrics.CPU_MHZ.title(), initialReport.getcPUMhz())
				.append(ItemCPUMetrics.CORES_X_SOCKETS.title(), initialReport.getCoresPerSocket())
				.append(ItemCPUMetrics.RAM_SIZE.title(), initialReport.getrAMMemorySize())
				.append(ItemCPUMetrics.SWAP_SIZE.title(), initialReport.getSwapMemorySize())
				.append(ItemCPUMetrics.HD_SPACE.title(), initialReport.getHardDiskSpace())
				.append(ItemCPUMetrics.HD_FILESYSTEM.title(), initialReport.getHardDiskFileSystem())
				.append(ItemCPUMetrics.MAC.title(), initialReport.getNetworkMACAddress());       
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
			BasicDBObject doc = new BasicDBObject(ItemCPUReport.HOSTNAME.title(),statusReport.getHostName())
			.append(ItemCPUReport.TIME.title(), statusReport.getTimest())
			.append(ItemCPUReport.TIME_MILLI.title(),statusReport.getTimeLong())
			.append(ItemCPUReport.USERNAME.title(), statusReport.getUserName())
			.append(ItemCPUReport.UP_TIME.title(), statusReport.getUptime())
			.append(ItemCPUReport.MFLOPS.title(), statusReport.getMflops())
			.append(ItemCPUReport.CPU_SECONDS.title(), statusReport.getTimeinSecs())
			.append(ItemCPUReport.CPU_IDLE.title(), statusReport.getIdle())
			.append(ItemCPUReport.NO_CPU_IDLE.title(), statusReport.getD())
			.append(ItemCPUReport.CPU_USER.title(), statusReport.getCPuser())
			.append(ItemCPUReport.CPU_SYS.title(), statusReport.getSys())
			.append(ItemCPUReport.CPU_NICE.title(), statusReport.getNice())
			.append(ItemCPUReport.CPU_WAIT.title(), statusReport.getWait())
			.append(ItemCPUReport.CPU_COMBINED.title(), statusReport.getCombined())
			.append(ItemCPUReport.TOTAL_USER.title(),statusReport.getUser())
			.append(ItemCPUReport.TOTAL_SYS.title(), statusReport.getSys0())
			.append(ItemCPUReport.TOTAL_NICE.title(), statusReport.getNice0())
			.append(ItemCPUReport.TOTAL_WAIT.title(), statusReport.getWait0())
			.append(ItemCPUReport.TOTAL_IDLE.title(), statusReport.getIdle0())
			.append(ItemCPUReport.RAM_FREE.title(), statusReport.getrAMMemoryFree())
			.append(ItemCPUReport.RAM_USED.title(), statusReport.getrAMMemoryUsed())
			.append(ItemCPUReport.MEM_FREE.title(), statusReport.getFreePercent())
			.append(ItemCPUReport.MEM_USED.title(), statusReport.getUsedPercent())
			.append(ItemCPUReport.SWAP_FREE.title(), statusReport.getSwapMemoryFree())
			.append(ItemCPUReport.SWAP_PAGE_IN.title(), statusReport.getSwapMemoryPageIn())
			.append(ItemCPUReport.SWAP_PAGE_OUT.title(), statusReport.getSwapMemoryPageOut())
			.append(ItemCPUReport.SWAP_USED.title(), statusReport.getSwapMemoryUsed())
			.append(ItemCPUReport.HD_FREE.title(), statusReport.getHardDiskFreeSpace())
			.append(ItemCPUReport.HD_USED.title(), statusReport.getHardDiskUsedSpace())
			.append(ItemCPUReport.NET_IP.title(), statusReport.getNetworkIPAddress())
			.append(ItemCPUReport.NET_INTERFACE.title(), statusReport.getNetworkInterface())
			.append(ItemCPUReport.NET_MASK.title(), statusReport.getNetworkNetmask())
			.append(ItemCPUReport.NET_GATEWAY.title(), statusReport.getNetworkGateway())
			.append(ItemCPUReport.NET_RX_BYTES.title(),statusReport.getRxBytes())
			.append(ItemCPUReport.NET_TX_BYTES.title(), statusReport.getTxBytes())
			.append(ItemCPUReport.NET_SPEED.title(), statusReport.getSpeed())
			.append(ItemCPUReport.NET_RX_ERRORS.title(), statusReport.getRxErrors())
			.append(ItemCPUReport.NET_TX_ERRORS.title(), statusReport.getTxErrors())
			.append(ItemCPUReport.NET_RX_PACKETS.title(), statusReport.getRxPackets())
            .append(ItemCPUReport.NET_TX_PACKETS.title(), statusReport.getTxPackets())
            .append(ItemCPUReport.PROCESSES.title(),listProcesses);		
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
		 if(!object.get(ItemCPUMetrics.OS_NAME.title()).equals(m1.getOperatingSystemName()))return true;
		 else if(!object.get(ItemCPUMetrics.OS_VERSION.title()).equals(m1.getOperatingSystemVersion()))return true;
		 else if(!object.get(ItemCPUMetrics.OS_ARQUITECTURE.title()).equals(m1.getOperatingSystemArchitect()))return true;
		 else if(!object.get(ItemCPUMetrics.CPU_MODEL.title()).equals(m1.getcPUModel()))return true;
		 else if(!object.get(ItemCPUMetrics.CPU_VENDOR.title()).equals(m1.getcPUVendor()))return true;
		 else if(!object.get(ItemCPUMetrics.CPU_CORES.title()).equals(m1.getcPUCores()))return true;
		 else if(!object.get(ItemCPUMetrics.CPU_SOCKETS.title()).equals(m1.getTotalSockets()))return true;
		 else if(!object.get(ItemCPUMetrics.CPU_MHZ.title()).equals(m1.getcPUMhz()))return true;
		 else if(!object.get(ItemCPUMetrics.CORES_X_SOCKETS.title()).equals(m1.getCoresPerSocket()))return true;
		 else if(!object.get(ItemCPUMetrics.RAM_SIZE.title()).equals(m1.getrAMMemorySize()))return true;
		 else if(!object.get(ItemCPUMetrics.SWAP_SIZE.title()).equals(m1.getSwapMemorySize()))return true;
		 else if(!object.get(ItemCPUMetrics.HD_SPACE.title()).equals(m1.getHardDiskSpace()))return true;
		 else if(!object.get(ItemCPUMetrics.HD_FILESYSTEM.title()).equals(m1.getHardDiskFileSystem()))return true;
		 else if(!object.get(ItemCPUMetrics.MAC.title()).equals(m1.getNetworkMACAddress()))return true;
		 return false;
	 }
}