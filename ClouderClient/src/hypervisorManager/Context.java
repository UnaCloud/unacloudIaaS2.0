package hypervisorManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales
 * Responsible for changing the .vmx configuration file for executing a virtual machine fulfilling the user context
 */
public class Context {

    public File vmxFile;

    /**
     * Constructor method
     * @param vmxPath
     */
    public Context(String vmxPath) {
        if (vmxPath != null) {
            vmxFile = new File(vmxPath.replace("\"",""));
        }
    }

    /**
     * Responible for sorting the .vmx file context process
     * @param vmCores
     * @param vmMemory
     * @return
     */
    public String changeVMXFileContext(String vmCores, String vmMemory,boolean persistent) {
        String per = persistent?"keep":"autoRevert";
        String[][] vmxParameters = {/*{VMW_VMX_HW, VMW_VMX_HW_VER},*/ {VMW_VMX_CPU, vmCores}, {VMW_VMX_MEMORY, vmMemory},{"snapshot.action",per},{"priority.ungrabbed","idle"}};
        return changeVMXparameter(vmxParameters);
    }

    /**
     * Responsible for converting a .vmx file to a String
     * @param vmxPath
     * @return
     */
    private static ArrayList<String> convertVMXToString(String vmxPath) {
        System.out.println("convertVMXToString "+vmxPath);
        ArrayList<String> ar=new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(vmxPath));
            for(String h;(h=br.readLine())!=null;){
                ar.add(h);
            }
            br.close();
            return ar;
        } catch (IOException ex) {
            System.err.println();
            return null;
        }
    }

    /**
     * Prints a new VMX file using a collection of string properties
     * @param vmxPath The path of the output VMX file
     * @param lines The content of the file
     */
    private static void convertStrignToVMX(String vmxPath,ArrayList<String> lines) {
        try {
            PrintWriter pw = new PrintWriter(vmxPath);
            for(int e=0;e<lines.size();e++){
                pw.println(lines.get(e));
            }
            pw.close();
        } catch (IOException ex) {
            System.err.println();
        }
    }

    /**
     * Responsible for changing a vmx parameter value or create it if not exist (only compatible with VMWare Workstation 6.5)
     * @param vmxParameters [][] = {{VMW_VMX_HW, VMW_VMX_HW_VER}, {VMW_VMX_CPU, vmCores}, {VMW_VMX_MEMORY, vmMemory}}
     */
    private String changeVMXparameter(String[][] vmxParameters) {
        ArrayList<String> splittedString = convertVMXToString(vmxFile.getPath());
        if(splittedString==null)return ERROR_MESSAGE;
        boolean found = false;
        for (int r = 0; r < vmxParameters.length; r++) {
            found = false;
            for (int i = 0; i < splittedString.size()&&!found; i++) {
                if (splittedString.get(i).contains(vmxParameters[r][0])) {
                    splittedString.set(i,vmxParameters[r][0] + " = " + DOUBLE_QUOTE + vmxParameters[r][1] + DOUBLE_QUOTE + " ");
                    found = true;
                }
            }
            if(!found){
                splittedString.add(vmxParameters[r][0] + " = " + DOUBLE_QUOTE + vmxParameters[r][1] + DOUBLE_QUOTE + " ");
            }
        }
        convertStrignToVMX(vmxFile.getPath(), splittedString);
        return OK_MESSAGE;
    }
}