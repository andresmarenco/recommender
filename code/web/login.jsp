<%@ include file="./include/common.jsp" %>

<c:if test="${credential.isLogged()}">
	<jsp:forward page="./index.jsp"></jsp:forward>
</c:if>


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
  			<div id="content-login">
  				<form id="login-form" action="./auth.do" method="post">
  					<h1><fmt:message key="auth.login" /></h1>
  					<div><label class="form-label"><fmt:message key="auth.username" />:</label><input type="text" id="username" name="username" value="${param['username']}" /></div>
  					<div><label class="form-label"><fmt:message key="auth.password" />:</label><input type="password" id="password" name="password" /></div>
  					<c:if test="${errors != null}">
  						<div>
	  						<label class="form-label"></label>
	  						<span class="error">
	  							<fmt:message key="${errors}" />
	  						</span>
	  					</div>
	  				</c:if>
  					<div><label class="form-label"></label><input type="submit" id="btnLogin" value="<fmt:message key="auth.login" />" /></div>
  					
  					<input type="hidden" name="auth" id="auth" value="<%=recommender.web.controller.AuthController.AuthService.LOGIN%>" />
  				</form>
  			</div>
  			
  			<div id="story-recommender">
  				fdsafas22 fdsajl lkd jflk afdsklf ajkf hhkafd hksaf hksaf ghkjsagf kjsa fhksda fghkdav hkjavbhka hjka bhcwe chjwk echjksd bhcjksa dbhcksa bchka bsdhvkjasd bhfkj webhjkf hwejkc bhkjsd cbhkjsda bewhkhjk vhbjsdak vhsdak cbhsadkf hewjk hjkdh vjkvsda yweu gciuc usd cjksad casjk dakj
  			</div>
  			
  			<div class="clear"></div>
  		</div>
  		
  		<jsp:include page="./include/footer.jsp" />
  	</div>
  </body>
</html>