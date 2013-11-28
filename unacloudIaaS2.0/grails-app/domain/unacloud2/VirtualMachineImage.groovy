package unacloud2

class VirtualMachineImage implements Comparable<VirtualMachineImage>{
	
	String name
    boolean avaliable
	boolean isPublic
	long fixedDiskSize
	String user
	String password
	OperatingSystem operatingSystem
	String accessProtocol
	String mainFile
	
	static hasMany = [files:File ]
	
	static constraints = {
    }
	
	def isDeployed(){
		boolean isDeployed=false
		def deployments= Deployment.findByStatusNotEqual(DeploymentStateEnum.FINISHED)
		deployments.each (){
			it.cluster.images.each(){
				if(it.image==this)
					isDeployed=true
			}
		}
		return isDeployed
	}

	@Override
	public int compareTo(VirtualMachineImage o) {
		return Long.compare(id,o.id);
	}
}	
