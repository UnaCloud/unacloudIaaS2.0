package clientconfigurer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.losandes.utils.VariableManager;
public class Main {
	
	public static void main(String... args) throws IOException{
    	VariableManager local= VariableManager.local;
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
    	String hostname=local.getStringValue("LOCAL_HOSTNAME");
    	System.out.println("Variable LOCAL_HOSTNAME value: "+ hostname);
    	System.out.println("Do you wanna change this value? (Y/N)");
    	String input = br.readLine();
    	if (input.equalsIgnoreCase("y")){
    		System.out.println("Enter new value for LOCAL_HOSTNAME:");
        	input = br.readLine();
        	if(!(input.isEmpty())){
        		local.setStringValue("LOCAL_HOSTNAME", input);
        	}
    	}
    	String vboxPath=local.getStringValue("VBOX_PATH");
    	System.out.println("Variable VBOX_PATH value: "+ vboxPath);
    	System.out.println("Do you wanna change this value? (Y/N)");
    	input = br.readLine();
    	if (input.equalsIgnoreCase("y")){
    		System.out.println("Enter new value for VBOX_PATH:");
        	input = br.readLine();
        	if(!(input.isEmpty())){
        		local.setStringValue("VBOX_PATH", input);
        	}
    	}
    	
    	String vmRunPath= local.getStringValue("VMRUN_PATH");
    	System.out.println("Variable VMRUN_PATH value: "+ vmRunPath);
    	System.out.println("Do you wanna change this value? (Y/N)");
    	input = br.readLine();
    	if (input.equalsIgnoreCase("y")){
    		System.out.println("Enter new value for VMRUN_PATH:");
        	input = br.readLine();
        	if(!(input.isEmpty())){
        		local.setStringValue("VMRUN_PATH", input);
        	}
    	}
    	String vmRepoPath=local.getStringValue("VM_REPO_PATH");
    	System.out.println("Variable VM_REPO_PATH value: "+ vmRepoPath);
    	System.out.println("Do you wanna change this value? (Y/N)");
    	input = br.readLine();
    	if (input.equalsIgnoreCase("y")){
    		System.out.println("Enter new value for VM_REPO_PATH:");
        	input = br.readLine();
        	if(!(input.isEmpty())){
        		local.setStringValue("VM_REPO_PATH", input);
        	}
    	}
    	br.close();
    }
	
}
