package recommender.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.dataaccess.EventDAO;
import recommender.dataaccess.StoryDAO;
import recommender.model.bag.BagKey;
import recommender.model.bag.BagValue;
import recommender.model.bag.FeatureBag;

public class FeedbackUserModel extends UserModel {
	
	private static final float USER_LOST_INTEREST_FACTOR = 0.1F;
	
	private List<IRStoryUserStatistics> userStoryViews;
	

	/**
	 * Default Constructor
	 */
	public FeedbackUserModel() {
		super();
		this.userStoryViews = null;
	}
	
	
	/**
	 * Constructor with a known user
	 * @param current_user
	 */
	public FeedbackUserModel(IRUser currentUser) {
		super(currentUser);
		this.userStoryViews = null;
	}
	
	
	/**
	 * Constructor with a defined user log
	 * @param currentUser
	 * @param userStoryViews
	 */
	public FeedbackUserModel(IRUser currentUser, List<IRStoryUserStatistics> userStoryViews) {
		super(currentUser);
		this.userStoryViews = userStoryViews;
	}


	@Override
	protected FeatureBag getCurrentFeatureBag() {
		FeatureBag bag = new FeatureBag();
		Map<IRStory, IRStoryUserStatistics> story_log;
		
		if(this.currentUser != null) {
			story_log = new LinkedHashMap<IRStory, IRStoryUserStatistics>();
			StoryDAO storyDAO = new StoryDAO();
			for(IRStoryUserStatistics stats : this.listUserStoryViews()) {
				stats.setStory(storyDAO.loadAllFields(stats.getStory()));
				story_log.put(stats.getStory(), stats);
				// System.out.println(stats.getStory().getId() + "  /views:" + stats.getViews() + " /score:" + stats.getScore());
			}
		} else {
			story_log = this.storySession;
		}
		
		List<Set<BagKey<?>>> recent_features = new ArrayList<Set<BagKey<?>>>();
		int recent_count = SESSION_SIZE;
		
		for(IRStoryUserStatistics stats : story_log.values()) {
			if(recent_count > 0) {
				recent_features.add(bag.addStoryData(stats));
				recent_count--;
			} else {
				bag.addStoryData(stats);
			}
		}
		
		
		// Adding more value to the recently viewed stories
		Collections.reverse(recent_features);
		
		for(Set<BagKey<?>> features : recent_features) {
			bag.transferUserInterest(features, USER_LOST_INTEREST_FACTOR);
		}
		
		return bag;
	}


	@Override
	public List<BagValue> getModelFeatures() {
		List<BagValue> features = this.getCurrentFeatureBag().getOrderedFeatures();
		
		if(!features.isEmpty()) {
			int size = features.size();
			int limit = Math.round(size * DEFAULT_PERCENTAGE_OF_IMPORTANT_FEATURES);
			limit = Math.max(DEFAULT_NUMBER_OF_IMPORTANT_FEATURES, limit);
			
			features = features.subList(0, Math.min(limit, size));
			
			Collections.shuffle(features);
		}
		
		return features;
	}


	@Override
	protected List<IRStoryUserStatistics> listUserStoryViews() {
		return (userStoryViews != null) ? userStoryViews : new EventDAO().listUserStoryViews(this.currentUser);
	}
}
