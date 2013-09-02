package configuration;

import com.losandes.communication.messages.UnaCloudMessage;
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
    private void writeFileOnVirtualMachine(UnaCloudMessage solicitud, AbstractCommunicator con) {
        try {
            String hypervisor = solicitud.getString(2), destinationMachine = solicitud.getString(3), login = solicitud.getString(4), pass = solicitud.getString(5), destinationFileRute = solicitud.getString(6), rutaVMRUN = solicitud.getString(7);
            temporalFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(temporalFile);
            recieveFileOverChannel(fos, con);
            System.out.println("archivo recibido");
            try {
                HypervisorFactory.getHypervisor(Integer.parseInt(hypervisor), rutaVMRUN, destinationMachine).copyFileOnVirtualMachine(login, pass, destinationFileRute, temporalFile);
                con.writeUTF("Archivo copiado exitosamente");
            } catch (HypervisorOperationException ex) {
                con.writeUTF("Error copiando el archivo " + ex.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    private void recieveUnicastFile(UnaCloudMessage solicitud, AbstractCommunicator con) {
        System.out.println("recibirArchivoUnicast");
	try {
            String rutaArchivo = solicitud.getString(2);
            new File(rutaArchivo).getParentFile().mkdirs();
            File c = new File(rutaArchivo);
            con.writeUTF("OK");
            FileOutputStream fos = new FileOutputStream(c);
            recieveFileOverChannel(fos, con);
            con.writeUTF("OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Termino recibirArchivoUnicast");
    }


    /**
     * Attends a request to start a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @throws ConnectionException
     */
    private void startVirtualMachine(UnaCloudMessage solicitud, AbstractCommunicator con) throws ConnectionException {
        String hypervisor = solicitud.getString(2), destinationMachine = solicitud.getString(3), rutaVMRUN = solicitud.getString(4);
        try {
            HypervisorFactory.getHypervisor(Integer.parseInt(hypervisor), rutaVMRUN, destinationMachine).turnOnVirtualMachine();
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
    private void takeSnapshotOnVirtualMachine(UnaCloudMessage solicitud, AbstractCommunicator con) throws ConnectionException {
        String hypervisor = solicitud.getString(2), destinationMachine = solicitud.getString(3), rutaVMRUN = solicitud.getString(4), snapshotname=solicitud.getString(5);
        try {
            HypervisorFactory.getHypervisor(Integer.parseInt(hypervisor), rutaVMRUN, destinationMachine).takeSnapshotOnMachine(snapshotname);
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
    private void executeCommandsOnVirtualMachine(UnaCloudMessage solicitud, AbstractCommunicator con) throws ConnectionException {
        String hypervisor = solicitud.getString(2), destinationMachine = solicitud.getString(3), user = solicitud.getString(4), pass = solicitud.getString(5), rutaVMRUN = solicitud.getString(6);
        int numeroComandos = solicitud.getInteger(7);
        String respuesta[] = new String[numeroComandos];
        Hypervisor v = HypervisorFactory.getHypervisor(Integer.parseInt(hypervisor), rutaVMRUN, destinationMachine);
        for (int e = 0; e < numeroComandos; e++) {
            String comando = solicitud.getString(8 + e);
            try {
                v.executeCommandOnMachine(user, pass, comando);
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
    private void stopVirtualMachine(UnaCloudMessage solicitud, AbstractCommunicator con) throws ConnectionException {
        String hypervisor = solicitud.getString(2), destinationMachine = solicitud.getString(3), rutaVMRUN = solicitud.getString(4);
        try {
            HypervisorFactory.getHypervisor(Integer.parseInt(hypervisor), rutaVMRUN, destinationMachine).turnOffVirtualMachine();
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
				recieveUnicastFile(solicitud, con);
				break;
			case VMC_WRITE_FILE:
				writeFileOnVirtualMachine(solicitud, con);
				break;
			case VMC_START:
				startVirtualMachine(solicitud, con);
				break;
			case VMC_STOP:
				stopVirtualMachine(solicitud, con);
				break;
			case VMC_COMMAND:
				executeCommandsOnVirtualMachine(solicitud, con);
				break;
			case VMC_TAKE_SNAPSHOT:
				takeSnapshotOnVirtualMachine(solicitud, con);
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