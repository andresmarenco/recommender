<%@ include file="../include/common.jsp" %>

<jsp:useBean id="browserBean" class="recommender.web.controller.BrowserBean" scope="page" />

<div id="story-browser">
	<div class="browser-header">
		<fmt:message key="subgenres" />
	</div>
	
	<c:forEach items="${browserBean.subgenres}" var="subgenre">
		<div class="browser-item">
			<c:out value="${subgenre.name}" />
		</div>
	</c:forEach>
</div>