package recommender.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.dataaccess.EventDAO;
import recommender.dataaccess.StoryDAO;
import recommender.dataaccess.StoryDAO.StoriesOrder;
import recommender.model.bag.BagKey;
import recommender.model.bag.BagValue;
import recommender.model.bag.FeatureBag;
import recommender.utils.LRUCacheMap;
import recommender.web.controller.StoryScoreController;

public class CachedFeedbackUserModel extends UserModel {

	private static final float USER_LOST_INTEREST_FACTOR = 0.1F;
	
	private Map<IRStory, IRStoryUserStatistics> story_session;
	private FeatureBag bag;
	
	/**
	 * Default Constructor
	 */
	public CachedFeedbackUserModel() {
		super();
		this.bag = new FeatureBag();
		this.initializeStorySession();
	}
	
	
	/**
	 * Constructor with a known user
	 * @param current_user
	 */
	public CachedFeedbackUserModel(IRUser current_user) {
		super(current_user);
		this.bag = new FeatureBag();
		this.initializeStorySession();
	}
	
	
	
	/**
	 * Initializes the story session as a LRU Cache (fixed size)
	 * If the current user is set, tries to fill it with his log
	 */
	private void initializeStorySession() {
		this.story_session = new LRUCacheMap<IRStory, IRStoryUserStatistics>(SESSION_SIZE);
		this.story_session = Collections.synchronizedMap(this.story_session);
		StoryDAO storyDAO = new StoryDAO();

		if(this.currentUser != null) {
			for(IRStoryUserStatistics stats : this.listUserStoryViews()) {
				stats.setStory(storyDAO.loadAllFields(stats.getStory()));
				this.story_session.put(stats.getStory(), stats);
				this.extractFeatures(stats);
			} 
		} else {
			IRStoryUserStatistics stats;
			
			// When there is no user, create a default model with the most viewed and best ranked stories
			for(IRStory story : storyDAO.listStories(SESSION_SIZE/2, 0, StoriesOrder.MOST_VIEWED)) {
				story = storyDAO.loadAllFields(story);
				stats = new IRStoryUserStatistics(story, story.getViews());
				this.story_session.put(story, stats);
				this.extractFeatures(stats);
			}
			
			for(IRStory story : storyDAO.listStories(SESSION_SIZE/2, 0, StoriesOrder.BEST_RANKED)) {
				story = storyDAO.loadAllFields(story);
				stats = new IRStoryUserStatistics(story, story.getViews());
				this.story_session.put(story, stats);
				this.extractFeatures(stats);
			}			
		}
	}
	
	
	
	
	/**
	 * Extracts the features of a story and the user statistics and
	 * transfers the user interest to the current story
	 * @param stats User statistics of the story
	 */
	private void extractFeatures(IRStoryUserStatistics stats) {
		Set<BagKey<?>> story_features = this.bag.addStoryData(stats);
		this.bag.transferUserInterest(story_features, USER_LOST_INTEREST_FACTOR);
	}
	
	
	
	/**
	 * Adds a viewed story to the user model
	 * @param story Viewed story
	 * @param stats User story statistics
	 */
	@Override
	public void viewedStory(IRStory story, IRStoryUserStatistics stats) {
		IRStoryUserStatistics session_stats = this.story_session.get(story);

		if(session_stats == null) {
			stats.setViews(1L);
			this.story_session.put(story, stats);

			this.extractFeatures(stats);
		} else {
			session_stats.setViews(session_stats.getViews() + 1);
		} 
	}




	/**
	 * Adds a score to a story in the user model
	 * @param story Scored Story
	 * @param score Score
	 */
	@Override
	public void scoredStory(IRStory story, float score) {
		IRStoryUserStatistics session_stats = this.story_session.get(story);
		if(session_stats != null) {

			if(session_stats.getScore() == StoryScoreController.NEUTRAL_SCORE) {
				this.bag.reScoreStoryData(story, score);
			} else {
				if(score == StoryScoreController.NEUTRAL_SCORE) {
					this.bag.reScoreStoryData(story, session_stats.getScore() * -1);
				} else {
					this.bag.reScoreStoryData(story, score * 2);
				}
			}

			session_stats.setScore(score);
		} else {
			this.bag.reScoreStoryData(story, score);
		}
	}
	

	
	@Override
	protected FeatureBag getCurrentFeatureBag() {
		return this.bag;
	}


	@Override
	public List<BagValue> getModelFeatures() {
		return this.getOrderedFeatures();
	}


	@Override
	protected List<IRStoryUserStatistics> listUserStoryViews() {
		return new EventDAO().listUserStoryViews(this.currentUser);
	}

}
