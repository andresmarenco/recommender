package recommender.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpSession;

import recommender.beans.IRStory;
import recommender.beans.IRUser;
import recommender.model.UserModel;
import recommender.querying.RecommendationManager;
import recommender.utils.RecommenderException;

public class RecommendationBean implements Serializable {
	
	private static final long serialVersionUID = 201212100440L;
	
	private HttpSession session;
	private IRUser credential;
	
	
	/**
	 * Default Constructor
	 */
	public RecommendationBean() {
		this.session = null;
		this.credential = null;
	}


	/**
	 * @return the session
	 */
	public HttpSession getSession() {
		return session;
	}


	/**
	 * @param session the session to set
	 */
	public void setSession(HttpSession session) {
		this.session = session;
	}


	/**
	 * @return the credential
	 */
	public IRUser getCredential() {
		return credential;
	}


	/**
	 * @param credential the credential to set
	 */
	public void setCredential(IRUser credential) {
		this.credential = credential;
	}





	/**
	 * @return the recommendations
	 */
	public List<IRStory> getRecommendations() throws RecommenderException {
		RecommendationManager recommendationManager = new RecommendationManager();
		return recommendationManager.recommendStories(UserModel.getSessionInstance(this.session, this.credential));
	}

	
	
	
}
