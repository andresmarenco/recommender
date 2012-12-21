package recommender.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import recommender.beans.IRUser;
import recommender.dataaccess.UserDAO;
import recommender.utils.RecommenderException;
import recommender.utils.ValidationUtil;
import recommender.web.FormActionServlet;
import recommender.web.WebUtil;

/**
 * Servlet implementation class RegisterController
 */
@WebServlet(name = "register.do", urlPatterns = { "/register.do" })
public class RegisterController extends FormActionServlet {
	private static final long serialVersionUID = 201212160434L;
	
	private String fullname;
	private String username;
	private String password;
	private String retype_password; 
	
	
    /**
     * @see FormActionServlet#FormActionServlet()
     */
    public RegisterController() {
        super("/register.jsp");
    }
    
    
    
    /**
     * Set variables and validation
     */
    public void beforeCreate() {
    	try
		{	
			fullname = request.getParameter("fullname");
			username = request.getParameter("username");
			password = request.getParameter("password");
			retype_password = request.getParameter("retype_password");
		
			this.validate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			WebUtil.addFieldError(errors, "default", ex.getMessage());
		}
    }
	
	
	
    /**
     * Creation of the user
     * @throws ServletException
     * @throws IOException
     */
	public void onCreate() throws ServletException, IOException {
		try
		{
			if(errors.isEmpty()) {
				IRUser user = new IRUser();
				user.setName(fullname);
				user.setUsername(username);
				user.setPassword(password);
				
				UserDAO userDAO = new UserDAO();
				user = userDAO.createUser(user, true);
				
				session.setAttribute("credential", user);
				this.setDefaultRedirect(this.getServletContext().getContextPath() + "/index.jsp");
				
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
	 * Validation of fields
	 * @throws RecommenderException
	 */
	private void validate() throws RecommenderException {
		if(WebUtil.checkRequiredField(errors, "fullname", fullname)) {
			fullname = fullname.trim();	
		}
		
		
		if(WebUtil.checkRequiredField(errors, "username", username)) {
			username = username.trim();
			
			if(!ValidationUtil.validateUsername(username)) {
				WebUtil.addFieldError(errors, "username", RecommenderException.MSG_ERROR_USERNAME_POLICY);
			}
			
			if(ValidationUtil.existingUsername(username)) {
				WebUtil.addFieldError(errors, "username", RecommenderException.MSG_ERROR_EXISTING_USERNAME);
			}
		}
		
		
		if(WebUtil.checkRequiredField(errors, "password", password)) {
			if(!ValidationUtil.validatePassword(password)) {
				WebUtil.addFieldError(errors, "password", RecommenderException.MSG_ERROR_PASSWORD_POLICY);
			}
		}
		
		
		if(WebUtil.checkRequiredField(errors, "retype_password", retype_password)) {
			if(!password.equals(retype_password)) {
				WebUtil.addFieldError(errors, "retype_password", RecommenderException.MSG_ERROR_CONFIRM_PASSWORD);
			}
		}
	}
}
