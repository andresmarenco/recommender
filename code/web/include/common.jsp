<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/TagUtilDescriptor.tld" prefix="utl" %>

<fmt:setLocale value="en"/>
<fmt:setBundle basename="recommender.web.i18n.default"/>

<jsp:useBean id="credential" class="recommender.beans.IRUser" scope="session" />
<jsp:useBean id="viewTypeBean" class="recommender.web.controller.StoryViewTypeBean" scope="application" />

<% session = request.getSession(true); %>