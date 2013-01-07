package recommender.model.beans;

import recommender.beans.IRKeyword;

public class FeatureKeyword implements Feature<IRKeyword> {
	
	private float IFW;
	private IRKeyword value;
	private final FeatureField feature_field;
	
	/**
	 * Default Constructor
	 * @param keyword Keyword
	 */
	public FeatureKeyword(IRKeyword keyword) {
		this.value = keyword;
		this.feature_field = FeatureField.KEYWORD;
		this.initIFW();
	}
	
	
	/**
	 * Calculates the IFW of the feature and stores it into the IFW variable
	 */
	private void initIFW() {
		
	}

	
	@Override
	public IRKeyword getValue() {
		return this.value;
	}


	@Override
	public float getIFW() {
		return this.IFW;
	}


	@Override
	public FeatureField getFeatureField() {
		return this.feature_field;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((feature_field == null) ? 0 : feature_field.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureKeyword other = (FeatureKeyword) obj;
		if (feature_field != other.feature_field)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}


	

	
}
