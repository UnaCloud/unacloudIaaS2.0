package unacloud2

import java.util.concurrent.TimeUnit
import java.text.DateFormat
import java.text.SimpleDateFormat

class VirtualMachineExecution {

    String name
	int cores	
	int ram
	IP ip
	Date startTime
	Date stopTime
	VirtualMachineExecutionStateEnum status
	String message
	PhysicalMachine executionNode
	
	static constraints = {
		executionNode nullable: true
		ip nullable: true
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
