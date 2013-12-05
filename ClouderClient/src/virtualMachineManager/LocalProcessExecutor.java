package virtualMachineManager;

import java.io.*;

import static com.losandes.utils.Constants.*;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Eduardo Rosales
 * Responsible for executing commands on local machine
 */
public class LocalProcessExecutor {

    private LocalProcessExecutor() {}

    /**
     * Responsible for executing local commands without output
     * @param inCommand Command to execute
     * @return If the command was suceesfully execute or nor
     */
    public static boolean executeCommand(String command){
    	System.out.println("Exec: "+command);
        try {
            Runtime.getRuntime().exec(command).waitFor();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Responsible for executing local commands with outputs
     * @param inCommand Command to execute
     * @return The output of the command execution
     */
    public static String executeCommandOutput(String...command){
    	Process p;
        try {
            p = Runtime.getRuntime().exec(command);
        }catch(IOException ex){
            System.out.println("Error: Executable not found");
            return "Error: Executable not found";
        }
        String outputs = "";
        try(BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()))){
            for(String line;(line = in.readLine()) != null;){
            	outputs += line + "\n";
            }
        }catch (IOException ex){
            outputs =ERROR_MESSAGE + "executing " + Arrays.toString(command) + " : " + ex.getMessage();
        }
        try(BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()))){
            for(String line;(line = in.readLine()) != null;){
            	outputs += line + "\n";
            }
        }catch (IOException ex){
        	ex.printStackTrace();
        }
        try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return outputs;
    }
}
