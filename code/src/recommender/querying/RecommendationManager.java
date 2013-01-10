package recommender.querying;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.terrier.matching.ResultSet;
import org.terrier.querying.parser.MultiTermQuery;
import org.terrier.querying.parser.SingleTermQuery;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.dataaccess.RetrievalManager;
import recommender.dataaccess.StoryDAO;
import recommender.dataaccess.TerrierManager;
import recommender.dataaccess.StoryDAO.StoriesOrder;
import recommender.dataaccess.TerrierManager.ManagerType;
import recommender.model.UserModel;
import recommender.model.bag.BagValue;
import recommender.web.controller.StoryScoreController;

public class RecommendationManager {
	
	RetrievalManager retrievalManager;

	private static final int DEFAULT_RECOMMENDATIONS = 6;
	
	/**
	 * Maximum number of features in a query performed when a recommendation is needed.
	 */
	private static final int NUMBER_OF_FEATURES = 50;
	
	
	/**
	 * Default Constructor
	 */
	public RecommendationManager() {
		super();

		try
		{
			this.retrievalManager = new RetrievalManager(
				TerrierManager.getInstance(ManagerType.RECOMMENDER));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	


	/**
	 * Recommend a set of stories based on the provides user model
	 * @param user_model Current user model
	 * @return List of recommended stories
	 */
	public List<IRStory> recommendStories(UserModel user_model) {
		List<IRStory> result = new ArrayList<IRStory>();
		if(user_model == null) user_model = UserModel.newInstance();
		
		if(user_model != null) {
			List<BagValue> features = user_model.getUnorderedFeatures();
			
			// If there are no features, use the features from the most viewed and best ranked stories
			if((features == null) || (features.isEmpty())) {
				System.out.println("NO FEATURES!");
				
				StoryDAO storyDAO = new StoryDAO();
				UserModel most_viewed_model = UserModel.newInstance();
				
				for(IRStory story : storyDAO.listStories(DEFAULT_RECOMMENDATIONS/2, 0, StoriesOrder.MOST_VIEWED)) {
					most_viewed_model.viewedStory(story, new IRStoryUserStatistics(story, story.getViews()));
				}
				
				for(IRStory story : storyDAO.listStories(DEFAULT_RECOMMENDATIONS/2, 0, StoriesOrder.BEST_RANKED)) {
					most_viewed_model.viewedStory(story, new IRStoryUserStatistics(story, story.getViews()));
				}
				
				features = most_viewed_model.getUnorderedFeatures();
			}
			
			
			Collections.shuffle(features);
			MultiTermQuery mtq = new MultiTermQuery();
			
			
			int j = 0;
			int size = Math.min(NUMBER_OF_FEATURES, features.size()/5);
			
			for(BagValue feature : features) {
				double weight = feature.getTotal_weight();
				
				if(weight > 0.0D) {
					if (j++ == size)
						break;
					SingleTermQuery stq = new SingleTermQuery();
					stq.setTerm(feature.toString());
					stq.setWeight(weight);
					mtq.add(stq);
				} else {
					System.out.println(MessageFormat.format("Ignoring negative weight {0} on feature {1}...", weight, feature.toString()));
				}
			}
			
			System.out.println("performing recommendation query: " + mtq.toString());
			ResultSet rs = this.retrievalManager.search(mtq, null, null);
			result = this.getRecommendations(user_model, rs);
			
			
			
			/*result.addAll(this.retrievalManager.searchStories(mtq, 1, DEFAULT_RECOMMENDATIONS+1));
			
			if(!result.remove(user_model.getLastViewedStory())) {
				if(result.size() == DEFAULT_RECOMMENDATIONS +1)
					result.remove(result.size()-1);
			}*/
								
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Gets the list of recommended stories ignoring disliked and repeated
	 * @param user_model Current User Model
	 * @param rs ResultSet of Terrier Query
	 * @return List of recommended stories
	 */
	private List<IRStory> getRecommendations(UserModel user_model, ResultSet rs) {
		List<IRStory> result = new ArrayList<IRStory>();
		
		try
		{
			StoryDAO storyDAO = new StoryDAO();
			String[] docnos = this.retrievalManager.getDocIDsFromResultSet(rs);
			IRStory  current_story, last_viewed = user_model.getLastViewedStory();
			String last_viewed_code = (last_viewed != null) ? last_viewed.getCode() : null;
			int recommended_total = 0;
			
			for(String docid : docnos) {
				if(docid.equalsIgnoreCase(last_viewed_code)) {
					System.out.println("Ignoring displayed story from recommendations...");
					continue;
				}
				
				current_story = storyDAO.loadStory(docid, true); 
				if(storyDAO.getStoryScore(current_story, user_model.getCurrent_user()) == StoryScoreController.DISLIKE_SCORE) {
					System.out.println(MessageFormat.format("Ignoring disliked story from recommendations ({0})...", docid));
					continue;
				} else {
					result.add(current_story);
					recommended_total++;
				}
				
				
				if(recommended_total >= DEFAULT_RECOMMENDATIONS) {
					break;
				}
			}
			
			/*result.addAll(this.retrievalManager.getStoriesFromResultSet(rs.getResultSet(1, Math.min(DEFAULT_RECOMMENDATIONS+1, result_size))));
			if(!result.remove(user_model.getLastViewedStory())) {
				if(result.size() == DEFAULT_RECOMMENDATIONS +1)
					result.remove(result.size()-1);
			}*/
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
}
