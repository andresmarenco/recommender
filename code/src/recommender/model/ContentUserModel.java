package recommender.model;

import java.util.List;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.model.bag.BagValue;

public class ContentUserModel extends UserModel {

	/**
	 * Default Constructor
	 */
	public ContentUserModel() {
		super();
	}
	
	
	/**
	 * Constructor with a known user
	 * @param current_user
	 */
	public ContentUserModel(IRUser current_user) {
		super(current_user);
	}


	@Override
	public List<BagValue> getUnorderedFeatures() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<BagValue> getOrderedFeatures() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void viewedStory(IRStory story, IRStoryUserStatistics stats) {
		// Ignore. Only important if cached.
	}


	@Override
	public void scoredStory(IRStory story, float score) {
		// Ignore. Only important if cached.
	}
}
