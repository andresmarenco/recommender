package recommender.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import recommender.beans.IRStory;
import recommender.beans.IRStoryUserStatistics;
import recommender.beans.IRUser;
import recommender.dataaccess.ConnectionManager;
import recommender.dataaccess.EventDAO;
import recommender.dataaccess.TerrierManager;
import recommender.dataaccess.UserDAO;
import recommender.model.ContentUserModel;
import recommender.model.UserModel;
import recommender.querying.RecommendationManager;
import recommender.web.controller.StoryScoreController;

public class Evaluation {

	private static final String OUTPUT_FILE_NAME = "/home/andres/evaluation.txt";
	private BufferedWriter writer;
	private RecommendationManager recommendationManager;
	
	public Evaluation() {
		this.recommendationManager = new RecommendationManager();
		this.initBufferedWriter(OUTPUT_FILE_NAME);
	}
	
	
	private BufferedWriter initBufferedWriter(String pathname) {
		this.writer = null;
		
		try
		{
			File file = new File(pathname);
			if(!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
			this.writer = new BufferedWriter(fw);
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		
		return this.writer;
	}
	
	
	
	
	private void closeWriter() {
		try
		{
			this.writer.close();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	private void evaluateUser(IRUser user) {
		try
		{
			EventDAO eventDAO = new EventDAO();
			List<IRStoryUserStatistics> viewedStories = eventDAO.listUserStoryViews(user);
			Collections.shuffle(viewedStories);
			
			int size = viewedStories.size();
			int limit = Math.round(size * 0.5F);
			
			List<IRStoryUserStatistics> trainingSet = viewedStories.subList(limit, size);
			List<IRStoryUserStatistics> testSet = viewedStories.subList(0, limit);
			Map<IRStory, IRStoryUserStatistics> testSetMap = new HashMap<IRStory, IRStoryUserStatistics>();
			
			int likes = 0;
			
			for(IRStoryUserStatistics stats : trainingSet) {
				if(stats.getScore() == StoryScoreController.LIKE_SCORE) likes++;
			}
			System.out.println("TRAINING SET LIKES: " + likes);
			
			
			likes = 0;
			System.out.println("TEST SET: ");
			for(IRStoryUserStatistics stats : testSet) {
				testSetMap.put(stats.getStory(), stats);
				System.out.print(stats.getStory().getCode() + " / ");
				if(stats.getScore() == StoryScoreController.LIKE_SCORE) likes++;
			}
			System.out.println();
			System.out.println("TEST SET LIKES: " + likes);
			
			
			UserModel userModel = new ContentUserModel(user, trainingSet);
			
			for (int i = 0; i < 10; i++) {
				System.out.println("ITERATION " + (i+1));
				List<IRStory> recommendations = this.recommendationManager.recommendStories(userModel);
				for(IRStory story : recommendations) {
					System.out.print(MessageFormat.format("Story: {0}", story.getCode()));
					IRStoryUserStatistics testStats = testSetMap.get(story);
					if(testStats != null) {
						System.out.println(MessageFormat.format(" / Score in Test Set: {0}", testStats.getScore()));
					} else {
						System.out.println();
					}
				}
				
				System.out.println("-----------------------------------\n\n\n");
			}
			
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	public void evaluate() {
		try
		{
			UserDAO userDAO = new UserDAO();
			
			this.evaluateUser(userDAO.loadUser(25l));
			
			
			this.closeWriter();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	public static void main(String[] args) {
		try
		{
			System.setProperty(ConnectionManager.URL_KEY, "jdbc:mysql://localhost/recommender_server");
			System.setProperty(ConnectionManager.DRIVERNAME_KEY, "com.mysql.jdbc.Driver");
			System.setProperty(ConnectionManager.USER_KEY, "root");
			System.setProperty(ConnectionManager.PASSWORD_KEY, "123");
			System.setProperty(ConnectionManager.CONNECTION_SOURCE_KEY, 
					ConnectionManager.ConnectionSource.DIRECT.toString());
			
			System.setProperty("terrier.home", "/usr/local/terrier");
			System.setProperty(TerrierManager.TERRIER_SEARCH_INDEX_PATH, "/usr/local/terrier/var/index");
			System.setProperty(TerrierManager.TERRIER_RECOMMENDER_INDEX_PATH, "/usr/local/terrier/var/index_recommender");
			
			
			Evaluation evaluation = new Evaluation();
			evaluation.evaluate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

