/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.multicast;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.messages.UnaCloudMessage;
import com.losandes.communication.security.utils.*;
import com.losandes.communication.security.SecureSocket;
import com.losandes.dataChannel.DataServerSocket;
import com.losandes.dataChannel.DataSocketConnection;
import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.persistence.entity.VirtualMachineExecution;
import com.losandes.utils.Queries;
import com.losandes.virtualmachine.IVirtualMachineServices;
import com.losandes.virtualmachineexecution.IVirtualMachineExecutionServices;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static com.losandes.utils.Constants.*;

/**
 *
 * @author Clouder
 */
@Stateless
public class VirtualMachineRetriver implements VirtualMachineRetriverLocal {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private VirtualMachineRetriverLocal virtualMachineRetriever;
    @EJB
    private IVirtualMachineExecutionServices virtualMachineExeServices;
    @EJB
    private IVirtualMachineServices virtualMachineServices;
    @EJB
    private IPersistenceServices persistenceServices;
    private int lastCurrent;
    /**
     * Retrieves a virtual machine to store it as a new deplayable template
     * @param ip IP address of the host with the virtual machine
     * @param remotePath The path which contains the virtual machine on the remote host
     * @param localPath The path which will be used to store the retrieved virtual machine
     * @param vme The virtual machine execution that represents this virtual machine operation
     */
    @Asynchronous
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void retrieveVirtualMachine(String ip, String remotePath, String localPath, VirtualMachineExecution vme) {
        vme.setVirtualMachineExecutionStatus(COPYING_STATE);
        vme.setVirtualMachineExecutionStatusMessage("Storing VM");
        virtualMachineExeServices.updateVirtualMachineExecution(vme);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        File p = new File(localPath).getParentFile();
        localPath = p.getAbsolutePath();
        if (p.isDirectory()) {
            for (File f : p.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        }
        try {
            System.out.println(ip + ";" + remotePath + ";" + localPath);
            SecureSocket socket = new SecureSocket(ip, persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
            AbstractCommunicator conexion = socket.connect();
            String[] linea = new String[4];
            linea[0] = "" + UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION;
            linea[1] = "" + PM_RETRIEVE_FOLDER;
            linea[2] = remotePath;
            final long id = DataServerSocket.getNextId();
            linea[3] = "" + id;
            conexion.writeUTF(linea);
            UnaCloudMessage respuesta = conexion.readUTFList();
            System.out.println("Respuesta recibida");
            if (respuesta.getString(0).startsWith(ERROR_MESSAGE)) {
                System.out.println("Error conectandose");
                conexion.close();
                return;
            }
            int numeroArchivos = respuesta.getInteger(0);
            String[] nombres = new String[numeroArchivos];
            long[] tamaños = new long[numeroArchivos];
            long totalLength = 0, actualLenght = 0;
            for (int e = 0; e < numeroArchivos; e++) {
                nombres[e] = respuesta.getString(e * 2 + 1);
                totalLength += (tamaños[e] = respuesta.getLong(e * 2 + 2));
            }
            byte[] buffer = new byte[1024 * 100];
            try {
                System.out.println("Conectando socket");
                Socket conArc = DataSocketConnection.connect(ip, id);
                System.out.println("Conexion establecida");
                InputStream dis = conArc.getInputStream();
                new File(localPath).mkdirs();
                for (int e = 0; e < numeroArchivos; e++) {
                    FileOutputStream fos = new FileOutputStream(new File(localPath, nombres[e]));
                    int j = 0;
                    for (long i = 0; i < tamaños[e];) {
                        j = dis.read(buffer, 0, (int) (((i + buffer.length) < tamaños[e]) ? buffer.length : tamaños[e] - i));
                        if (j == -1) {
                            break;
                        }
                        fos.write(buffer, 0, j);
                        actualLenght += j;
                        virtualMachineRetriever.refreshExecutions(vme, "Storing VM", (int) ((100 * actualLenght) / totalLength));
                        i += j;
                    }
                    fos.close();
                }
                dis.close();
                vme.setVirtualMachineExecutionStatus(OFF_STATE);
                vme.getVirtualMachine().setVirtualMachineState(OFF_STATE);
                virtualMachineExeServices.updateVirtualMachineExecution(vme);
                virtualMachineServices.updateVirtualMachine(vme.getVirtualMachine());
                List<VirtualMachine> l = em.createNativeQuery(Queries.getTemplateVirtualMachines(vme.getTemplate().getTemplateCode()), VirtualMachine.class).getResultList();
                for (VirtualMachine vm : l) {
                    vm.setConfigured(false);
                    vm.setLocallyStored(false);
                    virtualMachineServices.updateVirtualMachine(vm);
                }
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("Archivos enviados");
            conexion.close();
        } catch (ConnectionException ex) {
            Logger.getLogger(UnicastSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    /**
     * Update a virtual machine execution with the given message and step
     * @param vme
     * @param message
     * @param current
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void refreshExecutions(VirtualMachineExecution vme, String message, int current) {
        if (lastCurrent != current) {
            vme.setIsPercentage(true);
            vme.setVirtualMachineExecutionStatus(COPYING_STATE);
            vme.setVirtualMachineExecutionStatusMessage(message);
            vme.setShowProgressBar(true);
            vme.setMax(100);
            vme.setCurrent(current);
            virtualMachineExeServices.updateVirtualMachineExecution(vme);
        }
        lastCurrent = current;
    }
}
