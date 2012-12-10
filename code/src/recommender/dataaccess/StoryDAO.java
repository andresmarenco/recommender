package recommender.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import recommender.beans.IRStory;
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
	 * @return Story Object or null
	 */
	public IRStory loadStory(long id) throws SQLException {
		IRStory story = new IRStory();
		story.setId(id);
		story.setTitle("TITLE " + id);
		story.setText("story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId());
		
		return story;
		
		// TODO: make the real data access
		/*Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IRStory result = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement("select * from story where id = ?");
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
		
		return result;*/
	}
	
	
	
	
	/**
	 * Loads a Story based on its code
	 * @param code Code of the Story
	 * @return Story Object or null
	 */
	public IRStory loadStory(String code) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IRStory result = null;
		
		try
		{
			connection = _ConnectionManager.getConnection();
			stmt = connection.prepareStatement("select * from story where code = ?");
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
		// TODO Auto-generated method stub
		return null;
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
				stmt = connection.prepareStatement("select id, name from subgenre");
			} else {
				// TODO: Implement this!!!!!
			}
			
			rs = stmt.executeQuery();
			result = new ArrayList<IRSubgenre>();
			
			while(rs.next()) {
				result.add(new IRSubgenre(rs.getLong("id"), rs.getString("name")));
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
}
