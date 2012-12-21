package recommender.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import recommender.utils.RecommenderException;

public class WebUtil {
	
	/**
	 * Checks if a required field has some value
	 * @param errors Map with the errors of the controller
	 * @param field_key Name of the input field
	 * @param value Value of the field
	 * @return True if the field has some value
	 */
	public static boolean checkRequiredField(Map<String, List<String>> errors, String field_key, String value) {
		boolean has_value = true;
		
		if((value == null) || (value.trim().isEmpty())) {
			WebUtil.addFieldError(errors, field_key, RecommenderException.MSG_REQUIRED_FIELD);
			has_value = false;
		}
		
		return has_value;
	}
	
	
	
	
	/**
	 * Adds an error message to a field in the controller errors
	 * @param errors Map with the errors of the controller
	 * @param field_key Name of the input field
	 * @param new_error Text with the new error
	 */
	public static void addFieldError(Map<String, List<String>> errors, String field_key, String new_error) {
		List<String> field_errors = errors.get(field_key);
		if(field_errors == null) {
			field_errors = new ArrayList<String>();
		}
		
		field_errors.add(new_error);
		
		errors.put(field_key, field_errors);
	}
	
	
	
	
	/**
	 * Gets a request parameter and parses it into Integer
	 * @param request Servlet Request 
	 * @param parameter_name Parameter Name
	 * @return Integer parameter value
	 */
	public static Integer getIntegerParameter(HttpServletRequest request, String parameter_name) {
		Integer result = null;
		String value = request.getParameter(parameter_name);
		
		if(value != null) {
			try
			{
				result = Integer.parseInt(value);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Gets a request parameter and parses it into Long
	 * @param request Servlet Request 
	 * @param parameter_name Parameter Name
	 * @return Long parameter value
	 */
	public static Long getLongParameter(HttpServletRequest request, String parameter_name) {
		Long result = null;
		String value = request.getParameter(parameter_name);
		
		if(value != null) {
			try
			{
				result = Long.parseLong(value);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return result;
	}
}
