package recommender.utils;

import java.util.LinkedHashMap;

public class LRUCacheMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 201301070157L;
	private final int capacity;
	
	/**
	 * Default Constructor
	 * @param capacity Capacity of the Cache
	 */
	public LRUCacheMap(int capacity) {
		super(capacity, 0.75F, true);
		this.capacity = capacity;
	}

	
	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return size() > this.capacity;
	}
	
	
	/**
	 * Gets the capacity of the cache
	 * @return Capacity of the cache
	 */
	public int getCapacity() {
		return this.capacity;
	}
}
