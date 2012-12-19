package recommender.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	
	/**
	 * Closes a Database Connection
	 * @param connection Database Connection
	 */
	public static void closeConnection(Connection connection) {
		try
		{
			if(connection != null) {
				connection.close();
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Closes a Database Statement
	 * @param stmt Database Statement
	 */
	public static void closeStatement(Statement stmt) {
		try
		{
			if(stmt != null) {
				stmt.close();
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Closes a Database ResultSet
	 * @param rs Database ResultSet
	 */
	public static void closeResultSet(ResultSet rs) {
		try
		{
			if(rs != null) {
				rs.close();
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Defines if a connection auto commits
	 * @param connection Connection to set
	 * @param autoCommit True if auto commiting
	 */
	public static void setAutoCommit(Connection connection, boolean autoCommit) {
		try
		{
			if(connection != null) {
				connection.setAutoCommit(autoCommit);
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Rollbacks the statements of the connection
	 * @param connection Connection to rollback
	 */
	public static void rollback(Connection connection) {
		try
		{
			if(connection != null) {
				connection.rollback();
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}

}
