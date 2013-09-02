package communication;

import com.losandes.communication.security.utils.AbstractCommunicator;
import configuration.VirtualMachineConfigurator;
import execution.PersistentExecutionManager;
import fileTransfer.FileTrasferAttender;
import fileTransfer.UnicastSender;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import monitoring.PhysicalMachineMonitor;
import physicalmachine.OperatingSystem;
import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales
 * Responsible for attending or discarding a Clouder Server operation request in a thread
 */
public class ClouderServerAttentionThread extends Thread {

    /**
     * Abstract comunnicator used to recieve the request and send a response
     */
    private AbstractCommunicator communication;

    /**
     * Owner of this object. Used to attend upgrade and stop requests
     */
    private Closeable clouderAttention;

    /**
     * Constructs an attention thread for a given communicator channel. Recieves an owner object for stop requests
     * @param clientSocket
     * @param cca The owner of this object
     */
    public ClouderServerAttentionThread(AbstractCommunicator clientSocket, Closeable cca) {
        communication = clientSocket;
        clouderAttention = cca;
    }

    /**
     * Responsible for attending or discarding the Clouder Server operation request
     */
    public void run() {
        try {
            //receiving a operation request from the Clouder Server
            //spliting the message in a processable vector
            String[] clouderServerRequestSplitted = communication.readUTFList();
            System.out.println(Arrays.toString(clouderServerRequestSplitted));
            //clouderClientOperationResult is the result variable for responding to Clouder Server
            // operationDomain = {VIRTUAL_MACHINE_OPERATION, PHYSICAL_MACHINE_OPERATION}
            int operationDomain = 0;
            if (clouderServerRequestSplitted[0] != null && !clouderServerRequestSplitted[0].equals("")) {
                operationDomain = Integer.parseInt(clouderServerRequestSplitted[0]);
            }
            if (operationDomain != 0 && operationDomain < 7) {
                if (operationDomain == VIRTUAL_MACHINE_OPERATION) {
                    VIRTUAL_MACHINE_OPERATION(clouderServerRequestSplitted);
                } else if (operationDomain == PHYSICAL_MACHINE_OPERATION) {
                    PHYSICAL_MACHINE_OPERATION(clouderServerRequestSplitted, communication);
                } else if (operationDomain == ARTHUR_OPERATION) {
                    String rutaVMX = clouderServerRequestSplitted[1];
                    String rutaMaquina = clouderServerRequestSplitted[2];
                    String operacion = clouderServerRequestSplitted[3];
                    if (new File(rutaVMX).exists() && new File(rutaMaquina).exists() && rutaMaquina.toLowerCase().endsWith("vmx") && rutaVMX.toLowerCase().endsWith("vmrun.exe") && operacion.length() < 10);
                    rutaVMX = rutaVMX.replaceAll(";|\n|\r|$", "");
                    rutaMaquina = rutaMaquina.replaceAll(";|\n|\r|$", "");
                    String snapshot = clouderServerRequestSplitted[4];
                    try {
                        if (operacion.equals("fileWrite")) {
                            Runtime.getRuntime().exec(DOUBLE_QUOTE + rutaVMX + DOUBLE_QUOTE + " -gu root -gp un14nd35c0m17 copyFileFromHostToGuest \"" + rutaMaquina + "\" \"E:/GRID/masterSGE/Aniversario.rar\"  /usr/local/Aniversario.rar").waitFor();
                        } else if (operacion.contains("start")) {
                            if (snapshot.equals("1")) {
                                Runtime.getRuntime().exec("\"" + rutaVMX + "\" revertToSnapshot  \"" + rutaMaquina + "\" \"AutoProtect Snapshot/AutoProtect Snapshot/AutoProtect Snapshot/AutoProtect Snapshot/AutoProtect Snapshot\"").waitFor();
                            }
                            Process p = Runtime.getRuntime().exec("\"" + rutaVMX + "\" " + operacion + " \"" + rutaMaquina + "\" nogui");
                        } else {
                            Process p = Runtime.getRuntime().exec("\"" + rutaVMX + "\" " + operacion + " \"" + rutaMaquina + "\"");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (operationDomain == UPDATE_OPERATION) {
                    clouderAttention.close();
                    try {
                        Runtime.getRuntime().exec("javaw -jar ClientUpdater.jar 6");
                    } catch (Exception e) {
                    }
                    System.exit(6);
                } else if (operationDomain == STOP_CLIENT) {
                    clouderAttention.close();
                    System.exit(0);
                } else if (operationDomain == VIRTUAL_MACHINE_CONFIGURATION) {
                    new VirtualMachineConfigurator().attendConfigurationRequest(clouderServerRequestSplitted, communication);
                } else if (operationDomain == GET_VERSION) {
                    communication.writeUTF("1.30");
                } else {
                    //TODO manejar error
                    //System.err.println(clouderClientOperationResult);
                }
            } else {
                // clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server request is null or an invalid number: " + operationDomain;
                // System.err.println(clouderClientOperationResult);
            }
            communication.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            // System.err.println("The communication process with Clouder Server failed in ClouderServerAttentionThread: " + ex.getMessage());
        }
    }

    /**
     * Method responsible for attending requests for operations over virtual machines
     * @param clouderServerRequestSplitted Server request
     */
    private void VIRTUAL_MACHINE_OPERATION(String[] clouderServerRequestSplitted) {
        String clouderClientOperationResult = "";
        System.out.println("The Clouder Server operation request is virtual machine type");
        //virtualOperationType = {VM_TURN_ON, VM_TURN_OFF, VM_RESTART, VM_STATE}
        int virtualOperationType = -1;
        if (clouderServerRequestSplitted[1] != null && !clouderServerRequestSplitted[1].equals("")) {
            virtualOperationType = Integer.parseInt(clouderServerRequestSplitted[1]);
        }
        String virtualMachineCode = null;
        if (clouderServerRequestSplitted[2] != null && !clouderServerRequestSplitted[2].equals("")) {
            virtualMachineCode = clouderServerRequestSplitted[2];
        }
        if (virtualMachineCode == null || virtualOperationType == -1) {
            //TODO reportar el error.
            return;
            //return "ERROR";
        }
        switch (virtualOperationType) {
            case VM_TURN_ON:
                System.out.println("The Clouder Server operation request is VM_TURN_ON");
                clouderClientOperationResult = "VM_TURN_ON";
                if (clouderServerRequestSplitted.length > 11) {
                    int hypervisorName = Integer.parseInt(clouderServerRequestSplitted[3]);
                    int vmCores = Integer.parseInt(clouderServerRequestSplitted[4]);
                    int vmMemory = Integer.parseInt(clouderServerRequestSplitted[5]);
                    String vmPath = clouderServerRequestSplitted[6];
                    String hypervisorPath = clouderServerRequestSplitted[7];
                    int executionTime = Integer.parseInt(clouderServerRequestSplitted[8]);
                    String vmIP = clouderServerRequestSplitted[9];
                    String persistent = clouderServerRequestSplitted[10];
                    String checkPoint = clouderServerRequestSplitted[11];
                    String snapshotRoute = clouderServerRequestSplitted[12];
                    PersistentExecutionManager.addExecution(vmPath, hypervisorPath, vmIP, virtualMachineCode, executionTime, vmCores, vmMemory, hypervisorName, persistent, checkPoint);
                } else {
                    clouderClientOperationResult += ERROR_MESSAGE + "invalid number of parameters: " + clouderServerRequestSplitted.length;
                }
                break;

            case VM_TURN_OFF:
                clouderClientOperationResult = "VM_TURN_OFF";
                if (clouderServerRequestSplitted.length > 5) {
                    int hypervisorName = Integer.parseInt(clouderServerRequestSplitted[3]);
                    String vmPath = clouderServerRequestSplitted[4];
                    String hypervisorPath = clouderServerRequestSplitted[5];
                    PersistentExecutionManager.removeExecution(hypervisorName, vmPath, hypervisorPath, virtualMachineCode);
                } else {
                    clouderClientOperationResult += ERROR_MESSAGE + "invalid number of parameters: " + clouderServerRequestSplitted.length;
                }
                break;

            case VM_RESTART:
                clouderClientOperationResult = "VM_RESTART";
                if (clouderServerRequestSplitted.length > 5) {
                    int hypervisorName = Integer.parseInt(clouderServerRequestSplitted[3]);
                    String vmPath = clouderServerRequestSplitted[4];
                    String hypervisorPath = clouderServerRequestSplitted[5];
                    PersistentExecutionManager.restartMachine(hypervisorName, vmPath, hypervisorPath, virtualMachineCode);
                } else {
                    clouderClientOperationResult += ERROR_MESSAGE + "invalid number of parameters: " + clouderServerRequestSplitted.length;
                }
                break;
            case VM_TIME:
                System.out.println("The Clouder Server operation request is VM_TIME");
                clouderClientOperationResult = "VM_TIME";
                if (clouderServerRequestSplitted.length > 4) {
                    int executionTime = Integer.parseInt(clouderServerRequestSplitted[3]);
                    String id = clouderServerRequestSplitted[4];
                    PersistentExecutionManager.extendsVMTime(id, executionTime);
                } else {
                    clouderClientOperationResult += ERROR_MESSAGE + "invalid number of parameters: " + clouderServerRequestSplitted.length;
                }
                break;

            default:
                clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server virtual machine operation request is invalid: " + virtualOperationType;
            //System.err.println(clouderClientOperationResult);
        }
    }

    /**
     * Method responsible for attending requests for operations over the physical machine
     * @param clouderServerRequestSplitted Server request
     * @param con Channel used to interact with UnaCloud server to recieve or send additional data
     */
    private void PHYSICAL_MACHINE_OPERATION(String[] clouderServerRequestSplitted, AbstractCommunicator con) {
        String clouderClientOperationResult = "";
        System.out.println("The Clouder Server operation request is physical machine type");
        // clouderServerRequestSplitted[1] = {PM_TURN_OFF, PM_RESTART, PM_LOGOUT, PM_MONITOR}
        int physicalOperationType = 0;
        if (clouderServerRequestSplitted.length > 1) {
            if (clouderServerRequestSplitted[1] != null && !clouderServerRequestSplitted[1].equals("")) {
                physicalOperationType = Integer.parseInt(clouderServerRequestSplitted[1]);
            }
        }
        OperatingSystem opeSys = null;
        switch (physicalOperationType) {
            case PM_TURN_OFF:
                clouderClientOperationResult = "PM_TURN_OFF";
                opeSys = new OperatingSystem();
                clouderClientOperationResult += MESSAGE_SEPARATOR_TOKEN + opeSys.turnOff();
                break;

            case PM_RESTART:
                clouderClientOperationResult = "PM_RESTART";
                opeSys = new OperatingSystem();
                clouderClientOperationResult += MESSAGE_SEPARATOR_TOKEN + opeSys.restart();
                break;
            case PM_LOGOUT:
                clouderClientOperationResult = "PM_LOGOUT";
                opeSys = new OperatingSystem();
                clouderClientOperationResult += MESSAGE_SEPARATOR_TOKEN + opeSys.logOut();
                break;
            case PM_WRITE_FILE:
                clouderClientOperationResult = "PM_WRITE_FILE";
                FileTrasferAttender.attendFileOperation(clouderServerRequestSplitted, con);
                break;
            case PM_TURN_ON:
                String[] macs = Arrays.copyOfRange(clouderServerRequestSplitted, 2, clouderServerRequestSplitted.length);
                for (String mac : macs) {
                    try {
                        Runtime.getRuntime().exec("wol.exe " + mac.replace(":",""));
                    } catch (IOException ex) {
                    }
                }
                break;
            case PM_MONITOR:
                if (clouderServerRequestSplitted.length > 2) {
                    String op = clouderServerRequestSplitted[2];
                    if (op.equals("STOP")) {
                        PhysicalMachineMonitor.stop();
                    } else if (op.equals("START") && clouderServerRequestSplitted.length > 4) {
                        int fm = Integer.parseInt(clouderServerRequestSplitted[3]);
                        int fr = Integer.parseInt(clouderServerRequestSplitted[4]);
                        PhysicalMachineMonitor.start(fm, fr);
                    }

                }
                break;
            case PM_RETRIEVE_FOLDER:
                System.out.println("The Clouder Server operation request is MACHINE_RESTORE");
                clouderClientOperationResult = "MACHINE_RESTORE";
                if (clouderServerRequestSplitted.length > 2) {
                    System.out.println("Atendiendo MAchineStore");
                    try{
                        new UnicastSender().attendFileRetrieveRequest(clouderServerRequestSplitted, communication);
                    }catch(Exception e){
                        clouderClientOperationResult += ERROR_MESSAGE + " "+e.getMessage();
                    }
                    
                } else {
                    clouderClientOperationResult += ERROR_MESSAGE + "invalid number of parameters: " + clouderServerRequestSplitted.length;
                }

                break;
            default:
                clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server physical machine operation request is invalid: " + physicalOperationType;
            //System.err.println(clouderClientOperationResult);
        }
    }
}//end of ClouderServerAttentionThread

