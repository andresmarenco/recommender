package recommender.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import recommender.beans.IRStory;
import recommender.querying.QueryManager;
import recommender.utils.RecommenderException;

/**
 * Servlet implementation class SearchController
 */
@WebServlet(name = "search.do", urlPatterns = { "/search.do" })
public class SearchController extends HttpServlet {
	private static final long serialVersionUID = 201212100042L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchController() {
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
			QueryManager queryManager = new QueryManager();
			String query = request.getParameter("query");
			query = (query != null) ? query.trim() : "";
			
			List<IRStory> result = queryManager.search(query);
			request.setAttribute("results", result);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			request.setAttribute("errors", RecommenderException.MSG_UNKNOWN_ERROR);
		}
		finally {
			request.getRequestDispatcher("/search.jsp").forward(request, response);
		}
	}

}
