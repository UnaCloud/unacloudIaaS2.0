package unacloud2

class TemplateDeploymentPolicy {
	
	int instancesToDeploy
	int ram
	int cores
	boolean green
	boolean persistant
	boolean preferSameIpPool
	
    static constraints = {
    }
}
