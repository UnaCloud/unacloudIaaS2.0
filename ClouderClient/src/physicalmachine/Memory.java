/*
 * Copyright (C) [2004, 2005, 2006], Hyperic, Inc.
 * This file is part of SIGAR.
 * 
 * SIGAR is free software; you can redistribute it and/or modify
 * it under the terms version 2 of the GNU General Public License as
 * published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

package physicalmachine;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

/**
 * Responsible for obtaining local RAM Memory information
 */
public class Memory extends SigarWrapper {
    
    private float rAMMemorySize;
    private float rAMMemoryFree;
    private float rAMMemoryUsed;
    private float swapMemorySize;
    private float swapMemoryFree;
    private float swapMemoryPageIn;
    private float swapMemoryPageOut;
    private float swapMemoryUsed;


    private Mem mem;
    private Swap swap;

    public Memory() {
        try {
            mem = this.sigar.getMem();
            swap = this.sigar.getSwap();
            rAMMemorySize= mem.getRam();
            rAMMemoryFree= mem.getFree()/1024/1024;
            rAMMemoryUsed = mem.getUsed()/1024/1024;
            swapMemorySize = swap.getTotal()/1024/1024;
            swapMemoryFree = swap.getFree()/1024/1024;
            swapMemoryPageIn =swap.getPageIn()/1024/1024;
            swapMemoryPageOut = swap.getPageOut()/1024/1024;
            swapMemoryUsed = swap.getUsed()/1024/1024;
        } catch (SigarException ex) {
            Logger.getLogger(Memory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the RAMMemorySize
     */
    public float getRAMMemorySize() {
        return rAMMemorySize;
    }

    /**
     * @return the RAMMemoryFree
     */
    public float getRAMMemoryFree() {
        return rAMMemoryFree;
    }

    /**
     * @return the RAMMemoryUsed
     */
    public float getRAMMemoryUsed() {
        return rAMMemoryUsed;
    }

    /**
     * @return the SwapMemorySize
     */
    public float getSwapMemorySize() {
        return swapMemorySize;
    }
    
    public float getSwapMemoryFree() {
        return swapMemoryFree;
    }
    
    public float getSwapMemoryPageIn() {
        return swapMemoryPageIn;
    }
    
    public float getSwapMemoryPageOut() {
        return swapMemoryPageOut;
    }
    
    public float getSwapMemoryUsed() {
        return swapMemoryUsed;
    }

}//end of RAMMemory
