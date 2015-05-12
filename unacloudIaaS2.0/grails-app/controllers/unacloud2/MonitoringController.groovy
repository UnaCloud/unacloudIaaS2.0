package unacloud2

import java.text.SimpleDateFormat;
import java.util.Date;

import javassist.bytecode.stackmap.BasicBlock.Catch;

class MonitoringController {
	
	/**
	 * Representation of monitoring services
	 */
	MonitoringService monitoringService
	
	def beforeInterceptor = {
		if(!session.user){
			flash.message="You must log in first"
			redirect(uri:"/login", absolute:true)
			return false
		}
		else if(!(session.user.userType.equals("Administrator"))){
			flash.message="You must be administrator to see this content"
			redirect(uri:"/error", absolute:true)
			return false
		}
	}
	
	def index(){
		def pm = PhysicalMachine.get(params.id);
		def monitor = monitoringService.getMetricsCPU(pm.getName())
		//def machineSet= lab.getOrderedMachines()
		[machine: pm, components:monitor, lab:pm.laboratory.id]
	}	
	
	def getReports(){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS aa");
			Date init = dateFormat.parse(params.year+'-'+(params.month.length()==1?'0'+params.month:params.month)+'-'+(params.day.length()==1?'0'+params.day:params.day)+' '+params.hour+':00:00.000 '+params.sched);
			String host = params.host;
			Date end = new Date(init.getTime()+(1000*60*60*Integer.parseInt(params.range)));
			boolean type = params.report.equals("cpu")?false:true;
			def file = monitoringService.generateReport(host, init, end,type);
			response.setContentType("application/octet-stream")
			response.setHeader("Content-disposition", "attachment;filename="+file.getName())			
			response.outputStream << file.newInputStream()
			
		}catch(Exception e){
			e.printStackTrace();
			flash.message="There is an error with file, check logs for more information"
			redirect(uri:"/error", absolute:true)
			return false
		}
	}
}
