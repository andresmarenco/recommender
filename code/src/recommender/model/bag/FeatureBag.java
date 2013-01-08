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

import recommender.beans.IRKeyword;
import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.dataaccess.StoryDAO;
import recommender.model.FeatureManager;
import recommender.model.beans.FeatureField;
import recommender.utils.KeyValuePair;
import recommender.utils.OrderDirection;

public class FeatureBag {
	
	private static final int BAG_DESIRED_SIZE = 50;
	private Map<BagKey<?>, BagValue> bag;
	
	/**
	 * Default Constructor
	 */
	public FeatureBag() {
		this.bag = new LinkedHashMap<BagKey<?>, BagValue>();
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
		KeyValuePair<BagKey<?>, BagValue> feature_pair;
		Set<BagKey<?>> story_features = new HashSet<BagKey<?>>();
		
		for(IRKeyword keyword : storyDAO.listKeywords(stats.getStory())) {
			feature_pair = this.addKeyword(keyword);
			story_features.add(feature_pair.getKey());
			this.refreshValues(feature_pair.getValue(), stats);
		}
		
		
		
		for(BagValue v : this.getOrderedFeatures()) {
			System.out.println(v.getFeature().toString() + "  w:" + v.getTotal_weight() + "   f:" + v.getFrequency());
		}
		
		return story_features;
	}
	
	
	
	
	/**
	 * Applies a transfer model to the user interest to the given keys
	 * @param story_features Features currently more interesting
	 * @param lost_interest_factor Value between ]0, 1[ that indicates the lost of interest on the previous values
	 */
	public void transferUserInterest(Set<BagKey<?>> story_features, float lost_interest_factor) {
		for(Entry<BagKey<?>, BagValue> element : this.bag.entrySet()) {
			if(story_features.contains(element.getKey())) {
				System.out.println("+ " + element.getValue().toString());
				element.getValue().increaseInterest_factor_percentage(1 - lost_interest_factor);
			} else {
				System.out.println("- " + element.getValue().toString());
				element.getValue().increaseInterest_factor_percentage(lost_interest_factor);
			}
		}
		
		for(BagValue v : this.getOrderedFeatures()) {
			System.out.println(v.getFeature().toString() + "  w:" + v.getTotal_weight() + "   f:" + v.getFrequency());
		}
	}
	
	
	
	/**
	 * ReScores all the features of a story
	 * @param stats User Statistics of the story
	 */
	public void reScoreStoryData(IRStory story, float score) {
		StoryDAO storyDAO = new StoryDAO();
		KeyValuePair<BagKey<?>, BagValue> feature_pair;
		
		for(IRKeyword keyword : storyDAO.listKeywords(story)) {
			feature_pair = this.addKeyword(keyword);
			feature_pair.getValue().increaseWeight(score);
		}
		
		
		for(BagValue v : this.getOrderedFeatures()) {
			System.out.println(v.getFeature().toString() + "  w:" + v.getTotal_weight() + "   f:" + v.getFrequency());
		}
	}
	
	
	
	/**
	 * Refreshes the weights
	 * @param value Value of the Bag
	 * @param stats User Statistics of the story
	 */
	private void refreshValues(BagValue value, IRStoryUserStatistics stats) {
		value.increaseFrequency();
		value.increaseWeight(stats.getScore());
	}
	
	
	
	/**
	 * Adds a Keyword in the bag
	 * @param keyword Keyword
	 * @return Key-Value Pair of the Bag
	 */
	private KeyValuePair<BagKey<?>, BagValue> addKeyword(IRKeyword keyword) {
		return this.addFeature(FeatureField.KEYWORD, keyword);
	}
	
	
	
	/**
	 * Adds a feature into the bag
	 * @param field Field of the Feature
	 * @param value Value of the Feature
	 * @return Key-Value Pair of the Bag
	 */
	private KeyValuePair<BagKey<?>, BagValue> addFeature(FeatureField field, Object value) {
		BagKey<?> key = null;
		switch(field) {
		case KEYWORD: {
			if(value instanceof IRKeyword) {
				key = new BagKey<IRKeyword>((IRKeyword)value, FeatureField.KEYWORD);
			}
			break;
		}
		
		case EXTREME: {
			if(value instanceof Boolean) {
				key = new BagKey<Boolean>((Boolean)value, FeatureField.EXTREME);
			}
			break;
		}
		}
		
		BagValue bagValue = this.bag.get(key);
		if(bagValue == null) {
			bagValue = new BagValue(FeatureManager.getInstance().getFeature(field, value));
			this.bag.put(key, bagValue);
		}
		
		return new KeyValuePair<BagKey<?>, BagValue>(key, bagValue);
	}
}
