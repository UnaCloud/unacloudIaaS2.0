package fileTransfer;

import com.losandes.dataChannel.DataServerSocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.losandes.utils.Log;

import communication.UnaCloudMessage;
import communication.security.utils.AbstractCommunicator;

/**
 * Class responsible for attending requests to retrieve the contens of a local folder and send it to UnaCloud server
 * @author Clouder
 */
public class UnicastSender {

    /**
     *
     * @param solicitud UnaCloud server request to retrieve a folder
     * @param conexion The connection to be used to stablish the file retrieval process
     * @throws Exception If there is an error reading the requested folder
     */
    public void attendFileRetrieveRequest(UnaCloudMessage solicitud, AbstractCommunicator conexion) throws Exception {
        Log.print("Entro");
        String ruta = solicitud.getString(2),longid=solicitud.getString(3);
        long id = Long.parseLong(longid);
        File archivosAEnviar[] = getFolderFiles(ruta);
        String[] respuesta = new String[2 + archivosAEnviar.length * 2];
        respuesta[0] = "" + archivosAEnviar.length;
        Log.print("Leyo sol");
        Log.print("Conecto canal datos "+archivosAEnviar.length+" "+ruta);
        for (int e = 0; e < archivosAEnviar.length; e++) {
            respuesta[e * 2 + 1] = archivosAEnviar[e].getName();
            respuesta[e * 2 + 2] = "" + archivosAEnviar[e].length();
        }
        Log.print("Respuesta enviada");
        conexion.writeUTF(respuesta);
        Socket s = DataServerSocket.accept(id);
        byte[] buffer = new byte[1024*100];
        OutputStream os=s.getOutputStream();
        Log.print("DOS abierto");
        for (int e = 0,l; e < archivosAEnviar.length; e++) {
            Log.print("Enviando:" + archivosAEnviar[e].getName());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(archivosAEnviar[e]);
                while ((l = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, l);
                }
            } catch (IOException ex) {
                Log.print(ex.getLocalizedMessage());
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Log.print(ex.getLocalizedMessage());
                }
            }
        }
        Log.print("DOS cerrado");
        try {
            s.close();
        } catch (IOException ex) {
            Log.print(ex.getLocalizedMessage());
        }
        conexion.close();
    }

    /**
     * Return a list of readable files to be send by the request attender
     * @param path The folder path that must be examined
     * @return An array containing the children files of the given folder path
     */
    private File[] getFolderFiles(String path) {
        ArrayList<File> archivos = new ArrayList<File>();
        File maquina = new File(path).getParentFile();
        for (File c : maquina.listFiles()) {
            Log.print(c.getName()+" "+c.canRead()+" "+c.isFile());
            if (c.isFile()) {
                archivos.add(c);
            }
        }
        Log.print(""+archivos.size());
        return archivos.toArray(new File[archivos.size()]);
    }
}
