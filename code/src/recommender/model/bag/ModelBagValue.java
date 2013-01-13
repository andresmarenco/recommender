package recommender.model.bag;

import recommender.model.beans.Feature;

public class ModelBagValue extends BagValue {

	/**
	 * Default Constructor
	 * @param feature
	 */
	public ModelBagValue(Feature<?> feature) {
		super(feature);
	}

	public void increaseFrequency() {
		this.frequency++;
	}
	
	public void decreaseFrequency() {
		if(--this.frequency < 0) {
			this.frequency = 0;
		}
	}
	
	public void increaseWeight(float factor) {
		this.weight += factor;
	}
	
	public void decreaseWeight(float factor) {
		this.weight -= factor;
	}
	
	public void increaseInterestFactorBy(float factor) {
		this.interest_factor *= Math.max((factor + 1), 1);
	}
	
	public void decreaseInterestFactorBy(float factor) {
		this.interest_factor /= Math.max((factor + 1), 1);
	}
	
	@Override
	public double getTotal_weight() {
		return ((this.feature.getIFW() * this.frequency) + (this.weight * this.frequency) * this.interest_factor);
	}
}
