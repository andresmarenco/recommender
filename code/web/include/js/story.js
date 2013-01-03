
/**
 * Scores the current story with a like
 */
function likeStory() {
	$.post("./score.do", { action: 'LikeStory', story_id: story_id },
			function(data) {
				$("#like-container").off('click');
				$("#like-container").click(removeScoreStory);
				
				$("#dislike-container").off('click');
				$("#dislike-container").click(dislikeStory);
				
				$("#dislike-image").attr("src", "./include/img/dislike.gif");
				$("#like-image").attr("src", "./include/img/liked.gif");
			}
	);
}




/**
 * Scores the current story with a dislike
 */
function dislikeStory() {
	$.post("./score.do", { action: 'DislikeStory', story_id: story_id },
			function(data) {
				$("#dislike-container").off('click');
				$("#dislike-container").click(removeScoreStory);
				
				$("#like-container").off('click');
				$("#like-container").click(likeStory);
				
				$("#dislike-image").attr("src", "./include/img/disliked.gif");
				$("#like-image").attr("src", "./include/img/like.gif");
			}
	);
}




/**
 * Removes the score of the current story
 */
function removeScoreStory() {
	$.post("./score.do", { action: 'RemoveScoreStory', story_id: story_id },
			function(data) {
				$("#like-container").off('click');
				$("#dislike-container").off('click');
				
				$("#like-container").click(likeStory);
				$("#dislike-container").click(dislikeStory);
				
				$("#dislike-image").attr("src", "./include/img/dislike.gif");
				$("#like-image").attr("src", "./include/img/like.gif");
			}
	);
}
