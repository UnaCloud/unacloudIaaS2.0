package unacloud2

class ExternalCloudProvider {
	
	String name
	
	String endpoint
	
	ExternalCloudTypeEnum type
	
	static hasMany = [hardwareProfiles: HardwareProfile]
		
    static constraints = {
    }
}
