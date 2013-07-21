package unacloud2

class Laboratory {

    String name
	boolean highAvaliability
	String networkQuality
	static hasMany = [physicalMachines: PhysicalMachine]
	
	static constraints = {
      
	}
	
	ArrayList <PhysicalMachine> getOrderedMachines(){
		PhysicalMachineComparator c= new PhysicalMachineComparator()
		ArrayList <PhysicalMachine> array = new ArrayList(physicalMachines).sort(c)
		return array
	}
	
}
