package recommender.model.bag;

import java.util.Comparator;

import recommender.model.beans.Feature;
import recommender.model.beans.FeatureField;

public class BagValue {
	private Feature<?> feature;
	private int frequency;
	private float weight;
	private float interest_factor;
	
	/**
	 * Default Constructor
	 */
	public BagValue(Feature<?> feature) {
		this.feature = feature;
		this.interest_factor = 1;
	}
	
	
	public Feature<?> getFeature() {
		return this.feature;
	}
	
	
	public FeatureField getField() {
		return this.feature.getFeatureField();
	}
	
	
	public void increaseFrequency() {
		this.frequency++;
	}
	
	public void decreaseFrequency() {
		if(--this.frequency < 0) {
			this.frequency = 0;
		}
	}
	
	public int getFrequency() {
		return this.frequency;
	}
	
	public void increaseWeight(float factor) {
		this.weight += factor;
	}
	
	public void decreaseWeight(float factor) {
		this.weight -= factor;
	}
	
	public void increaseInterest_factor_percentage(float factor) {
		this.interest_factor *= factor;
	}
	
	public void decreaseInterest_factor_percentage(float factor) {
		this.interest_factor /= Math.max(factor, 1);
	}
	
	public double getTotal_weight() {
		return (this.feature.getIFW() + this.weight) * this.interest_factor;
	}
	
	@Override
	public String toString() {
		return this.feature.toString();
	}
	
	
	/**
	 * Comparator to order the values of the bag using the total weight
	 */
	public static Comparator<BagValue> WEIGHT_COMPARATOR = new Comparator<BagValue>() {
		@Override
		public int compare(BagValue o1, BagValue o2) {
			return Double.valueOf(o1.getTotal_weight()).compareTo(Double.valueOf(o2.getTotal_weight()));
		}
	};
}
