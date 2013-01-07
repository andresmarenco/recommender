package recommender.model;

import java.io.Serializable;
import java.util.Map;

import recommender.model.beans.Feature;
import recommender.model.beans.FeatureField;
import recommender.utils.KeyValuePair;
import recommender.utils.LRUCacheMap;

/**
 * Keeps a cache of the Features and their IDF
 * @author andres
 *
 */
public class FeatureManager {
	
	private static final int CACHE_SIZE = 30;
	
	/** Current instance of the class for the Singleton Pattern */
	private static FeatureManager _ClassInstance;
	
	private LRUCacheMap<KeyValuePair<FeatureField, Serializable>, Feature<?>> cache;
	private LRUCacheMap<Feature<?>, Integer> feature_size;
	private Map<FeatureField, Integer> dimension_size;
	
	
	/**
	 * Default Constructor
	 */
	private FeatureManager() {
		cache = new LRUCacheMap<KeyValuePair<FeatureField, Serializable>, Feature<?>>(CACHE_SIZE);
		feature_size = new LRUCacheMap<Feature<?>, Integer>(CACHE_SIZE);
	}
	
	
	
	/**
     * Returns an instance of the class
     * @return Singleton instance of the class
     */
    public synchronized static FeatureManager getInstance()
    {
        try
		{
		    if(_ClassInstance == null)
		    {
			    _ClassInstance = new FeatureManager();    
		    }
		    return _ClassInstance;
		}
		catch(Exception ex)
		{
            ex.printStackTrace();
		    return null;
		}
    }
}
