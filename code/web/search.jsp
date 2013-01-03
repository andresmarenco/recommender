<%@ include file="./include/common.jsp" %>

<html>
  <head>
    <title><fmt:message key="system_name" /></title>
    <script type="text/javascript" src="./include/js/jquery.js"></script>
    <script type="text/javascript" src="./include/js/jquery-pagination/jquery.simplePagination.js"></script>
    
    <link rel="stylesheet" type="text/css" href="./include/css/default.css">
    <link rel="stylesheet" type="text/css" href="./include/js/jquery-pagination/simplePagination.css">
  </head>
 
  <body>
  	<script type="text/javascript">
  		
  		$(document).ready(function(){
  			
  			var paginator_cfg = {
  			    	prevText: '<fmt:message key="paginator.previous" />',
  			    	nextText: '<fmt:message key="paginator.next" />',
  			        items: '${complete_size}',
  			        itemsOnPage: '${results}',
  			      	currentPage: '${start}',
  			        cssStyle: 'light-theme',
		        	onPageClick: function(page) {
		        		window.location = "./search.do?query=${param['query']}&start=" + page + "&results=${results}";
		        	}
  			};
  			
  			$(function() {
  			    $("#pagination-top").pagination(paginator_cfg);
  			  	$("#pagination-bottom").pagination(paginator_cfg);
  			});
  			
		});
  	</script>
  	
 	<div id="wrapper">
  		<jsp:include page="./include/header.jsp" />
  		
  		<div id="main">
  			<jsp:include page="./include/browser.jsp" />
  			<c:set var="viewTypeSearch" value="${viewTypeBean.view_types['search'].id}" />
  			
  			<div id="content">
  				<c:forEach items="${errors['default']}" var="field_error"><div><span class="error"><fmt:message key="${field_error}" /></span></div></c:forEach>
  			
  				<div class="results-header">
  					<fmt:message key="search.results">
  						<fmt:param value="${first_story}" />
  						<fmt:param value="${last_story}" />
  						<fmt:param value="${complete_size}" />
  						<fmt:param value="${param['query']}" />
  					</fmt:message>
  				</div>
  				
  				<div class="pagination">
  					<div id="pagination-top"></div>
  				</div>
  				
  				<c:forEach items="${stories}" var="story">
  					<div class="found-story">
  						<div class="title">
  							<a href="${pageContext.request.contextPath}/story?id=${story.id}&vt=${viewTypeSearch}">
  								<c:out value="${story.title}" />
  							</a>
  						</div>
  						<div class="text">
  							<c:out value="${story.text}" />
  						</div>
  					</div>
  				</c:forEach>
  				
  				<div class="pagination">
  					<div id="pagination-bottom"></div>
  				</div>
  			</div>
  			
  			<jsp:include page="./include/recommendation.jsp" />
  			
  			<div class="clear"></div>
  		</div>
  		
  		<jsp:include page="./include/footer.jsp" />
  	</div>
  </body>
</html>