package back.pmallocators

import back.services.PhysicalMachineStateManagerService;

import java.util.Comparator;

import javassist.bytecode.stackmap.BasicBlock.Catch;

import org.hibernate.collection.PersistentSet

import pmStateManager.StateManager;
import groovy.sql.Sql;
import unacloud2.*
import unacloudEnums.VirtualMachineExecutionStateEnum;

class PhysicalMachineAllocatorService {
	javax.sql.DataSource dataSource
	def allocatePhysicalMachines(DeployedCluster cluster, boolean addInstancesDeployment){
		ArrayList<VirtualMachineExecution> vms=new ArrayList<>();
		List<PhysicalMachine> pms=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		//pms=StateManager.filterPhysicalMachines(pms);
		Map<Long,PhysicalMachineAllocationDescription> pmDescriptions = getPhysicalMachineUsage()
		if(!addInstancesDeployment)
			for(DeployedImage image:cluster.images)vms.addAll(image.virtualMachines);
		else{
			println "adding only new instances for allocation"
			for(DeployedImage image:cluster.images){
				
				def virtualMachines= new ArrayList<>()
				for(VirtualMachineExecution vm:image.virtualMachines)
				if(vm.message.equals("Adding instance")){
					virtualMachines.add(vm)}
				vms.addAll(virtualMachines)
			}
		}
		println vms
		ServerVariable allocatorName=ServerVariable.findByName("VM_ALLOCATOR_NAME");
		AllocatorEnum allocator=AllocatorEnum.ROUND_ROBIN;
		try{
			if(allocatorName!=null){
				AllocatorEnum allocEnum=AllocatorEnum.valueOf(allocatorName.getVariable());
				if(allocEnum!=null)allocator=allocEnum;
			}
		}catch(Exception ex){
		}

		allocator.getAllocator().allocateVirtualMachines(vms,pms,pmDescriptions);
	}
	def allocatePhysicalMachine(VirtualMachineExecution vme ){
		List<PhysicalMachine> l=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		Collections.sort(l,new Comparator<PhysicalMachine>(){
					public int compare(PhysicalMachine p1,PhysicalMachine p2){
						return Long.compare(p1.id,p2.id);
					}
				});
		vme.executionNode = l.first();
	}
	def getPhysicalMachineUsage(){
		def sql = new Sql(dataSource)
		Map<Long,PhysicalMachineAllocationDescription> pmDescriptions=new TreeMap<>();
		sql.eachRow('select execution_node_id,count(*) as vms,sum(ram) as ram,sum(cores) as cores from virtual_machine_execution where status != \'FINISHED\' group by execution_node_id'){ row ->
			if(row.execution_node_id!=null)pmDescriptions.put(row.execution_node_id, new PhysicalMachineAllocationDescription(row.execution_node_id,row.ram.toInteger(),row.cores.toInteger(),row.vms.toInteger()));
		}
		return pmDescriptions;
	}
}
