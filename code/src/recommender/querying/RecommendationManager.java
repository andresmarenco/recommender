package recommender.querying;

import java.util.ArrayList;
import java.util.List;

import recommender.beans.IRStory;
import recommender.dataaccess.RetrievalManager;
import recommender.dataaccess.TerrierManager;
import recommender.model.UserModel;
import recommender.model.bag.BagValue;
import recommender.utils.RecommenderException;

public class RecommendationManager {
	
	RetrievalManager retrievalManager;

	private static final int DEFAULT_RECOMMENDATIONS = 6;
	
	
	/**
	 * Default Constructor
	 */
	public RecommendationManager() {
		super();

		System.setProperty("terrier.home", "/home/andres/git/recommender/code/resources/terrier-3.5");
		System.setProperty(TerrierManager.TERRIER_SEARCH_INDEX_PATH, 
				"/home/andres/git/recommender/code/resources/terrier-3.5/var/index");
		System.setProperty(TerrierManager.TERRIER_RECOMMENDER_INDEX_PATH, 
				"/home/andres/git/recommender/code/resources/terrier-3.5/var/index");
		
		
		this.retrievalManager = new RetrievalManager(
				new TerrierManager(
						System.getProperty(TerrierManager.TERRIER_RECOMMENDER_INDEX_PATH),"data"));
	}



	/**
	 * Recommend a set of stories based on the provides user model
	 * @param user_model Current user model
	 * @return List of recommended stories
	 * @throws RecommenderException
	 */
	public List<IRStory> recommendStories(UserModel user_model) throws RecommenderException {
		List<IRStory> result = new ArrayList<IRStory>();
		
		if(user_model != null) {
			List<BagValue> features = user_model.getUnorderedFeatures();
			
			if(features != null) {
				features = features.subList(0, Math.min(20, features.size()));
				
				// TODO: Implement this!!!
				StringBuilder ir_query = new StringBuilder();
				for(BagValue feature : features) {
					ir_query.append(feature.toString()).append(" ");
				}
				
				System.out.println(ir_query.toString());
				if(!ir_query.toString().trim().isEmpty()) {
					result = this.retrievalManager.searchStories(ir_query.toString(), 1, DEFAULT_RECOMMENDATIONS);
				}
				
			} else {
				// TODO: and if we don't have data???
				
			}
			
		}
		
		return result;
	}
}
