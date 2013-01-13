package recommender.querying;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.terrier.matching.ResultSet;
import org.terrier.querying.parser.FieldQuery;
import org.terrier.querying.parser.MultiTermQuery;
import org.terrier.querying.parser.SingleTermQuery;

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
	
	RetrievalManager retrievalManager;

	private static final int DEFAULT_RECOMMENDATIONS = 20;
	
	/**
	 * Maximum number of features in a query performed when a recommendation is needed.
	 */
	private static final int NUMBER_OF_FEATURES = 20;
	
	
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
		List<IRStory> result = new ArrayList<IRStory>();
		if(userModel == null) userModel = UserModel.newInstance();
		
		if(userModel != null) {
			List<BagValue> features = userModel.getModelFeatures();
			
			// If there are no features, use the features from the most viewed and best ranked stories
			if((features == null) || (features.isEmpty())) {
				System.out.println("NO FEATURES!");
				
				UserModel most_viewed_model = new CachedFeedbackUserModel();
				features = most_viewed_model.getModelFeatures();
			}
			
			MultiTermQuery mtq = new MultiTermQuery();
			
			int j = 0;
//			int size = Math.min(NUMBER_OF_FEATURES, features.size()/5);
			
			for(BagValue feature : features) {
				double weight = feature.getTotal_weight();
				
				if(weight > 0.0D) {
					FieldQuery fq = new FieldQuery();
					fq.setField(feature.getField().getIndexTag().toLowerCase());
					
					SingleTermQuery stq = this.createSingleTermQuery(feature);
					if(stq == null)
						continue;
					fq.setChild(stq);
					mtq.add(fq);
					if(j++ == NUMBER_OF_FEATURES)
						break;
				} else {
					System.out.println(MessageFormat.format("Ignoring negative weight {0} on feature {1}...", weight, feature.toString()));
				}
			}
			
//			FieldQuery fq = new FieldQuery();
//			fq.setField("TAAL");
//			SingleTermQuery stq = new SingleTermQuery("standaardnederlands");
//			fq.setChild(stq);
//			mtq.add(fq);
			
			System.out.println("performing recommendation query: " + mtq.toString());
			ResultSet rs = this.retrievalManager.search(mtq, null, null);
			result = this.getRecommendations(userModel, rs);
								
		}
		
		return result;
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
//			System.out.println(MessageFormat.format("{0}:\"{1}\"^{2}", bagFeature.getField().getIndexTag(), term, bagFeature.getTotal_weight()));
			stq.setWeight(bagFeature.getTotal_weight());
			stq.setTerm(bagFeature.toString());
		} else {
			System.out.println(MessageFormat.format("Ignoring empty term for {0}...", bagFeature.getField().getIndexTag()));
		}
				
		return stq;
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
				if(storyDAO.getStoryScore(current_story, user_model.getCurrentUser()) == StoryScoreController.DISLIKE_SCORE) {
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
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
}
