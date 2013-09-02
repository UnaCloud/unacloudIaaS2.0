/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fileTransfer;

import com.losandes.fileTransfer.Destination;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import physicalmachine.Network;
import com.losandes.utils.Log;

/**
 *
 * @author Clouder
 */
public class TransferenciaArchivo {

    private RandomAccessFile rafarchivo;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket s;
    private long cent=0;
    private long idTransferencia;
    private ArrayList<Destination> destinos;
    boolean conected =false;
    boolean dtCnt =false;
    final long tamano;
    File archivo;
    public TransferenciaArchivo(String[] ipDestinos,long idTransferencia,int nParticiones,File archivo,long tamano)throws IOException{
        this.archivo=archivo;
        Log.print("Abrir transfernecia "+idTransferencia+" "+archivo+" "+Arrays.toString(ipDestinos));
        this.idTransferencia=idTransferencia;
        destinos=crearDestinos(ipDestinos, nParticiones);
        archivo.getParentFile().mkdirs();
        archivo.delete();
        rafarchivo=new RandomAccessFile(archivo,"rw");
        this.tamano=tamano;
    }

    public void connect(Socket s,DataInputStream dis){
        Log.print("Abrir transfernecia "+idTransferencia);
        if(conected)return;
        try {
            this.s = s;
            this.dis = dis;
            Log.print("Escribiendo long");
            dos = new DataOutputStream(s.getOutputStream());
            dos.writeLong(cent);
            dos.flush();
            Log.print("Long escrito");
            conected=true;
            Log.print("conectando con destinos");
            if(!dtCnt){
                for(Destination d:destinos)d.connect();
                dtCnt=true;
            }
            Log.print("destinos conectados");
            recibirArchivo();
            conected=false;
        } catch (IOException ex) {
            Log.print(ex.getMessage());
            conected=false;
        }
    }

    public void close(){
        if(conected){
            try {s.close();} catch (IOException ex) {}
            for(Destination d:destinos)d.close();
        }
        try {
            rafarchivo.close();
        } catch (IOException ex) {
        }
    }
    volatile boolean enviando=false;
    public void recibirArchivo(){
        try {
            byte[] buffer = new byte[1024*5];
            for (int e = 0; (e=dis.read(buffer,0,buffer.length)) != -1;buffer=new byte[1024*100]) {
                rafarchivo.write(buffer,0, e);
                for(Destination d:destinos)d.sendBytes(buffer, e);
                cent+=e;
                if(cent==tamano)break;
            }
            Log.print("ArchivoRecibido");
            rafarchivo.close();
            Log.print("ArchivoRecibido");
            Log.print("Esperando destinos");
            String h = "";
            for(Destination d:destinos){
                Log.print("Esperando "+d.getIpDestino());
                h+=d.waitCompletation()+":";
                d.close();
            }
            Log.print("Escrito");
            dos.writeUTF(h+Network.getHostname());
            Log.print("Flush");
            dos.flush();
            s.close();
        } catch (IOException ex) {
            Log.print("Error "+ex.getMessage());
            //cerrar hijos y padre
        }
    }

    public ArrayList<Destination> crearDestinos(String[] ips,int nParticiones){
        if(ips.length==0)return new ArrayList<Destination>();
        System.out.println("-------------- "+Arrays.toString(ips));
        String[][] grupos = new String[nParticiones][];
        int d = (ips.length-nParticiones)/(nParticiones);
        int r = (ips.length-nParticiones)%(nParticiones);
        System.out.println(ips.length+" "+d+" "+r);
        if(ips.length<nParticiones){
            for(int e=0;e<grupos.length;e++)grupos[e]=new String[0];
        }
        else{
            for(int e=0;e<r;e++)grupos[e]=new String[d+1];
            for(int e=r;e<grupos.length;e++)grupos[e]=new String[d];
            for(int e=0,j=nParticiones;e<grupos.length;e++)for(int i=0;i<grupos[e].length;i++){
                grupos[e][i]=ips[j];
                j++;
            }
        }
        ArrayList<Destination> dest = new ArrayList<Destination>(Math.min(nParticiones,ips.length));
        for(int e=0,i=Math.min(nParticiones,ips.length);e<i;e++)dest.add(new Destination(ips[e],grupos[e], idTransferencia));
        return dest;
    }

}
