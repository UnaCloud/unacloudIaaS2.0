package com.losandes.utils;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for sorting State variables to constans
 */
public class StringOperations {

    public static String getStateName(int numberState){
        String stateName ="";
        if(numberState == OFF_STATE)
            stateName = STATE_OFF;
        else if(numberState == ON_STATE)
            stateName = STATE_ON;
        else if(numberState == LOCK_STATE)
            stateName = STATE_LOCK;
        else
            stateName = NOTHING_AVAILABLE;
        return stateName;
    }

}
