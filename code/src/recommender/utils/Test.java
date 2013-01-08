package recommender.utils;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import recommender.beans.IRStory;
import recommender.beans.IRSubgenre;
import recommender.beans.IRUser;
import recommender.dataaccess.ConnectionManager;
import recommender.dataaccess.StoryDAO;
import recommender.dataaccess.TerrierManager;
import recommender.dataaccess.UserDAO;
import recommender.model.ContentUserModel;
import recommender.model.UserModel;
import recommender.model.bag.FeatureBag;
import recommender.querying.QueryManager;
import recommender.querying.RecommendationManager;
import recommender.querying.StoryDisplayer;
import recommender.web.beans.StoryViewTypeBean;

public class Test {

	public static void main(String[] args) {
		try
		{
			System.setProperty(ConnectionManager.URL_KEY, "jdbc:mysql://localhost/meertens");
			System.setProperty(ConnectionManager.DRIVERNAME_KEY, "com.mysql.jdbc.Driver");
			System.setProperty(ConnectionManager.USER_KEY, "root");
			System.setProperty(ConnectionManager.PASSWORD_KEY, "password");
			System.setProperty(ConnectionManager.CONNECTION_SOURCE_KEY, 
					ConnectionManager.ConnectionSource.DIRECT.toString());
			
//			UserDAO userdao = new UserDAO();
//			
//			//IRUser user = userdao.login("root", "123");
//			
//			StoryViewTypeBean sv = new StoryViewTypeBean();
			
//			System.setProperty("terrier.home", "/home/andres/git/recommender/code/resources/terrier-3.5");
			System.setProperty("terrier.home", "C:\\Users\\feroo\\workspace\\my_terrier");
			System.setProperty(TerrierManager.TERRIER_SEARCH_INDEX_PATH, 
					"C:\\Users\\feroo\\workspace\\my_terrier\\var\\index");
			System.setProperty(TerrierManager.TERRIER_RECOMMENDER_INDEX_PATH, 
					"C:\\Users\\feroo\\workspace\\my_terrier\\var\\index");
			
//			System.setProperty("terrier.home", "C:\\cygwin\\home\\feroo\\IR\\terrier-3.5");
//			System.setProperty(TerrierManager.TERRIER_SEARCH_INDEX_PATH, 
//					"C:\\cygwin\\home\\feroo\\IR\\terrier-3.5\\var\\index");
//			
			/*UserDAO userdao = new UserDAO();
			
			IRUser user = userdao.login("root", "123");
			System.out.println(user.getId());*/
			
			new Test().TestRecommendations();
//			new Test().searchTest();
			/*IRUser user = new IRUser();
			user.setPassword("123");
			
			System.out.println(user.getPassword());*/
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void searchTest(){
		QueryManager qm = new QueryManager();
		QueryManager.QueryResult res = qm.search("druten", 1, 100);
		System.out.println("Number of result are: " + res.getStories().size());
		for(IRStory s : res.getStories()){
			System.out.println(s.getTitle().toString());
		}
	}
	
	public void TestRecommendations(){
		IRStory s = new StoryDAO().loadStory(609L, true);
		
		try {
			IRUser user = new UserDAO().loadUser(1);
			RecommendationManager rm = new RecommendationManager();
			UserModel usermodel = new ContentUserModel(user);
			for(IRStory story : rm.recommendStories(usermodel))
			{
				System.out.println(story.getTitle().toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
