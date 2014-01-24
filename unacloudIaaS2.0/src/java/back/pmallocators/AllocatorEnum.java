package back.pmallocators;

public enum AllocatorEnum {
	RANDOM(new RandomAllocator()),ROUND_ROBIN(new RoundRobinAllocator()),GREEN(null);
	VirtualMachineAllocatorInterface allocator;
	private AllocatorEnum(VirtualMachineAllocatorInterface allocator) {
		this.allocator=allocator;
	}
	public VirtualMachineAllocatorInterface getAllocator() {
		return allocator;
	}
}
