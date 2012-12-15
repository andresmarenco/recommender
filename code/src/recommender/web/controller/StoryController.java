package recommender.web.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import recommender.beans.IRStory;
import recommender.beans.IRUser;
import recommender.querying.StoryDisplayer;
import recommender.utils.RecommenderException;

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
			
			String string_story_id = request.getParameter("id");
			Long story_id = (string_story_id != null) ? Long.parseLong(string_story_id) : null;
			
			if(story_id != null) {
				IRUser user = (IRUser)session.getAttribute("credential");
				
				@SuppressWarnings("unchecked")
				Queue<Long> story_session = (Queue<Long>)session.getAttribute("story_session");
				if(story_session == null) story_session = new LinkedList<Long>();
				
				StoryDisplayer storyDisplayer = new StoryDisplayer();
				IRStory story = storyDisplayer.showStory(story_id.longValue(), user, story_session);
				
				request.setAttribute("story", story);
				session.setAttribute("story_session", story_session);
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
