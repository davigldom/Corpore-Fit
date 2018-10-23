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

<jstl:if test="${food!=null}">


	<jstl:if test="${food.time=='BREAKFAST'}">
		<spring:message code="food.breakfast" var="time" />
	</jstl:if>
	<jstl:if test="${food.time=='MIDMORNING_SNACK'}">
		<spring:message code="food.midmorning.snack" var="time" />
	</jstl:if>
	<jstl:if test="${food.time=='LUNCH'}">
		<spring:message code="food.lunch" var="time" />
	</jstl:if>
	<jstl:if test="${food.time=='MIDAFTERNOON_SNACK'}">
		<spring:message code="food.midafternoon.snack" var="time" />
	</jstl:if>
	<jstl:if test="${food.time=='DINNER'}">
		<spring:message code="food.dinner" var="time" />
	</jstl:if>
	
	<spring:message code="food.image" var="image" />
	<b><jstl:out value="${image }: " /></b>
	<br>
	<img src="<jstl:out value="${food.image }" />" alt="${image }"
		height="200" />
	<br>


	<spring:message code="food.name" var="name" />
	<b><jstl:out value="${name}: " /></b>
	<jstl:out value="${food.name}" />
	<br>

	<spring:message code="food.calories" var="calories" />
	<b><jstl:out value="${calories}: " /></b>
	<jstl:out value="${food.calories}" />
	<br>


	<spring:message code="food.time" var="timeTitle" />
	<b><jstl:out value="${timeTitle}: " /></b>
	<jstl:out value="${time}" />
	<br>

	<spring:message code="food.amount" var="amount" />
	<b><jstl:out value="${amount}: " /></b>
	<jstl:out value="${food.amount}" />
	<br>
	<br>


	<security:authorize access="hasRole('NUTRITIONIST')">
		<acme:button url="food/nutritionist/edit.do?foodId=${food.id}"
			code="food.edit" />
		<acme:button url="food/nutritionist/delete.do?foodId=${food.id}"
			code="food.delete" />
		<br />
		<br />
		<acme:button url="diet/nutritionist/display.do?dietId=${dietId }"
			code="actor.back" />
	</security:authorize>
	<security:authorize access="hasRole('USER')">
		<acme:button url="diet/user/display.do?dietId=${dietId }"
			code="actor.back" />
	</security:authorize>
	<br />

</jstl:if>