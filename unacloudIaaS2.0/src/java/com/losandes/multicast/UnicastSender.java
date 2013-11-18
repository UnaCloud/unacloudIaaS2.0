package com.losandes.multicast;

import java.io.OutputStream;
import java.net.Socket;

/**
 * Class for point to point file transfers
 * @author ga.sotelo69
 */
public class UnicastSender {

    /**
     * Sends a file to the specified host on with the given port
     * @param host
     * @param archivo
     * @param fileTransferSocketPort
     * @throws Exception
     */
    public static void sendFile(String host,byte[] content,int fileTransferSocketPort)throws Exception{
        Socket c = new Socket(host,fileTransferSocketPort+1);
        OutputStream os = c.getOutputStream();
        byte[] buffer = new byte[1024*1024];
        os.write(content,0,content.length);
        os.flush();
        c.close();
    }
}