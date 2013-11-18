/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.transfer;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.security.utils.*;
import com.losandes.communication.security.SecureSocket;
import com.losandes.fileTransfer.Destination;
import com.losandes.virtualmachine.PairMachineExecution;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.*;
import static com.losandes.utils.Constants.*;

/**
 *
 * @author Clouder
 */
public class ManualFileSender {

    static long id = 43;
    final int N = 1;

    public void sendFolder(String[] receivers, File folder, String remotePath) {
        File fs[] = folder.listFiles();
        int e = 1;
        for (File f : fs) {
            if (f.isFile() && f.length() > 0) {
                id++;
                sendFile(receivers, f, remotePath + (remotePath.endsWith("/") || remotePath.endsWith("\\") ? "" : "/") + f.getName());
            }
        }
    }

    private void sendFile(String[] receivers, File archivo, String rutaDestino) {
        System.out.println("Enviar archivo");
        String[] ips = new String[receivers.length];
        for (int e = 0; e < receivers.length; e++) {
            ips[e] = receivers[e];
        }
        System.out.println(Arrays.toString(ips));
        TreeMap<String, AbstractCommunicator> cons = new TreeMap<String, AbstractCommunicator>();
        {
            ArrayList<String> tempIps = new ArrayList<String>();
            ArrayList<String> tempReceivers = new ArrayList<String>();
            for (int e = 0; e < ips.length; e++) {
                try {
                    SecureSocket socket = new SecureSocket(ips[e], 81);
                    cons.put(ips[e], socket.connect());
                    tempIps.add(ips[e]);
                    tempReceivers.add(receivers[e]);
                } catch (ConnectionException ex) {
                    ex.printStackTrace();
                }
            }
            ips = tempIps.toArray(new String[0]);
            receivers = tempReceivers.toArray(new String[0]);
        }
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
            System.out.println(Arrays.toString(destinos));
            for (com.losandes.fileTransfer.Destination d : destinos) {
                System.out.println(d.getIpDestino() + " " + Arrays.toString(d.getHijos()));
            }
        }
        System.out.println("Hijos conectados");
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
            msg[7] = "" + destinos[i].getHijos().length;
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
        System.out.println("mensaje enviado");
        com.losandes.fileTransfer.Destination[] disNivel = Arrays.copyOfRange(destinos, 0, Math.min(destinos.length, N));
        System.out.println("enviando archivo a " + Arrays.toString(disNivel));
        for (com.losandes.fileTransfer.Destination d : disNivel) {
            d.connect();
        }
        System.out.println("conectado a destinos");
        try {
            RandomAccessFile fr = new RandomAccessFile(archivo, "r");
            long length = fr.length(), sent = 0;
            byte[] buffe = new byte[1024 * 1024];
            for (int i; (i = fr.read(buffe)) != -1; buffe = new byte[1024 * 1024]) {
                sent += i;
                for (com.losandes.fileTransfer.Destination d : disNivel) {
                    d.sendBytes(buffe, i);
                }
            }
            fr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (com.losandes.fileTransfer.Destination d : disNivel) {
            System.out.println(d.waitCompletation());
        }
        for (com.losandes.fileTransfer.Destination d : disNivel) {
            d.close();
        }
        System.out.println("Archivo enviao");
    }

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

    public static void main(String ...args){
        Destination.FILE_TRANSFER_SOCKET=1575;
        String[]ips=new String[]{"157.253.239.44","157.253.239.43"};
        ManualFileSender mfs=new ManualFileSender();
        ManualFileSender.id=new Random().nextInt();
        mfs.sendFolder(ips,new File("G:\\tigo"),"E:\\GRID\\goti\\");
    }

}
