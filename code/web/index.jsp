<%@ include file="./include/common.jsp" %>

<html>
  <head>
    <title><fmt:message key="system_name" /></title>
    <script type="text/javascript" src="./include/js/jquery.js"></script>
    
    <link rel="stylesheet" type="text/css" href="./include/css/default.css">
  </head>
 
  <body>
 	<div id="wrapper">
  		<jsp:include page="./include/header.jsp" />
  		
  		<div id="main">
  			<jsp:include page="./include/browser.jsp" />
  			
  			<div id="content">
  				<jsp:useBean id="statisticsBean" class="recommender.web.beans.StatisticsStoriesBean" scope="page">
  					<jsp:setProperty name="statisticsBean" property="credential" value="${credential}" />
  				</jsp:useBean>
  				
  				<c:set var="viewTypeStatistics" value="${viewTypeBean.view_types['statistics'].id}" />
  				
  				<img class="centered" src="./include/img/folk.jpg" />
  				<h4 class="centered">"<fmt:message key="system_name" />"</h4>
  				
				<c:set var="recentlyViewed" value="${statisticsBean.userLastViewedStories}" />
  				<c:choose>
  					<c:when test="${empty recentlyViewed}">
	  					<h3 style="padding-top: 10px;"><fmt:message key="story.most_viewed_msg" />:</h3>
		  				<c:forEach items="${statisticsBean.mostViewedStories}" var="story">
		  					<div class="found-story">
		  						<div class="title">
		  							<a href="${pageContext.request.contextPath}/story?id=${story.id}&vt=${viewTypeStatistics}">
		  								<c:out value="${story.title}" />
		  							</a>
		  						</div>
		  						<div class="info">
		  							<fmt:message key="story.views" />: <c:out value="${story.views}" />
		  						</div>
		  						<div class="text">
		  							<c:out value="${story.text}" />
		  						</div>
		  					</div>
		  				</c:forEach>  				
  					</c:when>
  				
  					<c:otherwise>
	  					<h3 style="padding-top: 10px;"><fmt:message key="story.recently_viewed" />:</h3>
		  				<c:forEach items="${recentlyViewed}" var="story">
		  					<div class="found-story">
		  						<div class="title">
		  							<a href="${pageContext.request.contextPath}/story?id=${story.id}&vt=${viewTypeStatistics}">
		  								<c:out value="${story.title}" />
		  							</a>
		  						</div>
		  						<div class="info">
		  							<fmt:message key="story.views" />: <c:out value="${story.views}" />
		  						</div>
		  						<div class="text">
		  							<c:out value="${story.text}" />
		  						</div>
		  					</div>
						</c:forEach>
	  				</c:otherwise>
  				</c:choose>
  				
  				
  				
  				<c:set var="likedStories" value="${statisticsBean.userLikedStories}" />
  				<c:choose>
  					<c:when test="${empty likedStories}">
	  					<h3 style="padding-top: 10px;"><fmt:message key="story.best_ranked_msg" />:</h3>
		  				<c:forEach items="${statisticsBean.bestRankedStories}" var="story">
		  					<div class="found-story">
		  						<div class="title">
		  							<a href="${pageContext.request.contextPath}/story?id=${story.id}&vt=${viewTypeStatistics}">
		  								<c:out value="${story.title}" />
		  							</a>
		  						</div>
		  						<div class="info">
		  							<fmt:message key="story.views" />: <c:out value="${story.views}" />
		  						</div>
		  						<div class="text">
		  							<c:out value="${story.text}" />
		  						</div>
		  					</div>
  				</c:forEach>  				
  					</c:when>
  				
  					<c:otherwise>
	  					<h3 style="padding-top: 10px;"><fmt:message key="story.liked_stories" />:</h3>
		  				<c:forEach items="${likedStories}" var="story">
		  					<div class="found-story">
		  						<div class="title">
		  							<a href="${pageContext.request.contextPath}/story?id=${story.id}&vt=${viewTypeStatistics}">
		  								<c:out value="${story.title}" />
		  							</a>
		  						</div>
		  						<div class="info">
		  							<fmt:message key="story.views" />: <c:out value="${story.views}" />
		  						</div>
		  						<div class="text">
		  							<c:out value="${story.text}" />
		  						</div>
		  					</div>
						</c:forEach>
	  				</c:otherwise>
  				</c:choose>  					
  			</div>
  			
  			<jsp:include page="./include/recommendation.jsp" />
  			
  			<div class="clear"></div>
  		</div>
  		
  		<jsp:include page="./include/footer.jsp" />
  	</div>
  </body>
</html>