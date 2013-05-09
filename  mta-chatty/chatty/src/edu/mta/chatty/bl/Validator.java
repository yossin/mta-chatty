package edu.mta.chatty.bl;

import java.util.regex.Pattern;


public class Validator {
	public static void validateNotEmpty(String property, String propertyName){
		if (property == null || property.trim().equals("")){
			String message = String.format("please provide %s", propertyName);
			throw new IllegalArgumentException(message);
		}
	}
	public static void validateEmail(String email, String propertyName){
		validateNotEmpty(email, propertyName);
		String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		if (Pattern.matches(regex, email) == false){
			String message = String.format("please provide a valid email address in field %s", propertyName);
			throw new IllegalArgumentException(message);
		}
		
	}
}
