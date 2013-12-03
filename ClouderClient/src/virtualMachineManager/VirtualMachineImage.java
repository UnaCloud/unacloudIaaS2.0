package virtualMachineManager;

import java.io.File;
import java.io.Serializable;

public class VirtualMachineImage implements Serializable{
	private static final long serialVersionUID = -2386734224180305694L;
	long id;
	String virtualMachineName;
	File mainFile;
	String username;
	String password;
	
	String configuratorClass;
	String hypervisorId;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public File getMainFile() {
		return mainFile;
	}
	public void setMainFile(File mainFile) {
		this.mainFile = mainFile;
	}
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getVirtualMachineName() {
		return virtualMachineName;
	}
	public void setVirtualMachineName(String virtualMachineName) {
		this.virtualMachineName = virtualMachineName;
	}
	public String getConfiguratorClass() {
		return configuratorClass;
	}
	public String getHypervisorId() {
		return hypervisorId;
	}
	public void setHypervisorId(String hypervisorId) {
		this.hypervisorId = hypervisorId;
	}
	public void setConfiguratorClass(String configuratorClass) {
		this.configuratorClass = configuratorClass;
	}
}