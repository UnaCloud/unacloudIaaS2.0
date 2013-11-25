package unacloud2

class IP {

    String ip
	boolean used
	static belongsTo = [ipPool: IPPool]
	
	static constraints = {
		
    }
}
