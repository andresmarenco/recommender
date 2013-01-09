package recommender.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.dataaccess.EventDAO;
import recommender.dataaccess.StoryDAO;
import recommender.dataaccess.StoryDAO.StoriesOrder;

public class StatisticsStoriesBean implements Serializable {

	private static final long serialVersionUID = 201301090548L;

	private static final int DEFAULT_LIMIT = 3;
	private StoryDAO storyDAO;
	private EventDAO eventDAO;
	private IRUser credential;
	
	/**
	 * Default Constructor
	 */
	public StatisticsStoriesBean() {
		storyDAO = new StoryDAO();
		eventDAO = new EventDAO();
	}
	
	
	/**
	 * @return the credential
	 */
	public IRUser getCredential() {
		return credential;
	}


	/**
	 * @param credential the credential to set
	 */
	public void setCredential(IRUser credential) {
		this.credential = credential;
	}


	/**
	 * Gets the most viewed stories in the database
	 * @return List of stories
	 */
	public List<IRStory> getMostViewedStories() {
		return storyDAO.listStories(DEFAULT_LIMIT, 0, StoriesOrder.MOST_VIEWED);
	}
	
	
	
	
	/**
	 * Gets the best ranked stories in the database
	 * @return List of stories
	 */
	public List<IRStory> getBestRankedStories() {
		return storyDAO.listStories(DEFAULT_LIMIT, 0, StoriesOrder.BEST_RANKED);
	}
	
	
	
	
	/**
	 * Gets the last viewed stories of the user
	 * @return List of stories
	 */
	public List<IRStory> getUserLastViewedStories() {
		List<IRStory> result = new ArrayList<IRStory>();
		if((credential != null) && (credential.isLogged())) {
			for(IRStoryUserStatistics stats : eventDAO.listUserStoryViews(credential, DEFAULT_LIMIT)) {
				result.add(stats.getStory());
			}
		}
		return result;
	}
	
	
	
	
	/**
	 * Gets the stories liked by the user
	 * @return List of stories
	 */
	public List<IRStory> getUserLikedStories() {
		return eventDAO.listUserLikedStories(credential, DEFAULT_LIMIT);
	}
}
