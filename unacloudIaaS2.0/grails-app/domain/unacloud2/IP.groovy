package unacloud2

class IP {

    String ip
	boolean used
	IPPool ipPool
	
	static constraints = {
		ipPool nullable:true 	
    }
}
