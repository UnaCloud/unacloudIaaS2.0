package configuration;

import com.losandes.communication.messages.UnaCloudMessage;
import com.losandes.communication.messages.configuration.ChangeVirtualMachineMacMessage;
import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
import com.losandes.utils.VariableManager;
import com.losandes.utils.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.*;

import virtualmachine.Hypervisor;
import virtualmachine.HypervisorFactory;
import static com.losandes.utils.Constants.*;
import static com.losandes.communication.messages.configuration.ConfigurationAbstractMessage.*;
import com.losandes.communication.messages.configuration.ExecuteCommandMessage;
import com.losandes.communication.messages.configuration.RecieveFileMessage;
import com.losandes.communication.messages.configuration.StartVirtualMachineMessage;
import com.losandes.communication.messages.configuration.StopVirtualMachineMessage;
import com.losandes.communication.messages.configuration.TakeSnapshotMessage;
import com.losandes.communication.messages.configuration.WriteFileOnVirtualMachineMessage;

import virtualmachine.HypervisorOperationException;

/**
 * Class responsible for attending virtual machine configuration requests
 * @author Clouder
 */
public class VirtualMachineConfigurator {

    /**
     * Temporal file to be used for writeFileOnVirtualMachine operations
     */
    private static final File temporalFile = new File("temp/a.txt");

    /**
     * Attends a requets to write a file on a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     */
    private void writeFileOnVirtualMachine(WriteFileOnVirtualMachineMessage message, AbstractCommunicator con) {
        try {
            temporalFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(temporalFile);
            recieveFileOverChannel(fos, con);
            try {
                HypervisorFactory.getHypervisor(Integer.parseInt(message.getHypervisor()),message.getRutaVMRUN(),message.getDestinationMachine()).copyFileOnVirtualMachine(message.getLogin(),message.getPass(),message.getDestinationFileRute(),temporalFile);
                con.writeUTF("Archivo copiado exitosamente");
            } catch (HypervisorOperationException ex) {
                con.writeUTF("Error copiando el archivo " + ex.getMessage());
            }
        } catch (Exception e) {
        }
    }

    /**
     * Recieves a File over the file transfer socket and writes it on the given File output stream
     * @param fos The file output stream in which the file will be write
     * @param con The channel to interact with UnaCloud server
     * @throws Exception
     */
    private void recieveFileOverChannel(FileOutputStream fos, AbstractCommunicator con) throws Exception {
        ServerSocket ss = new ServerSocket(VariableManager.getIntValue("FILE_TRANSFER_SOCKET") + 1);
        con.writeUTF(OK_MESSAGE);
        Socket c = ss.accept();
        ss.close();
        InputStream isr = (c.getInputStream());
        byte[] buffer = new byte[1024];
        for (int e = 0; (e = isr.read(buffer)) != -1;) {
            fos.write(buffer, 0, e);
            System.out.println(e);
        }
        c.close();
        fos.close();
    }

