package configuration;

import static com.losandes.utils.Constants.OK_MESSAGE;
import static communication.messages.vmo.ConfigurationAbstractMessage.VMC_CHANGE_MAC;
import static communication.messages.vmo.ConfigurationAbstractMessage.VMC_COMMAND;
import static communication.messages.vmo.ConfigurationAbstractMessage.VMC_START;
import static communication.messages.vmo.ConfigurationAbstractMessage.VMC_STOP;
import static communication.messages.vmo.ConfigurationAbstractMessage.VMC_TAKE_SNAPSHOT;
import static communication.messages.vmo.ConfigurationAbstractMessage.VMC_TRANSFER_FILE;
import static communication.messages.vmo.ConfigurationAbstractMessage.VMC_WRITE_FILE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import virtualmachine.Hypervisor;
import virtualmachine.HypervisorFactory;
import virtualmachine.HypervisorOperationException;

import com.losandes.utils.Log;
import com.losandes.utils.VariableManager;
import communication.messages.vmo.ConfigurationAbstractMessage;
import communication.messages.vmo.configuration.ChangeVirtualMachineMacMessage;
import communication.messages.vmo.configuration.ExecuteCommandMessage;
import communication.messages.vmo.configuration.RecieveFileMessage;
import communication.messages.vmo.configuration.StartVirtualMachineMessage;
import communication.messages.vmo.configuration.StopVirtualMachineMessage;
import communication.messages.vmo.configuration.TakeSnapshotMessage;
import communication.messages.vmo.configuration.WriteFileOnVirtualMachineMessage;
import communication.security.utils.ConnectionException;

/**
 * Class responsible for attending virtual machine configuration requests
 * 
 * @author Clouder
 */
public class VirtualMachineConfigurator {

	/**
	 * Temporal file to be used for writeFileOnVirtualMachine operations
	 */
	private static final File temporalFile = new File("temp/a.txt");

