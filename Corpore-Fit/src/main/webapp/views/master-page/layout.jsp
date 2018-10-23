<%--
 * layout.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<base
	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
<link rel="icon" href="favicon.ico" type="image/x-icon">

<script type="text/javascript" src="scripts/jquery.js"></script>
<script type="text/javascript" src="scripts/jquery-ui.js"></script>
<script type="text/javascript" src="scripts/jmenu.js"></script>

<link rel="stylesheet" href="styles/common.css" type="text/css">
<link rel="stylesheet" href="styles/jmenu.css" media="screen" type="text/css" />
<link rel="stylesheet" href="styles/displaytag.css" type="text/css">

<link rel="stylesheet" href="styles/all1.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link href="styles/all2.css" rel="stylesheet">
<link href="styles/all3.css" rel="stylesheet"/>
<link href="styles/fonts/lobster.css" rel="stylesheet">
<link href="styles/fonts/raleway.css" rel="stylesheet">
<link href="styles/fonts/abel.css" rel="stylesheet">
<link rel="stylesheet" href="styles/starrating.css">

<title><tiles:insertAttribute name="title" ignore="true" /></title>

<script type="text/javascript">
	$(document).ready(function() {
		$("#jMenu").jMenu();
	});

	function askSubmission(msg, form) {
		if (confirm(msg))
			form.submit();
	}
	
	function relativeRedir(loc) {	
		var b = document.getElementsByTagName('base');
		if (b && b[0] && b[0].href) {
  			if (b[0].href.substr(b[0].href.length - 1) == '/' && loc.charAt(0) == '/')
    		loc = loc.substr(1);
  			loc = b[0].href + loc;
		}
		window.location.replace(loc);
	}
</script>

</head>

<body id=body>
	
	<div>
		<tiles:insertAttribute name="header" />
	</div>
		<div id="content">
	
	<div>
		<h1><b>
			<jstl:choose>
				<jstl:when test="${displayArticle == true}">
					<jstl:out value="${article.title }" />
				</jstl:when>
				<jstl:when test="${searchArticle == true}">
					<spring:message code="article.searchResult" var="searchResult" />
					<jstl:out value="${searchResult} '${keyword }'" />
				</jstl:when>
				<jstl:when test="${displayProduct == true}">
					<jstl:out value="${product.name }" />
				</jstl:when>
				<jstl:when test="${searchProduct == true}">
					<spring:message code="product.searchResult" var="searchResult" />
					<jstl:out value="${searchResult} '${keyword }'" />
				</jstl:when>
				<jstl:when test="${displayAdvert == true}">
					<jstl:out value="${advert.name }" />
				</jstl:when>
				<jstl:when test="${searchActor == true}">
					<spring:message code="actor.searchResult" var="searchResult" />
					<jstl:out value="${searchResult} '${keyword }'" />
				</jstl:when>
				<jstl:otherwise>
					<tiles:insertAttribute name="title" />
				</jstl:otherwise>	
			</jstl:choose>
		</b></h1>
		<tiles:insertAttribute name="body" />	
		<jstl:if test="${message != null}">
			<jstl:if test="${displayArticle != true}">
				<br />
				<span class="message"><spring:message code="${message}" /></span>
			</jstl:if>
		</jstl:if>	
	</div>
	</div>
	<div>
		<tiles:insertAttribute name="footer" />
	</div>

<script type="text/javascript" id="cookiebanner"
  src="https://cdn.jsdelivr.net/gh/dobarkod/cookie-banner@1.2.1/dist/cookiebanner.min.js"
  data-height="30px" data-close-text="<spring:message code="master.page.cookies.close"/>" data-moreinfo="misc/seecookies.do" 
  data-message="<spring:message code="master.page.cookies" />" data-link="#46C6C6" data-linkmsg="<spring:message code="master.page.cookies.more" />" >
</script>



</body>

</html>