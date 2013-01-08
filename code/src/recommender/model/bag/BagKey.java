package recommender.model.bag;

import recommender.model.beans.FeatureField;

public class BagKey<T> {
	private T value;
	private FeatureField field;
	
	/**
	 * Default Constructor
	 * @param value Object with the key
	 * @param field Field of the key
	 */
	public BagKey(T value, FeatureField field) {
		this.value = value;
		this.field = field;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
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
		BagKey<?> other = (BagKey<?>) obj;
		if (field != other.field)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
