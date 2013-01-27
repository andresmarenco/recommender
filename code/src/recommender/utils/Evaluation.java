package recommender.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
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
import recommender.model.CachedContentUserModel;
import recommender.model.UserModel;
import recommender.querying.RecommendationManager;
import recommender.querying.RecommendationManager.RecommendationMetadata;
import recommender.web.controller.StoryScoreController;

public class Evaluation {

	private static final String OUTPUT_FILE_NAME = "/home/andres/models_evaluation.txt";
	private static final String HISTOGRAM5_FILE_NAME = "/home/andres/models_histogram5.txt";
	private static final String HISTOGRAM10_FILE_NAME = "/home/andres/models_histogram10.txt";
	private static final String HISTOGRAM20_FILE_NAME = "/home/andres/models_histogram20.txt";
	private static final int RECOMMENDATIONS_TOTAL = 25;
	private static final int EVALUATION_ITERATIONS = 100;
	
	/**
	 * The number of pieces used in the validation.
	 */
	private static final int CROSS_VALIDATION_PIECES = 3;
	
	private HistogramList histograms;
	private BufferedWriter writer;
	private RecommendationManager recommendationManager;
	private int queryId;
	private EventDAO eventDAO;
	
	
	/**
	 * Default Constructor
	 */
	public Evaluation() {
		this.recommendationManager = new RecommendationManager();
		this.writer = initBufferedWriter(OUTPUT_FILE_NAME);
		this.histograms = new HistogramList();
		this.histograms.add(new Histogram(HISTOGRAM5_FILE_NAME, 5));
		this.histograms.add(new Histogram(HISTOGRAM10_FILE_NAME, 10));
		this.histograms.add(new Histogram(HISTOGRAM20_FILE_NAME, 20));
		this.eventDAO = new EventDAO();
		
		this.queryId = 0;
	}
	
	
	
	
	/**
	 * Creates a BufferedWriter in the path name. If it already exists, clears it 
	 * @param pathName Name of the file for the writer
	 * @return BufferedWriter in the specified path name.
	 */
	private static BufferedWriter initBufferedWriter(String pathName) {
		BufferedWriter writer = null;
		
		try
		{
			File file = new File(pathName);
			if(!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
			writer = new BufferedWriter(fw);
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		
		return writer;
	}
	
	
	
	
	/**
	 * Closes the specified BufferedWriter
	 * @param writer Writer to close
	 */
	private static void closeWriter(BufferedWriter writer) {
		try
		{
			if(writer != null) writer.close();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Closes all the writers used in the evaluation
	 */
	private void closeAllWriters() {
		Evaluation.closeWriter(this.writer);
		this.histograms.closeAllWriters();
	}
	
	
	
	
	/**
	 * Runs the evaluation on a specified user
	 * @param user User to evaluate
	 */
	private void evaluateUser(IRUser user) {
		try
		{
			// Gets the log of all the viewed stories of the user and puts it into a hash map
			List<IRStoryUserStatistics> viewedStories = (user != null) ? eventDAO.listUserStoryViews(user) : eventDAO.listAllUsersStoryViews(null);
			Collections.shuffle(viewedStories);
			
			Map<IRStory, IRStoryUserStatistics> viewedSetMap = new HashMap<IRStory, IRStoryUserStatistics>();
			for(IRStoryUserStatistics stats : viewedStories) {
				viewedSetMap.put(stats.getStory(), stats);
			}
			
			int viewedTotal = viewedStories.size();
			double pieceSize = viewedTotal / CROSS_VALIDATION_PIECES;
			
			
			
			// Separates the log into a test set and a training set and counts their size and
			// how many likes they contain
			List<IRStoryUserStatistics> trainingSet;
			List<IRStoryUserStatistics> testSet;
			

			
			// Sets the current user for the histogram
			this.histograms.setCurrentUser(user);
			
			
			
			for (int i = 0; i < CROSS_VALIDATION_PIECES; i++) {
				trainingSet = new ArrayList<IRStoryUserStatistics>();
				testSet = new ArrayList<IRStoryUserStatistics>();
				testSet = viewedStories.subList((int)(i * pieceSize), (int)((i + 1) * pieceSize));
				trainingSet.addAll(viewedStories);
				trainingSet.removeAll(testSet);
				evaluateUser(user, trainingSet, testSet, viewedSetMap, viewedTotal);
			}
			
			
			// Flushes the histogram into the files
			this.histograms.flushAll();
			
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	private void evaluateUser(IRUser user,
			List<IRStoryUserStatistics> trainingSet,
			List<IRStoryUserStatistics> testSet,
			Map<IRStory, IRStoryUserStatistics> viewedSetMap,
			int viewedTotal) throws IOException {
		
		int trainingLikes = 0;
		int testLikes = 0;
		Map<IRStory, IRStoryUserStatistics> testSetMap = new HashMap<IRStory, IRStoryUserStatistics>();
		
		for(IRStoryUserStatistics stats : trainingSet) {
			if(stats.getScore() > StoryScoreController.NEUTRAL_SCORE) trainingLikes++;
		}
		System.out.println("TRAINING LIKES " + trainingLikes);
		
		int testTotal = testSet.size();
		int trainingTotal = trainingSet.size();
		for(IRStoryUserStatistics stats : testSet) {
			testSetMap.put(stats.getStory(), stats);
			if(stats.getScore() > StoryScoreController.NEUTRAL_SCORE) testLikes++;
		}
		
		System.out.println("TEST LIKES " + testLikes);
		
		
		// Creates a user model using only the training set. 
		// Iterates EVALUATION_ITERATIONS times, applying the recommendation
		// algorithm and checking if the recommended stories are part of the
		// test set and also if they are part of the complete log.
		// After that, constructs a histogram with the found recommendations. 
		if(user == null) {
			user = new IRUser();
			user.setId(-1L);
		}
		UserModel userModel = new CachedContentUserModel(user, trainingSet);
		
					
					
		for (int i = 0; i < EVALUATION_ITERATIONS; i++) {
			this.histograms.resetAllCounters();
			
			System.out.println(MessageFormat.format("User: {0} / Iteration: {1}", user.getId(), (i+1)));
			queryId++;
			RecommendationMetadata recommendations = this.recommendationManager.recommendStoriesMetadata(userModel, RECOMMENDATIONS_TOTAL);
			double foundStories = 0;
			double retrievedStories = 0;
			double precesionAt5 = 0;
			double precesionAt10 = 0;
			double precesionAt20 = 0;
			double recallAt5 = 0;
			double recallAt10 = 0;
			double recallAt20 = 0;
			for(IRStory story : recommendations.getResult()) {
				
				IRStoryUserStatistics testStats = testSetMap.get(story);
				if(testStats != null) {
					if (testStats.getScore() > 0) {
						foundStories++;
					}
				}
				
				
				
//				IRStoryUserStatistics viewStats = viewedSetMap.get(story);
//				if(viewStats != null) {
//					this.writer.write(MessageFormat.format("{0};{1};{2};{3};{4};{5};{6};{7};{8};{9};{10}",
//							user.getId(),
//							story.getCode(),
//							viewStats.getScore(),
//							story.getRecommendationRank(),
//							testSetMap.containsKey(story),
//							queryId,
//							recommendations.getQuery(),
//							testTotal,
//							viewedTotal,
//							testLikes,
//							(testLikes+trainingLikes)
//							));
//					this.writer.newLine();
//					if (viewStats.getScore() > 0) {
//						foundStories++;
//					}
//					
//				}
				
				retrievedStories++;
				

				if(retrievedStories == 5) {
					precesionAt5 = foundStories / retrievedStories;
//					recallAt5 = foundStories / (testLikes + trainingLikes);
					recallAt5 = foundStories / Math.max(testLikes, 1);
				}
				if(retrievedStories == 10) {
					precesionAt10 = foundStories / retrievedStories;
//					recallAt10 = foundStories / (testLikes + trainingLikes);
					recallAt10 = foundStories / Math.max(testLikes, 1);
				}
				if(retrievedStories == 20) {
					precesionAt20 = foundStories / retrievedStories;
//					recallAt20 = foundStories / (testLikes + trainingLikes);
					recallAt20 = foundStories / Math.max(testLikes, 1);
				}
				
				this.histograms.putInAll(story);
			}
			

			this.writer.write(MessageFormat.format("{0};{1};{2};{3};{4};{5};{6};{7};{8};{9};{10};{11}",
					user.getId(),
					queryId,
					precesionAt5,
					precesionAt10,
					precesionAt20,
					recallAt5,
					recallAt10,
					recallAt20,
					testTotal,
					trainingTotal,
					testLikes,
					trainingLikes
					));
			this.writer.newLine();
		}
		
	}




	/**
	 * Performs the evaluation on all the users of the system
	 */
	public void evaluate() {
		try
		{
//			this.writer.write("User ID; Story Code; Score; Rank; Present in Test Set; Query ID; Query; # Stories in Test Set; # Stories in Total; # Likes in Test Set; # Likes in Total");
			this.writer.write("User ID; Query ID; P@5; P@10; P@20; R@5; R@10; R@20; Test Size; Training Size; Test Likes; Training Likes ");
			this.writer.newLine();
			
			// Evaluates all the users (except root)
			UserDAO userDAO = new UserDAO();
			for(IRUser user : userDAO.listUsers()) {
				if((!user.isRoot()) && (user.isActive())) {
					this.evaluateUser(user);
				}
			}
			
			// User with all the data
			this.evaluateUser(null);
		
			this.closeAllWriters();
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
	
	
	
	/**
	 * Histogram list
	 * @author andres
	 *
	 */
	private static class HistogramList extends ArrayList<Histogram> {
		private static final long serialVersionUID = 201301140140L;
		
		/**
		 * Closes all the histograms in the list
		 */
		public void closeAllWriters() {
			for(Histogram histogram : this) {
				histogram.closeWriter();
			}
		}
		
		
		/**
		 * Resets all counter of the histograms in the list
		 */
		public void resetAllCounters() {
			for(Histogram histogram : this) {
				histogram.resetCounter();
			}
		}
		
		
		/**
		 * Sets the user in all the histograms in the list
		 * @param user Current User
		 */
		public void setCurrentUser(IRUser user) {
			for(Histogram histogram : this) {
				histogram.setCurrentUser(user);
			}
		}
		
		
		/**
		 * Puts a story in all the histograms in the list
		 * @param story Story
		 */
		public void putInAll(IRStory story) {
			for(Histogram histogram : this) {
				histogram.put(story);
			}
		}
		
		
		/**
		 * Flushes all the histograms into their corresponding output files
		 */
		public void flushAll() {
			for(Histogram histogram : this) {
				histogram.flush();
			}
		}
	}
	
	
	
	/**
	 * Hash Map with the Histogram of the Recommendations
	 * @author andres
	 *
	 */
	private static class Histogram extends HashMap<IRStory, Integer> {
		
		private static final long serialVersionUID = 201301140139L;
		
		private BufferedWriter histogramWriter;
		private final int limit;
		private int currentCount;
		private IRUser currentUser;
		
		/**
		 * Default Constructor
		 * @param pathName Path Name of the Output File
		 * @param limit Limit of the histogram (evaluate n recommendations per iteration)
		 */
		public Histogram(String pathName, int limit) {
			this.histogramWriter = initBufferedWriter(pathName);
			this.limit = limit;
			this.currentCount = 0;
			
			try
			{
				this.histogramWriter.write("User Id; Story Code; Story Count");
				this.histogramWriter.newLine();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		@Override
		public void clear() {
			super.clear();
			this.currentUser = null;
			this.resetCounter();
		}
		
		
		/**
		 * Resets the counter of the recommendations in the iteration
		 */
		public void resetCounter() {
			this.currentCount = 0;
		}
		
		
		/**
		 * Current user for the Histogram
		 * @param user
		 */
		public void setCurrentUser(IRUser user) {
			this.currentUser = user;
		}
		
		
		/**
		 * Flushes the content of the Histogram into the output file
		 */
		public void flush() {
			try
			{
				if(this.currentUser == null) {
					this.currentUser = new IRUser();
					this.currentUser.setId(-1L);
				}
				
				for(Map.Entry<IRStory, Integer> values : this.entrySet()) {
					this.histogramWriter.write(MessageFormat.format("{0};{1};{2}",
							this.currentUser.getId(),
							values.getKey().getCode(),
							values.getValue()));
					
					this.histogramWriter.newLine();
				}
				
				this.clear();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		
		/**
		 * Closes the writer to the output file
		 */
		public void closeWriter() {
			Evaluation.closeWriter(this.histogramWriter);
		}
		
		
		/**
		 * Puts a new story into the histogram. If it already exists, increments the count
		 * @param story Story
		 * @return Count of the story in the histogram
		 */
		public Integer put(IRStory story) {
			Integer result = null;
			
			if(currentCount < limit) {
				result = super.get(story);
				if(result == null) {
					super.put(story, 1);
					result = 1;
				} else {
					super.put(story, result+1);
				}
				
				currentCount++;
			}
			
			return result;
		}
	}
}

