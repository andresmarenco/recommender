package recommender.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import recommender.utils.RecommenderException;

public class WebCommon {
	
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
			WebCommon.addFieldError(errors, field_key, RecommenderException.MSG_REQUIRED_FIELD);
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
}
