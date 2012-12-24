package recommender.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import recommender.beans.IRKeyword;
import recommender.beans.IRStory;
import recommender.beans.IRStoryStats;
import recommender.beans.IRSubgenre;
import recommender.utils.DBUtil;

public class StoryDAO {
	
	/** Connection Manager */
	private transient ConnectionManager _ConnectionManager;
	
	
	/**
	 * Default Constructor
	 */
	public StoryDAO() {
		this._ConnectionManager = ConnectionManager.getInstance();
	}
	
	
	
	/**
	 * Loads a Story based on its Id
	 * @param id Id of the Story
	 * @param subfields True if only return a substring of the fields
	 * @return Story Object or null
	 */
	public IRStory loadStory(long id, boolean subfields) throws SQLException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IRStory result = null;
		
		try
		{
			String query = "select s.*, " + (subfields ? " substring(c.`Text`, 1, 140) as text " : " c.`Text` " ) + " from story as s inner join content as c on c.id = s.contentId where s.id = ?";
			
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement(query);
			stmt.setLong(1, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				result = this.loadStory(rs);
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Loads a Story based on its code
	 * @param code Code of the Story
	 * @param subfields True if only return a substring of the fields
	 * @return Story Object or null
	 */
	public IRStory loadStory(String code, boolean subfields) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IRStory result = null;
		
		try
		{
			String query = "select s.*, " + (subfields ? " concat(substring(c.`Text`, 1, 140), '...') as text " : " c.`Text` " ) + " from story as s inner join content as c on c.id = s.contentId where s.storyCode = ?";
			
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement(query);
			stmt.setString(1, code);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				result = this.loadStory(rs);
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Maps the story ResultSet into an IRStory Object
	 * @param rs ResultSet with the Story
	 * @return Story Object
	 * @throws SQLException
	 */
	private IRStory loadStory(ResultSet rs) throws SQLException {
		IRStory result = new IRStory();
		
		result.setId(rs.getLong("id"));
		result.setCode(rs.getString("storyCode"));
		result.setTitle(rs.getString("title"));
		result.setText(rs.getString("text"));
		
		// TODO: complete!!!
		
		return result;
	}
	
	
	
	
	/**
	 * Loads a Subgenre base on its Id
	 * @param id Id of the Subgenre
	 * @return Subgenre Object or null
	 */
	public IRSubgenre loadSubgenre(long id) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IRSubgenre result = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement("select s.id, s.name, (select count(1) from story as st where st.subgenreId = s.id) as total from subgenre as s where s.id=?");
			stmt.setLong(1, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				result = new IRSubgenre(rs.getLong("id"), rs.getString("name"), rs.getInt("total"));
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Lists all the stories with a specific subgenre
	 * @param subgenre Subgenre of the story
	 * @param limit Limit of results
	 * @param offset Offset for the first result
	 * @return List of stories
	 */
	public List<IRStory> listStories(IRSubgenre subgenre, Integer limit, Integer offset, StoriesOrder order) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IRStory> result = null;
		
		try
		{
			result = new ArrayList<IRStory>();
			connection = _ConnectionManager.getConnection();
			//StringBuilder query = new StringBuilder("select s.* from irv_story_stats as s where s.subgenreId = ? ");
			StringBuilder query = new StringBuilder("select s.*, concat(substring(c.`Text`, 1, 140), '...') as text, (select count(1) from `ir_story_view_log` `sl` where (`sl`.`StoryId` = `s`.`Id`)) AS `views` from story as s inner join content as c on c.id = s.contentId where s.subgenreId = ? ");
			
			switch(order) {
			case MOST_VIEWED: {
				query.append(" order by views desc, s.id asc ");
				break;
			}
			
			case ALPHABETICALLY:
			default: {
				query.append(" order by s.id ");
				break;
			}
			}
			
			if((limit == null) && (offset == null)) {
				stmt = connection.prepareStatement(query.toString());
			} else if(offset == null) {
				stmt = connection.prepareStatement(query.append(" LIMIT ?").toString());
				stmt.setLong(2, limit.intValue());
			} else if(limit == null) {
				stmt = connection.prepareStatement(query.append(" LIMIT ?,?").toString());
				stmt.setLong(2, offset.intValue());
				stmt.setLong(3, Integer.MAX_VALUE);
			} else {
				stmt = connection.prepareStatement(query.append(" LIMIT ?,?").toString());
				stmt.setLong(2, offset.intValue());
				stmt.setLong(3, limit.intValue());
			}
			
			stmt.setLong(1, subgenre.getId());
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				IRStory story = this.loadStory(rs);
				story.setStatistics(new IRStoryStats(rs.getLong("views")));
				result.add(story);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Lists all the subgenres of a story (or all if the story is null)
	 * @param story Story to find the subgenres (or null)
	 * @return List of subgenres
	 */
	public List<IRSubgenre> listSubgenres(IRStory story) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IRSubgenre> result = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			
			if(story == null) {
				//stmt = connection.prepareStatement("select id, name from subgenre");
				stmt = connection.prepareStatement("select s.id, s.name, (select count(1) from story as st where st.subgenreId = s.id) as total from subgenre as s order by total desc, name asc");
			} else {
				// TODO: Implement this!!!!!
			}
			
			rs = stmt.executeQuery();
			result = new ArrayList<IRSubgenre>();
			
			while(rs.next()) {
				result.add(new IRSubgenre(rs.getLong("id"), rs.getString("name"), rs.getInt("total")));
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Lists all the keywords of a story (or all if the story is null)
	 * @param story Story to find the keywords (or null)
	 * @return List of keywords
	 */
	public List<IRKeyword> listKeywords(IRStory story) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IRKeyword> result = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			
			if(story == null) {
				stmt = connection.prepareStatement("select id, name from keyword");
			} else {
				stmt = connection.prepareStatement("select k.id, k.name from keyword as k inner join storykeywords as sk on k.id = sk.keywordId where sk.storyId = ?");
				stmt.setLong(1, story.getId());
			}
			
			rs = stmt.executeQuery();
			result = new ArrayList<IRKeyword>();
			
			while(rs.next()) {
				result.add(new IRKeyword(rs.getLong("id"), rs.getString("name")));
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Enumeration for the expected order of stories
	 * @author andres
	 *
	 */
	public static enum StoriesOrder {
		MOST_VIEWED,
		BEST_RANKED,
		ALPHABETICALLY
	}
}