	/**
	 * Attends a request to write a file on a virtual machine
	 * @param solicitud The UnaCloud server request
	 * @param con The channel to interact with UnaCloud server
	 */
	private void writeFileOnVirtualMachine(WriteFileOnVirtualMachineMessage message, ObjectInputStream ois, PrintWriter pw) {
		try {
			temporalFile.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(temporalFile);
			recieveFileOverChannel(fos,ois,pw);
			try {
				HypervisorFactory.getHypervisor(Integer.parseInt(message.getHypervisor()), message.getRutaVMRUN(), message.getDestinationMachine()).copyFileOnVirtualMachine(message.getLogin(), message.getPass(), message.getDestinationFileRute(), temporalFile);
				pw.println("Archivo copiado exitosamente");
			} catch (HypervisorOperationException ex) {
				pw.println("Error copiando el archivo " + ex.getMessage());
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Recieves a File over the file transfer socket and writes it on the given
	 * File output stream
	 * @param fos The file output stream in which the file will be write
	 * @param con The channel to interact with UnaCloud server
	 * @throws Exception
	 */
	private void recieveFileOverChannel(FileOutputStream fos, ObjectInputStream ois, PrintWriter pw) throws Exception {
		ServerSocket ss = new ServerSocket(VariableManager.getIntValue("FILE_TRANSFER_SOCKET") + 1);
		pw.println(OK_MESSAGE);
		Socket c = ss.accept();
		ss.close();
		InputStream isr = (c.getInputStream());
		byte[] buffer = new byte[1024];
		for (int e = 0; (e = isr.read(buffer)) != -1;) {
			fos.write(buffer, 0, e);
		}
		c.close();
		fos.close();
	}

	/**
	 * Attends a request to write a file on this physical machine
	 * @param solicitud The UnaCloud server request
	 * @param con The channel to interact with UnaCloud server
	 */
	private void recieveUnicastFile(RecieveFileMessage solicitud, ObjectInputStream ois, PrintWriter pw) {
		try {
			new File(solicitud.getFilePath()).getParentFile().mkdirs();
			File c = new File(solicitud.getFilePath());
			pw.println("OK");
			FileOutputStream fos = new FileOutputStream(c);
			recieveFileOverChannel(fos, ois,pw);
			pw.println("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Attends a request to start a virtual machine
	 * @param solicitud The UnaCloud server request
	 * @throws ConnectionException
	 */
	private String startVirtualMachine(StartVirtualMachineMessage solicitud) {
		try {
			HypervisorFactory.getHypervisor(Integer.parseInt(solicitud.getHypervisor()), solicitud.getRutaVMRUN(), solicitud.getDestinationMachine()).startVirtualMachine();
			return ("Maquina encendida exitosamente");
		} catch (HypervisorOperationException ex) {
			Log.print2("Error Starting Machine:" + ex.getMessage());
			return (ex.getMessage());
		}
	}

	/**
	 * Attends a request to start a virtual machine
	 * 
	 * @param solicitud The UnaCloud server request
	 * @param con The channel to interact with UnaCloud server
	 * @throws ConnectionException
	 */
	private String changeVirtualMachineMac(ChangeVirtualMachineMacMessage solicitud) {
		try {
			HypervisorFactory.getHypervisor(Integer.parseInt(solicitud.getHypervisor()), solicitud.getRutaVMRUN(), solicitud.getDestinationMachine()).changeVirtualMachineMac();
			return "Maquina encendida exitosamente";
		} catch (HypervisorOperationException ex) {
			return ex.getMessage();
		}
	}

	/**
	 * Attends a request to take a snapshot on a virtual machine
	 * 
	 * @param solicitud The UnaCloud server request
	 * @param con The channel to interact with UnaCloud server
	 * @throws ConnectionException
	 */
	private String takeSnapshotOnVirtualMachine(TakeSnapshotMessage solicitud) {
		try {
			HypervisorFactory.getHypervisor(Integer.parseInt(solicitud.getHypervisor()), solicitud.getRutaVMRUN(), solicitud.getDestinationMachine()).takeSnapshotOnMachine(solicitud.getSnapshotname());
			return "Snapshot tomado";
		} catch (HypervisorOperationException ex) {
			return (ex.getMessage());
		}
	}

	/**
	 * Attends a request to execute commands on a virtual machine
	 * 
	 * @param solicitud The UnaCloud server request
	 * @param con The channel to interact with UnaCloud server
	 * @throws ConnectionException
	 */
	private String[] executeCommandsOnVirtualMachine(ExecuteCommandMessage solicitud) {
		String respuesta[] = new String[solicitud.getComandos().length];
		Hypervisor v = HypervisorFactory.getHypervisor(solicitud.getHypervisor(), solicitud.getRutaVMRUN(), solicitud.getDestinationMachine());
		for (int e = 0; e < solicitud.getComandos().length; e++) {
			try {
				v.executeCommandOnMachine(solicitud.getUser(), solicitud.getPass(), solicitud.getComandos()[e].getExecName(),solicitud.getComandos()[e].getVars());
				respuesta[e] = "Successful execution";
			} catch (HypervisorOperationException ex) {
				respuesta[e] = ex.getMessage();
			}
		}
		return (respuesta);
	}

	/**
	 * Attends a request to stop a virtual machine
	 * 
	 * @param solicitud The UnaCloud server request
	 * @param con The channel to interact with UnaCloud server
	 * @throws ConnectionException
	 */
	private String stopVirtualMachine(StopVirtualMachineMessage solicitud) {
		HypervisorFactory.getHypervisor(Integer.parseInt(solicitud.getHypervisor()), solicitud.getRutaVMRUN(), solicitud.getDestinationMachine()).stopVirtualMachine();
		return ("Maquina apagada exitosamente");
	}

	/**
	 * Attends a request to make a configuration operation over a virtual
	 * machine
	 * @param solicitud The UnaCloud server request
	 * @param con The channel to interact with UnaCloud server
	 */
	public void attendConfigurationRequest(ConfigurationAbstractMessage solicitud, ObjectInputStream ois, PrintWriter pw) {
		try {
			switch (solicitud.getConfigurationOperation()) {
			case VMC_TRANSFER_FILE:
				recieveUnicastFile((RecieveFileMessage) solicitud, ois, pw);
				break;
			case VMC_WRITE_FILE:
				writeFileOnVirtualMachine((WriteFileOnVirtualMachineMessage) solicitud, ois, pw);
				break;
			case VMC_START:
				pw.println(startVirtualMachine((StartVirtualMachineMessage) solicitud));
				break;
			case VMC_STOP:
				pw.println(stopVirtualMachine((StopVirtualMachineMessage) solicitud));
				break;
			case VMC_COMMAND:
				pw.println(executeCommandsOnVirtualMachine((ExecuteCommandMessage) solicitud));
				break;
			case VMC_CHANGE_MAC:
				pw.println(changeVirtualMachineMac((ChangeVirtualMachineMacMessage) solicitud));
				break;
			case VMC_TAKE_SNAPSHOT:
				pw.println(takeSnapshotOnVirtualMachine((TakeSnapshotMessage) solicitud));
				break;
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}
}