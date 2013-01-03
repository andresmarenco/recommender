package recommender.querying;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import recommender.beans.IRStory;

public class QueryManager {
	
	public QueryResult search(String query, Integer limit, Integer offset) {
		// TODO: Make the real search!!!
		QueryResult result =  new QueryResult();
		int total = 87;
		result.setComplete_size(total);
		result.setStories(new ArrayList<IRStory>());
		result.setOffset(offset.intValue());
		result.setResults_per_page(limit.intValue());
		
		for(int i = 0; i < Math.min(limit.intValue(), total); i++) {
			IRStory story = new IRStory();
			story.setId(new Random().nextLong());
			story.setTitle("TITLE " + story.getId());
			story.setText("story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId());
			result.getStories().add(story);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Class to store the results and the related metadata
	 * @author andres
	 *
	 */
	public static class QueryResult implements Serializable {
		
		private static final long serialVersionUID = 201301031842L;
		
		private int complete_size;
		private List<IRStory> stories;
		private int offset;
		private int results_per_page;
		
		/**
		 * Default Constructor
		 */
		public QueryResult() {
			
		}

		/**
		 * @return the complete_size
		 */
		public int getComplete_size() {
			return complete_size;
		}

		/**
		 * @param complete_size the complete_size to set
		 */
		public void setComplete_size(int complete_size) {
			this.complete_size = complete_size;
		}

		/**
		 * @return the stories
		 */
		public List<IRStory> getStories() {
			return stories;
		}

		/**
		 * @param stories the stories to set
		 */
		public void setStories(List<IRStory> stories) {
			this.stories = stories;
		}

		/**
		 * @return the offset
		 */
		public int getOffset() {
			return offset;
		}

		/**
		 * @param offset the offset to set
		 */
		public void setOffset(int offset) {
			this.offset = offset;
		}

		/**
		 * @return the results_per_page
		 */
		public int getResults_per_page() {
			return results_per_page;
		}

		/**
		 * @param results_per_page the results_per_page to set
		 */
		public void setResults_per_page(int results_per_page) {
			this.results_per_page = results_per_page;
		}
	}
}
