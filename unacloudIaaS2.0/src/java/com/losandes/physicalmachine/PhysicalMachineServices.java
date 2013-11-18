package com.losandes.physicalmachine;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.security.utils.*;
import com.losandes.communication.security.SecureServerStream;
import com.losandes.communication.security.SecureSocket;
import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.ExecutionSlot;
import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.persistence.entity.VirtualMachineExecution;
import com.losandes.utils.Queries;
import com.losandes.utils.VariableManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.kohsuke.rngom.digested.Main;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the Physical Machine persistence services
 */
@Stateless
public class PhysicalMachineServices implements IPhysicalMachineServices {

    @EJB
    private IPersistenceServices persistenceServices;

    @Override
    public String createOrUpdatePhysicalMachine(PhysicalMachine machine) {
        PhysicalMachine pm = (PhysicalMachine)persistenceServices.findById(PhysicalMachine.class,machine.getPhysicalMachineName());
        if(pm!=null){
            pm.setPhysicalMachineCores(machine.getPhysicalMachineCores());
            pm.setPhysicalMachineArchitecture(machine.getPhysicalMachineArchitecture());
            pm.setOperatingSystem(machine.getOperatingSystem());
            pm.setPhysicalMachineIP(machine.getPhysicalMachineIP());
            pm.setPhysicalMachineMAC(machine.getPhysicalMachineMAC());
            pm.setPhysicalMachineRAMMemory(machine.getPhysicalMachineRAMMemory());
            pm.setPhysicalMachineVirtualIP(machine.getPhysicalMachineVirtualIP());
            pm.setPhysicalMachineVirtualMAC(machine.getPhysicalMachineVirtualMAC());
            pm.setPhysicalMachineVirtualNetmask(machine.getPhysicalMachineVirtualNetmask());
            pm.setPhysicalMachineDisk(machine.getPhysicalMachineDisk());
            pm.setPhysicalMachineHypervisorPath(machine.getPhysicalMachineHypervisorPath());
            pm.setLaboratory(machine.getLaboratory());
            return "Physical machine modified";
        }
        else{
            machine.setMaxvirtualmachineson(1);
            machine.setPhysicalmachinevirtualmachineson(0);
            persistenceServices.create(machine);
            return "Physical machine created";
        }
        //return persistenceServices.createorUpdate(machine);
    }

    /**
     * Responsible for exposing the Physical Machine delete persistence services
     */
    @Override
    public boolean deletePhysicalMachine(String physicalMachineName) {
        PhysicalMachine machine = getPhysicalMachineByName(physicalMachineName);
        return persistenceServices.delete(machine);
    }

    /**
     * Responsible for exposing the Physical Machine query by name
     */
    @Override
    public PhysicalMachine getPhysicalMachineByName(String physicalMachineName) {
        return (PhysicalMachine) persistenceServices.findByName(PhysicalMachine.class, physicalMachineName);
    }

    /**
     * Responsible for exposing all Physical Machines available
     */
    @Override
    public List getPhysicalMachines() {
        return persistenceServices.findAll(PhysicalMachine.class);
    }

    /**
     * Responsible for turning off Physical Machines
     */
    @Override
    public String turnOffPhysicalMachine(ArrayList<PhysicalMachine> physicalMachines) {
        String result = "";
        for (PhysicalMachine phyMac : physicalMachines) {
            if (!phyMac.getLaboratory().getLaboratoryType().getLaboratoryTypeName().toLowerCase().equals("Virtual")) {
                if (sendMessageToPhysicalMachine(phyMac.getPhysicalMachineIP(), UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION + MESSAGE_SEPARATOR_TOKEN + PM_TURN_OFF + MESSAGE_SEPARATOR_TOKEN)) {
                    phyMac.setPhysicalMachineState(0);
                    phyMac.setPhysicalMachineUser(NOTHING_AVAILABLE);
                    persistenceServices.update(phyMac);
                }
            } else {
                System.err.println(ERROR_MESSAGE + phyMac.getPhysicalMachineName() + " is a default laboratory physical machine");
                result = ERROR_MESSAGE;
            }
        }
        if (result.equals(ERROR_MESSAGE)) {
            return UNSUCCESSFUL_OPERATION;
        }
        return SUCCESSFUL_OPERATION;
    }

