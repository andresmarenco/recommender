<%@ include file="../include/common.jsp" %>

<jsp:useBean id="recommendationBean" class="recommender.web.beans.RecommendationBean" scope="page">
	<jsp:setProperty name="recommendationBean" property="credential" value="${credential}" />
	<jsp:setProperty name="recommendationBean" property="session" value="${pageContext.session}" />
</jsp:useBean>

<c:set var="viewTypeRecommendation" value="${viewTypeBean.view_types['recommendation'].id}" />

<div id="story-recommender">
	<h4>
		<fmt:message key="story.recommended" />:
	</h4>
	
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
