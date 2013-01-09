package recommender.beans;

import java.io.Serializable;

public class IRStoryUserStatistics implements Serializable {

	private static final long serialVersionUID = 201301062043L;

	private IRStory story;
	private IRUser user;
	private long views;
	private float score;
	
	/**
	 * Default Constructor
	 */
	public IRStoryUserStatistics() {
		this.clear();
	}
	
	
	
	
	/**
	 * Default Constructor
	 * @param story Current Story
	 */
	public IRStoryUserStatistics(IRStory story) {
		this.clear();
		this.story = story;
	}
	
	
	
	
	/**
	 * Default Constructor
	 * @param story Current Story
	 * @param views Views of the Story
	 */
	public IRStoryUserStatistics(IRStory story, long views) {
		this.clear();
		this.story = story;
		this.views = views;
	}
	
	
	
	
	/**
	 * Clears all the fields of the object
	 */
	public void clear() {
		this.user = null;
		this.story = null;
		this.score = 0.0f;
		this.views = 0L;
	}




	/**
	 * @return the story
	 */
	public IRStory getStory() {
		return story;
	}




	/**
	 * @param story the story to set
	 */
	public void setStory(IRStory story) {
		this.story = story;
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
	 * @return the views
	 */
	public long getViews() {
		return views;
	}




	/**
	 * @param views the views to set
	 */
	public void setViews(long views) {
		this.views = views;
	}




	/**
	 * @return the score
	 */
	public float getScore() {
		return score;
	}




	/**
	 * @param score the score to set
	 */
	public void setScore(float score) {
		this.score = score;
	}
	
	
	
	
}
