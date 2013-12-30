package unacloud2;

public enum ServerVariableTypeEnum {
	INT("Integer"),STRING("String");
	String type; 
	private ServerVariableTypeEnum(String typeString){
		type=typeString;
	}
}
