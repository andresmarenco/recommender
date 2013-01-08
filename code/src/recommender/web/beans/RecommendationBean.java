package recommender.web.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Queue;

import recommender.beans.IRStory;
import recommender.beans.IRUser;
import recommender.model.UserModel;
import recommender.querying.RecommendationManager;
import recommender.utils.RecommenderException;

public class RecommendationBean implements Serializable {
	
	private static final long serialVersionUID = 201212100440L;
	
	private UserModel user_model;
	
	
	/**
	 * Default Constructor
	 */
	public RecommendationBean() {
		this.user_model = null;
	}


	/**
	 * @return the user_model
	 */
	public UserModel getUser_model() {
		return user_model;
	}


	/**
	 * @param user_model the user_model to set
	 */
	public void setUser_model(UserModel user_model) {
		this.user_model = user_model;
	}




	/**
	 * @return the recommendations
	 */
	public List<IRStory> getRecommendations() throws RecommenderException {
		RecommendationManager recommendationManager = new RecommendationManager();
		return recommendationManager.recommendStories(this.user_model);
	}

	
	
	
}
