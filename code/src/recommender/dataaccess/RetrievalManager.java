package recommender.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.terrier.matching.ResultSet;
import org.terrier.querying.Manager;
import org.terrier.querying.SearchRequest;
import org.terrier.querying.parser.MultiTermQuery;
import org.terrier.querying.parser.SingleTermQuery;
import org.terrier.structures.MetaIndex;
import org.terrier.utility.ApplicationSetup;

import recommender.beans.IRStory;
import recommender.utils.RecommenderException;

public class RetrievalManager {
	
	private TerrierManager terrierManager;
	
	
	/**
	 * Default Manager
	 */
	public RetrievalManager(TerrierManager tm) {
		this.terrierManager = tm;
	}
	
	public TerrierManager getTerrierManager() {
		return terrierManager;
	}
	
	public void setTerrierManager(TerrierManager terrierManager) {
		this.terrierManager = terrierManager;
	}
	
	/**
	 * Converts a ResultSet to a List of IRStory
	 * @param rs ResultSet with result of query
	 * @return List of Stories
	 * @throws RecommenderException
	 */
	public List<IRStory> getStoriesFromResultSet(ResultSet rs) throws RecommenderException {
		List<IRStory> result = null;
		
		try
		{
			result = new ArrayList<IRStory>();
			StoryDAO storyDAO = new StoryDAO();
			MetaIndex metaIndex = terrierManager.getMetaIndex();
			String[] docnos = metaIndex.getItems("docno", rs.getDocids());
			
			for(String code : docnos) {
				result.add(storyDAO.loadStory(code, true));
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RecommenderException(RecommenderException.MSG_ERROR_TERRIER_CONVERSION);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Uses Terrier to search for a query
	 * @param query Query to search
	 * @return List of Stories
	 * @throws RecommenderException
	 */
	public List<IRStory> searchStories(String query) throws RecommenderException {
		return this.getStoriesFromResultSet(this.search(query));
	}
	
	
	
	/**
	 * Uses Terrier to search for a query
	 * @param query Query to search
	 * @param results Results per page
	 * @return List of Stories
	 * @throws RecommenderException
	 */
	public List<IRStory> searchStories(String query, int results) throws RecommenderException {
		return this.getStoriesFromResultSet(this.search(query, results));
	}
	
	
	
	
	/**
	 * Uses Terrier to search for a query
	 * @param query Query to search
	 * @param start Number of the first result
	 * @param results Results per page
	 * @return List of Stories
	 * @throws RecommenderException
	 */
	public List<IRStory> searchStories(String query, Integer start, Integer results) throws RecommenderException {
		return this.getStoriesFromResultSet(this.search(query, start, results));
	}
	
	
	
	
	/**
	 * Uses Terrier to search for a query
	 * @param query Query to search
	 * @return ResultSet with the results of the query
	 * @throws RecommenderException
	 */
	public ResultSet search(String query) throws RecommenderException {
		return this.search(query, null, null);
	}
	
	
	
	
	/**
	 * Uses Terrier to search for a query
	 * @param query Query to search
	 * @param results Results per page
	 * @return ResultSet with the results of the query
	 * @throws RecommenderException
	 */
	public ResultSet search(String query, int results) throws RecommenderException {
		return this.search(query, null, results);
	}
	
    
	
	
	/**
	 * Uses Terrier to search for a query
	 * @param query Query to search
	 * @param start Number of the first result
	 * @param results Results per page
	 * @return ResultSet with the results of the query
	 * @throws RecommenderException
	 */
    public ResultSet search(String query, Integer start, Integer results) throws RecommenderException {

    	try
    	{
			//breaks down the query into terms, and create a multitermquery, from singletermquery instances
			String[] searchwords = query.split(" ");
			MultiTermQuery mtq = new MultiTermQuery();
			for (String word : searchwords) {
				mtq.add(new SingleTermQuery(word));
			}
			
			return this.search(mtq, start, results);
			
    		
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    		
    		throw new RecommenderException(RecommenderException.MSG_ERROR_TERRIER_RETRIEVAL);
    	}
    	
    }

    /**
	 * performs a search for a multiTermQuery
	 * 
	 * @param mtq
	 * @param start the offset (position) of the first result
	 * @param results the number of the returned results (size of the window)
	 * @return the resultset
     */
	private ResultSet search(MultiTermQuery mtq, Integer start, Integer results) {
		ResultSet rs = null;
		Manager manager = terrierManager.getManager();
		

		
		SearchRequest search = manager.newSearchRequest();
		search.setQuery(mtq);
		search.addMatchingModel("Matching", ApplicationSetup.getProperty("trec.model", "PL2"));
		
		if(results != null) {
			if(start == null) {
				start = new Integer(0); 
			}
			
			search.setControl("start", String.valueOf(Math.max(start, 1)));
			search.setControl("end", String.valueOf(Math.max(start + results - 1, 1)));
		}
		
		manager.runPreProcessing(search);
        manager.runMatching(search);
        manager.runPostProcessing(search);
        manager.runPostFilters(search);
        
        rs = search.getResultSet();
        return rs;
	}

	/**
	 * performs a search for a multiTermQuery
	 * 
	 * @param mtq
	 * @param start the offset (position) of the first result
	 * @param results the number of the returned results (size of the window)
	 * @return list of stories, their size equals or is smaller than the @param results
	 */
	public List<IRStory> searchStories(MultiTermQuery mtq, Integer start, Integer results) {
		try {
			return getStoriesFromResultSet(this.search(mtq, start, results));
		} catch (RecommenderException e) {
			e.printStackTrace();
		}
		return null;
	}
}
