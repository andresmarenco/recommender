package recommender.model.beans;

import recommender.beans.IRKeyword;

public class FeatureKeyword extends Feature<IRKeyword> {
	
	/**
	 * Default Constructor
	 * @param keyword Keyword
	 */
	public FeatureKeyword(IRKeyword keyword) {
		super(FeatureField.KEYWORD, keyword);
	}


	@Override
	public double getIFW() {
		return this.value.getIFW();
	}
	
}
