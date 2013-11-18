/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.transfer;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.security.utils.*;
import com.losandes.communication.security.*;
import java.io.*;
import java.util.*;
import javax.ejb.Stateless;
import static com.losandes.utils.Constants.*;
import com.losandes.fileTransfer.*;
import com.losandes.persistence.IPersistenceServices;
import com.losandes.virtualmachine.PairMachineExecution;
import com.losandes.virtualmachineexecution.IVirtualMachineExecutionServices;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Responsible for sending files to virtual machines using a tree based distribution protocol
 * @author Clouder
 */
@Stateless
public class FileSender implements FileSenderLocal {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private FileSenderLocal fileSender;
    @EJB
    private IVirtualMachineExecutionServices virtualMachineExecutionServices;
    @EJB
    private IPersistenceServices persistenceServices;
    /**
     * The id of the next file transfer operation
     */
    long id = 43;

    /**
     * The grade of the tree
     */
    final int N = 3;

    /**
     * Sends a folder to a list of nodes. It just read each file folder and sends it usign the sendFile methos if this class
     * @param receivers
     * @param folder
     * @param remotePath
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public void sendFolder(PairMachineExecution[] receivers, File folder, String remotePath) {
        File fs[] = folder.listFiles();
        int e = 1;
        if(fs!=null)for (File f : fs) {
            if (f.isFile() && f.length() > 0) {
                id++;
                sendFile(receivers, f, remotePath + (remotePath.endsWith("/") || remotePath.endsWith("\\") ? "" : "/") + f.getName(), e++, fs.length);
            }
        }
    }

    /**
     * Sends a file to a list of nodes.
     * @param receivers
     * @param archivo
     * @param rutaDestino
     * @param fileNumber
     * @param totalFiles
     */
    private void sendFile(PairMachineExecution[] receivers, File archivo, String rutaDestino, final int fileNumber, final int totalFiles) {
        String[] ips = new String[receivers.length];
        for (int e = 0; e < receivers.length; e++) {
            ips[e] = receivers[e].getVirtualMachine().getPhysicalMachine().getPhysicalMachineIP();
        }
        TreeMap<String, AbstractCommunicator> cons = new TreeMap<String, AbstractCommunicator>();
        {
            ArrayList<String> tempIps = new ArrayList<String>();
            ArrayList<PairMachineExecution> tempReceivers = new ArrayList<PairMachineExecution>();
            for (int e = 0; e < ips.length; e++) {
                try {
                    SecureSocket socket = new SecureSocket(ips[e], persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
                    cons.put(ips[e], socket.connect());
                    tempIps.add(ips[e]);
                    tempReceivers.add(receivers[e]);
                } catch (ConnectionException ex) {
                    fileSender.setMachineError(receivers[e],"Can't connect to physical machine");
                }
            }
            ips = tempIps.toArray(new String[0]);
            receivers = tempReceivers.toArray(new PairMachineExecution[0]);
        }
        fileSender.refreshExecutions(receivers, "File " + fileNumber + "/" + totalFiles + ": setting connections", 0);
        com.losandes.fileTransfer.Destination[] destinos = new com.losandes.fileTransfer.Destination[ips.length];
        {
            int e = 0;
            ArrayList<com.losandes.fileTransfer.Destination> dTemp = buildReceivers(ips, N);
            while (!dTemp.isEmpty()) {
                ArrayList<com.losandes.fileTransfer.Destination> p = new ArrayList<com.losandes.fileTransfer.Destination>();
                for (int i = 0; i < dTemp.size(); i++) {
                    destinos[e] = dTemp.get(i);
                    if (destinos[e].getHijos().length != 0) {
                        p.addAll(buildReceivers(destinos[e].getHijos(), N));
                    }
                    e++;
                }
                dTemp = p;
            }
        }
        fileSender.refreshExecutions(receivers, "File " + fileNumber + "/" + totalFiles + ": building tree", 0);
        for (int i = 0; i < destinos.length; i++) {
            AbstractCommunicator c = cons.get(destinos[i].getIpDestino());
            String[] msg = new String[8 + destinos[i].getHijos().length];
            msg[0] = "" + UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION;
            msg[1] = "" + PM_WRITE_FILE;
            msg[2] = "" + PM_WRITE_FILE_TREE_DISB;
            msg[3] = "" + id;
            msg[4] = rutaDestino;
            msg[5] = "" + archivo.length();
            msg[6] = "" + N;
            msg[7] = "" + (fileNumber==1);
            for (int j = 0; j < destinos[i].getHijos().length; j++) {
                msg[8 + j] = destinos[i].getHijos()[j];
            }
            try {
                c.writeUTF(msg);
                c.readUTF();
            } catch (ConnectionException ex) {
                ex.printStackTrace();
            }
            c.close();
        }
        com.losandes.fileTransfer.Destination[] disNivel = Arrays.copyOfRange(destinos, 0, Math.min(destinos.length, N));
        for (com.losandes.fileTransfer.Destination d : disNivel) {
            d.connect();
        }
        try {
            RandomAccessFile fr = new RandomAccessFile(archivo, "r");
            long length = fr.length(), sent = 0;
            byte[] buffe = new byte[1024 * 1024];
            for (int i; (i = fr.read(buffe)) != -1; buffe = new byte[1024 * 1024]) {
                sent += i;
                for (com.losandes.fileTransfer.Destination d : disNivel) {
                    d.sendBytes(buffe, i);
                }
                fileSender.refreshExecutions(receivers, "File " + fileNumber + "/" + totalFiles, (int) ((100l * sent) / length));

            }
            fr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        fileSender.refreshExecutions(receivers, "File " + fileNumber + "/" + totalFiles + ": closing connections", 100);
        for (com.losandes.fileTransfer.Destination d : disNivel) {
            System.out.println(d.waitCompletation());
        }
        for (com.losandes.fileTransfer.Destination d : disNivel) {
            d.close();
        }
    }

    /**
     * Constructs a tree to be used by a file transfer. This method returns a list of Destinations, that contains the information of each node and its child nodes
     * @param ips
     * @param nParticiones
     * @return
     */
    public ArrayList<com.losandes.fileTransfer.Destination> buildReceivers(String[] ips, int nParticiones) {
        if (ips.length == 0) {
            return new ArrayList<Destination>();
        }
        String[][] grupos = new String[nParticiones][];
        int d = (ips.length - nParticiones) / (nParticiones);
        int r = (ips.length - nParticiones) % (nParticiones);
        if (ips.length < nParticiones) {
            for (int e = 0; e < grupos.length; e++) {
                grupos[e] = new String[0];
            }
        } else {
            for (int e = 0; e < r; e++) {
                grupos[e] = new String[d + 1];
            }
            for (int e = r; e < grupos.length; e++) {
                grupos[e] = new String[d];
            }
            for (int e = 0, j = nParticiones; e < grupos.length; e++) {
                for (int i = 0; i < grupos[e].length; i++) {
                    grupos[e][i] = ips[j];
                    j++;
                }
            }
        }
        ArrayList<com.losandes.fileTransfer.Destination> dest = new ArrayList<Destination>(Math.min(nParticiones, ips.length));
        for (int e = 0, i = Math.min(nParticiones, ips.length); e < i; e++) {
            dest.add(new Destination(ips[e], grupos[e], id));
        }
        return dest;
    }
    private int lastCurrent = -1;

    /**
     * Refresh a list of virtual machine execution and updates it with the given message and progress
     * @param receivers
     * @param message
     * @param current
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public void refreshExecutions(PairMachineExecution[] receivers, String message, int current) {
        if (lastCurrent != current) {
            System.out.println("refreshExecutions " + current);
            for (PairMachineExecution pme : receivers) {
                pme.getExecution().setIsPercentage(true);
                pme.getExecution().setVirtualMachineExecutionStatus(COPYING_STATE);
                pme.getExecution().setVirtualMachineExecutionStatusMessage(message);
                pme.getExecution().setShowProgressBar(true);
                pme.getExecution().setMax(100);
                pme.getExecution().setCurrent(current);
                virtualMachineExecutionServices.updateVirtualMachineExecution(pme.getExecution());
            }
            lastCurrent = current;
        }
    }

    /**
     * Reports an error and stores it on the database
     * @param receiver
     * @param message
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void setMachineError(PairMachineExecution receiver, String message) {
        receiver.getExecution().setVirtualMachineExecutionStatus(ERROR_STATE);
        receiver.getExecution().setVirtualMachineExecutionStatusMessage(message);
        em.merge(receiver.getExecution());
        receiver.getVirtualMachine().setVirtualMachineState(OFF_STATE);
        em.merge(receiver.getVirtualMachine());
    }
}
