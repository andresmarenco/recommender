package recommender.web.controller;

import java.io.Serializable;
import java.util.List;

import recommender.beans.IRSubgenre;
import recommender.dataaccess.StoryDAO;

public class BrowserBean implements Serializable {

	private static final long serialVersionUID = 201212101450L;
	
	/**
	 * Default Constructor
	 */
	public BrowserBean() {
		
	}


	/**
	 * Lists all the subgenres
	 * @return
	 */
	public List<IRSubgenre> getSubgenres() {
		StoryDAO storyDAO = new StoryDAO();
		return storyDAO.listSubgenres(null);
	}
}
