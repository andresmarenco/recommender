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
  				<c:if test="${errors != null}">
  					<div class="error">
  						<c:out value="${errors}" />
  					</div>
  				</c:if>
  				
  				<div class="story">
	  				<c:if test="${story != null}">
	  					<h1>
	  						<c:out value="${story.title}" />
	  					</h1>
	  					
	  					<p>
	  						<c:out value="${story.text}" />
	  					</p>
	  				</c:if>
  				</div>
  			</div>
  			
  			<jsp:include page="./include/recommendation.jsp" />
  			
  			<div class="clear"></div>
  		</div>
  		
  		<jsp:include page="./include/footer.jsp" />
  	</div>
  </body>
</html>