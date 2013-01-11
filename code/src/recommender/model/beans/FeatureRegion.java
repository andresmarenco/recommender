package recommender.model.beans;

import recommender.beans.IRRegion;

public class FeatureRegion extends Feature<IRRegion> {
	
	/**
	 * Default Constructor
	 * @param region Region
	 */
	public FeatureRegion(IRRegion region) {
		super(FeatureField.REGION, region);
	}


	@Override
	public double getIFW() {
		return this.value.getIFW();
	}
	
}
