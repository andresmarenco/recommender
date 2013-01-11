package recommender.model.beans;

import recommender.beans.IRLanguage;

public class FeatureLanguage extends Feature<IRLanguage> {
	
	/**
	 * Default Constructor
	 * @param language Language
	 */
	public FeatureLanguage(IRLanguage language) {
		super(FeatureField.LANGUAGE, language);
	}


	@Override
	public double getIFW() {
		return this.value.getIFW();
	}
	
}
