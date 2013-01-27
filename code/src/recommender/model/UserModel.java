package recommender.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.model.bag.BagValue;
import recommender.model.bag.FeatureBag;
import recommender.model.bag.StaticBagValue;
import recommender.utils.ConfigUtil;
import recommender.utils.LRUCacheMap;

public abstract class UserModel {
	protected static final int DEFAULT_NUMBER_OF_IMPORTANT_FEATURES = 60;
	protected static final float DEFAULT_PERCENTAGE_OF_IMPORTANT_FEATURES = 0.3F;
	protected static final int SESSION_SIZE = 10;
	protected IRUser currentUser;
	protected Map<IRStory, IRStoryUserStatistics> storySession;
	
	/**
	 * Default Constructor
	 */
	public UserModel() {
		this.currentUser = null;
		this.storySession = new LRUCacheMap<>(SESSION_SIZE);
		this.storySession = Collections.synchronizedMap(this.storySession);
	}
	
	
	/**
	 * Constructor with a known user
	 * @param current_user
	 */
	public UserModel(IRUser user) {
		this.currentUser = user;
		this.storySession = new LRUCacheMap<>(SESSION_SIZE);
		this.storySession = Collections.synchronizedMap(this.storySession);
	}
	
	
	
	
	/**
	 * Creates a new instance of the corresponding User Model
	 * @param user Current User or null
	 */
	public static final UserModel newInstance() {
		return UserModel.newInstance(null);
	}
	
	
	
	
	/**
	 * Creates a new instance of the corresponding User Model
	 * @param user Current User or null
	 * @return User Model
	 */
	public static final UserModel newInstance(IRUser user) {
		UserModel result = null;
		
		try
		{
			if((user != null) && (user.isLogged())) {
				String userModelName = new StringBuilder(UserModel.class.getPackage().getName()).append(".").append(ConfigUtil.getContextParameter("loggedUserModel", String.class)).toString();
				result = (UserModel) Class.forName(userModelName).getDeclaredConstructor(IRUser.class).newInstance(user);
			} else {
				String userModelName = new StringBuilder(UserModel.class.getPackage().getName()).append(".").append(ConfigUtil.getContextParameter("unloggedUserModel", String.class)).toString();
				result = (UserModel) Class.forName(userModelName).newInstance();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Finds the user model in the session. If not found, creates a new one
	 * @param session Http Session
	 * @param user Current User
	 * @return User Model
	 */
	public static UserModel getSessionInstance(HttpSession session, IRUser user) {
		UserModel user_model = (UserModel)session.getAttribute("user_model");
		if(user_model == null) {
			user_model = UserModel.newInstance(user);
			session.setAttribute("user_model", user_model);
		}
		return user_model;
	}
	
	
	
	
	/**
	 * Gets the current user defined by the model
	 * @return Current user
	 */
	public IRUser getCurrentUser() {
		return this.currentUser;
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
		LinkedList<IRStoryUserStatistics> stories = new LinkedList<>(this.storySession.values());
		
		if(!stories.isEmpty()) {
			result = stories.getLast().getStory();
		}
		
		return result;
	}
	

	
	
	/**
	 * Adds a viewed story to the user model
	 * @param story Viewed story
	 * @param stats User story statistics
	 */
	public void viewedStory(IRStory story, IRStoryUserStatistics stats) {
		IRStoryUserStatistics sessionStats = this.storySession.get(story);
		
		if(sessionStats == null) {
			stats.setViews(1L);
			this.storySession.put(story, stats);
		} else {
			sessionStats.setViews(sessionStats.getViews() + 1);
		}
	}
	
	
	
	/**
	 * Adds a score to a story in the user model
	 * @param story Scored Story
	 * @param score Score
	 */
	public void scoredStory(IRStory story, float score) {
		IRStoryUserStatistics sessionStats = this.storySession.get(story);
		if(sessionStats != null) {
			sessionStats.setScore(score);
		}
	}
	
	
	
	
	/**
	 * Gets the list of features in the model with a normalized weight
	 * @return List of features and weights 
	 */
	public List<BagValue> getNormalizedModelFeatures() {
		List<BagValue> features = this.getModelFeatures();
		List<BagValue> normalized = new ArrayList<BagValue>();
		
		if(!features.isEmpty()) {
			BagValue min = Collections.min(features, BagValue.WEIGHT_COMPARATOR);
			BagValue max = Collections.max(features, BagValue.WEIGHT_COMPARATOR);
			double valueMin = min.getTotal_weight();
			double scaleMin = 0; //the normalized minimum desired
			double scaleMax = 1; //the normalized maximum desired
	
			double valueRange = max.getTotal_weight() - valueMin;
			double scaleRange = scaleMax - scaleMin;
			
			
			
			for(BagValue value : features) {
				normalized.add(new StaticBagValue(value.getFeature(), (scaleRange * (value.getTotal_weight() - valueMin) / valueRange) + scaleMin));
			}
		}
		
		return normalized;
	}
	
	
	protected abstract List<IRStoryUserStatistics> listUserStoryViews();
	protected abstract FeatureBag getCurrentFeatureBag();
	public abstract List<BagValue> getModelFeatures();
}
