package recommender.model.beans;

import recommender.beans.IRFolktaleType;

public class FeatureFolktaleType extends Feature<IRFolktaleType> {
	
	/**
	 * Default Constructor
	 * @param folktaleType Folktale Type
	 */
	public FeatureFolktaleType(IRFolktaleType folktaleType) {
		super(FeatureField.FOLKTALE_TYPE, folktaleType);
	}
	
	@Override
	public double getIFW() {
		return this.value.getIFW();
	}

}
