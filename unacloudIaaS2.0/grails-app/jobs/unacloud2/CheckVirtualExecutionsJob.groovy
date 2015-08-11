package unacloud2



class CheckVirtualExecutionsJob {
    static triggers = {
      cron name:'cronCheckExecutions', startDelay:1000, cronExpression: '0 0/1 * 1/1 * ? *' //'0 0/2 * 1/1 * ? *' //
    }

    def execute() {
		try {
			Date current = new Date();
			long time = current.getTime()-60000*4;
			PhysicalMachine.executeUpdate('update VirtualMachineExecution pm set pm.status = \'FAILED\', pm.message = \'Execution does not respond\' where pm.status = \'DEPLOYED\' and :date1 > pm.reportTime',[date1:new Date(time)]);
		
		} catch (Exception e) {
			e.printStackTrace()
		}
    }
}
