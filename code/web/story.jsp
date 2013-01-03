<%@ include file="./include/common.jsp" %>

<c:set var="like_score" value="<%=recommender.web.controller.StoryScoreController.LIKE_SCORE%>" scope="page" />
<c:set var="neutral_score" value="<%=recommender.web.controller.StoryScoreController.NEUTRAL_SCORE%>" scope="page" />
<c:set var="dislike_score" value="<%=recommender.web.controller.StoryScoreController.DISLIKE_SCORE%>" scope="page" />
							
								
<html>
  <head>
    <title><fmt:message key="system_name" /></title>
    <script type="text/javascript" src="./include/js/jquery.js"></script>
    
    <link rel="stylesheet" type="text/css" href="./include/css/default.css">
    
    <script type="text/javascript">
	    $(document).ready(function(){
	    	$("#like-container").click($.parseJSON('${score != like_score}') ? likeStory : removeScoreStory);
	    	$("#dislike-container").click($.parseJSON('${score != dislike_score}') ? dislikeStory : removeScoreStory);
	    });
    
    
    	function likeStory() {
    		$.post("./score.do", { action: 'LikeStory', story_id: '${story.id}' },
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
    	
    	
    	function dislikeStory() {
    		$.post("./score.do", { action: 'DislikeStory', story_id: '${story.id}' },
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
    	
    	
    	function removeScoreStory() {
    		$.post("./score.do", { action: 'RemoveScoreStory', story_id: '${story.id}' },
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
    
    </script>
  </head>
 
  <body>
 	<div id="wrapper">
  		<jsp:include page="./include/header.jsp" />
  		
  		<div id="main">
  			<jsp:include page="./include/browser.jsp" />
  			
  			<div id="content">
  				<c:if test="${errors != null}">
  					<div class="error">
  						<c:out value="${errors}" />
  					</div>
  				</c:if>
  				
  				<div class="story">
  					<c:choose>
  						<c:when test="${story != null}">
  							<h1>
	  							<c:out value="${story.title}" />
		  					</h1>
		  					<c:choose>
								<c:when test="${credential.isLogged()}">
									<span class="button-img" id="like-container">
										<c:choose>
											<c:when test="${score == like_score}">
												<img id="like-image" src="./include/img/liked.gif" alt="<fmt:message key="story.like" />" title="<fmt:message key="story.like" />" >
											</c:when>
											<c:otherwise>
												<img id="like-image" src="./include/img/like.gif" alt="<fmt:message key="story.like" />" title="<fmt:message key="story.like" />" >
											</c:otherwise>
										</c:choose>
									</span>
									<span class="button-img" id="dislike-container">
										<c:choose>
											<c:when test="${score == dislike_score}">
												<img id="dislike-image" src="./include/img/disliked.gif" alt="<fmt:message key="story.dislike" />" title="<fmt:message key="story.dislike" />" >
											</c:when>
											<c:otherwise>
												<img id="dislike-image" src="./include/img/dislike.gif" alt="<fmt:message key="story.dislike" />" title="<fmt:message key="story.dislike" />" >
											</c:otherwise>
										</c:choose>
									</span>
								</c:when>
								<c:otherwise>
									<span class="button-img" id="like-container" onclick="window.location='${pageContext.request.contextPath}/login.do?return_url=${utl:findReturnUrl(pageContext.request)}'"><img id="like-image" src="./include/img/like.gif" alt="<fmt:message key="story.like" />" title="<fmt:message key="story.like" />" ></span>
									<span class="button-img" id="dislike-container" onclick="window.location='${pageContext.request.contextPath}/login.do?return_url=${utl:findReturnUrl(pageContext.request)}'"><img id="dislike-image" src="./include/img/dislike.gif" alt="<fmt:message key="story.dislike" />" title="<fmt:message key="story.dislike" />"></span>
								</c:otherwise>
							</c:choose>
		  					
		  					<p>
		  						<c:out value="${utl:escapeNewline(story.text)}" escapeXml="false" />
		  					</p>
  						</c:when>
  						<c:otherwise>
  							<div class="error">
  								<fmt:message key="story.not_found" />
  							</div>
  						</c:otherwise>
  					</c:choose>
  				</div>
  			</div>
  			
  			<jsp:include page="./include/recommendation.jsp" />
  			
  			<div class="clear"></div>
  		</div>
  		
  		<jsp:include page="./include/footer.jsp" />
  	</div>
  </body>
</html>