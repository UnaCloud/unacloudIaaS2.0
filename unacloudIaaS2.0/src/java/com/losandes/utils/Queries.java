package com.losandes.utils;

import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Cloud and Grid users queries
 */
public class Queries {

    /**
     * Responsible for sorting Operating System Types available query for cloud users
     */
    public static String getOperatingSystemTypeAvailable() {
        String query =
                "SELECT DISTINCT ost.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, OperatingSystem os, OperatingSystemType ost, Systemuser su " +
                "WHERE ost.operatingSystemTypeCode = os.operatingSystemType_operatingSystemTypeCode AND " +
                "os.operatingSystemCode = te.operatingSystem_operatingSystemCode AND " +
                "te.TemplateType_TemplateTypeCode = " + CLOUD_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.physicalMachine_physicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting Operating System Types available query for grid users
     */
    public static String getGridOperatingSystemTypeAvailable(String systemUserName) {
        String query =
                "SELECT DISTINCT ost.*  " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, OperatingSystem os, OperatingSystemType ost, Systemuser_template sut, Systemuser su " +
                "WHERE ost.operatingSystemTypeCode = os.operatingSystemType_operatingSystemTypeCode AND " +
                "os.operatingSystemCode = te.operatingSystem_operatingSystemCode AND " +
                "su.systemUserName = '" + systemUserName + "' AND " +
                "sut.systemUser_systemUserName = '" + systemUserName + "' AND " +
                "sut.templates_TemplateCode = te.TemplateCode AND " +
                "te.TemplateType_TemplateTypeCode =  " + GRID_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.physicalMachine_physicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachineState = " + OFF_STATE + "  AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting Operating Systems available query for cloud users
     */
    public static String getOperatingSystemAvailable(int operatingSystemTypeSelected) {
        String query =
                "SELECT DISTINCT os.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, OperatingSystem os, SystemUser su " +
                "WHERE os.operatingSystemType_operatingSystemTypeCode = " + operatingSystemTypeSelected + " AND " +
                "os.operatingSystemCode = te.operatingSystem_operatingSystemCode AND " +
                "te.TemplateType_TemplateTypeCode = " + CLOUD_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_physicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting Operating Systems available query for grid users
     */
    public static String getGridOperatingSystemAvailable(int operatingSystemTypeSelected, String systemUserName) {
        String query =
                "SELECT DISTINCT os.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, OperatingSystem os, Systemuser_template sut, Systemuser su " +
                "WHERE os.operatingSystemType_operatingSystemTypeCode =  " + operatingSystemTypeSelected + "  AND " +
                "os.operatingSystemCode = te.operatingSystem_operatingSystemCode AND " +
                "su.systemUserName =  '" + systemUserName + "' AND " +
                "sut.systemUser_systemUserName = '" + systemUserName + "' AND " +
                "sut.templates_TemplateCode = te.TemplateCode AND " +
                "te.TemplateType_TemplateTypeCode =  " + GRID_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_physicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachineState =  " + OFF_STATE + "  AND " +
                "pm.physicalMachineState = " + ON_STATE + " AND "+
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting Templates available query for cloud users
     */
    public static String getTemplatesAvailable(int operatingSystemSelected) {
        String query = "SELECT DISTINCT te.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, SystemUser su " +
                "WHERE te.operatingSystem_operatingSystemCode = " + operatingSystemSelected + " AND " +
                "te.TemplateType_TemplateTypeCode = " + CLOUD_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachinestate = " + OFF_STATE + " AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting Templates available fast query for cloud users
     */
    public static String getFastTemplatesAvailable(String userName) {
        String query = "SELECT DISTINCT te.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, systemuser_template su " +
                "WHERE te.TemplateType_TemplateTypeCode = " + CLOUD_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "su.templates_templatecode = te.templateCode AND " +
                "su.systemUser_systemusername = '"+userName+"' AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachinestate = " + OFF_STATE + " AND " +
                "pm.physicalmachinevirtualmachineson < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting Templates available query for grid users
     */
    public static String getGridTemplatesAvailable(int operatingSystemSelected, String systemUserName) {
        String query = "SELECT DISTINCT te.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, Systemuser_template sut, SystemUser su " +
                "WHERE te.operatingSystem_operatingSystemCode = " + operatingSystemSelected + " AND " +
                "su.systemUserName = '" + systemUserName + "' AND " +
                "sut.systemUser_SystemUserName = '" + systemUserName + "' AND " +
                "sut.templates_TemplateCode = te.TemplateCode AND " +
                "te.TemplateType_TemplateTypeCode = " + GRID_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachinestate = " + OFF_STATE + " AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting Templates available fast query for grid users
     */
    public static String getFastGridTemplatesAvailable(String systemUserName) {
        String query = "SELECT DISTINCT te.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, Systemuser_template sut, SystemUser su " +
                "WHERE su.systemUserName = '" + systemUserName + "' AND " +
                "sut.systemUser_SystemUserName = '" + systemUserName + "' AND " +
                "sut.templates_TemplateCode = te.TemplateCode AND " +
                "te.TemplateType_TemplateTypeCode = " + GRID_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachinestate = " + OFF_STATE + " AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting Applications available in a template
     */
    public static String getApplicationsAvailables(int templateSelected) {
        String query = "SELECT ap.* " +
                "FROM Template te, Application ap, template_application ta " +
                "WHERE ap.applicationCode = ta.applications_applicationCode AND " +
                "ta.templates_templateCode = te.templateCode AND " +
                "te.templateCode = " + templateSelected + " ;";
        return query;
    }

    /**
     * Responsible for sorting Virtual Hard Disk available query for cloud users
     */
    public static String getVirtualHardDiskAvailable(int templateSelected) {
        String query = "SELECT DISTINCT vm.virtualMachineHardDisk " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, SystemUser su " +
                "WHERE te.templateCode = " + templateSelected + " AND " +
                "te.TemplateType_TemplateTypeCode = " + CLOUD_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting Virtual Hard Disk available query for grid users
     */
    public static String getGridVirtualHardDiskAvailable(int templateSelected, String systemUserName) {
        String query = "SELECT DISTINCT vm.virtualMachineHardDisk " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, Systemuser_template sut, SystemUser su " +
                "WHERE te.templateCode = " + templateSelected + " AND " +
                "su.systemUserName = '" + systemUserName + "' AND " +
                "su.systemUserName = sut.systemUser_SystemUserName AND " +
                "sut.templates_TemplateCode = te.TemplateCode AND " +
                "te.TemplateType_TemplateTypeCode = " + GRID_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "pm.physicalMachineState = " + ON_STATE + " ;";
        return query;
    }

    /**
     * Responsible for sorting CPU Cores available query for cloud users
     */
    public static String getPhysicalCoresAvailable(int templateSelected) {
        String query = "SELECT DISTINCT pm.physicalMachineCores " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, SystemUser su " +
                "WHERE te.templateCode = " + templateSelected + " AND " +
                "te.TemplateType_TemplateTypeCode = " + CLOUD_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting CPU Cores available query for grid users
     */
    public static String getGridPhysicalCoresAvailable(int templateSelected, String systemUserName) {
        String query = "SELECT DISTINCT pm.physicalMachineCores " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, Systemuser_template sut, SystemUser su " +
                "WHERE te.templateCode = " + templateSelected + " AND " +
                "su.systemUserName = '" + systemUserName + "' AND " +
                "su.systemUserName = sut.systemUser_SystemUserName AND " +
                "sut.templates_TemplateCode = te.TemplateCode AND " +
                "te.TemplateType_TemplateTypeCode = " + GRID_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "pm.physicalMachineState = " + ON_STATE + " ;";
        return query;
    }

    /**
     * Responsible for sorting RAM Memory available query for cloud users
     */
    public static String getPhysicalRAMAvailable(int templateSelected) {
        String query = "SELECT DISTINCT pm.physicalMachineRAMMemory " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, SystemUser su " +
                "WHERE te.templateCode = " + templateSelected + " AND " +
                "te.TemplateType_TemplateTypeCode = " + CLOUD_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson ; ";
        return query;
    }

    /**
     * Responsible for sorting RAM Memory available  query for grid users
     */
    public static String getGridPhysicalRAMAvailable(int templateSelected, String systemUserName) {
        String query = "SELECT DISTINCT pm.physicalMachineRAMMemory " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, Systemuser_template sut, SystemUser su " +
                "WHERE te.templateCode = " + templateSelected + " AND " +
                "su.systemUserName = '" + systemUserName + "' AND " +
                "su.systemUserName = sut.systemUser_SystemUserName AND " +
                "sut.templates_TemplateCode = te.TemplateCode AND " +
                "te.TemplateType_TemplateTypeCode = " + GRID_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "pm.physicalMachineState = " + ON_STATE + " ;";
        return query;
    }

    /**
     * Responsible for sorting Virtual Machines available query for cloud users
     */
    public static String getVirtualMachineAvailable(int templateSelected, int virtualMachineDisk, int virtualMachineCores, int virtualMachineRAM) {
        String query = "SELECT DISTINCT vm.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te " +
                "WHERE te.templateCode = " + templateSelected + " AND " +
                "te.TemplateType_TemplateTypeCode = " + CLOUD_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.virtualMachineHardDisk = " + virtualMachineDisk + " AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "pm.physicalMachineCores >= " + virtualMachineCores + " AND " +
                "pm.physicalMachineRAMMemory > " + virtualMachineRAM + " AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson AND " +
                "pm.physicalMachineState = " + ON_STATE + " AND vm.TURNONCOUNT > 0;";
        return query;
    }

    /**
     * Responsible for sorting Virtual Machines fast query available for cloud users
     */
    public static String getFastVirtualMachineAvailable(int templateSelected) {
        String query = "SELECT DISTINCT vm.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te " +
                "WHERE te.templateCode = " + templateSelected + " AND " +
                "te.TemplateType_TemplateTypeCode = " + CLOUD_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.virtualMachineHardDisk >= 1 AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "pm.physicalMachineCores >= 1 AND " +
                "pm.physicalMachineRAMMemory > 256 AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson AND " +
                "pm.physicalMachineState = " + ON_STATE + " order by vm.TURNONCOUNT desc;";
        return query;
    }

    /**
     * Responsible for sorting Virtual Machines available query for grid users
     */
    public static String getGridVirtualMachineAvailable(int templateSelected, int virtualMachineDisk, int virtualMachineCores, int virtualMachineRAM, String systemUserName) {
        String query = "SELECT DISTINCT vm.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, Systemuser_template sut, SystemUser su " +
                "WHERE te.templateCode = " + templateSelected + " AND " +
                "su.systemUserName = '" + systemUserName + "' AND " +
                "su.systemUserName = sut.systemUser_SystemUserName AND " +
                "sut.templates_TemplateCode = te.TemplateCode AND " +
                "te.TemplateType_TemplateTypeCode = " + GRID_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.virtualMachineHardDisk = " + virtualMachineDisk + " AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "pm.physicalMachineCores >= " + virtualMachineCores + " AND " +
                "pm.physicalMachineRAMMemory > " + virtualMachineRAM + " AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson AND " +
                "pm.physicalMachineState = " + ON_STATE + " ;";
        return query;
    }

    /**
     * Responsible for sorting Virtual Machines available fast query for grid users
     */
    public static String getFastGridVirtualMachineAvailable(int templateSelected, String systemUserName) {
        String query = "SELECT DISTINCT vm.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, Systemuser_template sut, SystemUser su " +
                "WHERE te.templateCode = " + templateSelected + " AND " +
                "su.systemUserName = '" + systemUserName + "' AND " +
                "su.systemUserName = sut.systemUser_SystemUserName AND " +
                "sut.templates_TemplateCode = te.TemplateCode AND " +
                "te.TemplateType_TemplateTypeCode = " + GRID_TEMPLATE + " AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.virtualMachineHardDisk >= 1 AND " +
                "vm.virtualMachineState = " + OFF_STATE + " AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "pm.physicalMachineCores >= 1 AND " +
                "pm.physicalMachineRAMMemory >= 256 AND " +
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson AND " +
                "pm.physicalMachineState = " + ON_STATE + " ;";
        return query;

    }

    public static String getOffHAVirtualMachines(String systemUserName) {
        String query = "SELECT DISTINCT pm.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, Template te, Systemuser_template sut, SystemUser su " +
                "WHERE su.systemUserName = '" + systemUserName + "' AND " +
                "su.systemUserName = sut.systemUser_SystemUserName AND " +
                "sut.templates_TemplateCode = te.TemplateCode AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "te.highAvailability = true AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "pm.physicalMachineState = " + OFF_STATE + " ;";
        return query;

    }

    /**
     * Responsible for sorting consistent Virtual Machines available query
     */
    public static String getConsistentVirtualMachines(String systemUserName) {
        String query = "SELECT vme.* " +
                "FROM virtualmachineexecution vme " +
                "WHERE vme.VIRTUALMACHINEEXECUTIONSTATUS != " + OFF_STATE + " AND " +
                "vme.SYSTEMUSER_SYSTEMUSERNAME = '" + systemUserName + "' ;";
        return query;
    }

    /**
     * Responsible for sorting consistent Virtual Machines available query
     */
    public static String getAllVirtualMachinesExecutions() {
        String query = "SELECT vme.* " +
                "FROM virtualmachineexecution vme " +
                "WHERE vme.VIRTUALMACHINEEXECUTIONSTATUS != " + OFF_STATE + " ; ";
        return query;
    }

    public static String getVirtualMachine(String route,String systemUser) {
        String query = "SELECT DISTINCT vm.* " +
                "FROM PhysicalMachine pm, VirtualMachine vm, SystemUser su, Template te, SystemUser_Template sut " +
                "WHERE sut.systemUser_SYSTEMUSERNAME = '" + systemUser + "' AND " +
                "te.templateCode = sut.templates_TEMPLATECODE AND " +
                "te.templateCode = vm.template_templateCode AND " +
                "vm.PhysicalMachine_PhysicalMachineName = pm.physicalMachineName AND " +
                "vm.virtualMachinepath = '" + route+ "' AND "+
                "vm.virtualMachineState = '" + OFF_STATE+ "' AND "+
                "pm.PHYSICALMACHINEVIRTUALMACHINESON < pm.maxvirtualmachineson AND " +
                "pm.physicalMachineState = " + ON_STATE + " ;";
        return query;
    }

    public static String getPhysicalMachineExecutions(String physicalMachine) {
        String query = "SELECT DISTINCT vme.* " +
                "FROM VirtualMachineExecution vme, VirtualMachine vm " +
                "WHERE vme.VIRTUALMACHINE_VIRTUALMACHINECODE = vm.VIRTUALMACHINECODE AND " +
                "vm.PHYSICALMACHINE_PHYSICALMACHINENAME = '"+physicalMachine+"' AND " +
                "vme.VIRTUALMACHINEEXECUTIONSTATUS != "+OFF_STATE+" ;";
        return query;
    }

    public static String getAllOnVirtualMacnines(){
        String query = "SELECT DISTINCT vm.* " +
                "FROM VirtualMachine vm " +
                "WHERE vm.virtualMachineState = " + ON_STATE + " ; ";
        return query;
    }

    public static String getTemplateVirtualMachines(int template){
        String query = "SELECT DISTINCT vm.* " +
                "FROM VirtualMachine vm, Template t " +
                "WHERE vm.TEMPLATE_TEMPLATECODE = t.TEMPLATECODE AND "+
                "t.TEMPLATECODE = "+template+" ;";
        return query;
    }

    public static String createEvent(int time,String user){
        String query = "CREATE EVENT "+user.replace(" ","").replace(".","")+System.currentTimeMillis()+" ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL "+time+" HOUR ON COMPLETION NOT PRESERVE DO "+
            "UPDATE VIRTUALMACHINE vm,VIRTUALMACHINEEXECUTION vme SET vm.VIRTUALMACHINESTATE=0, vme.VIRTUALMACHINEEXECUTIONSTATUS =0 "+
            "WHERE vme.VIRTUALMACHINEEXECUTIONTIME <= now() AND vme.VIRTUALMACHINE_VIRTUALMACHINECODE = vm.VIRTUALMACHINECODE AND "+
            "vme.VIRTUALMACHINEEXECUTIONSTATUS != 0;";
        return query;
    }

    public static String getBridgePhysicalMachines(){
        String query="select * from physicalmachine where `PHYSICALMACHINESTATE`=1;;";
        return query;
    }

    public static String getTurnableOnPhysicalMachines(int templateCode,String systemUser){
        String query="select pm.* from virtualmachine vm,physicalmachine pm,systemuser_template sute where vm.`TEMPLATE_TEMPLATECODE`="+templateCode+" and vm.`TEMPLATE_TEMPLATECODE`=sute.`templates_TEMPLATECODE` and sute.`systemUser_SYSTEMUSERNAME`='"+systemUser+"' and  vm.`PHYSICALMACHINE_PHYSICALMACHINENAME`=pm.`PHYSICALMACHINENAME` and pm.`PHYSICALMACHINESTATE`=0 and pm.`LABORATORY_LABORATORYCODE` IN (select distinct lab.`LABORATORYCODE` from physicalmachine pm,laboratory lab where pm.LABORATORY_LABORATORYCODE=lab.LABORATORYCODE and pm.physicalmachinestate=1);";
        return query;
    }

}//end of Queries

