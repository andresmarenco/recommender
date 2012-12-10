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
	public static final String MSG_ERROR_USERNAME_POLICY = "error.username_policy";
	public static final String MSG_ERROR_PASSWORD_POLICY = "error.password_policy";
	public static final String MSG_ERROR_EMAIL_POLICY = "error.email_policy";
	public static final String MSG_UNKNOWN_USER = "error.unknown_user";
	public static final String MSG_INVALID_PASSWORD = "error.invalid_password";
	public static final String MSG_INACTIVE_USER = "error.inactive_user";
	public static final String MSG_UNKNOWN_ERROR = "error.unknown";

}
