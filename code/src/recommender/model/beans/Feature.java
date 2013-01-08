package recommender.model.beans;

public interface Feature<T> {
	/**
	 * Gets the Item Feature Weight of the Feature
	 * @return Item Feature Weight 
	 */
	public double getIFW();
	public T getValue();
	public FeatureField getFeatureField();
}
