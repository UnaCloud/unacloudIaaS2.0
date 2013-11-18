package com.losandes.virtualmachine;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.security.utils.*;
import com.losandes.communication.security.SecureSocket;
import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.persistence.entity.VirtualMachineExecution;
import com.losandes.user.IUserServices;
import com.losandes.utils.Queries;
import com.losandes.virtualmachineexecution.IVirtualMachineExecutionServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;

import unacloud2.Deployment;
import static com.losandes.utils.Constants.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the Virtual Machine persistence services
 */
public class VirtualMachineServices{

    private IPersistenceServices persistenceServices;
    private IUserServices userServices;
    private VirtualMachineOperationsLocal virtualMachineOperations;
    private IVirtualMachineExecutionServices ivmes;
    
    public String turnOffVirtualMachineCluster(List<String> virtualMachineExecution) {
        String result = "";
        for (String vmeId : virtualMachineExecution){
            VirtualMachineExecution vme=(VirtualMachineExecution)persistenceServices.findById(VirtualMachineExecution.class,vmeId);
            if(vme.getVirtualMachineExecutionStatus()==ON_STATE||vme.getVirtualMachineExecutionStatus()==ERROR_STATE){
                result = turnOffVirtualMachine(vme);
                vme.setVirtualMachineExecutionStatus(OFF_STATE);
                vme.setVirtualMachineExecutionStop(new Date());
                vme.getVirtualMachine().setVirtualMachineState(OFF_STATE);
                persistenceServices.update(vme);
                persistenceServices.update(vme.getVirtualMachine());
            }
        }
        return result;
    }

    public String restartVirtualMachineCluster(List<VirtualMachineExecution> virtualMachineExecution) {
        String result = "";
        for (VirtualMachineExecution vme : virtualMachineExecution)if(vme.getVirtualMachineExecutionStatus()==ON_STATE){
            result = restartVirtualMachine(vme.getVirtualMachine());
        }
        return result;
    }

    /**
     * Responsible for extending a Virtual Machines execution time
     */
    
