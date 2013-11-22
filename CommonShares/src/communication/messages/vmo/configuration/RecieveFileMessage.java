/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication.messages.vmo.configuration;

import communication.UnaCloudMessage;
import communication.messages.vmo.ConfigurationAbstractMessage;

/**
 *
 * @author Clouder
 */
public class RecieveFileMessage extends ConfigurationAbstractMessage{
    private static final long serialVersionUID = -8277788120446128061L;
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