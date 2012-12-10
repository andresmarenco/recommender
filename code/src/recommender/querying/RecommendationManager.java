package recommender.querying;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import recommender.beans.IRStory;
import recommender.beans.IRUser;

public class RecommendationManager {

	// TODO: Make a real function for this
	public List<IRStory> recommendStories(IRUser user, IRStory current_story, Queue<Long> story_session) {
		List<IRStory> result = new ArrayList<IRStory>();
		
		IRStory story = new IRStory();
		
		for(int i = 0; i < 6; i++) {
			Random r = new Random();
			story = new IRStory();
			story.setId(r.nextInt(50) + 1);
			story.setTitle("TITLE " + story.getId());
			story.setText("recommended story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId() + ", story " + story.getId());
			result.add(story);
		}
		
		
		return result;
	}
	
}
