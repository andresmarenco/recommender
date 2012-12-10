package recommender.querying;

import java.util.Queue;

import recommender.beans.IRStory;
import recommender.beans.IRUser;
import recommender.dataaccess.StoryDAO;
import recommender.dataaccess.UserDAO;
import recommender.utils.RecommenderException;

public class StoryDisplayer {

	/**
	 * Default Constructor
	 */
	public StoryDisplayer() {
		
	}
	
	
	
	
	/**
	 * Shows the story, and keeps a log of the user's stories views (if logged in)
	 * @param story_id Id of the story to show
	 * @param user User who views the story
	 * @param story_session Current session of viewed stories
	 * @return Found story
	 * @throws RecommenderException
	 */
	public IRStory showStory(long story_id, IRUser user, Queue<Long> story_session) throws RecommenderException {
		IRStory story = null;
		
		try
		{
			StoryDAO storyDAO = new StoryDAO();
			story = storyDAO.loadStory(story_id);
			
			if(user != null) {
				UserDAO userDAO = new UserDAO();
				userDAO.logViewedStory(user, story);
			}
			
			story_session.offer(story_id);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RecommenderException(RecommenderException.MSG_UNKNOWN_ERROR);
		}
		
		return story;
	}
}
