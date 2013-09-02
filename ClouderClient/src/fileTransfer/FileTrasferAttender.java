/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fileTransfer;

import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.losandes.utils.Log;
import static com.losandes.utils.Constants.*;
/**
 * Class responsible for attend file operations requests over the physical machine file system
 * @author Clouder
 */
public class FileTrasferAttender {

    /**
     * Attends a file operation requests. Suported operation are multicast file distributions and file deletions,
     * @param message The request to be attended
     * @param abc The abstract communicator used to attend the request
     */
    public static void attendFileOperation(String[] message,AbstractCommunicator abc){
        Log.print(Arrays.toString(message));
        //message[0] = PHYSICAL MACHINE OPERATION
        //message[1] = PM_WRITE_FILE
        //message[2] = TIPO TRANSFERENCIA
        //message[3] = ID_TRANSFERENCIA
        //message[4] = RUTA
        //message[5] = TAMAÑO
        //message[6] = NUMERO PARTICIONES
        //message[7] = limpiar directorio
        //message[8...] = IP Destinos
        int tipoTransferencia = Integer.parseInt(message[2]);
        if(tipoTransferencia==PM_WRITE_FILE_TREE_DISB){
            long idTransferencia = Long.parseLong(message[3]);
            String ruta = message[4];
            int nParticiones = Integer.parseInt(message[6]);
            long tamaño = Long.parseLong(message[5]);
            
            try{
                boolean limpiarDirectorio=Boolean.parseBoolean(message[7]);
                if(limpiarDirectorio){
                    for(File f:new File(ruta).getParentFile().listFiles()){
                        if(f.getName().endsWith(".vmx")||f.getName().endsWith(".vmdk")||f.getName().endsWith(".vmxf")||f.getName().endsWith(".vmsn")||f.getName().endsWith(".vmsd")||f.getName().endsWith(".nvram"))f.delete();
                    }
                }
            }catch(Exception ex){
            }

            String[] destinos = Arrays.copyOfRange(message,8,message.length);
            try {
                TransferenciaArchivo ta = new TransferenciaArchivo(destinos, idTransferencia, nParticiones, new File(ruta),tamaño);
                System.out.println("Put transfer "+idTransferencia+" "+ta);
                TreeDistributionChannelManager.transfers.put(idTransferencia, ta);
                abc.writeUTF(OK_MESSAGE);
            } catch (IOException ex) {
                try {
                    abc.writeUTF(ERROR_MESSAGE);
                } catch (ConnectionException ex1) {
                    Logger.getLogger(FileTrasferAttender.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch( ConnectionException ex){
            }
        }
        else if(tipoTransferencia==PM_DELETE_FILE){
            String ruta = message[3];
            File c = new File(ruta);
            if(ruta.endsWith("lck")){
                deleteFolder(c.getParentFile());
            }else{
                if(c.isFile()&&c.delete())Log.print("borrado "+ruta);
            }
        }
    }

    /**
     * Safely deletes a given folder, just lck files are erased
     * @param c The folder to be
     */
    private static void deleteFolder(File c){
        for(File f:c.listFiles()){
            if(f.isFile()&&f.getName().toLowerCase().endsWith("lck"))f.delete();
            else if(f.isDirectory()&&f.getName().toLowerCase().endsWith("lck")){
                deleteFolder(f);
                f.delete();
            }
        }
    }


}
