package com.losandes.monitoring;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.losandes.connectionDb.MongoConnection;
import com.losandes.connectionDb.MonitorDatabaseConnection;
import com.losandes.connectionDb.enums.ItemCPUMetrics;
import com.losandes.connectionDb.enums.ItemCPUReport;
import com.losandes.connectionDb.enums.ItemEnergyReport;
import com.losandes.utils.RefactorUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class MonitorQuery {
	
	private MonitorDatabaseConnection connection;
	
	public MonitorQuery(MonitorDatabaseConnection c) {
		this.connection = c;		
	}
	
	public List<MonitorEnergyReport> getEnergyReportsByDate(Date start, Date end, String pm){
		List<MonitorEnergyReport> reports = new ArrayList<MonitorEnergyReport>();
		MongoConnection m = null;
		try {
			m = connection.generateConnection();		
			BasicDBObject orQuery = new BasicDBObject();
		    List<BasicDBObject> objects = new ArrayList<BasicDBObject>();
			BasicDBObject query = new BasicDBObject(ItemCPUReport.HOSTNAME.title(), pm.toLowerCase()).append("_id", new BasicDBObject("$gte",new ObjectId(start)).append("$lte", new ObjectId(end)));
			BasicDBObject query2 = new BasicDBObject(ItemCPUReport.HOSTNAME.title(), pm.toUpperCase()).append("_id", new BasicDBObject("$gte",new ObjectId(start)).append("$lte", new ObjectId(end)));
			objects.add(query2);objects.add(query);
			orQuery.put("$or", objects);
			DBCursor cursor = m.cpuCollection().find(orQuery);
			//BasicDBObject query = new BasicDBObject(ItemEnergyReport.HOSTNAME.title(), pm).append(ItemEnergyReport.REGISTER_DATE.title(), new BasicDBObject("$gte",start.getTime()).append("$lte", end.getTime()));
			//BasicDBObject query = new BasicDBObject(ItemEnergyReport.HOSTNAME.title(), pm).append("_id", new BasicDBObject("$gte",new ObjectId(start)).append("$lte", new ObjectId(end)));
			try {
				while(cursor.hasNext()) {
					BasicDBObject obj = (BasicDBObject) cursor.next();
//					System.out.println(obj);
//					System.out.println(parseToEnergyReport(obj,new Date(obj.getObjectId("_id").getTime())));
					reports.add(parseToEnergyReport(obj,new Date(obj.getObjectId("_id").getTime())));
				}
			} finally {
				 cursor.close();
			}
			m.close();
		} catch (Exception e) {
			if(m!=null)m.close();
		}
		return reports;
	}
	
	public List<MonitorReport> getCpuReportsByDate(Date start, Date end, String pm){
		List<MonitorReport> reports = new ArrayList<MonitorReport>();
		MongoConnection m = null;
		try {
			m = connection.generateConnection();
			BasicDBObject orQuery = new BasicDBObject();
		    List<BasicDBObject> objects = new ArrayList<BasicDBObject>();
			//BasicDBObject query = new BasicDBObject(ItemCPUReport.HOSTNAME.title(), pm).append(ItemCPUReport.TIME_MILLI.title(), new BasicDBObject("$gte",start.getTime()).append("$lte", end.getTime()));
			BasicDBObject query = new BasicDBObject(ItemCPUReport.HOSTNAME.title(), pm.toLowerCase()).append("_id", new BasicDBObject("$gte",new ObjectId(start)).append("$lte", new ObjectId(end)));
			BasicDBObject query2 = new BasicDBObject(ItemCPUReport.HOSTNAME.title(), pm.toUpperCase()).append("_id", new BasicDBObject("$gte",new ObjectId(start)).append("$lte", new ObjectId(end)));
			objects.add(query2);objects.add(query);
			orQuery.put("$or", objects);
			DBCursor cursor = m.cpuCollection().find(orQuery);
			try {
				while(cursor.hasNext()) {
					BasicDBObject obj = (BasicDBObject) cursor.next();
					if(obj.getString(ItemCPUReport.USERNAME.title()).equals("dc.bonilla10")){
						System.out.println(obj);
						System.out.println(parseToCpuReport(obj));
					}					
					reports.add(parseToCpuReport(obj));
				}
			} finally {
				 cursor.close();
			}
			m.close();
		} catch (Exception e) {
			if(m!=null)m.close();
		}
		return reports;
	}
	public static void main(String[] args) {
//		MonitorQuery m = new MonitorQuery(new MonitorDatabaseConnection() {			
//			@Override
//			public void callVariables() {
//				ip = "172.24.98.119";
//			    port = 27017;
//			    name = "cloudMongo";
//			    user = "cloudmonitoreo";
//			    password = "cloudmonitoreo$#";	
//				
//			}
//		});
////		Date end = new Date();
////		Date start = new Date(end.getTime()-1000*60*60*20*3);
//		try {
//			
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//		    Date start = dateFormat.parse("2015-05-05 16:00:00.000");
//		    Date end = new Date(start.getTime()+1000*60*60*6);
//		    System.out.println(start);
//		    System.out.println(end);
//		    m.getCpuReportsByDate(start, end, "ISC210");
////			for (MonitorReport string : m.getCpuReportsByDate(start, end, "ISC210")) {
////				System.out.println(string);
////			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
	
	   
		

//		for (MonitorReport string : m.getCpuReports("ISC202")) {
//			System.out.println(string);
//		}
		
	}
//	private List<MonitorReport> getCpuReports(String pm){
//		List<MonitorReport> reports = new ArrayList<MonitorReport>();
//		MongoConnection m = null;
//		try {
//			m = connection.generateConnection();		
//			BasicDBObject query = new BasicDBObject(ItemCPUReport.HOSTNAME.title(), pm);
//			DBCursor cursor = m.cpuCollection().find(query);
//			try {
//				while(cursor.hasNext()) {
//					BasicDBObject obj = (BasicDBObject) cursor.next();
//					System.out.println(obj);
//					reports.add(parseToCpuReport(obj));
//				}
//			} finally {
//				 cursor.close();
//			}
//			m.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			if(m!=null)m.close();
//		}
//		return reports;
//	}
	public MonitorInitialReport getCPUMetrics(String host){
		MongoConnection m = null;
		MonitorInitialReport mi = null;
		try {
			m = connection.generateConnection();
			BasicDBObject orQuery = new BasicDBObject();
		    List<BasicDBObject> objects = new ArrayList<BasicDBObject>();
			BasicDBObject query = new BasicDBObject(ItemCPUReport.HOSTNAME.title(), host.toLowerCase());
			BasicDBObject query2 = new BasicDBObject(ItemCPUReport.HOSTNAME.title(), host.toUpperCase());
			objects.add(query2);objects.add(query);
			orQuery.put("$or", objects);
			BasicDBObject obj = (BasicDBObject)m.infrastructureCollection().findOne(orQuery);
			mi = parseToInitialReport(obj);
			m.close();
		} catch (Exception e) {
			if(m!=null)m.close();			
		}	
		return mi;
	}
	
	private MonitorInitialReport parseToInitialReport(BasicDBObject obj){
		MonitorInitialReport mon = new MonitorInitialReport();
		mon.setCoresPerSocket(obj.getInt(ItemCPUMetrics.CORES_X_SOCKETS.title()));
		mon.setcPUCores(obj.getInt(ItemCPUMetrics.CPU_CORES.title()));
		mon.setcPUMhz(obj.getString(ItemCPUMetrics.CPU_MHZ.title()));
		mon.setcPUModel(obj.getString(ItemCPUMetrics.CPU_MODEL.title()));
		mon.setcPUVendor(obj.getString(ItemCPUMetrics.CPU_VENDOR.title()));
		mon.setHardDiskFileSystem(obj.getString(ItemCPUMetrics.HD_FILESYSTEM.title()));
		mon.setHardDiskSpace(obj.getLong(ItemCPUMetrics.HD_SPACE.title()));
		mon.setHostname(obj.getString(ItemCPUMetrics.HOSTNAME.title()));
		mon.setNetworkMACAddress(obj.getString(ItemCPUMetrics.MAC.title()));
		mon.setOperatingSystemArchitect(obj.getString(ItemCPUMetrics.OS_ARQUITECTURE.title()));
		mon.setOperatingSystemName(obj.getString(ItemCPUMetrics.OS_NAME.title()));
		mon.setOperatingSystemVersion(obj.getString(ItemCPUMetrics.OS_VERSION.title()));
		mon.setrAMMemorySize(obj.getDouble(ItemCPUMetrics.RAM_SIZE.title()));
		mon.setSwapMemorySize(obj.getDouble(ItemCPUMetrics.SWAP_SIZE.title()));
		mon.setTimeLong(obj.getLong(ItemCPUMetrics.TIME_MILLI.title()));
		mon.setTimestString(obj.getString(ItemCPUMetrics.TIME.title()));
		mon.setTotalSockets(obj.getInt(ItemCPUMetrics.CPU_SOCKETS.title()));
		try {
			 mon.setMflops(obj.getDouble(ItemCPUMetrics.MFLOPS.title()));
			 mon.setTimeinSecs(obj.getDouble(ItemCPUMetrics.CPU_SECONDS.title()));
		} catch (Exception e) {//XXX en caso de no tener representación en la bd.
			 mon.setMflops(-1);
			 mon.setTimeinSecs(-1);
		}	
		return mon;
	}
	
	private MonitorEnergyReport parseToEnergyReport(BasicDBObject obj, Date objectId){
		MonitorEnergyReport mon = new MonitorEnergyReport();
		mon.setCPUFrequency(obj.getString(ItemEnergyReport.CPU_FRECUENCY.title()));
		mon.setCumulativeIA(obj.getString(ItemEnergyReport.IA.title()));
		mon.setCumulativeIAEnergy(obj.getString(ItemEnergyReport.IA_ENERGY.title()));
		mon.setCumulativeProcessorEnergyJoules(obj.getString(ItemEnergyReport.ENERGY_JOULES.title()));
		mon.setCumulativeProcessorEnergyMhz(obj.getString(ItemEnergyReport.ENERGY_MHZ.title()));
		mon.setElapsedTime(obj.getString(ItemEnergyReport.ELAPSED_TIME.title()));
		mon.setHostName(obj.getString(ItemEnergyReport.HOSTNAME.title()));
		mon.setIAPower(obj.getString(ItemEnergyReport.IA_POWER.title()));
		mon.setPackageHot(obj.getString(ItemEnergyReport.PACK_HOT.title()));
		mon.setPackagePowerLimit(obj.getString(ItemEnergyReport.PACK_POWER.title()));
		mon.setPackageTemperature(obj.getString(ItemEnergyReport.PACK_TEMP.title()));
		mon.setProcessorPower(obj.getString(ItemEnergyReport.PROCESSOR_POWER.title()));
		mon.setRDTSC(obj.getString(ItemEnergyReport.RDTSC.title()));
		try {
			Long l = obj.getLong(ItemEnergyReport.REGISTER_DATE.title());
			mon.setRegisterDate(new Date(l));
		} catch (Exception e) {	
			mon.setRegisterDate(objectId);
		}		
		mon.setTime(obj.getString(ItemEnergyReport.TIME.title()));
		return mon;
	}
	private MonitorReport parseToCpuReport(BasicDBObject obj){
		MonitorReport mon = new MonitorReport();
		mon.setCombined(obj.getDouble(ItemCPUReport.CPU_COMBINED.title()));
		mon.setCPuser(obj.getDouble(ItemCPUReport.CPU_USER.title()));
		mon.setD(obj.getDouble(ItemCPUReport.NO_CPU_IDLE.title()));
		mon.setFreePercent(obj.getDouble(ItemCPUReport.MEM_FREE.title()));
		mon.setHardDiskFreeSpace(obj.getLong(ItemCPUReport.HD_FREE.title()));
		mon.setHardDiskUsedSpace(obj.getLong(ItemCPUReport.HD_USED.title()));
		mon.setHostName(obj.getString(ItemCPUReport.HOSTNAME.title()));
		mon.setIdle(obj.getDouble(ItemCPUReport.CPU_IDLE.title()));
		mon.setIdle0(obj.getLong(ItemCPUReport.TOTAL_IDLE.title()));
		mon.setNetworkGateway(obj.getString(ItemCPUReport.NET_GATEWAY.title()));
		mon.setNetworkInterface(obj.getString(ItemCPUReport.NET_INTERFACE.title()));
		mon.setNetworkIPAddress(obj.getString(ItemCPUReport.NET_IP.title()));
		mon.setNetworkNetmask(obj.getString(ItemCPUReport.NET_MASK.title()));
		mon.setNice(obj.getDouble(ItemCPUReport.CPU_NICE.title()));
		mon.setNice0(obj.getLong(ItemCPUReport.TOTAL_NICE.title()));
		mon.setrAMMemoryFree(obj.getDouble(ItemCPUReport.RAM_FREE.title()));
		mon.setrAMMemoryUsed(obj.getDouble(ItemCPUReport.RAM_USED.title()));
		mon.setProcesses(RefactorUtils.refactorString(obj.getString(ItemCPUReport.PROCESSES.title())));
		mon.setRxBytes(obj.getLong(ItemCPUReport.NET_RX_BYTES.title()));
		mon.setRxErrors(obj.getLong(ItemCPUReport.NET_RX_ERRORS.title()));
		mon.setRxPackets(obj.getLong(ItemCPUReport.NET_RX_PACKETS.title()));
		mon.setSpeed(obj.getLong(ItemCPUReport.NET_SPEED.title()));
		mon.setSwapMemoryFree(obj.getDouble(ItemCPUReport.SWAP_FREE.title()));
		mon.setSwapMemoryPageIn(obj.getDouble(ItemCPUReport.SWAP_PAGE_IN.title()));
		mon.setSwapMemoryPageOut(obj.getDouble(ItemCPUReport.SWAP_PAGE_OUT.title()));
		mon.setSwapMemoryUsed(obj.getDouble(ItemCPUReport.SWAP_USED.title()));
		mon.setSys(obj.getDouble(ItemCPUReport.CPU_SYS.title()));
		mon.setSys0(obj.getLong(ItemCPUReport.TOTAL_SYS.title()));
		mon.setTimeLong(obj.getLong(ItemCPUReport.TIME_MILLI.title()));
		mon.setTimestString(obj.getString(ItemCPUReport.TIME.title()));
		mon.setTxBytes(obj.getLong(ItemCPUReport.NET_TX_BYTES.title()));
		mon.setTxErrors(obj.getLong(ItemCPUReport.NET_TX_ERRORS.title()));
		mon.setTxPackets(obj.getLong(ItemCPUReport.NET_TX_PACKETS.title()));
		mon.setUptime(obj.getDouble(ItemCPUReport.UP_TIME.title()));
		mon.setUsedPercent(obj.getDouble(ItemCPUReport.MEM_USED.title()));
		mon.setUser(obj.getLong(ItemCPUReport.TOTAL_USER.title()));
		mon.setUserName(obj.getString(ItemCPUReport.USERNAME.title()));
		mon.setWait(obj.getDouble(ItemCPUReport.CPU_WAIT.title()));
		mon.setWait0(obj.getLong(ItemCPUReport.TOTAL_WAIT.title()));
		return mon;
	}

}
