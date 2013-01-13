package recommender.model.bag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import recommender.beans.IRFolktaleType;
import recommender.beans.IRKeyword;
import recommender.beans.IRLanguage;
import recommender.beans.IRRegion;
import recommender.beans.IRScriptSource;
import recommender.beans.IRStory;
import recommender.beans.IRStoryTeller;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRSubgenre;
import recommender.dataaccess.StoryDAO;
import recommender.model.FeatureManager;
import recommender.model.beans.FeatureField;
import recommender.utils.KeyValuePair;
import recommender.utils.OrderDirection;

public class FeatureBag {
	
	private Map<BagKey<?>, ModelBagValue> bag;
	
	/**
	 * Default Constructor
	 */
	public FeatureBag() {
		this.bag = new LinkedHashMap<BagKey<?>, ModelBagValue>();
	}
	
	
	
	
	/**
	 * Gets a list of the features ordered descending by the total weight
	 * @return List of ordered features
	 */
	public List<BagValue> getOrderedFeatures() {
		return this.getOrderedFeatures(BagValue.WEIGHT_COMPARATOR, OrderDirection.DESCENDING);
	}
	
	
	
	
	/**
	 * Gets an ordered list of the features according to the comparator and direction
	 * @param comparator Comparator to order the list
	 * @param direction Direction of the order
	 * @return List of ordered features
	 */
	public List<BagValue> getOrderedFeatures(Comparator<BagValue> comparator, OrderDirection direction) {
		List<BagValue> values = new ArrayList<BagValue>(this.bag.values());
		Collections.sort(values, (direction.equals(OrderDirection.ASCENDING)) ? comparator : Collections.reverseOrder(comparator));
		
		return values;
	}
	
	
	
	
	/**
	 * Gets an unordered list of the features
	 * @return List of features
	 */
	public List<BagValue> getUnorderedFeatures() {
		return new ArrayList<BagValue>(this.bag.values());
	}
	
	
	
	
	/**
	 * Extract all the features of a story and saves them into the bag
	 * @param stats User Statistics of the story
	 * @return Set with the identified features keys
	 */
	public Set<BagKey<?>> addStoryData(IRStoryUserStatistics stats) {
		StoryDAO storyDAO = new StoryDAO();
		Set<BagKey<?>> story_features = new HashSet<BagKey<?>>();
		IRStory current_story = stats.getStory();
		
		for(IRKeyword keyword : storyDAO.listKeywords(current_story)) {
			this.addFeatureAndRefresh(story_features, stats, FeatureField.KEYWORD, keyword);
		}
		
		this.addFeatureAndRefresh(story_features, stats, FeatureField.FOLKTALE_TYPE, current_story.getFolktaleType());
		this.addFeatureAndRefresh(story_features, stats, FeatureField.LANGUAGE, current_story.getLanguage());
		this.addFeatureAndRefresh(story_features, stats, FeatureField.REGION, current_story.getRegion());
		this.addFeatureAndRefresh(story_features, stats, FeatureField.STORY_TELLER, current_story.getStoryTeller());
		this.addFeatureAndRefresh(story_features, stats, FeatureField.SUBGENRE, current_story.getSubgenre());
		
		return story_features;
	}
	
	
	
	
	/**
	 * Adds a feature into the bag and refreshes its weight
	 * @param story_features Set with current story features
	 * @param stats User Statistics of the story
	 * @param field Field of the Feature
	 * @param value Value of the Feature
	 */
	private void addFeatureAndRefresh(Set<BagKey<?>> story_features, IRStoryUserStatistics stats, FeatureField field, Object value) {
		KeyValuePair<BagKey<?>, ModelBagValue> feature_pair = this.addFeature(field, value);
		if(feature_pair != null) {
			story_features.add(feature_pair.getKey());
			this.refreshValues(feature_pair.getValue(), stats);
		}
	}
	
	
	
	
	/**
	 * Applies a transfer model to the user interest to the given keys
	 * @param story_features Features currently more interesting
	 * @param lost_interest_factor Value between ]0, 1[ that indicates the lost of interest on the previous values
	 */
	public void transferUserInterest(Set<BagKey<?>> story_features, float lost_interest_factor) {
		for(Entry<BagKey<?>, ModelBagValue> element : this.bag.entrySet()) {
			if(story_features.contains(element.getKey())) {
//				System.out.print("+ " + element.getValue().toString());
				element.getValue().increaseInterestFactorBy(1 - lost_interest_factor);
//				System.out.println(" " + element.getValue().getInterestFactor());
			} else {
//				System.out.print("- " + element.getValue().toString());
				element.getValue().decreaseInterestFactorBy(lost_interest_factor);
//				System.out.println(" " + element.getValue().getInterestFactor());
			}
		}
		
//		for(BagValue v : this.getOrderedFeatures()) {
//			System.out.println(v.getFeature().toString() + "  w:" + v.getTotal_weight() + "   f:" + v.getFrequency());
//		}
	}
	
	
	
