package recommender.web.controller;

import javax.servlet.annotation.WebServlet;

import recommender.querying.QueryManager;
import recommender.utils.RecommenderException;
import recommender.web.FormActionServlet;
import recommender.web.WebUtil;

/**
 * Servlet implementation class SearchController
 */
@WebServlet(name = "search.do", urlPatterns = { "/search.do" })
public class SearchController extends FormActionServlet {
	private static final long serialVersionUID = 201212100042L;
	
	public static final int DEFAULT_RESULTS_PER_PAGE = 20;
    
	private String query;
	private Integer start;
	private Integer results;
	
	
    /**
     * @see FormActionServlet#FormActionServlet()
     */
    public SearchController() {
        super("/search.jsp");
    }
    
    
    
    
    /**
     * Set variables
     */
    public void beforeSearch() {
    	try
    	{
    		query = request.getParameter("query");
    		query = (query != null) ? query.trim() : "";
    		start = WebUtil.getIntegerParameter(request, "start");
    		if(start == null) start = 1;
    		results = WebUtil.getIntegerParameter(request, "results");
    		if(results == null) results = DEFAULT_RESULTS_PER_PAGE;
    	}
		catch(Exception ex) {
			ex.printStackTrace();
			WebUtil.addFieldError(errors, "default", RecommenderException.MSG_UNKNOWN_ERROR);
		}
    }
    
    
    
    
    /**
     * Searches a query into the index
     */
    public void onSearch() {
    	try
    	{
    		int offset = (Math.max(start - 1, 0) * results);
    		
    		QueryManager queryManager = new QueryManager();
    		QueryManager.QueryResult result = queryManager.search(query,  offset, results);
			
    		request.setAttribute("start", start);
    		request.setAttribute("results", results);
    		request.setAttribute("stories", result.getStories());
    		request.setAttribute("complete_size", result.getComplete_size());
    		
    		request.setAttribute("first_story", Math.min(offset + 1, result.getComplete_size()));
    		request.setAttribute("last_story", Math.min(offset + results, result.getComplete_size()));
    	}
		catch(Exception ex) {
			ex.printStackTrace();
			WebUtil.addFieldError(errors, "default", RecommenderException.MSG_UNKNOWN_ERROR);
		}
    }
    
    
    
    
    /**
     * Servlet default action
     */
    @Override
    protected String getAction() {
    	return "Search";
    }
}
