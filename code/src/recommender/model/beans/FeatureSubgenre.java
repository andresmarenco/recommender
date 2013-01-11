package recommender.model.beans;

import recommender.beans.IRSubgenre;

public class FeatureSubgenre extends Feature<IRSubgenre> {
	
	/**
	 * Default Constructor
	 * @param subgenre Subgenre
	 */
	public FeatureSubgenre(IRSubgenre subgenre) {
		super(FeatureField.SUBGENRE, subgenre);
	}


	@Override
	public double getIFW() {
		return this.value.getIFW();
	}
	
}
