/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.multicast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import static com.losandes.utils.Constants.*;

/**
 *
 * @author labredes
 */
@Deprecated
public class MulticastSender {

    /*private DatagramSocket socket;
    private int DEST_PORT;
    private String MCAST_ADDR;
    private byte[] b;

    private int totalPaquetes=1,paquetesEnviados=0;


    public void enviarArchivos(File[] archivos, String[] ips, String rutaMaquina) {
        b = new byte[DGRAM_LENGTH];
        MulticastChannel canal = new MulticastChannelManager().getAviableMulticastChannel();
        DEST_PORT=canal.puerto;
        MCAST_ADDR=canal.ip;
        try {
            enviarArchivoMulticast(ips, archivos, rutaMaquina);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void enviarArchivoMulticast(String[] ips, File[] archivos, String rutaMaquina) throws Exception {
        TCPConectionManager conexion = new TCPConectionManager(ips, 2569);
        conexion.connect();
        String[] linea = new String[5+(archivos.length*2)+1];
        linea[0]=""+PHYSICAL_MACHINE_OPERATION;
        linea[1]=""+PM_WRITE_FILE;
        linea[2]=""+PM_WRITE_FILE_MULTICAST;
        //TODO
        //linea[1]=""+MULTICAST_TRANSFER;
        linea[3]=rutaMaquina;
        linea[4]="" + archivos.length;
        linea[5]=MCAST_ADDR;
        linea[6]=""+DEST_PORT;
        for (int i = 0; i < archivos.length; i++) {
            linea[i*2+7]=archivos[i].getName();
            if(archivos[i].isFile()){
                linea[i*2+8]=""+archivos[i].length();
                totalPaquetes+=(int) (1 + (archivos[i].length() - 1) / (DGRAM_LENGTH - 18));
            }
            else linea[i*2+8]=""+0;
        }
        //Colocar path  linea[linea.length-1]="" + archivos.length;
        //TODO
        //conexion.println(linea);
        String[] respuestas = conexion.readLines();
        System.out.println("OK recibido");
        enviarArchivos(archivos);
        System.out.println("Reenviando perdidos...");
        reenviarPerdidos(archivos, conexion);
        enviarPaqueteFin();
        conexion.close();
    }

    private void enviarArchivos(File[] archivos) throws Exception {
        System.out.println("Inicio " + archivos.length);
        socket = new DatagramSocket();
        for (int a = 0; a < archivos.length; a++) {
            System.out.println("Inicio "+a+" " + archivos[a].length()+" "+archivos[a].getName());
            if (archivos[a].length() > 0) {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archivos[a]), 1024 * 1024);
                for (long numeroPaquete = 0, pos = 0; true; numeroPaquete++) {
                    int longitud = bis.read(b, 18, b.length - 18);
                    if (longitud == -1) {
                        break;
                    }
                    paquetesEnviados++;
                    colocarEncabezado(a, numeroPaquete, pos, longitud);
                    pos += longitud;
                }
                bis.close();
            }
        }
    }

    private void reenviarPerdidos(File[] archivos, TCPConectionManager conexion) throws FileNotFoundException, Exception {
        boolean[][] paquetesNoRecibidos = new boolean[archivos.length][];;
        boolean completo = true;
        do {
            completo=true;
            Thread.sleep(1000);
            //TODO
            conexion.println(MULTICAST_GET_LOSED);
            for(int e = 0; e < archivos.length; e++) {
                String[] resp = conexion.readLines(), t;
                if (archivos[e].length() != 0) {
                    paquetesNoRecibidos[e] = new boolean[(int) (1 + (archivos[e].length() - 1) / (DGRAM_LENGTH - 18))];
                    RandomAccessFile raf = new RandomAccessFile(archivos[e], "r");
                    for (int i = 0; i < resp.length; i++){
                        System.out.println(resp[i]);
                        if (resp[i] != null && (t = resp[i].split(MESSAGE_SEPARATOR_TOKEN)) != null) {
                            for(int r=1;r<t.length;r++){
                                completo = false;
                                paquetesNoRecibidos[e][Integer.parseInt(t[r])] = true;
                            }
                        }
                    }
                    for (int i = 0; i < paquetesNoRecibidos[e].length; i++)if(paquetesNoRecibidos[e][i]){
                        long pos = ((long) i) * (DGRAM_LENGTH - 18);
                        raf.seek(pos);
                        int longitud = raf.read(b, 18, b.length - 18);
                        colocarEncabezado(e, i, pos, longitud);
                    }
                    raf.close();
                }
            }
            System.out.println("No completo");
        } while (!completo);
        System.out.println("Completo!!!!");
    }

    private void colocarEncabezado(int nArchivo, long numeroPaquete, long pos, int longitud) throws UnknownHostException, InterruptedException, IOException {
        //TODO
        //b[0] = APPLICATION_IDENTIFIER;
        b[1] = (byte) nArchivo;
        colocarNumero(b, 2, (int) numeroPaquete);
        colocarNumeroLong(b, 6, pos);
        colocarNumero(b, 14, longitud);
        Thread.sleep(5);
        DatagramPacket dgram = new DatagramPacket(b, b.length, InetAddress.getByName(MCAST_ADDR), DEST_PORT);
        socket.send(dgram);
    }

    private void enviarPaqueteFin() throws Exception {
        b[0] = -1;
        DatagramPacket dgram = new DatagramPacket(b, b.length, InetAddress.getByName(MCAST_ADDR), DEST_PORT);
        socket.send(dgram);
    }

    private void colocarNumero(byte[] array, int pos, int number) {
        array[pos] = (byte) ((number >>> 8 * 3) & 0xFF);
        array[pos + 1] = (byte) ((number >> 8 * 2) & 0xFF);
        array[pos + 2] = (byte) ((number >> 8 * 1) & 0xFF);
        array[pos + 3] = (byte) (number & 0xFF);
    }

    private void colocarNumeroLong(byte[] array, int pos, long number) {
        array[pos] = (byte) ((number >>> 8 * 7) & 0xFF);
        array[pos + 1] = (byte) ((number >> 8 * 6) & 0xFF);
        array[pos + 2] = (byte) ((number >> 8 * 5) & 0xFF);
        array[pos + 3] = (byte) ((number >> 8 * 4) & 0xFF);
        array[pos + 4] = (byte) ((number >>> 8 * 3) & 0xFF);
        array[pos + 5] = (byte) ((number >> 8 * 2) & 0xFF);
        array[pos + 6] = (byte) ((number >> 8 * 1) & 0xFF);
        array[pos + 7] = (byte) (number & 0xFF);
    }

    public int getPaquetesEnviados() {
        return paquetesEnviados;
    }

    public void setPaquetesEnviados(int paquetesEnviados) {
        this.paquetesEnviados = paquetesEnviados;
    }

    public int getTotalPaquetes() {
        return totalPaquetes;
    }

    public void setTotalPaquetes(int totalPaquetes) {
        this.totalPaquetes = totalPaquetes;
    }
    */
    
}

