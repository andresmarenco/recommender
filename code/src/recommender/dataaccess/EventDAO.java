package recommender.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import recommender.beans.IREventType;
import recommender.beans.IRStory;
import recommender.beans.IRStoryViewType;
import recommender.beans.IRUser;
import recommender.utils.DBUtil;
import recommender.utils.RecommenderException;

public class EventDAO {
	
	/** Connection Manager */
	private transient ConnectionManager _ConnectionManager;
	
	
	/**
	 * Default Constructor
	 */
	public EventDAO() {
		this._ConnectionManager = ConnectionManager.getInstance();
	}
	
	
	
	
	/**
	 * Logs an Event
	 * @param user User who triggered the event or null
	 * @param event_type Name of the executed event
	 * @return Id of the generated event
	 * @throws RecommenderException
	 */
	public Long logEvent(IRUser user, String event_type) throws RecommenderException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long result = null;
		
		try
		{
			Timestamp now = new Timestamp(System.currentTimeMillis());
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement("insert into ir_event_log (TriggeredDate, UserId, EventTypeId) values (?,?,(select et.id from ir_event_type as et where et.name = ?))", Statement.RETURN_GENERATED_KEYS);
			
			stmt.setTimestamp(1, now);
			
			if((user != null) && (user.getId() != Long.MIN_VALUE)) {
				stmt.setLong(2, user.getId());
			} else {
				stmt.setNull(2, Types.INTEGER);
			}
			
			stmt.setString(3, event_type);
			stmt.executeUpdate();
			
			rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				result = new Long(rs.getLong(1));
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
		
		return result;
	}
	
	
	
	
	/**
	 * Logs the user's viewed story
	 * @param user Current User
	 * @param story Current Story
	 * @param viewType How the user finds the story
	 * @throws RecommenderException
	 */
	public void logViewedStory(IRUser user, IRStory story, IRStoryViewType viewType) throws RecommenderException {
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement("insert into ir_story_view_log (Id, StoryId, ViewTypeId) values (?,?,?)");
			stmt.setLong(1, this.logEvent(user, IREventType.EVENT_VIEW_STORY));
			stmt.setLong(2, story.getId());
			
			if(viewType != null) {
				stmt.setLong(3, viewType.getId());
			} else {
				stmt.setNull(3, Types.INTEGER);
			}
			
			stmt.execute();
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			throw new RecommenderException(RecommenderException.MSG_ERROR_LOGGING_EVENT);
		}
		finally {
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
	}
	
	
	
	
	/**
	 * Loads the Story View Type
	 * @param id Id of the View Type
	 * @return Story View Type Object or null
	 * @throws RecommenderException
	 */
	public IRStoryViewType loadViewType(long id) throws RecommenderException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IRStoryViewType result = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement("select id, name from ir_story_view_type where id = ?");
			stmt.setLong(1, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				result = new IRStoryViewType(rs.getLong("id"), rs.getString("name"));
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			throw new RecommenderException(RecommenderException.MSG_ERROR_LOGGING_EVENT);
		}
		finally {
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Loads the Story View Type
	 * @param name Name of the View Type
	 * @return Story View Type Object or null
	 * @throws RecommenderException
	 */
	public IRStoryViewType loadViewType(String name) throws RecommenderException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IRStoryViewType result = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement("select id, name from ir_story_view_type where name = ?");
			stmt.setString(1, name);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				result = new IRStoryViewType(rs.getLong("id"), rs.getString("name"));
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			throw new RecommenderException(RecommenderException.MSG_ERROR_LOGGING_EVENT);
		}
		finally {
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Lists all the available Story View Types in the Database
	 * @return List with Story View Types
	 */
	public List<IRStoryViewType> listViewTypes() {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IRStoryViewType> result = null;
		
		try
		{
			result = new ArrayList<IRStoryViewType>();
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement("select id, name from ir_story_view_type order by id");
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				result.add(new IRStoryViewType(rs.getLong("id"), rs.getString("name")));
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		finally {
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
}