    /**
     * Attends a request to write a file on this physical machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     */
    private void recieveUnicastFile(RecieveFileMessage solicitud, AbstractCommunicator con) {
	try {
            new File(solicitud.getFilePath()).getParentFile().mkdirs();
            File c = new File(solicitud.getFilePath());
            con.writeUTF("OK");
            FileOutputStream fos = new FileOutputStream(c);
            recieveFileOverChannel(fos, con);
            con.writeUTF("OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Attends a request to start a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @throws ConnectionException
     */
    private void startVirtualMachine(StartVirtualMachineMessage solicitud, AbstractCommunicator con) throws ConnectionException {
        try {
            HypervisorFactory.getHypervisor(Integer.parseInt(solicitud.getHypervisor()),solicitud.getRutaVMRUN(), solicitud.getDestinationMachine()).turnOnVirtualMachine();
            Log.print2("Maquina encendida exitosamente");
            con.writeUTF("Maquina encendida exitosamente");
        } catch (HypervisorOperationException ex) {
            Log.print2("Error Starting Machine:"+ ex.getMessage());
            con.writeUTF(ex.getMessage());
        }
    }
    /**
     * Attends a request to start a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @throws ConnectionException
     */
    private void changeVirtualMachineMac(ChangeVirtualMachineMacMessage solicitud, AbstractCommunicator con) throws ConnectionException {
        try {
            HypervisorFactory.getHypervisor(Integer.parseInt(solicitud.getHypervisor()),solicitud.getRutaVMRUN(), solicitud.getDestinationMachine()).changeVirtualMachineMac();
            Log.print2("Maquina encendida exitosamente");
            con.writeUTF("Maquina encendida exitosamente");
        } catch (HypervisorOperationException ex) {
            Log.print2("Error Starting Machine:"+ ex.getMessage());
            con.writeUTF(ex.getMessage());
        }
    }
    /**
     * Attends a request to take a snapshot on a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @throws ConnectionException
     */
    private void takeSnapshotOnVirtualMachine(TakeSnapshotMessage solicitud, AbstractCommunicator con) throws ConnectionException {
        try {
            HypervisorFactory.getHypervisor(Integer.parseInt(solicitud.getHypervisor()),solicitud.getRutaVMRUN(),solicitud.getDestinationMachine()).takeSnapshotOnMachine(solicitud.getSnapshotname());
            Log.print2("Maquina snapshot");
            con.writeUTF("Snapshot tomado");
        } catch (HypervisorOperationException ex) {
            Log.print2("Error Starting Machine:"+ ex.getMessage());
            con.writeUTF(ex.getMessage());
        }
    }
    
    /**
     * Attends a request to execute commands on a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @throws ConnectionException
     */
    private void executeCommandsOnVirtualMachine(ExecuteCommandMessage solicitud, AbstractCommunicator con) throws ConnectionException {
        String respuesta[] = new String[solicitud.getComandos().length];
        Hypervisor v = HypervisorFactory.getHypervisor(solicitud.getHypervisor(),solicitud.getRutaVMRUN(),solicitud.getDestinationMachine());
        for (int e = 0; e < solicitud.getComandos().length; e++){
            try {
                v.executeCommandOnMachine(solicitud.getUser(),solicitud.getPass(), solicitud.getComandos()[e]);
                respuesta[e] = "Successful execution";
            } catch (HypervisorOperationException ex) {
                respuesta[e] = ex.getMessage();
            }
        }
        con.writeUTF(respuesta);
    }

    /**
     * Attends a request to stop a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @throws ConnectionException
     */
    private void stopVirtualMachine(StopVirtualMachineMessage solicitud, AbstractCommunicator con) throws ConnectionException {
        try {
            HypervisorFactory.getHypervisor(Integer.parseInt(solicitud.getHypervisor()),solicitud.getRutaVMRUN(),solicitud.getDestinationMachine()).turnOffVirtualMachine();
            con.writeUTF("Maquina apagada exitosamente");
        } catch (HypervisorOperationException ex) {
            con.writeUTF(ex.getMessage());
        }
    }

    /**
     * Attends a request to make a configuration operation over a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @return
     */
    public boolean attendConfigurationRequest(UnaCloudMessage solicitud, AbstractCommunicator con) {
        System.out.println(solicitud);
        try {
        	switch (solicitud.getString(1)) {
			case VMC_TRANSFER_FILE:
                            recieveUnicastFile(new RecieveFileMessage(solicitud), con);
                            break;
			case VMC_WRITE_FILE:
                            writeFileOnVirtualMachine(new WriteFileOnVirtualMachineMessage(solicitud), con);
                            break;
			case VMC_START:
                            startVirtualMachine(new StartVirtualMachineMessage(solicitud), con);
                            break;
			case VMC_STOP:
                            stopVirtualMachine(new StopVirtualMachineMessage(solicitud), con);
                            break;
			case VMC_COMMAND:
                            executeCommandsOnVirtualMachine(new ExecuteCommandMessage(solicitud), con);
                            break;
                        case VMC_CHANGE_MAC:
                            changeVirtualMachineMac(new ChangeVirtualMachineMacMessage(solicitud), con);
                            break;
			case VMC_TAKE_SNAPSHOT:
                            takeSnapshotOnVirtualMachine(new TakeSnapshotMessage(solicitud), con);
                            break;
			default:
                            return false;
			}
            return true;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    
}