	/**
	 * ReScores all the features of a story
	 * @param stats User Statistics of the story
	 */
	public void reScoreStoryData(IRStory story, float score) {
		StoryDAO storyDAO = new StoryDAO();
		KeyValuePair<BagKey<?>, ModelBagValue> feature_bag_value;
		
		for(IRKeyword keyword : storyDAO.listKeywords(story)) {
			feature_bag_value = this.addFeature(FeatureField.KEYWORD, keyword);
			if(feature_bag_value != null) feature_bag_value.getValue().increaseWeight(score);
		}
		
		feature_bag_value = this.addFeature(FeatureField.FOLKTALE_TYPE, story.getFolktaleType());
		if(feature_bag_value != null) feature_bag_value.getValue().increaseWeight(score);
		feature_bag_value = this.addFeature(FeatureField.LANGUAGE, story.getLanguage());
		if(feature_bag_value != null) feature_bag_value.getValue().increaseWeight(score);
		feature_bag_value = this.addFeature(FeatureField.REGION, story.getRegion());
		if(feature_bag_value != null) feature_bag_value.getValue().increaseWeight(score);
//		feature_bag_value = this.addFeature(FeatureField.SCRIPT_SOURCE, story.getScriptSource());
//		if(feature_bag_value != null) feature_bag_value.getValue().increaseWeight(score);
		feature_bag_value = this.addFeature(FeatureField.STORY_TELLER, story.getStoryTeller());
		if(feature_bag_value != null) feature_bag_value.getValue().increaseWeight(score);
		feature_bag_value = this.addFeature(FeatureField.SUBGENRE, story.getSubgenre());
		if(feature_bag_value != null) feature_bag_value.getValue().increaseWeight(score);
			
		
//		for(BagValue v : this.getOrderedFeatures()) {
//			System.out.println(v.getFeature().toString() + "  w:" + v.getTotal_weight() + "   f:" + v.getFrequency());
//		}
	}
	
	
	
	/**
	 * Refreshes the weights
	 * @param value Value of the Bag
	 * @param stats User Statistics of the story
	 */
	private void refreshValues(ModelBagValue value, IRStoryUserStatistics stats) {
		value.increaseFrequency();
		value.increaseWeight(stats.getScore());
	}
	
	
	
	/**
	 * Adds a feature into the bag
	 * @param field Field of the Feature
	 * @param value Value of the Feature
	 * @return Key-Value Pair of the Bag
	 */
	private KeyValuePair<BagKey<?>, ModelBagValue> addFeature(FeatureField field, Object value) {
		KeyValuePair<BagKey<?>, ModelBagValue> result = null;
		
		if(value != null) {
			BagKey<?> key = null;
			
			switch(field) {
			case KEYWORD: {
				if(value instanceof IRKeyword) {
					key = new BagKey<IRKeyword>((IRKeyword)value, FeatureField.KEYWORD);
				}
				break;
			}
			
			case FOLKTALE_TYPE: {
				if(value instanceof IRFolktaleType) {
					key = new BagKey<IRFolktaleType>((IRFolktaleType)value, FeatureField.FOLKTALE_TYPE);
				}
				break;
			}
			
			case LANGUAGE: {
				if(value instanceof IRLanguage) {
					key = new BagKey<IRLanguage>((IRLanguage)value, FeatureField.LANGUAGE);
				}
				break;
			}
			
			case REGION: {
				if(value instanceof IRRegion) {
					key = new BagKey<IRRegion>((IRRegion)value, FeatureField.REGION);
				}
				break;
			}
			
			case SCRIPT_SOURCE: {
				if(value instanceof IRScriptSource) {
					key = new BagKey<IRScriptSource>((IRScriptSource)value, FeatureField.SCRIPT_SOURCE);
				}
				break;
			}
			
			case STORY_TELLER: {
				if(value instanceof IRStoryTeller) {
					key = new BagKey<IRStoryTeller>((IRStoryTeller)value, FeatureField.STORY_TELLER);
				}
				break;
			}
			
			case SUBGENRE: {
				if(value instanceof IRSubgenre) {
					key = new BagKey<IRSubgenre>((IRSubgenre)value, FeatureField.SUBGENRE);
				}
				break;
			}
			
			default: {
				// Ignore
			}
			}
			
			
			ModelBagValue bagValue = this.bag.get(key);
			if(bagValue == null) {
				bagValue = new ModelBagValue(FeatureManager.getInstance().getFeature(field, value));
				if(!bagValue.toString().trim().isEmpty()) {
					this.bag.put(key, bagValue);
				}
			}
			
			if(bagValue != null) {
				result = new KeyValuePair<BagKey<?>, ModelBagValue>(key, bagValue);
			}
		}
		
		return result;
	}
}
