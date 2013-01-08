package recommender.web.controller;

import javax.servlet.annotation.WebServlet;

import recommender.beans.IRStory;
import recommender.beans.IRUser;
import recommender.dataaccess.StoryDAO;
import recommender.model.UserModelBkp;
import recommender.web.FormActionServlet;
import recommender.web.WebUtil;

/**
 * Servlet implementation class StoryScoreController
 */
@WebServlet(name = "score.do", urlPatterns = { "/score.do" })
public class StoryScoreController extends FormActionServlet {
	private static final long serialVersionUID = 201212260152L;
	
	public static final float LIKE_SCORE = 1.0F;
	public static final float DISLIKE_SCORE = -1.0F;
	public static final float NEUTRAL_SCORE = 0.0F;
    
    /**
     * Default constructor. 
     */
    public StoryScoreController() {
    }
    
    
    
    
    /**
     * Sets the score that the user gives to the story to LIKE_SCORE 
     */
    public void onLikeStory() {
    	this.setStoryScore(LIKE_SCORE);
    }
    
    
    
    
    /**
     * Sets the score that the user gives to the story to DISLIKE_SCORE 
     */
    public void onDislikeStory() {
    	this.setStoryScore(DISLIKE_SCORE);
    }
    
    
    
    
    /**
     * Sets the score that the user gives to the story to NEUTRAL_SCORE 
     */
    public void onRemoveScoreStory() {
    	this.setStoryScore(NEUTRAL_SCORE);
    }
    
    
    
    
    /**
     * Sets the desired score to a story
     * @param score Score to set
     */
    private void setStoryScore(float score) {
    	try
    	{
    		IRUser user = this.getUserCredential();
    		
    		if((user != null) && (user.isLogged())) {
    			Long story_id = WebUtil.getLongParameter(request, "story_id");
        			
    			if(story_id != null) {
        			StoryDAO storyDAO = new StoryDAO();
        			IRStory story = new IRStory();
        			story.setId(story_id.longValue());
        			
        			storyDAO.scoreStory(story, user, score);
        			
        			/*UserModel user_model = UserModel.getSessionInstance(session, user);
        			user_model.scoredStory(story, score);*/
        		}
    		}
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
}
