<%@ include file="../include/common.jsp" %>

<div id="header">
	<form id="search-form" action="${pageContext.request.contextPath}/search.do" method="get">
		<span id="logo">
			<a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="system_name" /></a>
		</span>
	
		<span id="search-box">
			<input type="text" id="query" name="query" />
			<input type="submit" id="search" name="search" value="<fmt:message key="search" />" />
		</span>
	</form>
	
	<form id="login-box" action="${pageContext.request.contextPath}/auth.do" method="post">
		<c:choose>
			<c:when test="${credential.isLogged()}">
				<span>
					<fmt:message key="auth.welcome_message">
						<fmt:param value="${credential.getName()}" />
					</fmt:message>
				</span>
				<span>|</span>
				<span>
					<a href="#" onclick="$('form#login-box').submit()"><fmt:message key="auth.logout" /></a>
				</span>
				
				<input type="hidden" name="auth" id="auth" value="<%=recommender.web.controller.AuthController.AuthService.LOGOUT%>" />
			</c:when>
			
			<c:otherwise>
				<span>
					<a href="./login.jsp"><fmt:message key="auth.login" /></a>
				</span>
				<span>|</span>
				<span>
					<a href="./register.jsp"><fmt:message key="auth.sign_up" /></a>
				</span>
			</c:otherwise>
		</c:choose>
	</form>
	
	<div class="clear"></div>
</div>
