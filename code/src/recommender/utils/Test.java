package recommender.utils;

import recommender.beans.IRUser;
import recommender.dataaccess.ConnectionManager;
import recommender.dataaccess.UserDAO;

public class Test {

	public static void main(String[] args) {
		try
		{
			System.setProperty(ConnectionManager.URL_KEY, "jdbc:mysql://localhost/recommender");
			System.setProperty(ConnectionManager.DRIVERNAME_KEY, "com.mysql.jdbc.Driver");
			System.setProperty(ConnectionManager.USER_KEY, "root");
			System.setProperty(ConnectionManager.PASSWORD_KEY, "123");
			System.setProperty(ConnectionManager.CONNECTION_SOURCE_KEY, ConnectionManager.ConnectionSource.DIRECT.toString());
			
			UserDAO userdao = new UserDAO();
			
			IRUser user = userdao.login("root", "123");
			System.out.println(user.getId());
			/*IRUser user = new IRUser();
			user.setPassword("123");
			
			System.out.println(user.getPassword());*/
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
