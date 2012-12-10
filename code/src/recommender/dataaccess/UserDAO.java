package recommender.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.codec.binary.Hex;

import recommender.beans.IRStory;
import recommender.beans.IRUser;
import recommender.utils.CryptoUtil;
import recommender.utils.DBUtil;
import recommender.utils.RecommenderException;

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
	 * Logs the user's viewed story
	 * @param user Current User
	 * @param story Current Story
	 */
	public void logViewedStory(IRUser user, IRStory story) {
		// TODO: Are we going to log this?????
	}

}
