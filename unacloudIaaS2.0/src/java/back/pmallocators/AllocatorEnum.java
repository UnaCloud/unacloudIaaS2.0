package back.pmallocators;

public enum AllocatorEnum {
	RANDON("RandomAllocator"),ROUND_ROBIN("RoundRobinAllocator"),GREEN("");
	String className;
	private AllocatorEnum(String className) {
		this.className = className;
	}
}
