package recommender.utils;

import javax.naming.InitialContext;

public class ConfigUtil {

	/**
	 * Finds a value in the JNDI Initial Context of the Application
	 * @param key Key of the value
	 * @param clazz Type of the value
	 * @return Value or null
	 */
	public static <T> T getContextParameter(String key, Class<T> clazz) {
		Object result = null;
		
		try
		{
			InitialContext context = new InitialContext();
			result = context.lookup("java:/comp/env/" + key);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		return clazz.cast(result);
	}
}