    /**
     * Responsible for restarting Physical Machines
     */
    @Override
    public String restartPhysicalMachine(ArrayList<PhysicalMachine> physicalMachines) {
        String result = "";
        for (PhysicalMachine phyMac : physicalMachines) {
            if (!phyMac.getLaboratory().getLaboratoryType().getLaboratoryTypeName().toLowerCase().equals("default")) {
                if (sendMessageToPhysicalMachine(phyMac.getPhysicalMachineIP(), UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION + MESSAGE_SEPARATOR_TOKEN + PM_RESTART + MESSAGE_SEPARATOR_TOKEN)) {
                    phyMac.setPhysicalMachineState(0);
                    phyMac.setPhysicalMachineUser(NOTHING_AVAILABLE);
                    persistenceServices.update(phyMac);
                } else {
                    result = ERROR_MESSAGE;
                }
            } else {
                System.err.println(ERROR_MESSAGE + phyMac.getPhysicalMachineName() + " is a default laboratory physical machine");
                result = ERROR_MESSAGE;
            }
        }
        if (!result.contains(ERROR_MESSAGE)) {
            result = SUCCESSFUL_OPERATION;
        } else {
            result = UNSUCCESSFUL_OPERATION;
        }
        return result;
    }

    /**
     * Responsible for loging out Physical Machines
     */
    @Override
    public String logoutPhysicalMachine(ArrayList<PhysicalMachine> physicalMachines) {
        String result = "";
        for (PhysicalMachine phyMac : physicalMachines) {
            if (!phyMac.getLaboratory().getLaboratoryType().getLaboratoryTypeName().toLowerCase().equals("default")) {
                if (sendMessageToPhysicalMachine(phyMac.getPhysicalMachineIP(), UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION + MESSAGE_SEPARATOR_TOKEN + PM_LOGOUT + MESSAGE_SEPARATOR_TOKEN)) {
                    phyMac.setPhysicalMachineUser(NOTHING_AVAILABLE);
                    persistenceServices.update(phyMac);
                }
            } else {
                System.err.println(ERROR_MESSAGE + phyMac.getPhysicalMachineName() + " is a default laboratory physical machine");
                result = ERROR_MESSAGE;
            }
        }
        if (!result.contains(ERROR_MESSAGE)) {
            result = SUCCESSFUL_OPERATION;
        } else {
            result = UNSUCCESSFUL_OPERATION;
        }
        return result;
    }

    /**
     * Responsible for locking Physical Machines
     */
    @Override
    public String lockPhysicalMachine(ArrayList<PhysicalMachine> physicalMachines) {
        String result = "";
        boolean band = false;
        for (PhysicalMachine phyMac : physicalMachines) {
            phyMac.setPhysicalMachineState(2);
//            band = persistenceServices.update(phyMac);
            if (!band) {
                result = ERROR_MESSAGE;
            }
        }
        if (!result.contains(ERROR_MESSAGE)) {
            result = SUCCESSFUL_OPERATION;
        } else {
            result = UNSUCCESSFUL_OPERATION;
        }
        return result;
    }

    /**
     * Responsible for unlocking Physical Machines
     */
    @Override
    public String unlockPhysicalMachine(ArrayList<PhysicalMachine> physicalMachines) {
        String result = "";
        boolean band = false;
        for (PhysicalMachine phyMac : physicalMachines) {
            phyMac.setPhysicalMachineState(1);
            band = persistenceServices.update(phyMac) != null;
            if (!band) {
                result = ERROR_MESSAGE;
            }
        }
        if (!result.contains(ERROR_MESSAGE)) {
            result = SUCCESSFUL_OPERATION;
        } else {
            result = UNSUCCESSFUL_OPERATION;
        }
        return result;
    }

    /**
     * Responsible for exposing all the Physical Machine Cores available for execution
     */
    @Override
    public List getAvailablePhysicalMachineCores(int templateSelected) {
        return persistenceServices.executeNativeSQLList(Queries.getPhysicalCoresAvailable(templateSelected), null);
    }

    /**
     * Responsible for exposing all the Grid Physical Machine Cores available for execution
     */
    @Override
    public List getGridAvailablePhysicalMachineCores(int templateSelected, String systemUserName) {
        return persistenceServices.executeNativeSQLList(Queries.getGridPhysicalCoresAvailable(templateSelected, systemUserName), null);
    }

    /**
     * Responsible for exposing all the Physical Machine RAM Memories available for execution
     */
    @Override
    public List getAvailablePhysicalMachineRAM(int templateSelected) {
        return persistenceServices.executeNativeSQLList(Queries.getPhysicalRAMAvailable(templateSelected), null);
    }

