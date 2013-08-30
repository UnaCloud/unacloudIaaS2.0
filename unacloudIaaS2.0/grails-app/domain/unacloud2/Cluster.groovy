package unacloud2

class Cluster {
	
	String name
	Date startTime
	Date stopTime
	static belongsTo = [template:Template]
	static constraints = {
    }

		
}
