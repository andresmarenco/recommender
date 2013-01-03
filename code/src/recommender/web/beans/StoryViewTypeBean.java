package recommender.web.beans;

import java.io.Serializable;
import java.util.Hashtable;

import recommender.beans.IRStoryViewType;
import recommender.dataaccess.EventDAO;

public class StoryViewTypeBean implements Serializable {

	private static final long serialVersionUID = 201212240131L;
	
	public Hashtable<String, IRStoryViewType> view_types;
	
	/**
	 * Default Constructor
	 */
	public StoryViewTypeBean() {
		this.init();
	}
	
	
	
	/**
	 * Initialized the Values in the Hash Table
	 */
	private void init() {
		try
		{
			view_types = new Hashtable<String, IRStoryViewType>();
			EventDAO eventDAO = new EventDAO();
			for(IRStoryViewType viewType : eventDAO.listViewTypes()) {
				view_types.put(viewType.getName(), viewType);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}



	/**
	 * @return the view_types
	 */
	public Hashtable<String, IRStoryViewType> getView_types() {
		return view_types;
	}
}
