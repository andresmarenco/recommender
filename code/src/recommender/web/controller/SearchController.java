package recommender.web.controller;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import recommender.beans.IRStory;
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
    
	private String query;
	
	
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
    		QueryManager queryManager = new QueryManager();
			
			List<IRStory> result = queryManager.search(query);
			request.setAttribute("results", result);
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
