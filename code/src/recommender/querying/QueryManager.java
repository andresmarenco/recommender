package recommender.querying;

import java.util.ArrayList;
import java.util.List;

import recommender.beans.IRStory;

public class QueryManager {

	
	public List<IRStory> search(String query) {
		List<IRStory> result = new ArrayList<IRStory>();
		
		// TODO: Make the real search!!!
		IRStory story = new IRStory();
		story.setId(1L);
		story.setTitle("TITLE 1");
		story.setText("story 1, story 1, story 1, story 1, story 1, story 1, story 1, story 1, story 1, story 1");
		result.add(story);
		
		story = new IRStory();
		story.setId(2L);
		story.setTitle("TITLE 2");
		story.setText("story 2, story 2, story 2, story 2, story 2, story 2, story 2, story 2, story 2, story 2");
		result.add(story);
		
		story = new IRStory();
		story.setId(3L);
		story.setTitle("TITLE 3");
		story.setText("story 3, story 3, story 3, story 3, story 3, story 3, story 3, story 3, story 3, story 3");
		result.add(story);
		
		return result;
	}
	
}
