package recommender.model.bag;

import recommender.model.beans.Feature;

public class StaticBagValue extends BagValue {

	/**
	 * Default Constructor
	 * @param feature
	 * @param weight
	 */
	public StaticBagValue(Feature<?> feature, double weight) {
		super(feature);
		this.weight = weight;
	}

	@Override
	public double getTotal_weight() {
		return this.weight;
	}

}
