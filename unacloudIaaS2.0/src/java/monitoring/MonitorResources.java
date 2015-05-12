package monitoring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
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
			File temp = File.createTempFile("EnergyReport_"+host+'_'+(d.getYear()+1900)+"-"+(d.getMonth()+1)+"-"+d.getDate()+'_'+d.getHours()+"-"+d.getMinutes()+"-", ".csv");
			temp.deleteOnExit();
			PrintWriter pw = new PrintWriter(new FileOutputStream(temp,true),true);			
		    List<MonitorEnergyReport> reports = getListEnergyReports(start, end, host);
		    pw.println(MonitorEnergyReport.getHead());
		    for (MonitorEnergyReport report : reports) {
		    	pw.println(report.getLine());
			} 
		    //generate whatever data you want	 
		    pw.flush();
		    pw.close();
		    return temp;
		}
		catch(Exception e)
		{
		     e.printStackTrace();
		     return null;
		} 
	}
	public File createReportCPU(String host, Date start, Date end){
		Date d = new Date();
		try
		{
			File temp = File.createTempFile("CPUReport_"+host+'_'+(d.getYear()+1900)+""+(d.getMonth()+1)+""+d.getDate()+'_'+d.getHours()+"-"+d.getMinutes()+"-", ".csv");
			temp.deleteOnExit();
			BufferedWriter bw = new BufferedWriter(new FileWriter(temp), 8192*4);
			//PrintWriter pw = new PrintWriter(new FileOutputStream(temp,true),true);			
		    List<MonitorReport> reports = getListCPUReports(start, end, host);
		    bw.append(MonitorReport.getHead());
		    bw.flush();
		    for (MonitorReport report : reports) {
		    	bw.newLine();
		    	bw.append(report.getLine());
		    	bw.flush();
			} 	 
		    bw.close();
		    return temp;
		}
		catch(Exception e)
		{
		     e.printStackTrace();
		     return null;
		} 
	}


}
