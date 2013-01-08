package recommender.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.dataaccess.EventDAO;
import recommender.model.bag.BagKey;
import recommender.model.bag.BagValue;
import recommender.model.bag.FeatureBag;
import recommender.web.controller.StoryScoreController;

public class UserModelBkp {
	private static final int SESSION_SIZE = 6;
	private static final float USER_LOST_INTEREST_FACTOR = 0.5F;
	
	//private Map<IRStory, IRStoryUserStatistics> story_session;
	private IRUser current_user;
	//private FeatureBag bag;
	
	/**
	 * Default Constructor with undefined User
	 */
	public UserModelBkp() {
		this.current_user = null;
		//this.bag = new FeatureBag();
		this.initializeStorySession();
		System.out.println("EMPTY USER MODEL");
	}
	
	
	
	
	/**
	 * Constructor with a known user. Finds the stories session on his log
	 * @param current_user
	 */
	public UserModelBkp(IRUser current_user) {
		this.current_user = current_user;
		//this.bag = new FeatureBag();
		this.initializeStorySession();
	}
	
	
	
	
	/**
	 * Finds the user model in the session. If not found, creates a new one
	 * @param session Http Session
	 * @param user Current User
	 * @return User Model
	 */
	public static UserModelBkp getSessionInstance(HttpSession session, IRUser user) {
		UserModelBkp user_model = (UserModelBkp)session.getAttribute("user_model");
		if(user_model == null) {
			user_model = new UserModelBkp(user);
			session.setAttribute("user_model", user_model);
		}
		return user_model;
	}
	
	
	
	
	/**
	 * Initializes the story session as a LRU Cache (fixed size)
	 * If the current user is set, tries to fill it with his log
	 */
	private Map<IRStory, IRStoryUserStatistics> initializeStorySession() {
		
		
		//this.story_session = new LRUCacheMap<IRStory, IRStoryUserStatistics>(SESSION_SIZE);
		Map<IRStory, IRStoryUserStatistics> story_session = new LinkedHashMap<IRStory, IRStoryUserStatistics>(SESSION_SIZE);
		//this.story_session = Collections.synchronizedMap(this.story_session);
		
		if(this.current_user != null) {
			EventDAO eventDAO = new EventDAO();
			//for(IRStoryUserStatistics stats : eventDAO.listUserStoryViews(this.current_user, SESSION_SIZE)) {
			for(IRStoryUserStatistics stats : eventDAO.listUserStoryViews(this.current_user)) {
				story_session.put(stats.getStory(), stats);
				//this.extractFeatures(stats);
				System.out.println(stats.getStory().getId() + "  /views:" + stats.getViews() + " /score:" + stats.getScore());
			} 
		}
		
		return story_session;
	}
	
	
	
	public List<BagValue> getUnorderedFeatures() {
		FeatureBag bag = new FeatureBag();
		Map<IRStory, IRStoryUserStatistics> story_session = this.initializeStorySession();
		
		for(IRStoryUserStatistics stats : story_session.values()) {
			bag.addStoryData(stats);
		}
		
		
		return bag.getUnorderedFeatures();
	}
	
	
	
	
	/**
	 * Gets a list of the features ordered descending by the total weight
	 * @return List of ordered features
	 */
	/*public List<BagValue> getOrderedFeatures() {
		return this.bag.getOrderedFeatures();
	}*/
	
	
	
	
	/**
	 * Gets the last viewed story on the user session
	 * @return Last viewed story or null
	 */
	/*public IRStory getLastViewedStory() {
		IRStory result = null;
		Iterator<IRStoryUserStatistics> iterator = this.story_session.values().iterator();
		if(iterator.hasNext()) {
			result = iterator.next().getStory();
		}
		
		return result;
	}*/
	
	
	
	
	
	/**
	 * Extracts the features of a story and the user statistics and
	 * transfers the user interest to the current story
	 * @param stats User statistics of the story
	 */
	private void extractFeatures(IRStoryUserStatistics stats) {
		//this.bag.addStoryData(stats);
		//Set<BagKey<?>> story_features = this.bag.addStoryData(stats);
		//this.bag.transferUserInterest(story_features, USER_LOST_INTEREST_FACTOR);
	}
	
	
	
	
	/**
	 * Adds a viewed story to the user model
	 * @param story Viewed story
	 * @param stats User story statistics
	 */
	/*public void viewedStory(IRStory story, IRStoryUserStatistics stats) {
		IRStoryUserStatistics session_stats = this.story_session.get(story);
		
		if(session_stats == null) {
			stats.setViews(1L);
			this.story_session.put(story, stats);

			this.extractFeatures(stats);
		} else {
			session_stats.setViews(session_stats.getViews() + 1);
		}
		
		
		for(IRStoryUserStatistics s : this.story_session.values()) {
			System.out.println(s.getStory().getId() + "  /views:" + s.getViews() + " /score:" + s.getScore());
		} 
	}*/
	
	
	
	
	/**
	 * Adds a score to a story in the user model
	 * @param story Scored Story
	 * @param score Score
	 */
	/*public void scoredStory(IRStory story, float score) {
		IRStoryUserStatistics session_stats = this.story_session.get(story);
		if(session_stats != null) {
			System.out.println("WAS " + session_stats.getScore() + " now: " + score);

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
		
		for(IRStoryUserStatistics s : this.story_session.values()) {
			System.out.println(s.getStory().getId() + "  /views:" + s.getViews() + " /score:" + s.getScore());
		}
	}*/
}