    /**
     * Responsible for exposing all the Grid Physical Machine RAM Memories available for execution
     */
    @Override
    public List getGridAvailablePhysicalMachineRAM(int templateSelected, String systemUserName) {
        return persistenceServices.executeNativeSQLList(Queries.getGridPhysicalRAMAvailable(templateSelected, systemUserName), null);
    }

    @Override
    public void updateAllPhysicalMachineAgents() {
        List<PhysicalMachine> pms = persistenceServices.findAll(PhysicalMachine.class);
        for (PhysicalMachine pm : pms) {
            persistenceServices.update(pm);
        }
    }

    @Override
    public java.lang.String updatePhysicalMachineAgent(java.util.ArrayList<com.losandes.persistence.entity.PhysicalMachine> physicalMachines) {
        System.out.println("updatePhysicalMachineAgent "+physicalMachines.size());
        String result = null;
        for (PhysicalMachine phyMac : physicalMachines) {
            if (!sendMessageToPhysicalMachine(phyMac.getPhysicalMachineIP(), ""+UnaCloudAbstractMessage.UPDATE_OPERATION)) {
                if(result==null)result = UNSUCCESSFUL_OPERATION+" on";
                result+= " "+phyMac.getPhysicalMachineName();
            }
        }
        if (result==null)result = SUCCESSFUL_OPERATION;
        return result;
    }

    @Override
    public java.lang.String startMonitorPhysicalMachines(ArrayList<PhysicalMachine> physicalMachines) {
        String result = "";
        for (PhysicalMachine phyMac : physicalMachines) {
            if (!phyMac.getLaboratory().getLaboratoryType().getLaboratoryTypeName().toLowerCase().equals("default")) {
                if (sendMessageToPhysicalMachine(phyMac.getPhysicalMachineIP(), UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION + MESSAGE_SEPARATOR_TOKEN + PM_MONITOR + MESSAGE_SEPARATOR_TOKEN + "START" + MESSAGE_SEPARATOR_TOKEN + 60 + MESSAGE_SEPARATOR_TOKEN + 60)) {
                    //TODO colocar la maquina en monitoreo.
                }
            } else {
                System.err.println(ERROR_MESSAGE + phyMac.getPhysicalMachineName() + " is a default laboratory physical machine");
                result = ERROR_MESSAGE;
            }
        }
        if (!result.contains(ERROR_MESSAGE)) {
            result = SUCCESSFUL_OPERATION;
        } else {
            result = UNSUCCESSFUL_OPERATION;
        }
        return result;
    }

    @Override
    public java.lang.String stopMonitorPhysicalMachines(ArrayList<PhysicalMachine> physicalMachines) {
        String result = "";
        for (PhysicalMachine phyMac : physicalMachines) {
            if (!phyMac.getLaboratory().getLaboratoryType().getLaboratoryTypeName().toLowerCase().equals("default")) {
                if (sendMessageToPhysicalMachine(phyMac.getPhysicalMachineIP(), UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION + MESSAGE_SEPARATOR_TOKEN + PM_MONITOR + MESSAGE_SEPARATOR_TOKEN + "STOP")) {
                    //TODO colocar la maquina en NO monitoreo.
                }
            } else {
                System.err.println(ERROR_MESSAGE + phyMac.getPhysicalMachineName() + " is a default laboratory physical machine");
                result = ERROR_MESSAGE;
            }
        }
        if (!result.contains(ERROR_MESSAGE)) {
            result = SUCCESSFUL_OPERATION;
        } else {
            result = UNSUCCESSFUL_OPERATION;
        }
        return result;
    }

