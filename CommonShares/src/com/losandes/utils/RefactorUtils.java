package com.losandes.utils;

public class RefactorUtils {
	
	public static String refactorString(String word){
		return word.replace("\" , \"",";").replaceAll("[^\\dA-Za-z:,;]", "");
	}
	
}
