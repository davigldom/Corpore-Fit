<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<security:authorize access="isAuthenticated()">
	<security:authentication property="principal.username" var="user" />
</security:authorize>

<jstl:if test="${exercise!=null}">


	<spring:message code="exercise.photo" var="photo" />
	<b><jstl:out value="${photo }: " /></b>
	<br>
	<jstl:choose>
		<jstl:when test="${exercise.photo != '' }">
			<img src="<jstl:out value="${exercise.photo }" />" alt="${photo }"
				height="200" />
		</jstl:when>
		<jstl:otherwise>
		<spring:message code="exercise.noPicture" var="noPicture" />
			<jstl:out value="${noPicture }" />
		</jstl:otherwise>
	</jstl:choose>
	<br>


	<spring:message code="exercise.title" var="title" />
	<b><jstl:out value="${title}: " /></b>
	<jstl:out value="${exercise.title}" />
	<br>

	<spring:message code="exercise.description" var="description" />
	<b><jstl:out value="${description}: " /></b>
	<jstl:out value="${exercise.description}" />
	<br>

	<br>


<jstl:if test="${exercise.video!=null and exercise.video!=''}">
	<spring:message code="exercise.video" var="video" />
	<b><jstl:out value="${video}: " /></b>
	<br>
	<br>

	<iframe width="420" height="315" src="${exercise.video}"> </iframe>
	<br>

	<br />
	<br />
	<br />
</jstl:if>


	<jstl:if test="${isCreator}">
		<acme:button url="exercise/user/edit.do?exerciseId=${exercise.id}"
			code="exercise.edit" />
		<acme:button url="exercise/user/delete.do?exerciseId=${exercise.id}"
			code="exercise.delete" />
		<br />
		<br />
	</jstl:if>


	<acme:button url="routine/display.do?userId=${userId }" code="actor.back" />
	<br />

</jstl:if>





