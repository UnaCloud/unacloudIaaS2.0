package unacloud2

import unacloud2.PhysicalMachine;



class CheckMachineJob {
    static triggers = {
      cron name:'cronCheckMachines', startDelay:1000, cronExpression: '0 0/2 * 1/1 * ? *' // execute job once in 5 seconds
    }

    def execute() {
       PhysicalMachine.executeUpdate('update PhysicalMachine pm set pm.state = \'OFF\', pm.withUser = 0 , pm.monitorStatus = \'OFF\' where pm.lastReport < current_date -4.minutes');
    }
}
