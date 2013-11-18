/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.communication.messages.configuration;

import com.losandes.communication.messages.UnaCloudMessage;
import static com.losandes.communication.messages.configuration.ConfigurationAbstractMessage.VMC_TRANSFER_FILE;

/**
 *
 * @author Clouder
 */
public class RecieveFileMessage extends ConfigurationAbstractMessage{
    String filePath;

    public RecieveFileMessage(String rutaArchivo) {
        super(VMC_TRANSFER_FILE);
        this.filePath = rutaArchivo;
    }
    public RecieveFileMessage(UnaCloudMessage message){
        super(VMC_TRANSFER_FILE,message);
    }
    
    public String getFilePath() {
        return filePath;
    }
    @Override
    public void processMessage(UnaCloudMessage message){
        filePath = message.getString(2);
    }
}