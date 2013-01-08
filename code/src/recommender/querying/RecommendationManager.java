package recommender.querying;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.terrier.querying.parser.MultiTermQuery;
import org.terrier.querying.parser.SingleTermQuery;

import recommender.beans.IRStory;
import recommender.dataaccess.RetrievalManager;
import recommender.dataaccess.TerrierManager;
import recommender.dataaccess.TerrierManager.ManagerType;
import recommender.model.UserModelBkp;
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

//		System.setProperty("terrier.home",
//				"/home/andres/git/recommender/code/resources/terrier-3.5");
//		System.setProperty(TerrierManager.TERRIER_SEARCH_INDEX_PATH, 
//				"/home/andres/git/recommender/code/resources/terrier-3.5/var/index");
//		System.setProperty(TerrierManager.TERRIER_RECOMMENDER_INDEX_PATH, 
//				"/home/andres/git/recommender/code/resources/terrier-3.5/var/index");
		
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
	public List<IRStory> recommendStories(UserModelBkp user_model) throws RecommenderException {
		List<IRStory> result = new ArrayList<IRStory>();
		
		if(user_model != null) {
			List<BagValue> features = user_model.getUnorderedFeatures();
			
			if(features != null) {
				features = features.subList(0, Math.min(20, features.size()));
				Collections.shuffle(features);

				MultiTermQuery mtq = new MultiTermQuery();
				int i = 0;
				for(BagValue feature : features) {
					if (i++ == NUMBER_OF_FEATURES)
						break;
					SingleTermQuery stq = new SingleTermQuery();
					stq.setTerm(feature.toString());
					stq.setWeight(feature.getTotal_weight());
					mtq.add(stq);
				}
				
				System.out.println("performing recommendation query: " + mtq.toString());
				if(features.size() != 0) {
					result = this.retrievalManager.searchStories(mtq, 1, DEFAULT_RECOMMENDATIONS);
				}
				
			} else {
				// TODO: and if we don't have data???
				
			}
			
		}
		
		return result;
	}
}
