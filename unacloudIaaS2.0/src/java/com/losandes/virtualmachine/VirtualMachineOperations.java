/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.virtualmachine;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.security.utils.*;
import com.losandes.communication.security.SecureSocket;
import com.losandes.fileTransfer.Destination;
import com.losandes.machineconfigurators.AbstractSOConfigurator;
import com.losandes.machineconfigurators.MachineConfigurator;
import com.losandes.multicast.VirtualMachineRetriverLocal;
import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.*;
import com.losandes.physicalmachine.IPhysicalMachineServices;
import com.losandes.transfer.FileSenderLocal;
import com.losandes.user.IUserServices;
import com.losandes.utils.Queries;
import com.losandes.virtualmachineexecution.IVirtualMachineExecutionServices;
import com.losandes.vo.HostTable;
import java.io.File;
import java.util.*;
import java.util.ArrayList;
import static com.losandes.utils.Constants.*;
import javax.ejb.*;

/**
 *
 * @author Clouder
 */
@Stateless
public class VirtualMachineOperations implements VirtualMachineOperationsLocal {

    @EJB
    private IPersistenceServices persistenceServices;
    @EJB
    private IUserServices userServices;
    @EJB
    private VirtualMachineOperationsLocal machineOperations;
    @EJB
    private IVirtualMachineServices virtualMachineServices;
    @EJB
    private VirtualMachineRetriverLocal virtualMachineRetriever;
    @EJB
    private FileSenderLocal sender;
    @EJB
    private IVirtualMachineExecutionServices virtualMachinesExecutionServices;
    @EJB
    private IPhysicalMachineServices ipms;

