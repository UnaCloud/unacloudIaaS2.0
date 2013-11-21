package communication.messages.vmo.configuration;

import communication.UnaCloudMessage;
import communication.UnaCloudAbstractMessage;

/**
 *
 * @author Clouder
 */
public abstract class ConfigurationAbstractMessage extends UnaCloudAbstractMessage{
    public static final String VMC_COMMAND="comandMachine";
    public static final String VMC_START="StartMachine";
    public static final String VMC_STOP="StopMachine";
    public static final String VMC_WRITE_FILE="EscribirArchivoMaquina";
    public static final String VMC_TRANSFER_FILE="RecibirArchivoUnicast";
    public static final String VMC_TAKE_SNAPSHOT="takeSnapshot";
    public static final String VMC_CHANGE_MAC="changeMac";
    String configurationOperation;
    public ConfigurationAbstractMessage(String configurationOperation){
        super(VIRTUAL_MACHINE_OPERATION,VIRTUAL_MACHINE_CONFIGURATION);
        this.configurationOperation=configurationOperation;
    }
    public ConfigurationAbstractMessage(String configurationOperation,UnaCloudMessage message){
        super(VIRTUAL_MACHINE_OPERATION,VIRTUAL_MACHINE_CONFIGURATION, message);
        this.configurationOperation=configurationOperation;
    }
}