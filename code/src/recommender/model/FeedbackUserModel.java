package recommender.model;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.dataaccess.EventDAO;
import recommender.dataaccess.StoryDAO;
import recommender.model.bag.BagKey;
import recommender.model.bag.FeatureBag;
import recommender.utils.LRUCacheMap;
import recommender.web.controller.StoryScoreController;

public class FeedbackUserModel extends UserModel {

	private static final float USER_LOST_INTEREST_FACTOR = 0.1F;
	
	private Map<IRStory, IRStoryUserStatistics> story_session;
	private FeatureBag bag;
	
	/**
	 * Default Constructor
	 */
	public FeedbackUserModel() {
		super();
		this.bag = new FeatureBag();
		this.initializeStorySession();
	}
	
	
	/**
	 * Constructor with a known user
	 * @param current_user
	 */
	public FeedbackUserModel(IRUser current_user) {
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

		if(this.current_user != null) {
			EventDAO eventDAO = new EventDAO();
			StoryDAO storyDAO = new StoryDAO();
			for(IRStoryUserStatistics stats : eventDAO.listUserStoryViews(this.current_user, SESSION_SIZE)) {
				stats.setStory(storyDAO.loadAllFields(stats.getStory()));
				this.story_session.put(stats.getStory(), stats);
				this.extractFeatures(stats);
//				System.out.println(stats.getStory().getId() + "  /views:" + stats.getViews() + " /score:" + stats.getScore());
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


//		for(IRStoryUserStatistics s : this.story_session.values()) {
//			System.out.println(s.getStory().getId() + "  /views:" + s.getViews() + " /score:" + s.getScore());
//		} 
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
//			System.out.println("WAS " + session_stats.getScore() + " now: " + score);

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

//		for(IRStoryUserStatistics s : this.story_session.values()) {
//			System.out.println(s.getStory().getId() + "  /views:" + s.getViews() + " /score:" + s.getScore());
//		}
	}
	

	
	@Override
	protected FeatureBag getCurrentFeatureBag() {
		return this.bag;
	}

}
