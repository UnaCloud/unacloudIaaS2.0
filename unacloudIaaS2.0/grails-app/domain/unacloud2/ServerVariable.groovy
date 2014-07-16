package unacloud2

class ServerVariable {
	
	/**
	 * Server variable name
	 */
	String name
	
	/**
	 * Server variable data
	 */
	String variable
	
	/**
	 * type of variable (String, Integer)
	 */
	ServerVariableTypeEnum serverVariableType
	
	/**
	 * server side variable only
	 */
	boolean serverOnly
	
    static constraints = {
    }
}
