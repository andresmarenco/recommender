package recommender.querying;

import java.util.Queue;

import recommender.beans.IRStory;
import recommender.beans.IRStoryViewType;
import recommender.beans.IRUser;
import recommender.dataaccess.EventDAO;
import recommender.dataaccess.StoryDAO;
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
	 * @param view_type_id How the user finds the story
	 * @return Found story
	 * @throws RecommenderException
	 */
	public IRStory showStory(long story_id, IRUser user, Long view_type_id) throws RecommenderException {
		IRStory story = null;
		
		try
		{
			StoryDAO storyDAO = new StoryDAO();
			story = storyDAO.loadStory(story_id, false);
			
			if(story != null) {
				// Log the event
				EventDAO eventDAO = new EventDAO();
				IRStoryViewType viewType = (view_type_id != null) ? eventDAO.loadViewType(view_type_id) : null;
				eventDAO.logViewedStory(user, story, viewType);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RecommenderException(RecommenderException.MSG_UNKNOWN_ERROR);
		}
		
		return story;
	}
}
