package recommender.utils;

public class RecommenderException extends Exception {

	private static final long serialVersionUID = 201211301549L;
	
	public RecommenderException() {
		super();
	}
	
	public RecommenderException(String message) {
		super(message);
	}
	
	public static final String MSG_ERROR_CRYPTO = "error.crypto";
	public static final String MSG_ERROR_EXISTING_USERNAME = "error.existing_username";
	public static final String MSG_ERROR_USERNAME_POLICY = "error.username_policy";
	public static final String MSG_ERROR_PASSWORD_POLICY = "error.password_policy";
	public static final String MSG_ERROR_CONFIRM_PASSWORD = "error.confirm_password";
	public static final String MSG_ERROR_EMAIL_POLICY = "error.email_policy";
	public static final String MSG_ERROR_LOGGING_EVENT = "error.logging_event";
	public static final String MSG_REQUIRED_FIELD = "error.required_field";
	public static final String MSG_UNKNOWN_USER = "error.unknown_user";
	public static final String MSG_INVALID_PASSWORD = "error.invalid_password";
	public static final String MSG_INACTIVE_USER = "error.inactive_user";
	public static final String MSG_ERROR_TERRIER_RETRIEVAL = "error.terrier.retrieval";
	public static final String MSG_ERROR_TERRIER_CONVERSION = "error.terrier.conversion";
	public static final String MSG_UNKNOWN_ERROR = "error.unknown";

}
