package recommender.model.beans;

import recommender.beans.IRStoryTeller;

public class FeatureStoryTeller extends Feature<IRStoryTeller> {
	
	/**
	 * Default Constructor
	 * @param storyTeller Story Teller
	 */
	public FeatureStoryTeller(IRStoryTeller storyTeller) {
		super(FeatureField.STORY_TELLER, storyTeller);
	}


	@Override
	public double getIFW() {
		return this.value.getIFW();
	}
	
}
