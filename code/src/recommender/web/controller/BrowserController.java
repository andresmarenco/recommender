package recommender.web.controller;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import recommender.beans.IRStory;
import recommender.beans.IRSubgenre;
import recommender.dataaccess.StoryDAO;
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
	    		
	    		List<IRStory> stories = storyDAO.listStories(subgenre, results, (Math.max(start - 1, 0) * results));
	    		
	    		request.setAttribute("subgenre", subgenre);
	    		request.setAttribute("start", start);
	    		request.setAttribute("results", results);
	    		request.setAttribute("stories", stories);
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
