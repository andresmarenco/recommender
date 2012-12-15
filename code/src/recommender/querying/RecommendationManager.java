package recommender.querying;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import recommender.beans.IRKeyword;
import recommender.beans.IRStory;
import recommender.beans.IRUser;
import recommender.dataaccess.RetrievalManager;
import recommender.dataaccess.StoryDAO;
import recommender.utils.RecommenderException;

public class RecommendationManager {

	private static final int DEFAULT_RECOMMENDATIONS = 6;
	
	// TODO: Make a real function for this
	public List<IRStory> recommendStories(IRUser user, IRStory current_story, Queue<Long> story_session) throws RecommenderException {
		List<IRStory> result = new ArrayList<IRStory>();
		
		if((current_story != null) && (current_story.getId() != Long.MIN_VALUE)) {
			StoryDAO storyDAO = new StoryDAO();
			current_story.setKeywords(storyDAO.listKeywords(current_story));
			
			StringBuilder ir_query = new StringBuilder();
			for(IRKeyword keyword : current_story.getKeywords()) {
				ir_query.append(keyword.getName()).append(" ");
			}
			
			System.out.println(ir_query.toString());
			
			RetrievalManager rm = new RetrievalManager();
			result = rm.searchStories(ir_query.toString(), 1, DEFAULT_RECOMMENDATIONS);
			
			/*for(IRStory story : result) {
				System.out.println(MessageFormat.format("[{0}] {1}", story.getCode(), story.getText()));
			}*/
		}
		
		return result;
	}
	
}
