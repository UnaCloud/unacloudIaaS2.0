package unacloud2

class Template {

	String name
	int maxInstancesPerCluster
	boolean highAvaliability
	boolean persistant
	boolean rewritable
	boolean customizable
	int maxRAM
	int maxCores
	VirtualMachineImage image
	TemplateDeploymentPolicy templateDepPolicy
	static belongsTo = [user:User]
	Collection <LocalConfigurationRule> localConfRules
	static constraints = {
    }
	
}
