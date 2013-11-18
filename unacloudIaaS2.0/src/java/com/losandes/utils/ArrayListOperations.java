package com.losandes.utils;

import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.persistence.entity.VirtualMachine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the ArrayList operations
 */
public class ArrayListOperations {

    /**
     * Responsible for sorting an CPU cores ArrayList
     */
    public static ArrayList<Integer> getSortedUniqueCores(ArrayList<Integer> listaP) {
        ArrayList<Integer> resp = new ArrayList<Integer>();
        TreeSet<Integer> t = new TreeSet<Integer>();
        for (Integer i : listaP) {
            for (int j = 1; j < i; j++) {
                t.add(j);
            }
            t.add(i);
        }
        Iterator<Integer> it = t.iterator();
        while (it.hasNext()) {
            resp.add(it.next());
        }
        Collections.sort(resp);
        return resp;
    }

    /**
     * Responsible for sorting an RAM memories sizes ArrayList
     */
    public static ArrayList<Integer> getSortedUniqueRAM(ArrayList<Integer> listaP) {
        ArrayList<Integer> resp = new ArrayList<Integer>();
        TreeSet<Integer> t = new TreeSet<Integer>();
        for (Integer i : listaP) {
            t.add(i / 2);
            for (int j = 512; j < i; j += 512) {
                t.add(j / 2);
            }
        }
        Iterator<Integer> it = t.iterator();
        while (it.hasNext()) {
            resp.add(it.next());
        }
        Collections.sort(resp);
        return resp;
    }

    /**
     * Responsible for sorting an int values noFomatList
     */
    public static List getSortedIntValues(List noFormatList) {
        List formatList = new ArrayList();
        for (int i = 0; i < noFormatList.size(); i++) {
            String aux = noFormatList.get(i).toString().replaceAll("[^0-9]", "");
            formatList.add(Integer.parseInt(aux));
        }
        Collections.sort(formatList);
        return formatList;
    }

    /**
     * Responsible for getting executable virtual machines
     */
    public static List<VirtualMachine> getExecutablesVirtualMachines(List<VirtualMachine> virtualMachines, int executableInstances) {
        List<VirtualMachine> executableVMs = new ArrayList();
        for (int i = 0; i < executableInstances; i++) {
            executableVMs.add(virtualMachines.get(i));
        }
        return executableVMs;
    }

    /**
     * Responsible for getting executable grid virtual machines
     */
    public static List<VirtualMachine> getExecutablesGridVirtualMachines(List<VirtualMachine> virtualMachines, ArrayList<PhysicalMachine> executionPhysicalMachines) {
        System.out.println("VMs "+virtualMachines.size());
        System.out.println("PMs "+executionPhysicalMachines.size());
        List<VirtualMachine> executableVMs = new ArrayList();
        for (int i = 0; i < executionPhysicalMachines.size(); i++) {
            for (int j = 0; j < virtualMachines.size(); j++) {
                if (virtualMachines.get(j).getPhysicalMachine().getPhysicalMachineName().equals(executionPhysicalMachines.get(i).getPhysicalMachineName())){
                    executableVMs.add(virtualMachines.get(j));
                }
            }
        }
        return executableVMs;
    }
}//end of ArrayListOperations

