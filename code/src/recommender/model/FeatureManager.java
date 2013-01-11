package recommender.model;

import java.util.HashMap;
import java.util.Map;

import recommender.beans.IRFolktaleType;
import recommender.beans.IRKeyword;
import recommender.beans.IRLanguage;
import recommender.beans.IRRegion;
import recommender.beans.IRScriptSource;
import recommender.beans.IRStoryTeller;
import recommender.beans.IRSubgenre;
import recommender.model.beans.Feature;
import recommender.model.beans.FeatureField;
import recommender.model.beans.FeatureFolktaleType;
import recommender.model.beans.FeatureKeyword;
import recommender.model.beans.FeatureLanguage;
import recommender.model.beans.FeatureRegion;
import recommender.model.beans.FeatureScriptSource;
import recommender.model.beans.FeatureStoryTeller;
import recommender.model.beans.FeatureSubgenre;

/**
 * Keeps a cache of the Features and their IDF
 * @author andres
 *
 */
public class FeatureManager {
	
	@SuppressWarnings("unused")
	private static final int CACHE_SIZE = 30;
	
	/** Current instance of the class for the Singleton Pattern */
	private static FeatureManager _ClassInstance;
	
	private Map<FeatureField, Map<Object, Feature<?>>> cache_feature;
	
	
	/**
	 * Default Constructor
	 */
	private FeatureManager() {
		this.initCache();
	}
	
	
	
	/**
	 * Initializes the cache
	 */
	private void initCache() {
		this.cache_feature = new HashMap<FeatureField, Map<Object, Feature<?>>>();
		this.cache_feature.put(FeatureField.EXTREME, new HashMap<Object, Feature<?>>());
	}
	
	
	
	
	/**
	 * Creates a Feature with of the corresponding class with its value 
	 * @param field Field
	 * @param value Value
	 * @return Feature
	 */
	public static Feature<?> createFeature(FeatureField field, Object value) {
		Feature<?> result = null;
		
		switch(field) {
		case KEYWORD: {
			if(value instanceof IRKeyword) {
				result = new FeatureKeyword((IRKeyword)value);
			}
			break;
		}
		
		case FOLKTALE_TYPE: {
			if(value instanceof IRFolktaleType) {
				result = new FeatureFolktaleType((IRFolktaleType)value);
			}
			break;
		}
		
		case LANGUAGE: {
			if(value instanceof IRLanguage) {
				result = new FeatureLanguage((IRLanguage)value);
			}
			break;
		}
		
		case REGION: {
			if(value instanceof IRRegion) {
				result = new FeatureRegion((IRRegion)value);
			}
			break;
		}
		
		case SCRIPT_SOURCE: {
			if(value instanceof IRScriptSource) {
				result = new FeatureScriptSource((IRScriptSource)value);
			}
			break;
		}
		
		case STORY_TELLER: {
			if(value instanceof IRStoryTeller) {
				result = new FeatureStoryTeller((IRStoryTeller)value);
			}
			break;
		}
		
		case SUBGENRE: {
			if(value instanceof IRSubgenre) {
				result = new FeatureSubgenre((IRSubgenre)value);
			}
			break;
		}
		
		default: {
			// Ignore
		}
		}
		
		
		return result;
	}
	
	
	
	
	/**
	 * Tries to find a feature in the cache. If not found, creates the Feature with
	 * the given value. If the field is cached, it is stored in the cache 
	 * @param field Field
	 * @param value Value
	 * @return Feature
	 */
	public Feature<?> getFeature(FeatureField field, Object value) {
		Feature<?> result = null;
		Map<Object, Feature<?>> field_features = this.cache_feature.get(field);
		
		if(field_features != null) {
			result = field_features.get(value);
			
			if(result == null) {
				result = FeatureManager.createFeature(field, value);
				if(result != null) field_features.put(value, result);
			}
			
		} else {
			result = FeatureManager.createFeature(field, value);
		}
		
		return result;
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
