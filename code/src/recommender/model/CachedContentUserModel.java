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
import recommender.dataaccess.StoryDAO.StoriesOrder;
import recommender.model.bag.BagValue;
import recommender.model.bag.FeatureBag;


/**
 * Cached Contend Based User Model
 * Implemented only for evaluation purposes
 * @author andres
 *
 */
public class CachedContentUserModel extends UserModel {
	
	private Map<IRStory, IRStoryUserStatistics> story_session;
	private FeatureBag bag;

	/**
	 * Default Constructor
	 */
	public CachedContentUserModel() {
		super();
		this.bag = new FeatureBag();
		this.initializeStorySession(null);
	}
	
	
	/**
	 * Constructor with a known user
	 * @param currentUser
	 */
	public CachedContentUserModel(IRUser currentUser) {
		super(currentUser);
		this.bag = new FeatureBag();
		this.initializeStorySession(null);
	}
	
	
	/**
	 * Constructor with a defined user log
	 * @param currentUser
	 * @param userStoryViews
	 */
	public CachedContentUserModel(IRUser currentUser, List<IRStoryUserStatistics> userStoryViews) {
		super(currentUser);
		this.bag = new FeatureBag();
		this.initializeStorySession(userStoryViews);
	}
	
	
	
	/**
	 * Initializes the story session as a LRU Cache (fixed size)
	 * If the current user is set, tries to fill it with his log
	 */
	private void initializeStorySession(List<IRStoryUserStatistics> userStoryViews) {
		this.story_session = new LinkedHashMap<IRStory, IRStoryUserStatistics>();
		this.story_session = Collections.synchronizedMap(this.story_session);
		
		StoryDAO storyDAO = new StoryDAO();
		
		if(userStoryViews != null) {
			for(IRStoryUserStatistics stats : userStoryViews) {
				stats.setStory(storyDAO.loadAllFields(stats.getStory()));
				this.story_session.put(stats.getStory(), stats);
				this.bag.addStoryData(stats);
			}
		} else {
			if(this.currentUser != null) {
				for(IRStoryUserStatistics stats : new EventDAO().listUserStoryViews(this.currentUser)) {
					stats.setStory(storyDAO.loadAllFields(stats.getStory()));
					this.story_session.put(stats.getStory(), stats);
					this.bag.addStoryData(stats);
				}
			} else {
				IRStoryUserStatistics stats;
				
				// When there is no user, create a default model with the most viewed and best ranked stories
				for(IRStory story : storyDAO.listStories(SESSION_SIZE/2, 0, StoriesOrder.MOST_VIEWED)) {
					story = storyDAO.loadAllFields(story);
					stats = new IRStoryUserStatistics(story, story.getViews());
					this.story_session.put(story, stats);
					this.bag.addStoryData(stats);
				}
				
				for(IRStory story : storyDAO.listStories(SESSION_SIZE/2, 0, StoriesOrder.BEST_RANKED)) {
					story = storyDAO.loadAllFields(story);
					stats = new IRStoryUserStatistics(story, story.getViews());
					this.story_session.put(story, stats);
					this.bag.addStoryData(stats);
				}
			}
		}
	}


	@Override
	protected FeatureBag getCurrentFeatureBag() {
		return this.bag;
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
		return new ArrayList<IRStoryUserStatistics>(this.story_session.values());
	}
}
