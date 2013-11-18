/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.wss;

import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.*;
import com.losandes.template.ITemplateServices;
import com.losandes.user.IUserServices;
import com.losandes.virtualmachine.IVirtualMachineServices;
import com.losandes.wsEntities.*;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.*;

/**
 *
 * @author Clouder
 */
@WebService()
@Stateless()
public class UnaCloudWS {

    @EJB
    private IVirtualMachineServices ivms;
    @EJB
    private ITemplateServices itserv;
    @EJB
    private IUserServices userServ;
    @EJB
    private IPersistenceServices persServices;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "turnOnVirtualCluster")
    public VirtualMachineExecutionWS[] turnOnVirtualCluster(@WebParam(name = "username") String username, @WebParam(name = "pass") String pass, @WebParam(name = "templateID") int templateID, @WebParam(name = "size") int size, @WebParam(name = "ram") int ram, @WebParam(name = "cores") int cores, @WebParam(name = "hdSize") int hdSize, @WebParam(name = "time") int time) {
        if (!userServ.validateUser(username, pass)) {
            return null;
        }
        if (ram < 0) {
            return null;
        }
        if (cores < 0) {
            return null;
        }
        if (hdSize < 0) {
            return null;
        }
        if (time < 1) {
            return null;
        }
        System.out.println("turnOnVirtualCluster " + username + " " + templateID + " " + size);
        VirtualMachineExecution[] vmes = ivms.turnOnVirtualClusterBySize(templateID, time, size, cores, hdSize, ram, username, true);
        List<VirtualMachineExecutionWS> ret = new ArrayList<VirtualMachineExecutionWS>();
        for (VirtualMachineExecution vme : vmes) {
            VirtualMachineExecutionWS r = new VirtualMachineExecutionWS();
            r.setVirtualMachineExecutionCode(vme.getVirtualMachineExecutionCode());
            r.setVirtualMachine(vme.getVirtualMachine().getVirtualMachineCode());
            r.setVirtualMachineExecutionCores(vme.getVirtualMachineExecutionCores());
            r.setVirtualMachineExecutionHardDisk(vme.getVirtualMachineExecutionHardDisk());
            r.setVirtualMachineExecutionIP(vme.getVirtualMachineExecutionIP());
            r.setVirtualMachineExecutionRAMMemory(vme.getVirtualMachineExecutionRAMMemory());
            r.setVirtualMachineExecutionStart(vme.getVirtualMachineExecutionStart());
            r.setVirtualMachineExecutionStatus(vme.getVirtualMachineExecutionStatus());
            r.setVirtualMachineExecutionStatusMessage(vme.getVirtualMachineExecutionStatusMessage());
            r.setVirtualMachineExecutionStop(vme.getVirtualMachineExecutionStop());
            r.setVirtualMachineName(vme.getVirtualMachine().getVirtualMachineName());
            ret.add(r);
        }
        return ret.toArray(new VirtualMachineExecutionWS[0]);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "turnOnVirtualClusterCCGrid")
    public VirtualMachineExecutionWS[] turnOnVirtualClusterCCGrid(@WebParam(name = "username") String username, @WebParam(name = "pass") String pass, @WebParam(name = "templateID") int templateID, @WebParam(name = "size") int size, @WebParam(name = "ram") int ram, @WebParam(name = "cores") int cores, @WebParam(name = "hdSize") int hdSize, @WebParam(name = "time") int time,int usedPms,int noUsedPms) {
        if (!userServ.validateUser(username, pass)||ram < 0||cores < 0||hdSize < 0||time < 1)return null;
        System.out.println("turnOnVirtualClusterCCGrid " + username + " " + templateID + " " + size);
        VirtualMachineExecution[] vmes = ivms.turnOnVirtualClusterBySizeAndUsers(templateID, time, size, cores, hdSize, ram, username, usedPms,noUsedPms);
        List<VirtualMachineExecutionWS> ret = new ArrayList<VirtualMachineExecutionWS>();
        for (VirtualMachineExecution vme : vmes) {
            VirtualMachineExecutionWS r = new VirtualMachineExecutionWS();
            r.setVirtualMachineExecutionCode(vme.getVirtualMachineExecutionCode());
            r.setVirtualMachine(vme.getVirtualMachine().getVirtualMachineCode());
            r.setVirtualMachineExecutionCores(vme.getVirtualMachineExecutionCores());
            r.setVirtualMachineExecutionHardDisk(vme.getVirtualMachineExecutionHardDisk());
            r.setVirtualMachineExecutionIP(vme.getVirtualMachineExecutionIP());
            r.setVirtualMachineExecutionRAMMemory(vme.getVirtualMachineExecutionRAMMemory());
            r.setVirtualMachineExecutionStart(vme.getVirtualMachineExecutionStart());
            r.setVirtualMachineExecutionStatus(vme.getVirtualMachineExecutionStatus());
            r.setVirtualMachineExecutionStatusMessage(vme.getVirtualMachineExecutionStatusMessage());
            r.setVirtualMachineExecutionStop(vme.getVirtualMachineExecutionStop());
            r.setVirtualMachineName(vme.getVirtualMachine().getVirtualMachineName());
            ret.add(r);
        }
        return ret.toArray(new VirtualMachineExecutionWS[0]);
    }
    /**
     * Web service operation
     */
    @WebMethod(operationName = "getVirtualMachineExecutions")
    public VirtualMachineExecutionWS[] getVirtualMachineExecutions(@WebParam(name = "username") String username, @WebParam(name = "pass") String pass, @WebParam(name = "templateID") int templateID) {
        if (!userServ.validateUser(username, pass)) {
            return null;
        }
        List<VirtualMachineExecution> vmes = ivms.getVirtualMachineExecutions(username);
        System.out.println("getVirtualMachineExecutions " + vmes.size());
        List<VirtualMachineExecutionWS> ret = new ArrayList<VirtualMachineExecutionWS>();
        for (VirtualMachineExecution vme : vmes) {
            VirtualMachineExecutionWS r = new VirtualMachineExecutionWS();
            r.setVirtualMachineExecutionCode(vme.getVirtualMachineExecutionCode());
            r.setVirtualMachine(vme.getVirtualMachine().getVirtualMachineCode());
            r.setVirtualMachineExecutionCores(vme.getVirtualMachineExecutionCores());
            r.setVirtualMachineExecutionHardDisk(vme.getVirtualMachineExecutionHardDisk());
            r.setVirtualMachineExecutionIP(vme.getVirtualMachineExecutionIP());
            r.setVirtualMachineExecutionRAMMemory(vme.getVirtualMachineExecutionRAMMemory());
            r.setVirtualMachineExecutionStart(vme.getVirtualMachineExecutionStart());
            r.setVirtualMachineExecutionStatus(vme.getVirtualMachineExecutionStatus());
            r.setVirtualMachineExecutionStatusMessage(vme.getVirtualMachineExecutionStatusMessage());
            r.setVirtualMachineExecutionStop(vme.getVirtualMachineExecutionStop());
            r.setVirtualMachineName(vme.getVirtualMachine().getVirtualMachineName());
            ret.add(r);
        }
        return ret.toArray(new VirtualMachineExecutionWS[0]);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "turnOffVirtualMachine")
    public String turnOffVirtualMachine(@WebParam(name = "username") String username, @WebParam(name = "pass") String pass, @WebParam(name = "virtualMachineExID") String virtualMachineExID) {
        if (!userServ.validateUser(username, pass)) {
            return null;
        }
        List<String> vmIds = new ArrayList<String>();
        vmIds.add(virtualMachineExID);
        ivms.turnOffVirtualMachineCluster(vmIds);
        return "Operation successfull";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getTemplateLists")
    public TemplateWS[] getTemplateLists(@WebParam(name = "username") String username, @WebParam(name = "pass") String pass) {
        System.out.println("getTemplateLists");
        if (!userServ.validateUser(username, pass)) {
            return null;
        }
        List<Template> ts = itserv.getFastAvailableTemplates(username);
        List<TemplateWS> ret = new ArrayList<TemplateWS>();
        for (Template t : ts) {
            TemplateWS tws = new TemplateWS();
            tws.setApplications(new ArrayList<String>());
            tws.setCustomizable(t.isCustomizable());
            tws.setHighAvailability(t.isHighAvailability());
            tws.setOperatingSystem(new OperatingSystemWS());
            tws.getOperatingSystem().setOperatingSystemType(t.getOperatingSystem().getOperatingSystemType().getOperatingSystemTypeName());
            tws.getOperatingSystem().setOperatingSystemName(t.getOperatingSystem().getOperatingSystemName());
            tws.setTemplateCode(t.getTemplateCode());
            tws.setTemplateName(t.getTemplateName());
            tws.setTemplateType(t.getTemplateType().getTemplateTypeName());
            ret.add(tws);
        }
        return ret.toArray(new TemplateWS[0]);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAvailableVirtualMachines")
    public Integer getAvailableVirtualMachines(@WebParam(name = "templateSelected")
            final int templateSelected, @WebParam(name = "virtualMachineDisk")
            final int virtualMachineDisk, @WebParam(name = "virtualMachineCores")
            final int virtualMachineCores, @WebParam(name = "virtualMachineRAM")
            final int virtualMachineRAM, @WebParam(name = "user")
            final String user, @WebParam(name = "password")
            final String password) {
        if (!userServ.validateUser(user, password)) {
            return null;
        }
        SystemUser us = userServ.getUserByID(user);
        if (us != null && us.getTemplates() != null) {
            boolean tienePermiso = false;
            for (Template t : us.getTemplates()) {
                if (t.getTemplateCode() == templateSelected) {
                    tienePermiso = true;
                    break;
                }
            }
            if (tienePermiso) {
                return ivms.getAvailableVirtualMachines(templateSelected, virtualMachineDisk, virtualMachineCores, virtualMachineRAM).size();
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getTotalUnaCloudResources")
    public Integer getTotalUnaCloudResources(@WebParam(name = "machineDisk") int machineDisk, @WebParam(name = "machineCores") int machineCores, @WebParam(name = "machineRam") int machineRam) {
        List l = persServices.executeNativeSQLList("select count(*) c from physicalmachine where `PHYSICALMACHINERAMMEMORY`/2>=" + machineRam + " and `PHYSICALMACHINECORES`>=" + machineCores + " and `PHYSICALMACHINEDISK`>=" + machineDisk, null);
        if (l.isEmpty()) {
            return null;
        }
        return (int) (long) (Long) l.get(0);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAvailableUnaCloudResources")
    public Integer getAvailableUnaCloudResources(@WebParam(name = "machineDisk") int machineDisk, @WebParam(name = "machineCores") int machineCores, @WebParam(name = "machineRam") int machineRam) {
        List l = persServices.executeNativeSQLList("select count(*) c from physicalmachine where `PHYSICALMACHINEVIRTUALMACHINESON`<`MAXVIRTUALMACHINESON` and `PHYSICALMACHINERAMMEMORY`/2>=" + machineRam + " and `PHYSICALMACHINECORES`>=" + machineCores + " and `PHYSICALMACHINEDISK`>=" + machineDisk + ";", null);
        if (l.isEmpty()) {
            return null;
        }
        return (int) (long) (Long) l.get(0);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "writeFileOnVirtualMachine")
    public String writeFileOnVirtualMachine(@WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "virtualMachineExecutionId") String virtualMachineExecutionId, @WebParam(name = "path") String path, @WebParam(name = "content") String content) {
        if (!userServ.validateUser(username, password)) {
            return null;
        }
        ivms.writeFileOnVirtualMachine(virtualMachineExecutionId, path, content);
        return "Operation successfull";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getBusyUnaCloudResources")
    public Integer getBusyUnaCloudResources(@WebParam(name = "machineDisk") int machineDisk, @WebParam(name = "machineCores") int machineCores, @WebParam(name = "machineRam") int machineRam) {
        List l = persServices.executeNativeSQLList("select count(*) c from physicalmachine where `PHYSICALMACHINEVIRTUALMACHINESON`=`MAXVIRTUALMACHINESON` and `PHYSICALMACHINERAMMEMORY`/2>=" + machineRam + " and `PHYSICALMACHINECORES`>=" + machineCores + " and `PHYSICALMACHINEDISK`>=" + machineDisk + ";", null);
        if (l.isEmpty()) {
            return null;
        }
        return (int) (long) (Long) l.get(0);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getTotalVirtualMachines")
    public Integer getTotalVirtualMachines(
            @WebParam(name = "machineDisk") int machineDisk,
            @WebParam(name = "machineCores") int machineCores,
            @WebParam(name = "machineRam") int machineRam,
            @WebParam(name = "templateCode") int templateCode,
            @WebParam(name = "username") String username,
            @WebParam(name = "pass") String pass) {
        List l = persServices.executeNativeSQLList("select count(*) c from physicalmachine pm,virtualmachine vm, systemuser_template sute where pm.`PHYSICALMACHINERAMMEMORY`/2>=" + machineRam + " and pm.`PHYSICALMACHINECORES`>=" + machineCores + " and pm.`PHYSICALMACHINEDISK`>=" + machineDisk + " and vm.`PHYSICALMACHINE_PHYSICALMACHINENAME`=pm.`PHYSICALMACHINENAME`and vm.`TEMPLATE_TEMPLATECODE`=sute.`templates_TEMPLATECODE` and sute.`systemUser_SYSTEMUSERNAME`='" + username + "' and `TEMPLATE_TEMPLATECODE`=" + templateCode + ";", null);
        if (l.isEmpty()) {
            return null;
        }
        return (int) (long) (Long) l.get(0);
    }
}
