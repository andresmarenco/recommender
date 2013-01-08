package recommender.model;

import java.util.List;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.model.bag.BagValue;

public abstract class UserModel {
	protected IRUser current_user;
	
	/**
	 * Default Constructor
	 */
	public UserModel() {
		
	}
	
	
	/**
	 * Constructor with a known user
	 * @param current_user
	 */
	public UserModel(IRUser user) {
		this.current_user = user;
	}
	
	public abstract List<BagValue> getUnorderedFeatures();
	public abstract List<BagValue> getOrderedFeatures();
	public abstract void viewedStory(IRStory story, IRStoryUserStatistics stats);
	public abstract void scoredStory(IRStory story, float score);
}
