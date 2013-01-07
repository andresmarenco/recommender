package recommender.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import recommender.beans.IRUser;
import recommender.utils.ReflectionUtil;

public abstract class FormActionServlet extends HttpServlet {

	private static final long serialVersionUID = 201212191930L;
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected HashMap<String, List<String>> errors;
	protected final String view_servlet;
	private RedirectMethod redirect_method;
	private String redirect_page;
	
	
	/**
	 * Default Constructor
	 */
	public FormActionServlet() {
		super();
        this.errors = new HashMap<String, List<String>>();
        this.view_servlet = null;
	}
	
	
	
	
	/**
	 * Default Constructor
	 * @param view_servlet Name of the JSP with the view of the Servlet
	 */
	public FormActionServlet(String view_servlet) {
		super();
		this.view_servlet = view_servlet;
        this.errors = new HashMap<String, List<String>>();
	}
	
	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.setDefaultRedirect();
		this.redirect_page = this.view_servlet;
		
		this.request = request;
		this.response = response;
		this.session = request.getSession(true);
		
		this.errors.clear();
		this.execute();
		this.request.setAttribute("errors", errors);
		
		// Redirecting to the next page or view of the Servlet
		switch(this.redirect_method) {
		case REDIRECT: {
			response.sendRedirect(this.redirect_page);
			break;
		}
		
		case FORWARD: {
			request.getRequestDispatcher(this.view_servlet).forward(request, response);
			break;
		}
		
		case NONE: {
			break;
		}
		}
	}
	
	
	
	/**
	 * Executes the action defined if the form:
	 * Tries to call beforeAction, onAction and afterAction
	 * Where Action is the name of the parameter 'action'
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void execute() throws ServletException, IOException {
		try
		{
			String action = this.getAction();
			
			if(action != null) {
				action = action.trim();
				Class<?> clazz = this.getClass();
				
				ReflectionUtil.invokeMethodIfExists(clazz, this, "before" + action);
				ReflectionUtil.invokeMethodIfExists(clazz, this, "on" + action);
				ReflectionUtil.invokeMethodIfExists(clazz, this, "after" + action);
			}
		}
		catch(InvocationTargetException ex) {
			if(ex.getCause() instanceof ServletException) {
				throw (ServletException)ex.getCause();
			} else if(ex.getCause() instanceof IOException) {
				throw (IOException)ex.getCause();
			} else {
				ex.printStackTrace();
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Gets the user currently logged in the session
	 * @return Logged user
	 */
	protected IRUser getUserCredential() {
		return (IRUser)session.getAttribute("credential");
	}
	
	
	
	
	/**
	 * Gets the Form Action
	 * @return String with the Form Action
	 */
	protected String getAction() {
		return request.getParameter("action");
	}
	
	
	
	/**
	 * Defines the default redirect to forward to the View of the Servlet
	 */
	protected void setDefaultRedirect() {
		this.redirect_method = (this.view_servlet != null) ? RedirectMethod.FORWARD : RedirectMethod.NONE;
	}
	
	
	
	/**
	 * Defines the redirect of the page using sendRedirect (ignoring the view of the Servlet)
	 * @param page Page to redirect
	 */
	protected void setDefaultRedirect(String page) {
		this.redirect_method = RedirectMethod.REDIRECT;
		this.redirect_page = page;
	}
	
	
	
	
	/**
	 * Finds an attribute in the session and casts it to the specified class
	 * @param name Name of the attribute
	 * @param clazz Class of the attribute
	 * @return Casted Attribute or null
	 */
	protected <T> T getSessionAttribute(String name, Class<T> clazz) {
		return clazz.cast(this.session.getAttribute(name));
	}
	
	
	
	/**
	 * Methods to Redirect the Servlet
	 * @author andres
	 *
	 */
	private static enum RedirectMethod {
		REDIRECT,
		FORWARD,
		NONE
	}
}
