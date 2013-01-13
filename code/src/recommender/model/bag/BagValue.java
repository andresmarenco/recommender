package recommender.model.bag;

import java.util.Comparator;

import recommender.model.beans.Feature;
import recommender.model.beans.FeatureField;

public abstract class BagValue {
	protected Feature<?> feature;
	protected int frequency;
	protected double weight;
	protected float interest_factor;
	
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
	
	public int getFrequency() {
		return this.frequency;
	}
	
	public float getInterestFactor() {
		return this.interest_factor;
	}
	
	public abstract double getTotal_weight();
	
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
