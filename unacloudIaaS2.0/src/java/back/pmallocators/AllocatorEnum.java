package back.pmallocators;

public enum AllocatorEnum {
	RANDOM(new RandomAllocator(),"Random"),ROUND_ROBIN(new RoundRobinAllocator(),"Round Robin"),GREEN(null,"Green");
	VirtualMachineAllocatorInterface allocator;
	String name;
	
	private AllocatorEnum(VirtualMachineAllocatorInterface allocator, String name) {
		this.allocator=allocator;
		this.name=name;
	}
	public VirtualMachineAllocatorInterface getAllocator() {
		return allocator;
	}
	
	public String getName(){
		return name;
	}
}
