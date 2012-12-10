package recommender.web.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import recommender.beans.IRUser;
import recommender.dataaccess.UserDAO;
import recommender.utils.RecommenderException;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name = "auth.do", urlPatterns = "/auth.do")
public class AuthController extends HttpServlet {
	private static final long serialVersionUID = 201212082307L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthController() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			HttpSession session = request.getSession(true);
			AuthService service = AuthService.valueOf(request.getParameter("auth"));
			
			switch(service) {
			case LOGIN: {
				String username = request.getParameter("username");
				username = (username != null) ? username.trim() : "";
				String password = request.getParameter("password");
				
				UserDAO userDAO = new UserDAO();
				IRUser user = userDAO.login(username, password);
				
				if(user.isLogged()) {
					session.setAttribute("credential", user);
					response.sendRedirect(this.getServletContext().getContextPath() + "/index.jsp");
				}
				
				break;
			}
			
			case LOGOUT: {
				session.removeAttribute("credential");
				session.invalidate();
				response.sendRedirect(this.getServletContext().getContextPath() + "/index.jsp");
				break;
			}
			}
						
		}
		catch(RecommenderException ex) {
			request.setAttribute("errors", ex.getMessage());
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			
			request.setAttribute("errors", RecommenderException.MSG_UNKNOWN_ERROR);
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
	
	
	
	/**
	 * Enumerate the services provided by the controller
	 * @author andres
	 *
	 */
	public enum AuthService {
		LOGIN,
		LOGOUT
	}

}
