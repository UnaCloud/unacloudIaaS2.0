package monitoring;

import java.io.File;
import java.io.FileWriter;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import com.losandes.connectionDb.MonitorEnergyReport;
import com.losandes.connectionDb.MonitorInitialReport;
import com.losandes.connectionDb.MonitorQuery;
import com.losandes.connectionDb.MonitorReport;

public class MonitorResources {
	
	private MonitoringDBServerConnection connection;
	
	public MonitorResources() throws UnknownHostException {
		connection = new MonitoringDBServerConnection();
	}
	
//	public static void main(String[] args) {
//		
//		try {
//			MonitorResources query = new MonitorResources();
//			query.start();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public void start() throws UnknownHostException{
//		MonitorQuery query = new MonitorQuery(connection);
//		System.out.println(query.getCPUMetrics("ISC301"));
//		Date d = new Date();
//		d.setTime(d.getTime()-1000*60*60*24);
//		List<MonitorReport> monitors = query.getCpuReportsByDate(d, new Date(), "ISC301");
//		for (MonitorReport monitorReport : monitors) {
//			System.out.println(monitorReport);
//		}
//	}
	public MonitorInitialReport getCPUMetrics(String host){
		MonitorQuery query = new MonitorQuery(connection);
		return query.getCPUMetrics(host);
	}
	public List<MonitorReport> getListCPUReports(Date start, Date end, String host){
		MonitorQuery query = new MonitorQuery(connection);
		return query.getCpuReportsByDate(start, end, host);		
	}
	public List<MonitorEnergyReport> getListEnergyReports(Date start, Date end, String host){
		MonitorQuery query = new MonitorQuery(connection);
		return query.getEnergyReportsByDate(start, end, host);		
	}
	
	public File createReportEnergy(String host, Date start, Date end){
		Date d = new Date();
		try
		{
			File temp = File.createTempFile("EnergyReport_"+host+"_"+d.toString(), ".csv");
		    FileWriter writer = new FileWriter(temp);
		    List<MonitorEnergyReport> reports = getListEnergyReports(start, end, host);
		    for (MonitorEnergyReport report : reports) {
				
			} 
		    //generate whatever data you want
	 
		    writer.flush();
		    writer.close();
		    return temp;
		}
		catch(Exception e)
		{
		     e.printStackTrace();
		     return null;
		} 
	}
//	MongoConnection m = connection.generateConnection();
//	BasicDBObject query = new BasicDBObject("Hostname", "ISC208");
//	DBCursor cursor = m.energyCollection().find(query);
//	try {
//		while(cursor.hasNext()) {
//		BasicDBObject db = (BasicDBObject) cursor.next();
//		System.out.println(db);
//			   System.out.println(db.getString("_id"));
//			   System.out.println(db.getObjectId("_id").getTimestamp()+" --- "+new Date(db.getObjectId("_id").getTime()));
//		   }
//	} finally {
//		 cursor.close();
//	}

}
