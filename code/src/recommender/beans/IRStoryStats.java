package recommender.beans;

import java.io.Serializable;

public class IRStoryStats implements Serializable {

	private static final long serialVersionUID = 201212220144L;

	private long views;
	
	
	/**
	 * Default Constructor
	 */
	public IRStoryStats() {
		super();
		this.clear();
	}
	
	
	
	
	/**
	 * Constructor with Fields
	 * @param views Total views of the story
	 */
	public IRStoryStats(long views) {
		super();
		this.views = views;
	}




	/**
	 * Clears all the fields of the object
	 */
	public void clear() {
		this.views = Long.MIN_VALUE;
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
	
	
	
}
