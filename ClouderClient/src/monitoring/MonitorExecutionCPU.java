package monitoring;

import java.io.Serializable;
import java.util.ArrayList;

import communication.messages.monitoring.MonitorInitialReport;
import communication.messages.monitoring.MonitorReport;

public class MonitorExecutionCPU implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7491003255880753375L;
	
	private MonitorInitialReport initial;	
	private ArrayList<MonitorReport> reports;	
	
	public MonitorExecutionCPU() {	}
	
	public MonitorInitialReport getInitial() {
		return initial;
	}
	
	public ArrayList<MonitorReport> getReports() {
		return reports;
	}
	
	public void setInitial(MonitorInitialReport initial) {
		this.initial = initial;
	}
	
	public void setReports(ArrayList<MonitorReport> reports) {
		this.reports = reports;
	}
}
