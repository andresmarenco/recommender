package recommender.dataaccess;

import java.util.HashMap;

import javax.naming.InitialContext;

import org.terrier.querying.Manager;
import org.terrier.structures.Index;
import org.terrier.structures.MetaIndex;

import recommender.utils.RecommenderException;

public class TerrierManager {
	private Index index;
	private Manager manager;
	
	/** Terrier Home Key for the JNDI Resource in the Context file */
	private static final String TERRIER_HOME_CONTEXT_KEY = "terrierHome";
	
	/** The key to identify the path of the index used for searching */
	public static final String TERRIER_SEARCH_INDEX_PATH = "search_index_path";

	/** The key to identify the path of the index used for searching */
	public static final String TERRIER_RECOMMENDER_INDEX_PATH = "recommender_index_path";
	
	private static HashMap<ManagerType, TerrierManager> classInstances = new HashMap<>();
	
//	/**
//	 * Default Constructor
//	 */
//	TerrierManager() {
//		this.setTerrierHome();
//		index = Index.createIndex();
//		manager = new Manager(index);
//	}
	
	public static TerrierManager getInstance(ManagerType key) throws RecommenderException{
		TerrierManager manager = classInstances.get(key);
		if (manager == null) {
			switch (key) {
			case SEARCH:
				classInstances.put(
						ManagerType.SEARCH, 
						new TerrierManager(System.getProperty(
								TerrierManager.TERRIER_SEARCH_INDEX_PATH),"data"));
				manager = classInstances.get(key);
				break;
			case RECOMMENDER:
				classInstances.put(
						ManagerType.RECOMMENDER, 
						new TerrierManager(
								System.getProperty(
										TerrierManager.TERRIER_RECOMMENDER_INDEX_PATH),"data"));
				manager = classInstances.get(key);
				break;
			default:
					throw new RecommenderException("Unimplemented TerrierManager");
			}
		}
		return manager;
	}
	
	/**
	 * Specialized constructor to use a different index, not the default in terrier home.
	 * @param indexPath path on the disk
	 * @param indexPrefix the prefix that was used at indexing. Default value is "data".
	 */
	private TerrierManager(String indexPath, String indexPrefix) {
		this.setTerrierHome();
		if(indexPrefix == null || indexPrefix.isEmpty())
			 indexPrefix = "data";
		//index = Index.createIndex();
		index = Index.createIndex(indexPath, indexPrefix);
		manager = new Manager(index);
	}
	
	
	/**
	 * If the Terrier Home is not set, tries to look at it with the JNDI Resources
	 */
	private void setTerrierHome() {
		if(System.getProperty("terrier.home") == null) {
			try
			{
				InitialContext context = new InitialContext();
				String terrier_home = (String) context.lookup("java:/comp/env/" + TERRIER_HOME_CONTEXT_KEY);
				
				System.setProperty("terrier.home", terrier_home);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
//	/**
//     * Returns an instance of the class
//     * @return Singleton instance of the class
//     */
//	public synchronized static TerrierManager getInstance() {
//		try
//		{
//		    if(_ClassInstance == null)
//		    {
//			    _ClassInstance = new TerrierManager();    
//		    }
//		    return _ClassInstance;
//		}
//		catch(Exception ex)
//		{
//            ex.printStackTrace();
//		    return null;
//		}
//	}
	
	
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
	
	public enum ManagerType{
		SEARCH, RECOMMENDER
	}
}
