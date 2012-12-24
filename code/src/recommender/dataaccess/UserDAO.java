package recommender.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import org.apache.commons.codec.binary.Hex;

import recommender.beans.IREventType;
import recommender.beans.IRUser;
import recommender.utils.CryptoUtil;
import recommender.utils.DBUtil;
import recommender.utils.RecommenderException;
import recommender.utils.ValidationUtil;

public class UserDAO {
	
	/** Connection Manager */
	private transient ConnectionManager _ConnectionManager;
	
	
	/**
	 * Default Constructor
	 */
	public UserDAO() {
		super();
		_ConnectionManager = ConnectionManager.getInstance();
	}
	
	
	
	
	/**
	 * Logins the user into the system
	 * @param username Username
	 * @param password Password
	 * @return Credential of the logged User
	 * @throws RecommenderException
	 */
	public IRUser login(String username, String password) throws RecommenderException {
		IRUser credential = null;
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			final String query = "select id, password, last_login, active from ir_user where username = ?";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, username.trim().toLowerCase());
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				
				if(rs.getBoolean("active")) {
					String stored_password = rs.getString("password");
					String salt = stored_password.substring(stored_password.length() - 4);
					String check_password = password + salt;
					
					if(stored_password.equalsIgnoreCase(Hex.encodeHexString(CryptoUtil.digestToBinary(check_password)) + salt)) {
						final String updateTimestampQuery = "update ir_user set last_login = ? where id = ?";
						Timestamp now = new Timestamp(System.currentTimeMillis());
						long userId = rs.getLong("id");
						
						PreparedStatement updateStmt = connection.prepareStatement(updateTimestampQuery);
						updateStmt.setTimestamp(1, now);
						updateStmt.setLong(2, userId);
						updateStmt.execute();
						
						DBUtil.closeStatement(updateStmt);
						
						credential = this.loadUser(userId);
			
						// Log the event
						EventDAO eventDAO = new EventDAO();
						eventDAO.logEvent(credential, IREventType.EVENT_LOGIN);
						
					} else {
						throw new RecommenderException(RecommenderException.MSG_INVALID_PASSWORD);
					}
					
					
				} else {
					throw new RecommenderException(RecommenderException.MSG_INACTIVE_USER);
				}
				
			} else {
				throw new RecommenderException(RecommenderException.MSG_UNKNOWN_USER);
			}
			
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			throw new RecommenderException(ex.getMessage());
		}
		finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return credential;
	}
	
	
	
	
	/**
	 * Loads an User based on its Id
	 * @param id User's Id
	 * @return User Object or null
	 * @throws SQLException
	 */
	public IRUser loadUser(long id) throws SQLException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IRUser result = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement("select * from ir_user where id = ?");
			stmt.setLong(1, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				result = new IRUser();
				result.setId(rs.getLong("id"));
				result.setUsername(rs.getString("username"));
				result.setName(rs.getString("name"));
				result.setLast_login(rs.getTimestamp("last_login"));
				result.setActive(rs.getBoolean("active"));
			} else {
				throw new SQLException("Record not found");
			}
		}
		finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Creates a new user into the system
	 * @param user New user data
	 * @param login Logins the new user into the system
	 * @return User with the generated id
	 * @throws RecommenderException
	 */
	public IRUser createUser(IRUser user, boolean login) throws RecommenderException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			if(!ValidationUtil.validateUsername(user.getUsername())) {
				throw new RecommenderException(RecommenderException.MSG_ERROR_USERNAME_POLICY);
			}
			
			if(!ValidationUtil.validatePassword(user.getPassword())) {
				throw new RecommenderException(RecommenderException.MSG_ERROR_PASSWORD_POLICY);
			}
			
			if(ValidationUtil.existingUsername(user.getUsername())) {
				throw new RecommenderException(RecommenderException.MSG_ERROR_EXISTING_USERNAME);
			}
			
			Timestamp now = (login) ? new Timestamp(System.currentTimeMillis()) : null;
			connection = _ConnectionManager.getConnection();
			DBUtil.setAutoCommit(connection, false);
			stmt = connection.prepareStatement("insert into ir_user (username, password, name, last_login, active) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, CryptoUtil.encyptPassword(user.getPassword()));
			stmt.setString(3, user.getName());
			
			if(login) {
				stmt.setTimestamp(4, now); 
			} else {
				stmt.setNull(4, Types.TIMESTAMP);
			}
			
			stmt.setBoolean(5, true);
			stmt.executeUpdate();
			
			rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				user = this.loadUser(rs.getLong(1));
				
				// Log the events
				EventDAO eventDAO = new EventDAO();
				eventDAO.logEvent(user, IREventType.EVENT_SIGNUP);
				
				if(login) {
					eventDAO.logEvent(user, IREventType.EVENT_LOGIN);
				}
			}
			
			connection.commit();
		}
		catch(SQLException ex) {
			DBUtil.rollback(connection);
			
			ex.printStackTrace();
			throw new RecommenderException(ex.getMessage());
		}
		finally {
			DBUtil.setAutoCommit(connection, true);
			
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		
		return user;
	}
	
	
	
	
	/**
	 * Logs out a User from the system
	 * @param user User to log out
	 * @throws RecommenderException
	 */
	public void logout(IRUser user) throws RecommenderException {
		if((user != null) && (user.getId() != Long.MIN_VALUE)) {
			EventDAO eventDAO = new EventDAO();
			eventDAO.logEvent(user, IREventType.EVENT_LOGOUT);
		}
	}
}
