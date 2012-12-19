package recommender.utils;

import java.util.LinkedList;

import recommender.beans.IRStory;
import recommender.beans.IRUser;
import recommender.dataaccess.ConnectionManager;
import recommender.dataaccess.StoryDAO;
import recommender.dataaccess.UserDAO;
import recommender.querying.RecommendationManager;

public class Test {

	public static void main(String[] args) {
		try
		{
			System.setProperty(ConnectionManager.URL_KEY, "jdbc:mysql://localhost/recommender");
			System.setProperty(ConnectionManager.DRIVERNAME_KEY, "com.mysql.jdbc.Driver");
			System.setProperty(ConnectionManager.USER_KEY, "root");
			System.setProperty(ConnectionManager.PASSWORD_KEY, "123");
			System.setProperty(ConnectionManager.CONNECTION_SOURCE_KEY, ConnectionManager.ConnectionSource.DIRECT.toString());
			
			System.setProperty("terrier.home", "/home/andres/git/recommender/code/resources/terrier-3.5");
			
			IRUser u = new IRUser();
			u.setUsername("test");
			u.setPassword("test123");
			u.setName("Test User");
			
			UserDAO userDAO = new UserDAO();
			userDAO.createUser(u, true);
			
			System.out.println(u.getId());
			/*UserDAO userdao = new UserDAO();
			
			IRUser user = userdao.login("root", "123");
			System.out.println(user.getId());*/
			
			/*IRStory s = new StoryDAO().loadStory(609L, true);
			
			RecommendationManager rm = new RecommendationManager();
			rm.recommendStories(null, s, new LinkedList<Long>());*/
			
			
			/*IRUser user = new IRUser();
			user.setPassword("123");
			
			System.out.println(user.getPassword());*/
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
