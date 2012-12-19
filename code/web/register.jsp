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
  			<div id="content-register">
  				<c:forEach items="${errors['default']}" var="field_error"><div><span class="error"><fmt:message key="${field_error}" /></span></div></c:forEach>
					
  				<form id="register-form" action="./register.do" method="post">
  					<h1><fmt:message key="auth.sign_up" /></h1>
  					<div><label class="form-label"><fmt:message key="auth.fullname" />:</label><input type="text" id="fullname" name="fullname" value="${param['fullname']}" /></div>
					<c:forEach items="${errors['fullname']}" var="field_error"><div><label class="form-label"></label><span class="error"><fmt:message key="${field_error}" /></span></div></c:forEach>
					<div><label class="form-label"><fmt:message key="auth.username" />:</label><input type="text" id="username" name="username" value="${param['username']}" /></div>
  					<c:forEach items="${errors['username']}" var="field_error"><div><label class="form-label"></label><span class="error"><fmt:message key="${field_error}" /></span></div></c:forEach>
					<div><label class="form-label"><fmt:message key="auth.password" />:</label><input type="password" id="password" name="password" /></div>  					
  					<c:forEach items="${errors['password']}" var="field_error"><div><label class="form-label"></label><span class="error"><fmt:message key="${field_error}" /></span></div></c:forEach>
					<div><label class="form-label"><fmt:message key="auth.retype_password" />:</label><input type="password" id="retype_password" name="retype_password" /></div>
  					<c:forEach items="${errors['retype_password']}" var="field_error"><div><label class="form-label"></label><span class="error"><fmt:message key="${field_error}" /></span></div></c:forEach>
					
  					<div><label class="form-label"></label><input type="submit" id="btnRegister" value="<fmt:message key="auth.sign_up" />" /></div>
  					
  					<input type="hidden" id="action" name="action" value="Create" />
  				</form>
  			</div>
  			
  			<div id="column-message">
  				<div><fmt:message key="auth.username.validation_msg" /></div>
  				<div><fmt:message key="auth.password.validation_msg" /></div>
  			</div>
  			
  			<div class="clear"></div>
  		</div>
  		
  		<jsp:include page="./include/footer.jsp" />
  	</div>
  </body>
</html>