package recommender.querying;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import recommender.utils.RecommenderException;

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
	 * @throws RecommenderException
	 */
	public List<IRStory> recommendStories(UserModel user_model) throws RecommenderException {
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
				if (j++ == size)
					break;
				SingleTermQuery stq = new SingleTermQuery();
				stq.setTerm(feature.toString());
				stq.setWeight(feature.getTotal_weight());
				mtq.add(stq);
			}
			
			System.out.println("performing recommendation query: " + mtq.toString());
			result.addAll(this.retrievalManager.searchStories(mtq, 1, DEFAULT_RECOMMENDATIONS+1));
			
			if(!result.remove(user_model.getLastViewedStory())) {
				if(result.size() == DEFAULT_RECOMMENDATIONS +1)
					result.remove(result.size()-1);
			}
								
		}
		
		return result;
	}
}
