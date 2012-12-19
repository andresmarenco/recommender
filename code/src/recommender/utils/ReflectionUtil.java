package recommender.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

	/**
	 * Tries to invoke a class method
	 * @param clazz Class where the method is defined 
	 * @param instance Instance of the class where the method is called
	 * @param methodName Name of the method
	 * @param parameters Key-Value Pairs of parameter types and values of the method
	 * @throws InvocationTargetException
	 */
	@SafeVarargs
	public static void invokeMethodIfExists(Class<?> clazz, Object instance, String methodName, KeyValuePair<Class<?>, Object>... parameters) throws InvocationTargetException {
		try
		{
			Class<?>[] paramTypes = null;
			Object[] paramValues = null;
			
			if((parameters != null) && (parameters.length > 0)) {
				paramTypes = new Class<?>[parameters.length];
				paramValues = new Object[parameters.length];
				
				for(int i = 0; i < parameters.length; i++) {
					paramTypes[i] = parameters[i].getKey();
					paramValues[i] = parameters[i].getValue();
				}
			}
			
			
			Method method = clazz.getDeclaredMethod(methodName, paramTypes);
			method.setAccessible(true);
			method.invoke(instance, paramValues);
			
		}
		catch(NoSuchMethodException ex) {
			// Ignore
		}
		catch(InvocationTargetException ex) {
			throw ex;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
