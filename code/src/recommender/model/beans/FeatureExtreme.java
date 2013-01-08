package recommender.model.beans;

public class FeatureExtreme implements Feature<Boolean> {
	
	private boolean value;
	private double IFW;
	
	/**
	 * Default Constructor
	 * @param value Feature Value
	 */
	public FeatureExtreme(boolean value) {
		this.value = value;
		this.initIFW();
	}
	
	
	/**
	 * Initializes the IFW value
	 */
	private void initIFW() {
		this.IFW = Double.MIN_VALUE;
	}
	

	@Override
	public double getIFW() {
		return this.IFW;
	}

	@Override
	public Boolean getValue() {
		return this.value;
	}

	@Override
	public FeatureField getFeatureField() {
		return FeatureField.EXTREME;
	}

}
