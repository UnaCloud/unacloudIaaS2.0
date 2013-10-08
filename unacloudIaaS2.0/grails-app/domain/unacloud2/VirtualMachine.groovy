package unacloud2

import java.util.concurrent.TimeUnit
import java.text.DateFormat
import java.text.SimpleDateFormat

class VirtualMachine {

    String name
	int cores	
	int disk
	int ram
	IP ip
	Date startTime
	Date stopTime
	int status
	
	
	static COPYING=1
	static CONFIGURING=2
	static DEPLOYING=3
	static DEPLOYED=4
	static FAILED=5
	static FINISHED=6
		
	static constraints = {
    }
	
	def remainingTime(){
		long millisTime=(stopTime.getTime()-System.currentTimeMillis())/1000
		String s = ""+millisTime%60;
        String m = ""+((long)(millisTime/60))%60;
        String h = ""+((long)(millisTime/60/60));
        if(s.length()==1)s="0"+s;
        if(m.length()==1)m="0"+m;
        return h+"h:"+m+"m:"+s+"s"
	}
}
