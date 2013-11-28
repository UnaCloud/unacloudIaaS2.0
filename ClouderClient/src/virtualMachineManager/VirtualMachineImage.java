package virtualMachineManager;

import java.io.File;

public class VirtualMachineImage {
	long id;
	File configurationFile;
	String username;
	String password;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public File getConfigurationFile(){
		return configurationFile;
	}
	public void setConfigurationFile(File configurationFile){
		this.configurationFile = configurationFile;
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
}