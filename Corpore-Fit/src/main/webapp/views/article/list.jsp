<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<jstl:if test="${isListingCreated == false}">
	<jstl:if test="${!empty articles}">
		<div id="search">
			<form method="get" action="article/search-word.do">
				<spring:message code="article.search" var="searchButton" />
				<input type="text" name="keyword"> 
				<input type="submit" value="${searchButton }">
			</form>
		</div>
		<br>
		<br>
	</jstl:if>
</jstl:if>

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="articles" requestURI="${requestURI }" id="row">


	<!-- Attributes -->

	<acme:column code="article.title" path="title" />

	<jstl:choose>
		<jstl:when test="${row.publicationDate != null }">
			<fmt:formatDate value="${row.publicationDate.time}" type="date" pattern="dd/MM/yyyy" var ="formatedPublicationDate"/>
			<acme:columnOut code="article.moment" path="${formatedPublicationDate}" />
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="article.noPublishedYet" var="noPublishedYet" />
			<acme:columnOut code="article.moment" path="${noPublishedYet}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<jstl:if test="${isListingCreated == true}">
		<spring:message code="article.draft" var="draft"/>
		<display:column title="${draft}" sortable="true">
			<jstl:if test="${row.draft == true}">
					<span class="glyphicon glyphicon-ok"></span>
			</jstl:if>
			<jstl:if test="${row.draft == false}">
					<span class="glyphicon glyphicon-remove"></span>
			</jstl:if>
		</display:column>
	</jstl:if>

	<jstl:if test="${isListingCreated == false}">
		<acme:columnOut code="article.editor"
			path="${row.editor.name } ${row.editor.surname }" />
	</jstl:if>

	<acme:columnButton url="article/display.do?articleId=${row.id}"
		code="article.display" />
		
	<security:authorize access="hasRole('EDITOR')">
		<display:column>
			<jstl:if test="${isListingCreated == true}">
				<jstl:if test="${row.editor == principal}">
					<jstl:if test="${row.publicationDate == null}">
						<jstl:if test="${row.draft == true}">
							<acme:button url="article/editor/edit.do?articleId=${row.id}"
								code="article.edit" />
						</jstl:if>
						<jstl:if test="${row.draft == false}">
							<display:column>
								<acme:button url="article/editor/publish.do?articleId=${row.id}"
									code="article.publish" />
							</display:column>
						</jstl:if>
					</jstl:if>
				</jstl:if>
			</jstl:if>
		</display:column>
	</security:authorize>

</display:table>

<jstl:if test="${searchArticle == true}">
	<br>
	<acme:button url="/article/list.do"
		code="article.back" />
</jstl:if>

