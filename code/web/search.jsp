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
  				<div class="results-header">
  					<fmt:message key="search.results">
  						<fmt:param value="${results.size()}" />
  						<fmt:param value="${param['query']}" />
  					</fmt:message>
  				</div>
  			
  				<c:if test="${errors != null}">
  					<div class="error">
  						<c:out value="${errors}"></c:out>
  					</div>
  				</c:if>
  				
  				<c:forEach items="${results}" var="story">
  					<div class="found-story">
  						<div class="title">
  							<a href="./story?id=${story.id}">
  								<c:out value="${story.title}" />
  							</a>
  						</div>
  						<div class="text">
  							<c:out value="${story.text}" />
  						</div>
  					</div>
  				</c:forEach>
  			</div>
  			
  			<jsp:include page="./include/recommendation.jsp" />
  			
  			<div class="clear"></div>
  		</div>
  		
  		<jsp:include page="./include/footer.jsp" />
  	</div>
  </body>
</html>