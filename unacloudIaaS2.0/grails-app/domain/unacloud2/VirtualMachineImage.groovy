package unacloud2

class VirtualMachineImage {
	
	String name
    //boolean avaliable
	boolean isPublic
	boolean customizable
	int fixedDiskSize
	String user
	String password
	String volume
	OperatingSystem operatingSystem
	String accessProtocol
	Date startTime
	Date stopTime
	
	static hasMany = [files:File ]
	
	static constraints = {
		
	
    }
	
	def isDeployed(){
		boolean isDeployed=false
		def deployments= Deployment.findByStatusNotEqual(Deployment.FINISHED)
		deployments.each (){
			it.cluster.images.each(){
				if(it.image==this)
					isDeployed=true
			}
		}
		return isDeployed
	}
}
