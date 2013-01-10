package recommender.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import recommender.beans.IRFolktaleType;
import recommender.beans.IRKeyword;
import recommender.beans.IRLanguage;
import recommender.beans.IRRegion;
import recommender.beans.IRScriptSource;
import recommender.beans.IRStory;
import recommender.beans.IRStoryTeller;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRSubgenre;
import recommender.beans.IRUser;
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
	public IRStory loadStory(long id, boolean subfields) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IRStory result = null;
		
		try
		{
			String query = "select s.*, " + (subfields ? " substring(c.`Text`, 1, 140) as text " : " c.`Text` " ) + ", (select count(1) from `ir_story_view_log` `sl` where (`sl`.`StoryId` = `s`.`Id`)) AS `views` from story as s inner join content as c on c.id = s.contentId where s.id = ?";
			
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
			String query = "select s.*, " + (subfields ? " concat(substring(c.`Text`, 1, 140), '...') as text " : " c.`Text` " ) + ", (select count(1) from `ir_story_view_log` `sl` where (`sl`.`StoryId` = `s`.`Id`)) AS `views` from story as s inner join content as c on c.id = s.contentId where s.storyCode = ?";
			
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
	public IRStory loadStory(ResultSet rs) throws SQLException {
		IRStory result = new IRStory();
		
		result.setId(rs.getLong("id"));
		result.setCode(rs.getString("storyCode"));
		result.setTitle(rs.getString("title"));
		result.setText(rs.getString("text"));
		result.setViews(rs.getLong("views"));
		
		return result;
	}
	
	
	
	
	/**
	 * Completes all the fields of the story
	 * @param story Current story
	 * @return Completed story
	 */
	public IRStory loadAllFields(IRStory story) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		if(story.getId() != Long.MIN_VALUE) {
			try
			{
				story.setKeywords(this.listKeywords(story));
				connection = _ConnectionManager.getConnection();
				stmt = connection.prepareStatement("select s.*, l.name as LanguageName, ft.code as FolkTaleTypeCode, st.name as StoryTellerName, r.name as RegionName, sg.name as SubgenreName, ss.name as ScriptSourceName from story as s left outer join language as l on l.id = s.LanguageId left outer join folktaletype as ft on ft.id = s.FolkTaleTypeId left outer join storyteller as st on st.id = s.StoryTellerId left outer join region as r on r.id = s.RegionId left outer join subgenre as sg on sg.id = s.SubgenreId left outer join scriptsource as ss on ss.id = s.ScriptSourceId where s.id = ?");
				stmt.setLong(1, story.getId());
				
				rs = stmt.executeQuery();
				
				if(rs.next()) {
					story.setCopyright(new String("ja").equalsIgnoreCase(rs.getString("copyright")));
					story.setDateCreation(rs.getString("dateCreation"));
					story.setDateRecording(rs.getString("dateRecording"));
					story.setExtreme(new String("ja").equalsIgnoreCase(rs.getString("extreme")));
					story.setFolktaleType(new IRFolktaleType(rs.getLong("FolkTaleTypeId"), rs.getString("FolkTaleTypeCode")));
					story.setLanguage(new IRLanguage(rs.getLong("LanguageId"), rs.getString("LanguageName")));
					story.setRegion(new IRRegion(rs.getLong("RegionId"), rs.getString("RegionName")));
					story.setScriptSource(new IRScriptSource(rs.getLong("ScriptSourceId"), rs.getString("ScriptSourceName")));
					story.setStoryTeller(new IRStoryTeller(rs.getLong("StoryTellerId"), rs.getString("StoryTellerName")));
					story.setSubgenre(new IRSubgenre(rs.getLong("SubgenreId"), rs.getString("SubgenreName")));
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
		}
		return story;
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
	 * Lists all the stories
	 * @param limit Limit of results
	 * @param offset Offset for the first result
	 * @return List of stories
	 */
	public List<IRStory> listStories(Integer limit, Integer offset, StoriesOrder order) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IRStory> result = null;
		
		try
		{
			result = new ArrayList<IRStory>();
			connection = _ConnectionManager.getConnection();
			StringBuilder query = new StringBuilder("select s.*, concat(substring(c.`Text`, 1, 140), '...') as text, (select count(1) from `ir_story_view_log` `sl` where (`sl`.`StoryId` = `s`.`Id`)) AS `views` from story as s inner join content as c on c.id = s.contentId ");
			
			switch(order) {
			case MOST_VIEWED: {
				query.append(" order by views desc, s.id asc ");
				break;
			}
			
			case BEST_RANKED: {
				query.append(" order by (select sum(ss.score) from `ir_story_user_score` `ss` where (`ss`.`StoryId` = `s`.`Id`)) desc, s.id asc ");
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
				stmt.setLong(1, limit.intValue());
			} else if(limit == null) {
				stmt = connection.prepareStatement(query.append(" LIMIT ?,?").toString());
				stmt.setLong(1, offset.intValue());
				stmt.setLong(2, Integer.MAX_VALUE);
			} else {
				stmt = connection.prepareStatement(query.append(" LIMIT ?,?").toString());
				stmt.setLong(1, offset.intValue());
				stmt.setLong(2, limit.intValue());
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				result.add(this.loadStory(rs));
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
			StringBuilder query = new StringBuilder("select s.*, concat(substring(c.`Text`, 1, 140), '...') as text, (select count(1) from `ir_story_view_log` `sl` where (`sl`.`StoryId` = `s`.`Id`)) AS `views` from story as s inner join content as c on c.id = s.contentId where s.subgenreId = ? ");
			
			switch(order) {
			case MOST_VIEWED: {
				query.append(" order by views desc, s.id asc ");
				break;
			}
			
			case BEST_RANKED: {
				query.append(" order by (select sum(ss.score) from `ir_story_user_score` `ss` where (`ss`.`StoryId` = `s`.`Id`)) desc, s.id asc ");
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
				result.add(this.loadStory(rs));
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
	 * @return List of subgenres
	 */
	public List<IRSubgenre> listSubgenres() {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IRSubgenre> result = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement("select s.id, s.name, (select count(1) from story as st where st.subgenreId = s.id) as total from subgenre as s order by total desc, name asc");
			
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
				stmt = connection.prepareStatement("select id, name, ifw from keyword");
			} else {
				stmt = connection.prepareStatement("select k.id, k.name, k.ifw from keyword as k inner join storykeywords as sk on k.id = sk.keywordId where sk.storyId = ?");
				stmt.setLong(1, story.getId());
			}
			
			rs = stmt.executeQuery();
			result = new ArrayList<IRKeyword>();
			
			while(rs.next()) {
				result.add(new IRKeyword(rs.getLong("id"), rs.getString("name"), rs.getDouble("IFW")));
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
	 * Sets the score a user gives to a story
	 * @param story Story to score
	 * @param user User who gives the score
	 * @param score Score given
	 */
	public void scoreStory(IRStory story, IRUser user, float score) {
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try
		{
			if((story != null) && (story.getId() != Long.MIN_VALUE) && (user != null) && (user.isLogged())) {
				connection = _ConnectionManager.getConnection();
				stmt = connection.prepareStatement("insert into ir_story_user_score (storyId, userId, score) values (?,?,?) on duplicate key update score=values(score)");
				stmt.setLong(1, story.getId());
				stmt.setLong(2, user.getId());
				stmt.setFloat(3, score);
				
				stmt.execute();
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
	}
	
	
	
	
	/**
	 * Gets the score a user gave to a story
	 * @param story Scored story
	 * @param user User who gave the score
	 * @return Given score or 0
	 */
	public float getStoryScore(IRStory story, IRUser user) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		float result = 0.0F;
		
		try
		{
			if((user != null) && (user.getId() != Long.MIN_VALUE) && (story != null) && (story.getId() != Long.MIN_VALUE)) {
				connection = _ConnectionManager.getConnection();
				stmt = connection.prepareStatement("select score from ir_story_user_score where storyId = ? and userId = ?");
				stmt.setLong(1, story.getId());
				stmt.setLong(2, user.getId());
				
				rs = stmt.executeQuery();
				
				if(rs.next()) {
					result = rs.getFloat("score");
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(connection);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Gets the user statistics (views, score...) of the selected story
	 * @param story Selected Story
	 * @param user Selected User
	 * @return User Statistics
	 */
	public IRStoryUserStatistics getStoryUserStatistics(IRStory story, IRUser user) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IRStoryUserStatistics result = null;
		
		try
		{
			result = new IRStoryUserStatistics();
			result.setStory(story);
			
			if((user != null) && (user.getId() != Long.MIN_VALUE) && (story != null) && (story.getId() != Long.MIN_VALUE)) {
				connection = _ConnectionManager.getConnection();
				stmt = connection.prepareStatement("select us.score, (select count(1) from ir_story_view_log as ul inner join ir_event_log as el on el.id = ul.id where el.userId = us.userId and ul.storyId = us.storyId) as views from ir_story_user_score as us where us.storyId = ? and us.userId = ?");
				stmt.setLong(1, story.getId());
				stmt.setLong(2, user.getId());
				
				rs = stmt.executeQuery();
				result.setUser(user);
							
				if(rs.next()) {
					result.setScore(rs.getFloat("score"));
					result.setViews(rs.getLong("views"));
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
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
