package unacloud2

class IPPool {
	
	boolean virtual 
	String gateway
	String mask
	static hasMany = [ips: IP]
    static constraints = {
    }
}
