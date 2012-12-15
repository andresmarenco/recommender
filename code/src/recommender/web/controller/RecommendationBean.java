package recommender.web.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Queue;

import recommender.beans.IRStory;
import recommender.beans.IRUser;
import recommender.querying.RecommendationManager;
import recommender.utils.RecommenderException;

public class RecommendationBean implements Serializable {
	
	private static final long serialVersionUID = 201212100440L;
	
	private IRUser user;
	private IRStory current_story;
	private Queue<Long> story_session;
	
	
	/**
	 * Default Constructor
	 */
	public RecommendationBean() {
		this.user = null;
		this.current_story = null;
		this.story_session = null;
	}


	/**
	 * @return the user
	 */
	public IRUser getUser() {
		return user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(IRUser user) {
		this.user = user;
	}


	/**
	 * @return the story_session
	 */
	public Queue<Long> getStory_session() {
		return story_session;
	}


	/**
	 * @param story_session the story_session to set
	 */
	public void setStory_session(Queue<Long> story_session) {
		this.story_session = story_session;
	}


	/**
	 * @return the current_story
	 */
	public IRStory getCurrent_story() {
		return current_story;
	}


	/**
	 * @param current_story the current_story to set
	 */
	public void setCurrent_story(IRStory current_story) {
		this.current_story = current_story;
	}


	/**
	 * @return the recommendations
	 */
	public List<IRStory> getRecommendations() throws RecommenderException {
		RecommendationManager recommendationManager = new RecommendationManager();
		return recommendationManager.recommendStories(this.user, this.current_story, this.story_session);
	}

	
	
	
}
