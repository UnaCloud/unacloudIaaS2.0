package unacloud2

class IP {

    String ip
	boolean used
	String mask
	
	static transients = ['mask']
	static constraints = {
		
    }
}