    private boolean sendMessageToPhysicalMachine(String ip, String msg) {
        System.out.println(ip +" "+msg);
        try {
            SecureSocket ss = new SecureSocket(ip,persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
            AbstractCommunicator communication = ss.connect();
            communication.writeUTF(msg);
            communication.close();
            return true;
        } catch (ConnectionException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String sendRecieveMessageFromPhysicalMachine(String ip, String msg) {
        try {
            SecureSocket ss = new SecureSocket(ip, persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
            AbstractCommunicator communication = ss.connect();
            communication.writeUTF(msg);
            String h = communication.readUTF();
            communication.close();
            return h;
        } catch (ConnectionException ex) {
            return null;
        }
    }

    @Override
    public List<ExecutionSlot> getExecutionSlots(String physicalMachineID) {
        List l = persistenceServices.executeNativeSQLList(Queries.getPhysicalMachineExecutions(physicalMachineID), VirtualMachineExecution.class);
        PhysicalMachine pm = getPhysicalMachineByName(physicalMachineID);
        List<ExecutionSlot> ret = new ArrayList<ExecutionSlot>();
        for (int e = 0; e < l.size(); e++) {
            VirtualMachineExecution vme = (VirtualMachineExecution) l.get(e);
            ret.add(new ExecutionSlot(vme.getVirtualMachineExecutionStatus(), vme.getVirtualMachine().getVirtualMachineName() + "\n" + vme.getSystemUser().getSystemUserName()));
        }
        while (ret.size() < pm.getMaxvirtualmachineson()) {
            ret.add(new ExecutionSlot(0, "free"));
        }
        return ret;
    }

    @Override
    public void turnOnPhysicalMachine(PhysicalMachine bridge, PhysicalMachine... toWake) {
        String msg = UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION + MESSAGE_SEPARATOR_TOKEN + PM_TURN_ON;
        for (PhysicalMachine pm : toWake) {
            msg += MESSAGE_SEPARATOR_TOKEN + pm.getPhysicalMachineMAC();
        }
        sendMessageToPhysicalMachine(bridge.getPhysicalMachineIP(), msg);
    }
    
    @Override
    public void sendMachineParameters(Map<String, String> map) {
        System.out.println("sendMachineParameters");
        for(Map.Entry<String,String> e:map.entrySet())System.out.println(e.getKey()+" "+e.getValue());
        persistenceServices.setProperties(map);
        VariableManager.init("./vars");
        VariableManager.mergeValues(map);
        try {
            Socket s = new Socket("127.0.0.1", 2563);
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            pw.println("UPDATE_VARIABLES");
            for (Map.Entry<String, String> e : map.entrySet()) {
                pw.println(e.getKey() + "=" + e.getValue());
            }
            pw.println(-1);
            pw.close();
            s.close();
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateAllPhysicalMachineAgents();
    }

    @Override
    public void setKeyPair(KeyPair keys) {
        try {
            Socket s = new Socket("127.0.0.1", 2563);
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            RSAPublicKey publicKey = (RSAPublicKey) keys.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keys.getPrivate();
            pw.println("UPDATE_KEYS");
            pw.println(publicKey.getModulus().toString());
            pw.println(publicKey.getPublicExponent().toString());
            pw.println(privateKey.getModulus().toString());
            pw.println(privateKey.getPrivateExponent().toString());
            String h = br.readLine();
            if(h.equalsIgnoreCase("true")){
                updateAllPhysicalMachineAgents();
                SecureClientStream.setKeys(publicKey.getModulus().toString(),publicKey.getPublicExponent().toString());
                SecureServerStream.setKeys(privateKey.getModulus().toString(),privateKey.getPrivateExponent().toString());
            }
            br.close();
            pw.close();
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String...args){
        new PhysicalMachineServices().turnOnPhysicalMachine("157.253.201.144","2c41388b1536","3cd92b76eb24","2c41388b1515","3cd92b76eb28","3cd92b76eb52","3cd92b76eb1d","3cd92b76eb55","2c41388b14be","3cd92b76eb00","2c41388b1527","3cd92b76eb2c","3cd92b76eb5b","2c41388b152e","3cd92b76eb25","3cd92b76eb30","3cd92b76eaf6","3cd92b76eb1a","3cd92b76eb58","3cd92b76eb20","3cd92b76eac4","000FFE946110","2c41388b1530","000FFE94613F","3cd92b76eb29","3cd92b76eb50","3cd92b76eb66","3cd92b76eadb","3cd92b76eb1c","3cd92b76eb37","3cd92b76eb22","2c41388b146b","3cd92b76eb38","3cd92b76ead7","3cd92b76eb21","3cd92b76eac5","3cd92b76eb56","2c41388b1520","2c41388b1509");
    }
    public void turnOnPhysicalMachine(String bridge, String... toWake) {
        String msg = UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION + MESSAGE_SEPARATOR_TOKEN + PM_TURN_ON;
        for (String pm : toWake) {
            msg += MESSAGE_SEPARATOR_TOKEN + pm;
        }
        sendMessageToPhysicalMachine(bridge, msg);
    }
}//end of PhysicalMachineServices

