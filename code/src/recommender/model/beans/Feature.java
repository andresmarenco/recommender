package recommender.model.beans;

public abstract class Feature<T> {
	protected final FeatureField feature_field;
	protected T value;
	
	/**
	 * Default Constructor
	 * @param feature_field Type of the Feature Field
	 * @param value Value of the Feature Field
	 */
	public Feature(FeatureField feature_field, T value) {
		this.feature_field = feature_field;
		this.value = value;
	}
	
	/**
	 * Gets the value of the Feature Field
	 * @return Value of the Feature Field
	 */
	public T getValue() {
		return this.value;
	}
	

	/**
	 * Gets the Type of the Feature Field
	 * @return value of the Feature Field
	 */
	public FeatureField getFeatureField() {
		return this.feature_field;
	}
	
	
	/**
	 * Gets the Item Feature Weight of the Feature
	 * @return Item Feature Weight 
	 */
	public abstract double getIFW();

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
		@SuppressWarnings("unchecked")
		Feature<T> other = (Feature<T>) obj;
		if (feature_field != other.feature_field)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
	@Override
	public String toString() {
		return this.value.toString();
	}
}
