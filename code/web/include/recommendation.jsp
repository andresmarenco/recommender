<%@ include file="../include/common.jsp" %>

<jsp:useBean id="recommendationBean" class="recommender.web.controller.RecommendationBean" scope="page">
	<jsp:setProperty name="recommendationBean" property="user" value="${credential}" />
	<jsp:setProperty name="recommendationBean" property="story_session" value="${story_session}" />
	<jsp:setProperty name="recommendationBean" property="current_story" value="${story}" />
</jsp:useBean>

<c:set var="viewTypeRecommendation" value="${viewTypeBean.view_types['recommendation'].id}" />

<div id="story-recommender">
	<c:forEach items="${recommendationBean.recommendations}" var="recommendation">
		<div class="recommendation">
			<div class="title">
				<a href="./story?id=${recommendation.id}&vt=${viewTypeRecommendation}">
					<c:out value="${recommendation.title}" />
				</a>
			</div>
			<div class="text">
				<c:out value="${recommendation.text}" />
			</div>
		</div>
	</c:forEach>
</div>
