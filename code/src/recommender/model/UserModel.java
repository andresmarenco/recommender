package recommender.model;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpSession;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.dataaccess.EventDAO;
import recommender.utils.LRUCacheMap;

public class UserModel {
	public static final int SESSION_SIZE = 6;
	
	private Map<IRStory, IRStoryUserStatistics> story_session;
	private IRUser current_user;
	
	/**
	 * Default Constructor with undefined User
	 */
	public UserModel() {
		this.current_user = null;
		this.initializeStorySession();
		System.out.println("EMPTY USER MODEL");
	}
	
	
	
	
	/**
	 * Constructor with known user. Finds the stories session on his log
	 * @param current_user
	 */
	public UserModel(IRUser current_user) {
		this.current_user = current_user;
		this.initializeStorySession();
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
			user_model = new UserModel(user);
			session.setAttribute("user_model", user_model);
		}
		return user_model;
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
			for(IRStoryUserStatistics stats : eventDAO.listUserStoryViews(this.current_user, SESSION_SIZE)) {
				this.story_session.put(stats.getStory(), stats);
				System.out.println(stats.getStory().getId() + "  /views:" + stats.getViews() + " /score:" + stats.getScore());
			} 
		}
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
		
		
		for(IRStoryUserStatistics s : this.story_session.values()) {
			System.out.println(s.getStory().getId() + "  /views:" + s.getViews() + " /score:" + s.getScore());
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
		
		
		for(IRStoryUserStatistics s : this.story_session.values()) {
			System.out.println(s.getStory().getId() + "  /views:" + s.getViews() + " /score:" + s.getScore());
		} 
	}
}
