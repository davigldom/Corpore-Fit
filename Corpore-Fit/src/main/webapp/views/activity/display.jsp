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

<jstl:if test="${activity!=null}">


<%-- 	<spring:message code="activity.photo" var="photo" /> --%>
<%-- 	<b><jstl:out value="${photo }: " /></b> --%>
<!-- 	<br> -->
	<jstl:choose>
		<jstl:when test="${activity.photo != '' }">
			<img src="<jstl:out value="${activity.photo }" />" alt="${photo }"
				height="200" />
		</jstl:when>
		<jstl:otherwise>
		<spring:message code="activity.noPicture" var="noPicture" />
			<jstl:out value="${noPicture }" />
		</jstl:otherwise>
	</jstl:choose>
	<br>
	<br>


	<spring:message code="activity.title" var="title" />
	<b><jstl:out value="${title}: " /></b>
	<jstl:out value="${activity.title}" />
	<br><br>
	
	<spring:message code="activity.description" var="description" />
	<b><jstl:out value="${description}: " /></b>
	<jstl:out value="${activity.description}" />
	<br><br>
	
	<spring:message code="activity.trainer" var="trainer" />
	<b><jstl:out value="${trainer}: " /></b>
	<jstl:out value="${activity.trainer}" />
	<br><br>
	
	<spring:message code="activity.room" var="room" />
	<b><jstl:out value="${room}: " /></b>
	<jstl:out value="${activity.room.name}" />
	<br><br>


	<jstl:if test="${isCreator}">
		<acme:button url="activity/manager/edit.do?activityId=${activity.id}"
			code="activity.edit" />
		<br />
		<br />
	</jstl:if>
	
	<security:authorize access="hasRole('USER')">
		<jstl:if test="${isSubscribed}">
		
			<jstl:if test="${!alreadyBooked}">
				<jstl:if test="${!activityFull}">
					<acme:button url="activity-book/user/create.do?activityId=${activity.id }" code="activity.book" />
				</jstl:if>
			</jstl:if>
			<jstl:if test="${!alreadyBooked}">
				<jstl:if test="${activityFull}">
					<acme:button url="activity-book/user/create.do?activityId=${activity.id }" code="activity.book" disabled="disabled"/><br/>
					<spring:message code="activity.full" var="fullWarning" />
					<b><jstl:out value="${fullWarning}" /></b>
				</jstl:if>
			</jstl:if>
			
			<jstl:if test="${alreadyBooked}">
				<acme:button url="activity-book/user/cancel.do?activityId=${activity.id }" code="activity.book.cancel" />
			</jstl:if>
			
		</jstl:if>
		<br/>
		<br/>
	</security:authorize>


	<acme:button url="activity/list.do?gymId=${activity.gym.id }" code="activity.back" />
	<br />

</jstl:if>





