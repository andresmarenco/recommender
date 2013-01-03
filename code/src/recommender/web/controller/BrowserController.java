package recommender.web.controller;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import recommender.beans.IRStory;
import recommender.beans.IRSubgenre;
import recommender.dataaccess.StoryDAO;
import recommender.utils.RecommenderException;
import recommender.web.FormActionServlet;
import recommender.web.WebUtil;

/**
 * Servlet implementation class BrowserController
 */
@WebServlet(name = "stories_list", urlPatterns = { "/stories_list" })
public class BrowserController extends FormActionServlet {
	private static final long serialVersionUID = 201212200325L;
	
	public static final int DEFAULT_RESULTS_PER_PAGE = 20;
	
	private Long subgenreId;
	private Integer start;
	private Integer results;
	

    /**
     * @see FormActionServlet#FormActionServlet(String) 
     */
    public BrowserController() {
        super("/stories_list.jsp");
    }

    
    
    /**
     * Set variables
     */
    public void beforeBrowse() {
    	try
    	{
    		subgenreId = WebUtil.getLongParameter(request, "subgenre");
    		start = WebUtil.getIntegerParameter(request, "start");
    		if(start == null) start = 1;
    		results = WebUtil.getIntegerParameter(request, "results");
    		if(results == null) results = DEFAULT_RESULTS_PER_PAGE;
    	}
		catch(Exception ex) {
			ex.printStackTrace();
			WebUtil.addFieldError(errors, "default", ex.getMessage());
		}
    }
    
    
    
    
    /**
     * Finds the list of stories that matches the criteria
     */
    public void onBrowse() {
    	try
    	{
    		if(this.subgenreId != null) {
	    		StoryDAO storyDAO = new StoryDAO();
	    		IRSubgenre subgenre = storyDAO.loadSubgenre(this.subgenreId.longValue());
	    		int offset = (Math.max(start - 1, 0) * results);
	    		
	    		if((subgenre != null) && (subgenre.getId() != Long.MIN_VALUE)) {
	    			List<IRStory> stories = storyDAO.listStories(subgenre, results, offset, StoryDAO.StoriesOrder.MOST_VIEWED);
		    		
		    		request.setAttribute("subgenre", subgenre);
		    		request.setAttribute("start", start);
		    		request.setAttribute("results", results);
		    		request.setAttribute("stories", stories);
		    		
		    		request.setAttribute("first_story", Math.min(offset + 1, subgenre.getTotal()));
		    		request.setAttribute("last_story", Math.min(offset + results, subgenre.getTotal()));
	    		} else {
	    			WebUtil.addFieldError(errors, "default", RecommenderException.MSG_UNKNOWN_SUBGENRE);
	    			
	    			request.setAttribute("first_story", 0);
		    		request.setAttribute("last_story", 0);
	    		}
	    		
	    		
    		}
    	}
		catch(Exception ex) {
			ex.printStackTrace();
			WebUtil.addFieldError(errors, "default", ex.getMessage());
		}
    }
    
    
    
    
    /**
     * Servlet default action
     */
    @Override
    protected String getAction() {
    	return "Browse";
    }
}
