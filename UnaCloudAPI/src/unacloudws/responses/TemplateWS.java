
package unacloudws.responses;


import java.util.List;

public class TemplateWS {

    protected List<String> applications;
    protected boolean customizable;
    protected boolean highAvailability;
    protected OperatingSystemWS operatingSystem;
    protected Integer templateCode;
    protected String templateName;
    protected String templateType;
	public List<String> getApplications() {
		return applications;
	}
	public void setApplications(List<String> applications) {
		this.applications = applications;
	}
	public boolean isCustomizable() {
		return customizable;
	}
	public void setCustomizable(boolean customizable) {
		this.customizable = customizable;
	}
	public boolean isHighAvailability() {
		return highAvailability;
	}
	public void setHighAvailability(boolean highAvailability) {
		this.highAvailability = highAvailability;
	}
	public OperatingSystemWS getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(OperatingSystemWS operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	public Integer getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(Integer templateCode) {
		this.templateCode = templateCode;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
}
