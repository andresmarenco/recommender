package recommender.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import recommender.dataaccess.ConnectionManager;

public class ValidationUtil {
	
	/**
	 * Username Policy:
	 * - Starts with letter
	 * - Only letter, numbers, underscore and dot
	 */
	private static final String REGEX_USERNAME_POLICY = "^[A-Za-z](?=[A-Za-z0-9_.])[a-zA-Z0-9_]*\\.?[a-zA-Z0-9_]*$";
	
	/**
	 * Password Policy:
	 * - 7 to 20 characters
	 * - At least one letter
	 * - At least one number
	 */
	private static final String REGEX_PASSWORD_POLICY = "^(?=.*\\d)(?=.*[a-zA-Z])(?!.*\\s).{7,20}$";
	
	/**
	 * Email Policy
	 */
	private static final String REGEX_EMAIL_POLICY = "^[\\w-]+(\\.[\\w-]+)*@(([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?|localhost)$";
	
	
	
	
	/**
	 * Checks if a String is a valid username according to the policy
	 * @param username String to check
	 * @return true if it is a valid username
	 */
	public static boolean validateUsername(String username) {
		return ((username != null) && (username.matches(REGEX_USERNAME_POLICY)));
	}
	
	
	
	
	/**
	 * Checks if a String is a valid password according to the policy
	 * @param password String to check
	 * @return true if it is a valid password
	 */
	public static boolean validatePassword(String password) {
		return ((password != null) && (password.matches(REGEX_PASSWORD_POLICY)));
	}
	
	
	
	
	/**
	 * Checks if a String is a valid email according to the policy
	 * @param email String to check
	 * @return true if it is a valid email
	 */
	public static boolean validateEmail(String email) {
		return ((email != null) && (email.matches(REGEX_EMAIL_POLICY)));
	}
	
	
	
	
	/**
	 * Checks if a username already exists
	 * @param username Username to check
	 * @return True if the username exists
	 * @throws RecommenderException
	 */
	public static boolean existingUsername(String username) throws RecommenderException {
		boolean result = false;
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try
		{
			connection = ConnectionManager.getInstance().getConnection();
			stmt = connection.prepareStatement("select 1 from ir_user where username=?");
			stmt.setString(1, username);
			
			result = stmt.executeQuery().next();
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			throw new RecommenderException(ex.getMessage());
		}
		finally {
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
}
