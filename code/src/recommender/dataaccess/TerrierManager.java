package recommender.dataaccess;

import org.terrier.querying.Manager;
import org.terrier.structures.Index;
import org.terrier.structures.MetaIndex;

public class TerrierManager {
	private Index index;
	private Manager manager;
	
	/** Current instance of the class for the Singleton Pattern */
	private static TerrierManager _ClassInstance;
	
	
	/**
	 * Default Constructor
	 */
	private TerrierManager() {
		index = Index.createIndex();
		manager = new Manager(index);
	}
	
	
	
	
	/**
     * Returns an instance of the class
     * @return Singleton instance of the class
     */
	public synchronized static TerrierManager getInstance() {
		try
		{
		    if(_ClassInstance == null)
		    {
			    _ClassInstance = new TerrierManager();    
		    }
		    return _ClassInstance;
		}
		catch(Exception ex)
		{
            ex.printStackTrace();
		    return null;
		}
	}
	
	
	/**
	 * Gets the loaded index
	 * @return
	 */
	public Index getIndex() {
		return this.index;
	}
	
	
	/**
	 * Gets the Meta Data of the Index
	 * @return
	 */
	public MetaIndex getMetaIndex() {
		return this.index.getMetaIndex();
	}
	
	
	/**
	 * Gets the Terrier Manager 
	 * @return
	 */
	public Manager getManager() {
		return this.manager;
	}
}