    /**
     * Generates a collection of virtual machine executions and asign them to the given user. The returned objects are stored on data base and must be used to update the virtual machine execution status.
     * @param vmCores
     * @param vmRAM
     * @param executionTime
     * @param userName
     * @param cluster
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public VirtualMachineExecution[] generateVirtualExecutions(int vmCores, int vmRAM, int executionTime, SystemUser userName, VirtualMachine... cluster) {
        VirtualMachineExecution[] vmes = new VirtualMachineExecution[cluster.length];
        for (int e = 0; e < cluster.length; e++) {
            VirtualMachine virMac = cluster[e];
            VirtualMachineExecution virtualMachineExecution = new VirtualMachineExecution();
            virtualMachineExecution.setVirtualMachineExecutionCode("" + System.currentTimeMillis() + Math.random()+""+e);
            virtualMachineExecution.setSystemUser(userName);
            virtualMachineExecution.setTemplate(virMac.getTemplate());
            virtualMachineExecution.setVirtualMachineExecutionCores(virMac.getVirtualMachineCores());
            virtualMachineExecution.setVirtualMachineExecutionHardDisk(virMac.getVirtualMachineHardDisk());
            virtualMachineExecution.setVirtualMachineExecutionIP(virMac.getVirtualMachineIP());
            virtualMachineExecution.setVirtualMachineExecutionRAMMemory(virMac.getVirtualMachineRAMMemory());
            virtualMachineExecution.setVirtualMachine(virMac);
            virtualMachineExecution.setVirtualMachineExecutionStart(new Date());
            virtualMachineExecution.setVirtualMachineExecutionStatus(DEPLOYING_STATE);
            if(!cluster[e].isConfigured()||!cluster[e].isLocallyStored())virtualMachineExecution.setVirtualMachineExecutionStatus(COPYING_STATE);
            virtualMachineExecution.setVirtualMachineExecutionStatusMessage("Starting up...");
            virtualMachineExecution.setVirtualMachineExecutionTime(new Date(System.currentTimeMillis() + executionTime * 60l * 60l * 1000l));
            if (vmCores != 0 && vmRAM != 0) {
                virMac.setVirtualMachineCores(vmCores);
                virMac.setVirtualMachineRAMMemory(vmRAM);
            }
            virMac.setVirtualMachineState(ON_STATE);
            persistenceServices.create(virtualMachineExecution);
            vmes[e] = (VirtualMachineExecution)persistenceServices.update(virtualMachineExecution);
            persistenceServices.update(virMac);
            try{
                //persistenceServices.executeSQLString(Queries.createEvent(executionTime,userName.getSystemUserName()));
            }catch(Throwable t){
                System.err.println(t.getMessage());
            }
            
        }
        return vmes;
    }

    /**
     * Turn on a cluster of virtual machines. If the machines aren´t copied and configured, this method will copy and configure them.
     * @param vmCores
     * @param vmRAM
     * @param executionTime
     * @param userName
     * @param cluster
     * @return
     */
    public VirtualMachineExecution[] turnOnCluster(int vmCores, int vmRAM, int executionTime, SystemUser userName, VirtualMachine... cluster) {
        VirtualMachineExecution[] vmes = machineOperations.generateVirtualExecutions(vmCores, vmRAM, executionTime, userName, cluster);
        List<PairMachineExecution> notConfigured = new ArrayList<PairMachineExecution>(), notCopied = new ArrayList<PairMachineExecution>(), ready = new ArrayList<PairMachineExecution>();
        for (int e = 0; e < cluster.length; e++) {
            if(vmCores!=0)cluster[e].setVirtualMachineCores(vmCores);
            if(vmRAM!=0)cluster[e].setVirtualMachineRAMMemory(vmRAM);
            if (!cluster[e].isLocallyStored()) {
                notCopied.add(new PairMachineExecution(cluster[e],vmes[e]));
            }else if (!cluster[e].isConfigured()) {
                notConfigured.add(new PairMachineExecution(cluster[e],vmes[e]));
            } else {
                ready.add(new PairMachineExecution(cluster[e],vmes[e]));
            }

        }
        System.out.println("Estado "+ready.size()+" "+notConfigured.size()+" "+notCopied.size());
        machineOperations.turnOnVirtualMachines(executionTime,vmCores,vmRAM, ready.toArray(new PairMachineExecution[ready.size()]));
        machineOperations.configureVirtualMachines(false,executionTime, notConfigured.toArray(new PairMachineExecution[notConfigured.size()]));
        machineOperations.sendFiles(false,executionTime, notCopied.toArray(new PairMachineExecution[notCopied.size()]));
        return vmes;
    }

    /**
     * starts a virtual machine copy for a given cluster
     * @param poweroff
     * @param executionTime
     * @param cluster
     */
    @Asynchronous
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendFiles(boolean poweroff,int executionTime,PairMachineExecution... cluster) {
        if (cluster.length == 0) {
            return;
        }
        Template t = cluster[0].getVirtualMachine().getTemplate();
        Destination.FILE_TRANSFER_SOCKET=persistenceServices.getIntValue("FILE_TRANSFER_SOCKET");
        sender.sendFolder(cluster, new File(t.getVmxFileLocation()).getParentFile(), new File(cluster[0].getVirtualMachine().getVirtualMachinePath()).getParent());
        for(PairMachineExecution pme:cluster){
            pme.getVirtualMachine().setLocallyStored(true);
            persistenceServices.update(pme.getVirtualMachine());
        }
        machineOperations.configureVirtualMachines(poweroff,executionTime, cluster);
    }

