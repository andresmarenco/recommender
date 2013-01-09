<%@ include file="../include/common.jsp" %>

<jsp:useBean id="browserBean" class="recommender.web.beans.BrowserBean" scope="page" />

<div id="story-browser">
	<h4>
		<fmt:message key="subgenres" />:
	</h4>
	
	<c:forEach items="${browserBean.subgenres}" var="subgenre">
		<div class="browser-item">
			<a href="${pageContext.request.contextPath}/stories_list?subgenre=${subgenre.id}"><c:choose><c:when test="${!subgenre.name.isEmpty()}"><c:out value="${subgenre.name}" /></c:when><c:otherwise><fmt:message key="undefined" /></c:otherwise></c:choose></a> (<c:out value="${subgenre.total}" />)
		</div>
	</c:forEach>
</div>