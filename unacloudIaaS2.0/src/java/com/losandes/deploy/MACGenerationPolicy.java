/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.deploy;

/**
 *
 * @author Clouder
 */
public enum MACGenerationPolicy {

    STATIC_MACHINE_BASED,RANDOM;
    public String nameString = name().replace("_"," ").toLowerCase();
}
