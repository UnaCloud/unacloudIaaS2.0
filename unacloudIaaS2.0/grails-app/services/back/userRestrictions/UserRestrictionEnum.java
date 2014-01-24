package back.userRestrictions;

public enum UserRestrictionEnum {
	MAX_RAM_PER_VM("Max RAM per VM"),MAX_CORES_PER_VM("Max cores per VM"),ALLOWED_LABS("Allowed Labs");
	String name;

	private UserRestrictionEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	
}
