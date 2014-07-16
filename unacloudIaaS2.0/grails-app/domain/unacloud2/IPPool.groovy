package unacloud2

class IPPool {
	
	/**
	 * Indicates if the IPs in this IPPool are public or private
	 */
	boolean virtual 
	
	/**
	 * Common gateway to any IP in this pool
	 */
	String gateway
	
	/**
	 * common mask to any IP in this pool
	 */
	String mask
	
	/**
	 * list of IPs
	 */
	static hasMany = [ips: IP]
	
    static constraints = {
    }
}
