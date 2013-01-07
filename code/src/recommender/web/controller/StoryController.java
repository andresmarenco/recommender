package recommender.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.dataaccess.StoryDAO;
import recommender.model.UserModel;
import recommender.querying.StoryDisplayer;
import recommender.utils.RecommenderException;
import recommender.web.WebUtil;

/**
 * Servlet implementation class StoryController
 */
@WebServlet(name = "story", urlPatterns = { "/story" })
public class StoryController extends HttpServlet {
	private static final long serialVersionUID = 201212100241L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StoryController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			HttpSession session = request.getSession(true);
			Long story_id = WebUtil.getLongParameter(request, "id");
			
			if(story_id != null) {
				IRUser user = (IRUser)session.getAttribute("credential");
				
				/*@SuppressWarnings("unchecked")
				Queue<Long> story_session = (Queue<Long>)session.getAttribute("story_session");
				if(story_session == null) story_session = new LinkedList<Long>();*/
				Long view_type = WebUtil.getLongParameter(request, "vt");
				
				StoryDisplayer storyDisplayer = new StoryDisplayer();
				IRStory story = storyDisplayer.showStory(story_id, user, view_type);
				
				StoryDAO storyDAO = new StoryDAO();
				IRStoryUserStatistics stats = storyDAO.getStoryUserStatistics(story, user);
				
				UserModel user_model = UserModel.getSessionInstance(session, user);
				user_model.viewedStory(story, stats);
				
				request.setAttribute("score", stats.getScore());
				request.setAttribute("story", story);
			}
		}
		catch(RecommenderException ex) {
			request.setAttribute("errors", ex.getMessage());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			request.setAttribute("errors", RecommenderException.MSG_UNKNOWN_ERROR);
		}
		finally {
			request.getRequestDispatcher("/story.jsp").forward(request, response);
		}
	}

}