    public String extendVirtualMachineExecutionTime(int executionTime, VirtualMachineExecution... virtualMachineExecutions) {
        System.out.println("extendVirtualMachineExecutionTime "+executionTime );
        String result = "";
        if (virtualMachineExecutions != null) {
            for (VirtualMachineExecution vme : virtualMachineExecutions) {
                System.out.println(vme.getVirtualMachine().getVirtualMachineName());
                try {
                    SecureSocket ss = new SecureSocket(vme.getVirtualMachine().getPhysicalMachine().getPhysicalMachineIP(), persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
                    AbstractCommunicator communication = ss.connect();
                    communication.writeUTF("" + UnaCloudAbstractMessage.VIRTUAL_MACHINE_OPERATION, VM_TIME + "", "" + vme.getVirtualMachine().getVirtualMachineCode(), "" + executionTime);
                    communication.close();
                    vme.setVirtualMachineExecutionTime(new Date(vme.getVirtualMachineExecutionTime().getTime() + (((long) executionTime) * 60l * 60000l)));
                    persistenceServices.update(vme);
                } catch (Exception e) {
                    e.printStackTrace();
                }   
            }
        } else {
            result = ERROR_MESSAGE;
        }
        if (!result.contains(ERROR_MESSAGE)) {
            result = SUCCESSFUL_OPERATION;
        } else {
            result = UNSUCCESSFUL_OPERATION;
        }

        return result;
    }

    /**
     * Responsible for turning off a Virtual Machine Execution
     */
    public void setTurnOffVirtualMachineExecution(String systemUserName, int virtualMachineCode) {
        List<VirtualMachineExecution> virtualMachineExecutions = getVirtualMachineExecutions(systemUserName);
        for (int i = 0; i < virtualMachineExecutions.size(); i++) {
            if (virtualMachineExecutions.get(i).getVirtualMachine().getVirtualMachineCode() == virtualMachineCode) {
                virtualMachineExecutions.get(i).setVirtualMachineExecutionStatus(OFF_STATE);
                virtualMachineExecutions.get(i).setVirtualMachineExecutionStop(new Date());
                virtualMachineExecutions.get(i).getVirtualMachine().setVirtualMachineState(OFF_STATE);
                virtualMachineExecutions.get(i).getVirtualMachine().getPhysicalMachine().setPhysicalMachineState(ON_STATE);
                persistenceServices.update(virtualMachineExecutions.get(i));
            }
        }
    }

    
    public void setVirtualMachineExecutionState(int executionId, int state, String message) {
        VirtualMachineExecution vme = (VirtualMachineExecution) persistenceServices.findById(VirtualMachineExecution.class, executionId);
        vme.setVirtualMachineExecutionStatus(state);
        vme.setVirtualMachineExecutionStatusMessage(message);
        persistenceServices.update(vme);
    }

    private String restartVirtualMachine(VirtualMachine virMac) {
        PhysicalMachine phyMac = virMac.getPhysicalMachine();
        try {
            SecureSocket socket = new SecureSocket(phyMac.getPhysicalMachineIP(), persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF(UnaCloudAbstractMessage.VIRTUAL_MACHINE_OPERATION + MESSAGE_SEPARATOR_TOKEN + VM_RESTART + MESSAGE_SEPARATOR_TOKEN + virMac.getVirtualMachineCode() + MESSAGE_SEPARATOR_TOKEN + virMac.getHypervisor().getHypervisorCode() + MESSAGE_SEPARATOR_TOKEN + virMac.getVirtualMachinePath() + MESSAGE_SEPARATOR_TOKEN + phyMac.getPhysicalMachineHypervisorPath());
            communication.close();
            return "";
        } catch (ConnectionException ex) {
            return ERROR_MESSAGE;
        }
    }

    private String turnOffVirtualMachine(VirtualMachineExecution vme) {
        VirtualMachine virMac=vme.getVirtualMachine();
        PhysicalMachine phyMac = virMac.getPhysicalMachine();
        System.out.println("TURN OFF " + phyMac.getPhysicalMachineName());
        try {
            SecureSocket socket = new SecureSocket(phyMac.getPhysicalMachineIP(), persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF("" + UnaCloudAbstractMessage.VIRTUAL_MACHINE_OPERATION, "" + VM_TURN_OFF, "" + vme.getVirtualMachineExecutionCode(), "" + virMac.getHypervisor().getHypervisorCode(), virMac.getVirtualMachinePath(), phyMac.getPhysicalMachineHypervisorPath());
            communication.close();
            return "";
        } catch (ConnectionException ex) {
        }
        return ERROR_MESSAGE;
    }

    public String turnOnVirtualCluster(Deployment deployment) {
    	return null;
    }
    public String turnOnVirtualCluster(List<PhysicalMachine> physicalMachines, int executionTime, int templateSelected, int vmCores, int HDsize, int vmRAM, String userName) {
    	System.out.println("turnOnVirtualCluster");
        for (PhysicalMachine pm : physicalMachines) {
            System.out.println("PM " + pm.getPhysicalMachineName());
        }
        List<VirtualMachine> vms = virtualMachineServices.getGridAvailableVirtualMachines(templateSelected, HDsize, vmCores, vmRAM, userName);
        List<VirtualMachine> toTurnOn = new ArrayList<VirtualMachine>();
        for (VirtualMachine vm : vms) {
            for (PhysicalMachine pm : physicalMachines) {
                if (pm.getPhysicalMachineName().equals(vm.getPhysicalMachine().getPhysicalMachineName())) {
                    toTurnOn.add(vm);
                }
            }
        }
        System.out.println("vms " + vms.size());
        for (VirtualMachine vm : vms) {
            System.out.println("VM " + vm.getVirtualMachineName() + " " + vm.getPhysicalMachine().getPhysicalMachineName());
        }
        System.out.println("toTurnOn " + toTurnOn.size());
        virtualMachineOperations.turnOnCluster(vmCores, vmRAM, executionTime, userServices.getUserByID(userName), toTurnOn.toArray(new VirtualMachine[0]));
        return "";
    }

    
    public void writeFileOnVirtualMachine(String virtualMachineExecutionId,String path,String content){
        virtualMachineOperations.writeFileOnVirtualMachine(virtualMachineExecutionId, path, content);
    }

    public VirtualMachineExecution[] turnOnVirtualClusterBySize(int template, int executionTime, int numberInstances, int vmCores, int HDsize, int vmRAM, String userName,boolean retry) {
        List<VirtualMachine> vms = getAvailableVirtualMachines(template, HDsize, vmCores, vmRAM);
        VirtualMachine[] avms = new VirtualMachine[Math.min(numberInstances, vms.size())];
        for (int e = 0; e < avms.length; e++)avms[e] = vms.get(e);
        if(retry&&numberInstances-avms.length>0)virtualMachineOperations.turnOnPhysicalMachines(template, executionTime, numberInstances-avms.length, vmCores, HDsize, vmRAM, userName);
        return virtualMachineOperations.turnOnCluster(vmCores, vmRAM, executionTime, userServices.getUserByID(userName), avms);
    }
}