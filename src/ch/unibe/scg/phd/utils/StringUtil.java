package ch.unibe.scg.phd.utils;

public class StringUtil {
	 
	public static boolean isEmpty(final Object target) { 
		 return Boolean.valueOf((target == null || target.toString().trim().equals(""))); 
	}

}
