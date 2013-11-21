package domain;

import com.losandes.dataChannel.DataServerSocket;

import monitoring.PhysicalMachineStateReporter;
import communication.ClouderClientAttention;
import communication.messages.UnaCloudAbstractMessage;
import communication.security.utils.ConnectionException;
import execution.PersistentExecutionManager;
import fileTransfer.TreeDistributionChannelManager;

import java.util.Arrays;
import java.util.Date;

import monitoring.PhysicalMachineMonitor;
import physicalmachine.Network;
import physicalmachine.OperatingSystem;
import physicalmachine.PhysicalMachineState;

import com.losandes.utils.Log;
import com.losandes.utils.VariableManager;

import static com.losandes.utils.Constants.*;
import execution.LocalProcessExecutor;
import physicalmachine.PhysicalMachine;
import virtualmachine.VMwareWorkstation;

/**
 * @author Eduardo Rosales
 * Responsible for starting the Clouder Client
 *
 */
public class Main {

    /**
     * Responsible for sorting and starting the Clouder Client
     * @param args[0] = {0 = TURN_OFF_DB, 1 = TURN_ON_DB , 2 = LOGIN_DB, 3 = LOGOUT_DB}
     */
    public static void main(String[] args){
        
        VariableManager.init("./vars");
        Log.print("Inicio "+Arrays.toString(args));
        LocalProcessExecutor.executeCommandOutput("whoami");
        int mainCase = 1;
        if (args != null && args.length>0 && !args[0].equals("")) {
            mainCase = Integer.parseInt(args[0]);
        }
        PhysicalMachineState state = new PhysicalMachineState();
        switch (mainCase) {
            case TURN_OFF_DB:
                String turnOffMessage = UnaCloudAbstractMessage.DATABASE_OPERATION + MESSAGE_SEPARATOR_TOKEN + TURN_OFF_DB + MESSAGE_SEPARATOR_TOKEN + Network.getHostname();
                state.reportPhysicalMachine(turnOffMessage,false);
                break;
            case TURN_ON_DB:
                VMwareWorkstation.startUpServices();
                System.out.println("Clouder Client Started at2: " + new Date());
                String turnOnMessage = UnaCloudAbstractMessage.DATABASE_OPERATION + MESSAGE_SEPARATOR_TOKEN + TURN_ON + MESSAGE_SEPARATOR_TOKEN + Network.getHostname();
                System.out.println("Clouder Client Started at3: " + new Date());
                PhysicalMachineMonitor.restart();
                state.reportPhysicalMachine(turnOnMessage,true);
                DataServerSocket.init();
                new PhysicalMachineStateReporter(Network.getHostname(),state.getREPORT_DELAY()).start();
                new Thread(){
                @Override
                public void run() {
                    super.run();
                        PersistentExecutionManager.loadExecutions();
                    }
                }.start();
                new TreeDistributionChannelManager();
                System.out.println("paso reporter4");
                new ClouderClientAttention();
                break;
            case LOGIN_DB:
                String loginMessage = UnaCloudAbstractMessage.DATABASE_OPERATION + MESSAGE_SEPARATOR_TOKEN + LOGIN_DB + MESSAGE_SEPARATOR_TOKEN + Network.getHostname() + MESSAGE_SEPARATOR_TOKEN + OperatingSystem.getUserName();
                state.reportPhysicalMachine(loginMessage,false);
                break;
            case LOGOUT_DB:
                String logoutMessage = UnaCloudAbstractMessage.DATABASE_OPERATION + MESSAGE_SEPARATOR_TOKEN + LOGOUT_DB + MESSAGE_SEPARATOR_TOKEN + Network.getHostname();
                state.reportPhysicalMachine(logoutMessage,false);
                break;
            default:
                System.out.println(FATAL_ERROR_MESSAGE + "Clouder Client execution parameters are invalid");
                System.exit(0);
        }
    }

    
}//end of Main

