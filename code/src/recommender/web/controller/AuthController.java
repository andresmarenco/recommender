package recommender.web.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import recommender.beans.IRUser;
import recommender.dataaccess.UserDAO;
import recommender.utils.RecommenderException;
import recommender.web.FormActionServlet;
import recommender.web.WebUtil;

/**
 * Servlet implementation class AuthController
 */
@WebServlet(name = "auth.do", urlPatterns ={"/auth.do", "/login.do"} )
public class AuthController extends FormActionServlet {
	private static final long serialVersionUID = 201212082307L;
    
	private String username;
	private String password;
	
	
    /**
     * @see FormActionServlet#FormActionServlet()
     */
    public AuthController() {
        super("/login.jsp");
    }
    
    
    
    
    /**
     * Set variables and validation
     */
    public void beforeLogin() {
    	try
    	{
    		username = request.getParameter("username");
			username = (username != null) ? username.trim() : "";
			password = request.getParameter("password");
			
			WebUtil.checkRequiredField(errors, "username", username);
    		WebUtil.checkRequiredField(errors, "password", password);
    	}
		catch(Exception ex) {
			ex.printStackTrace();
			WebUtil.addFieldError(errors, "default", ex.getMessage());
		}
    }
    
    
    
    
    /**
     * Logs in a User into the system
     * @throws ServletException
     * @throws IOException
     */
    public void onLogin() throws ServletException, IOException {
    	try
		{
    		if(errors.isEmpty()) {
    			UserDAO userDAO = new UserDAO();
				IRUser user = userDAO.login(username, password);
				
				if(user.isLogged()) {
					session.setAttribute("credential", user);
					this.setDefaultRedirect(this.getServletContext().getContextPath() + "/index.jsp");
				}
    		}
		}
    	catch(RecommenderException ex) {
    		WebUtil.addFieldError(errors, "default", ex.getMessage());
    	}
		catch(Exception ex) {
			ex.printStackTrace();
			WebUtil.addFieldError(errors, "default", RecommenderException.MSG_UNKNOWN_ERROR);
		}
    }
    
    
    
    
    /**
     * Logs out a User from the system
     * @throws ServletException
     * @throws IOException
     */
    public void onLogout() throws ServletException, IOException {
    	try
    	{
    		session.removeAttribute("credential");
			session.invalidate();
    		this.setDefaultRedirect(this.getServletContext().getContextPath() + "/index.jsp");
    	}
		catch(Exception ex) {
			ex.printStackTrace();
			WebUtil.addFieldError(errors, "default", ex.getMessage());
		}
    }

}
