package recommender.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.model.bag.BagValue;
import recommender.model.bag.FeatureBag;
import recommender.utils.ConfigUtil;
import recommender.utils.LRUCacheMap;

public abstract class UserModel {
	protected static final int SESSION_SIZE = 10;
	protected IRUser current_user;
	protected Map<IRStory, IRStoryUserStatistics> story_session;
	
	/**
	 * Default Constructor
	 */
	public UserModel() {
		this.story_session = new LRUCacheMap<>(SESSION_SIZE);
	}
	
	
	/**
	 * Constructor with a known user
	 * @param current_user
	 */
	public UserModel(IRUser user) {
		this.story_session = new LRUCacheMap<>(SESSION_SIZE);
		this.current_user = user;
	}
	
	
	
	
	public static final UserModel newInstance(IRUser user) {
		UserModel result = null;
		String userModelName = new StringBuilder(UserModel.class.getPackage().getName()).append(".").append(ConfigUtil.getContextParameter(((user != null) && (user.isLogged())) ? "loggedUserModel" : "unloggedUserModel", String.class)).toString();
		try
		{
			result = (UserModel) Class.forName(userModelName).newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
	
	
	

	/**
	 * Gets an unordered list of the features
	 * @return List of features
	 */
	public List<BagValue> getUnorderedFeatures() {
		return this.getCurrentFeatureBag().getUnorderedFeatures();
	}
	
	
	
	
	/**
	 * Gets a list of the features ordered descending by the total weight
	 * @return List of ordered features
	 */
	public List<BagValue> getOrderedFeatures() {
		return this.getCurrentFeatureBag().getOrderedFeatures();
	}
	
	
	
	/**
	 * Gets the last viewed story on the user session
	 * @return Last viewed story or null
	 */
	public IRStory getLastViewedStory() {
		IRStory result = null;
		Iterator<IRStoryUserStatistics> iterator = this.story_session.values().iterator();
		if(iterator.hasNext()) {
			result = iterator.next().getStory();
		}
		
		return result;
	}
	

	
	
	/**
	 * Adds a viewed story to the user model
	 * @param story Viewed story
	 * @param stats User story statistics
	 */
	public void viewedStory(IRStory story, IRStoryUserStatistics stats) {
		IRStoryUserStatistics session_stats = this.story_session.get(story);
		
		if(session_stats == null) {
			stats.setViews(1L);
			this.story_session.put(story, stats);
		} else {
			session_stats.setViews(session_stats.getViews() + 1);
		}
	}
	
	
	
	/**
	 * Adds a score to a story in the user model
	 * @param story Scored Story
	 * @param score Score
	 */
	public void scoredStory(IRStory story, float score) {
		IRStoryUserStatistics session_stats = this.story_session.get(story);
		if(session_stats != null) {
			session_stats.setScore(score);
		}
	}
	
	
	
	protected abstract FeatureBag getCurrentFeatureBag();
}
