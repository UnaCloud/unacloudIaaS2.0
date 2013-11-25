package back.deployers

import unacloud2.*;

class TransferService {

    def transferVMs(DeployedCluster cluster) {
		for(image in cluster.images) {
			for(vm in image.virtualMachines){
					if(vm.status ==MachineState.COPYING){
							vm.status = MachineState.CONFIGURING
							vm.save()
					}
			}
		}
    }
}