    /**
     * Just turn on the given cluster. The cluster must be copied and configured.
     * @param executionTime
     * @param cluster
     */
    @Asynchronous
    @Override
    public void turnOnVirtualMachines(int executionTime,int cores,int ram, PairMachineExecution ... cluster) {
        System.out.println("turnOnMachines "+executionTime+" "+cores+" "+ram);
        for (PairMachineExecution pair : cluster){
            VirtualMachine virMac=pair.getVirtualMachine();
            VirtualMachineExecution virtualMachineExecution=pair.getExecution();
            virtualMachineExecution.setVirtualMachineExecutionStatus(DEPLOYING_STATE);
            virtualMachineExecution.setVirtualMachineExecutionStatusMessage("Deploying...");
            virtualMachineExecution.setShowProgressBar(false);
            persistenceServices.update(virtualMachineExecution);
            PhysicalMachine phyMac = virMac.getPhysicalMachine();
            AbstractCommunicator communication=null;
            try {
                SecureSocket socket = new SecureSocket(phyMac.getPhysicalMachineIP(), persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
                communication = socket.connect();
            } catch (ConnectionException ex) {
                phyMac.setPhysicalMachineState(OFF_STATE);
                virMac.setVirtualMachineState(OFF_STATE);
                virtualMachineExecution.setVirtualMachineExecutionStatus(ERROR_STATE);
                virtualMachineExecution.setVirtualMachineExecutionStatusMessage("Connection error");
                persistenceServices.update(virtualMachineExecution);
                persistenceServices.update(phyMac);
                persistenceServices.update(virMac);
                System.err.println(ERROR_MESSAGE + "There is not response from Clouder Client: " + phyMac.getPhysicalMachineName() + " with the IP Address: " + phyMac.getPhysicalMachineIP());
                continue;
            }
            try {
                communication.writeUTF("" + UnaCloudAbstractMessage.VIRTUAL_MACHINE_OPERATION,
                        "" + VM_TURN_ON,
                        virtualMachineExecution.getVirtualMachineExecutionCode(),
                        "" + virMac.getHypervisor().getHypervisorCode(),
                        "" + (cores==0?virMac.getVirtualMachineCores():cores),
                        "" + (ram==0?virMac.getVirtualMachineRAMMemory():ram),
                        virMac.getVirtualMachinePath(),
                        phyMac.getPhysicalMachineHypervisorPath(),
                        "" + executionTime,
                        virMac.getVirtualMachineIP(),
                        "" + virMac.isPersistent(),
                        virMac.isAutoProtect() ? "1" : "0",
                        virMac.isAutoProtect() ? virMac.getSnapshotId() : "N/A");
                communication.close();
            } catch (ConnectionException ex) {
                phyMac.setPhysicalMachineState(OFF_STATE);
                virMac.setVirtualMachineState(OFF_STATE);
                virtualMachineExecution.setVirtualMachineExecutionStatus(ERROR_STATE);
                virtualMachineExecution.setVirtualMachineExecutionStatusMessage("Connection error");
                persistenceServices.update(phyMac);
                persistenceServices.update(phyMac);
                persistenceServices.update(virMac);
                persistenceServices.delete(virtualMachineExecution);
                System.err.println(ERROR_MESSAGE + "There is not response from Clouder Client: " + phyMac.getPhysicalMachineName() + " with the IP Address: " + phyMac.getPhysicalMachineIP());
            }
        }

    }

    /**
     * Configure the given virtual machine cluster.
     * @param poweroff
     * @param executionTime
     * @param virMacs
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void configureVirtualMachines(boolean poweroff,int executionTime, PairMachineExecution... virMacs) {
        for(PairMachineExecution virMac:virMacs){
            machineOperations.configureVirtualMachine(virMac, poweroff,executionTime);
        }
    }

    /**
     * Configures a virtual machine
     * @param mac
     * @param poweroff
     * @param executionTime
     */
    @Asynchronous
    public void configureVirtualMachine(PairMachineExecution mac,boolean poweroff,int executionTime) {
        String cla = mac.getVirtualMachine().getTemplate().getOperatingSystem().getConfigurationClass();
        if (cla != null) {
            try {
                Class c = Class.forName("com.losandes.machineconfigurators." + cla);
                MachineConfigurator mc = (MachineConfigurator) c.getConstructor().newInstance();
                mc.setVirtualMachineExecutionServices(virtualMachinesExecutionServices);
                mc.init(persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"),persistenceServices.getIntValue("FILE_TRANSFER_SOCKET"));
                mc.configureMachine(mac, new HostTable(), poweroff);
                mac.getVirtualMachine().setConfigured(true);
                persistenceServices.update(mac.getVirtualMachine());
                turnOnVirtualMachines(executionTime,0,0, mac);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    @Override
    public void writeFileOnVirtualMachine(String virtualMachineExecutionId,String path,String content){
        VirtualMachineExecution vme=virtualMachinesExecutionServices.getVirtualMachineExecutionByID(virtualMachineExecutionId);
        if(vme!=null)AbstractSOConfigurator.writeMachineFile(vme.getVirtualMachine(),persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"),persistenceServices.getIntValue("FILE_TRANSFER_SOCKET"),path,content.getBytes());
    }
    /**
     * Retrieves a virtual machine and stores it as a template on UnaCloud Server.
     * @param toRetrieve
     * @param user
     */
    @Override
    public void retrieveVirtualMachine(VirtualMachine toRetrieve,String user) {
        Template t = toRetrieve.getTemplate();
        List<VirtualMachineExecution> vmes=virtualMachineServices.getVirtualMachineExecutions(user);
        List<String> toPowerOff = new ArrayList<String>();
        for(VirtualMachineExecution vme:vmes)if(vme.getTemplate().getTemplateCode()==t.getTemplateCode()){
            toPowerOff.add(vme.getVirtualMachineExecutionCode());
        }
        virtualMachineServices.turnOffVirtualMachineCluster(toPowerOff);
        VirtualMachineExecution vme = machineOperations.generateVirtualExecutions(0, 0, 1, userServices.getUserByID(user),toRetrieve)[0];
        virtualMachineRetriever.retrieveVirtualMachine(vme.getVirtualMachine().getPhysicalMachine().getPhysicalMachineIP(), vme.getVirtualMachine().getVirtualMachinePath(), vme.getVirtualMachine().getTemplate().getVmxFileLocation(),vme);
    }

    @Asynchronous
    @Override
    public void turnOnPhysicalMachines(int template, int executionTime, int numberInstances, int vmCores, int HDsize, int vmRAM, String userName) {
        List on=persistenceServices.executeNativeSQLList(Queries.getBridgePhysicalMachines(),PhysicalMachine.class);
        List off=persistenceServices.executeNativeSQLList(Queries.getTurnableOnPhysicalMachines(template, userName),PhysicalMachine.class);
        Map<Integer,List<PhysicalMachine>> onLabs=new HashMap<Integer,List<PhysicalMachine>>();
        Map<Integer,List<PhysicalMachine>> offMachines=new HashMap<Integer,List<PhysicalMachine>>();
        for(Object f:on)if(f instanceof PhysicalMachine){
            PhysicalMachine pm=(PhysicalMachine)f;
            List l =onLabs.get(pm.getLaboratory().getLaboratoryCode());
            if(l==null)onLabs.put(pm.getLaboratory().getLaboratoryCode(),l=new ArrayList());
            l.add(pm);
        }
        for(Object f:off)if(f instanceof PhysicalMachine){
            PhysicalMachine pm=(PhysicalMachine)f;
            List l =offMachines.get(pm.getLaboratory().getLaboratoryCode());
            if(l==null)offMachines.put(pm.getLaboratory().getLaboratoryCode(),l=new ArrayList());
            l.add(pm);
        }
        Random r=new Random();
        for(Map.Entry<Integer,List<PhysicalMachine>> e:offMachines.entrySet()){
            List<PhysicalMachine> ons=onLabs.get(e.getKey());
            ipms.turnOnPhysicalMachine(ons.get(r.nextInt(ons.size())),e.getValue().toArray(new PhysicalMachine[0]));
        }
        try {
            Thread.sleep(1 * 60000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        virtualMachineServices.turnOnVirtualClusterBySize(template, executionTime, numberInstances, vmCores, HDsize, vmRAM, userName, false);
    }
}