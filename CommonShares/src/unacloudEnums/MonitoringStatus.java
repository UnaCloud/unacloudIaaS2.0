package unacloudEnums;

public enum MonitoringStatus {
	RUNNING("Running"), STOPPED("Stopping"), OFF("Off"), RESUME("Resume"), DISABLE("Disable");
	
	private String title;
	
	private MonitoringStatus(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public static MonitoringStatus getEnum(String title){
		if(RUNNING.getTitle().equals(title))return RUNNING;
		else if(STOPPED.getTitle().equals(title))return STOPPED;
		else if(RESUME.getTitle().equals(title))return RESUME;
		else if(DISABLE.getTitle().equals(title))return DISABLE;
		else return OFF;
	}
}
