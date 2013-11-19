/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import back.services.BackPersistenceServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static com.losandes.utils.Constants.*;

/**
 *
 * @author Clouder
 */
public class MachineStateManager {

    private final Map<String, Long> syncLastReports = Collections.synchronizedSortedMap(new TreeMap<String, Long>());
    public static long period = 30000;
    public static long limitFail = 2;
    
    public MachineStateManager() {
        long l = System.currentTimeMillis();
        String[] pms=new BackPersistenceServices().getAllPhysicalMachines();
        for(String m:pms){
            syncLastReports.put(m, l);
        }
        new BackPersistenceServices().updatePhysicalMachineState(ON_STATE, pms);

        TimerTask tt = new TimerTask() {
            public void run() {
                searchLossedConnections();
            }
        };
        Timer t = new Timer();
        t.schedule(tt, period, period);
    }

    public void reportMachine(String machineId) {
        synchronized (syncLastReports){
            if(!syncLastReports.containsKey(machineId)){
            	new BackPersistenceServices().updatePhysicalMachineState(ON_STATE, machineId);
            }
            syncLastReports.put(machineId, System.currentTimeMillis());
        }
        
    }

    private void searchLossedConnections() {
        ArrayList<String> aDesconectar = new ArrayList<>();
        long l = System.currentTimeMillis();
        synchronized (syncLastReports){
            boolean b=false;
            Set<Map.Entry<String, Long>> es = syncLastReports.entrySet();
            Iterator<Map.Entry<String, Long>> it = es.iterator();
            while(it.hasNext()) {
                Map.Entry<String, Long> e = it.next();
                if (l - e.getValue() > period * limitFail) {
                    aDesconectar.add(e.getKey());
                    if(!b){
                        System.out.print("Desconectar: ");
                        b=true;
                    }
                    System.out.print(" "+e.getKey());
                }
            }
            if(b)System.out.println("");
            for(String h:aDesconectar)syncLastReports.remove(h);
        }
        if(!aDesconectar.isEmpty()) {
            if(!aDesconectar.isEmpty())new BackPersistenceServices().updatePhysicalMachineState(OFF_STATE, aDesconectar.toArray(new String[0]));
        }
    }
}
