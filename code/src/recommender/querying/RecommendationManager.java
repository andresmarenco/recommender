package recommender.querying;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.terrier.matching.ResultSet;
import org.terrier.querying.parser.FieldQuery;
import org.terrier.querying.parser.MultiTermQuery;
import org.terrier.querying.parser.Query;
import org.terrier.querying.parser.SingleTermQuery;
import org.terrier.utility.ApplicationSetup;

import recommender.beans.IRStory;
import recommender.dataaccess.RetrievalManager;
import recommender.dataaccess.StoryDAO;
import recommender.dataaccess.TerrierManager;
import recommender.dataaccess.TerrierManager.ManagerType;
import recommender.model.CachedFeedbackUserModel;
import recommender.model.UserModel;
import recommender.model.bag.BagValue;
import recommender.web.controller.StoryScoreController;

public class RecommendationManager {
	
	/**
	 * Maximum number of features in a query performed when a recommendation is needed.
	 */
	private static final int NUMBER_OF_FEATURES = 20;
	/**
	 * Default number of recommendations retrieved.
	 */
	private static final int DEFAULT_RECOMMENDATIONS = 6;

	
	
	private RetrievalManager retrievalManager;
	
	private static final String[] FIELD_MODELS = { "PL2F", "BM25F" };
	
	
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
	 * Recommend a set of stories based on the provided user model
	 * @param userModel Current user model
	 * @return List of recommended stories
	 */
	public List<IRStory> recommendStories(UserModel userModel) {
		return this.recommendStoriesMetadata(userModel, DEFAULT_RECOMMENDATIONS).getResult();
	}
	
	
	
	
	/**
	 * Gets the recommendations and the corresponding metadata for a user model
	 * @param userModel Current user model
	 * @param limit Limit of the results
	 * @return Recommendation Metadata
	 */
	public RecommendationMetadata recommendStoriesMetadata(UserModel userModel, int limit) {
		RecommendationMetadata metadata = new RecommendationMetadata();
		List<IRStory> result = new ArrayList<IRStory>();
		if(userModel == null) userModel = UserModel.newInstance();
		
		if(userModel != null) {
			// Find the current current model
			String currentModel = ApplicationSetup.getProperty("trec.model", "PL2");
			QueryTermType queryTermType;
			if(Arrays.asList(FIELD_MODELS).contains(currentModel)) {
				queryTermType = QueryTermType.FIELD;
			} else {
				queryTermType = QueryTermType.BASIC;
			}
			
			
			List<BagValue> features = userModel.getModelFeatures();
			
			// If there are no features, use the features from the most viewed and best ranked stories
			if((features == null) || (features.isEmpty())) {
				System.out.println("NO FEATURES!");
				
				UserModel most_viewed_model = new CachedFeedbackUserModel();
				features = most_viewed_model.getModelFeatures();
			}
			
			MultiTermQuery mtq = new MultiTermQuery();
			
			int j = 0;
			
			for(BagValue feature : features) {
				double weight = feature.getTotal_weight();
				
				if(weight > 0.0D) {
					Query q = this.createQueryTerm(queryTermType, feature);
					if(q != null) {
						mtq.add(q);
						
						if(j++ == NUMBER_OF_FEATURES)
							break;
					}
					
				} else {
					System.out.println(MessageFormat.format("Ignoring negative weight {0} on feature {1}...", weight, feature.toString()));
				}
			}
			
			System.out.println("performing recommendation query: " + mtq.toString());
			ResultSet rs = this.retrievalManager.search(mtq, null, null);
			result = this.getRecommendations(userModel, rs, limit);
			
			metadata.setResult(result);
			metadata.setQuery(mtq.toString());
		}
		
		return metadata;
	}
	
	
	
	
	/**
	 * Creates a Single Term Query based on the Bag Feature
	 * @param bagFeature Bag Feature
	 * @return Single Term Query
	 */
	private SingleTermQuery createSingleTermQuery(BagValue bagFeature) {
		SingleTermQuery stq = null;
		String term = bagFeature.toString().replaceAll("\"|\\+|\\-|\\{|\\}|^|:|~", " ").trim();
		
		if((term != null) && (!term.isEmpty())) {
			stq = new SingleTermQuery();
			stq.setWeight(bagFeature.getTotal_weight());
			stq.setTerm(bagFeature.toString());
		} else {
			System.out.println(MessageFormat.format("Ignoring empty term for {0}...", bagFeature.getField().getIndexTag()));
		}
				
		return stq;
	}
	
	
	
	
	/**
	 * Creates a query term with the feature
	 * @param queryTermType Query Term Type according to the Terrier Property
	 * @param feature Bag Feature
	 * @return Query Term
	 */
	private Query createQueryTerm(QueryTermType queryTermType, BagValue feature) {
		Query result = null;
		SingleTermQuery stq = this.createSingleTermQuery(feature);
		
		if((feature != null) && (stq != null)) {
			switch(queryTermType) {
			case FIELD: {
				FieldQuery fq = new FieldQuery();
				fq.setField(feature.getField().getIndexTag().toLowerCase());
				fq.setChild(stq);
				result = fq;
				break;
			}
			
			case BASIC:
			default: {
				result = stq;
				break;
			}
			}
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Gets the list of recommended stories ignoring disliked and repeated
	 * @param user_model Current User Model
	 * @param rs ResultSet of Terrier Query
	 * @param limit Limit of the results
	 * @return List of recommended stories
	 */
	private List<IRStory> getRecommendations(UserModel user_model, ResultSet rs, int limit) {
		List<IRStory> result = new ArrayList<IRStory>();

		try
		{
			StoryDAO storyDAO = new StoryDAO();
			String[] docnos = this.retrievalManager.getDocIDsFromResultSet(rs);
			IRStory  current_story, last_viewed = user_model.getLastViewedStory();
			String last_viewed_code = (last_viewed != null) ? last_viewed.getCode() : null;
			int recommended_total = 0;
			long index = 0;
			
			for(String docid : docnos) {
				index++;
				if(docid.equalsIgnoreCase(last_viewed_code)) {
					System.out.println("Ignoring displayed story from recommendations...");
					continue;
				}
				
				current_story = storyDAO.loadStory(docid, true);
				if(current_story != null) {
					if(storyDAO.getStoryScore(current_story, user_model.getCurrentUser()) < StoryScoreController.NEUTRAL_SCORE) {
						System.out.println(MessageFormat.format("Ignoring disliked story from recommendations ({0})...", docid));
						continue;
					} else {
						current_story.setRecommendationRank(index);
						result.add(current_story);
						recommended_total++;
					}
				}
				
				if(recommended_total >= limit) {
					break;
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Contains the metadata for a recommendation
	 * @author andres
	 *
	 */
	public static class RecommendationMetadata {
		public List<IRStory> result;
		public String query;
		
		public RecommendationMetadata() {
			
		}

		/**
		 * @return the result
		 */
		public List<IRStory> getResult() {
			return result;
		}

		/**
		 * @param result the result to set
		 */
		public void setResult(List<IRStory> result) {
			this.result = result;
		}

		/**
		 * @return the query
		 */
		public String getQuery() {
			return query;
		}

		/**
		 * @param query the query to set
		 */
		public void setQuery(String query) {
			this.query = query;
		}
		
		
	}
	
	
	
	private enum QueryTermType {
		FIELD,
		BASIC
	}
}
