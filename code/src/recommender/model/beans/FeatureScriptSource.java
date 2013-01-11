package recommender.model.beans;

import recommender.beans.IRScriptSource;

public class FeatureScriptSource extends Feature<IRScriptSource> {
	
	/**
	 * Default Constructor
	 * @param scriptSource Script Source
	 */
	public FeatureScriptSource(IRScriptSource scriptSource) {
		super(FeatureField.SCRIPT_SOURCE, scriptSource);
	}


	@Override
	public double getIFW() {
		return this.value.getIFW();
	}
	
}
