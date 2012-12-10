<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="en"/>
<fmt:setBundle basename="recommender.web.i18n.default"/>

<jsp:useBean id="credential" class="recommender.beans.IRUser" scope="session" />

<% session = request.getSession(true); %>