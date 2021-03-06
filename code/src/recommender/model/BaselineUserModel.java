package recommender.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.dataaccess.EventDAO;
import recommender.dataaccess.StoryDAO;
import recommender.model.bag.BagValue;
import recommender.model.bag.FeatureBag;
import recommender.model.beans.FeatureField;

public class BaselineUserModel extends UserModel {
	
	private List<IRStoryUserStatistics> userStoryViews;

	/**
	 * Default Constructor
	 */
	public BaselineUserModel() {
		super();
		this.userStoryViews = null;
	}
	
	
	/**
	 * Constructor with a known user
	 * @param currentUser
	 */
	public BaselineUserModel(IRUser currentUser) {
		super(currentUser);
		this.userStoryViews = null;
	}
	
	
	/**
	 * Constructor with a defined user log
	 * @param currentUser
	 * @param userStoryViews
	 */
	public BaselineUserModel(IRUser currentUser, List<IRStoryUserStatistics> userStoryViews) {
		super(currentUser);
		this.userStoryViews = userStoryViews;
	}


	@Override
	protected FeatureBag getCurrentFeatureBag() {
		FeatureBag bag = new FeatureBag();
		Map<IRStory, IRStoryUserStatistics> storyLog;
		
		if(this.currentUser != null) {
			storyLog = new LinkedHashMap<IRStory, IRStoryUserStatistics>();
			StoryDAO storyDAO = new StoryDAO();
			for(IRStoryUserStatistics stats : this.listUserStoryViews()) {
				stats.setStory(storyDAO.loadAllFields(stats.getStory()));
				storyLog.put(stats.getStory(), stats);
			}
		} else {
			storyLog = this.storySession;
		}
		
		for(IRStoryUserStatistics stats : storyLog.values()) {
			bag.addStoryData(stats);
		}
		
		return bag;
	}


	@Override
	public List<BagValue> getModelFeatures() {
		List<BagValue> features = this.getCurrentFeatureBag().getUnorderedFeatures();
		List<BagValue> keywords_features = new ArrayList<BagValue>();
		
		if(!features.isEmpty()) {
			for(BagValue feature : features) {
				if(feature.getField().equals(FeatureField.KEYWORD)) {
					keywords_features.add(feature);
				}
			}
			
			
			if(!keywords_features.isEmpty()) {
				Collections.shuffle(keywords_features);
			}
		}
		
		return keywords_features;
	}


	@Override
	protected List<IRStoryUserStatistics> listUserStoryViews() {
		return (userStoryViews != null) ? userStoryViews : new EventDAO().listUserStoryViews(this.currentUser);
	}
}
