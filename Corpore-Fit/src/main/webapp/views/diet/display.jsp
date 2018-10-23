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





<!-- Attributes -->

<h4>
	<jstl:out value="${diet.name }" />
</h4>
<br>
<jstl:out value="${diet.comments }" />

<br>
<br>
<table>
	<tr>
		<jstl:forEach items="${diet.dailyDiets}" var="day">

			<jstl:if test="${day.day == 'MONDAY'}">
				<th style="text-align: center;"><spring:message
						code="day.monday" /></th>
			</jstl:if>

			<jstl:if test="${day.day == 'TUESDAY'}">
				<th style="text-align: center;"><spring:message
						code="day.tuesday" /></th>
			</jstl:if>

			<jstl:if test="${day.day == 'WEDNESDAY'}">
				<th style="text-align: center;"><spring:message
						code="day.wednesday" /></th>
			</jstl:if>

			<jstl:if test="${day.day == 'THURSDAY'}">
				<th style="text-align: center;"><spring:message
						code="day.thursday" /></th>
			</jstl:if>

			<jstl:if test="${day.day == 'FRIDAY'}">
				<th style="text-align: center;"><spring:message
						code="day.friday" /></th>
			</jstl:if>

			<jstl:if test="${day.day == 'SATURDAY'}">
				<th style="text-align: center;"><spring:message
						code="day.saturday" /></th>
			</jstl:if>

			<jstl:if test="${day.day == 'SUNDAY'}">
				<th style="text-align: center;"><spring:message
						code="day.sunday" /></th>
			</jstl:if>

		</jstl:forEach>
	</tr>

	<tr>
		<jstl:forEach var="day" items="${diet.dailyDiets}">
			<td><jstl:forEach var="food" items="${day.foods }">
					<security:authorize access="hasRole('NUTRITIONIST')">
						<a href="food/nutritionist/display.do?foodId=${food.id }"><jstl:out
								value="${food.name }" /></a>
						<a href="food/nutritionist/delete.do?foodId=${food.id }"><span
							class="glyphicon glyphicon-minus"></span></a>
					</security:authorize>

					<security:authorize access="hasRole('USER')">
						<a href="food/user/display.do?foodId=${food.id }"><jstl:out
								value="${food.name }" /></a>
					</security:authorize>
					<br>
				</jstl:forEach></td>
		</jstl:forEach>

	</tr>

	<security:authorize access="hasRole('NUTRITIONIST')">
		<tr>
			<jstl:forEach var="day" items="${diet.dailyDiets}">
				<td><a href="food/nutritionist/create.do?dayId=${day.id }"><span
						class="glyphicon glyphicon-plus"></span></a></td>

			</jstl:forEach>
		</tr>
	</security:authorize>
</table>

<br>
<br>

<security:authorize access="hasRole('USER')">
	<jstl:if test="${nutritionistAssigned}">
	<jstl:if test="${!isFollowing }">
		<acme:button url="/diet/user/follow-diet.do?dietId=${diet.id }"
			code="diet.follow" />
	</jstl:if>

	<jstl:if test="${isFollowing }">
		<acme:button url="/diet/user/unfollow-diet.do?dietId=${diet.id }"
			code="diet.unfollow" />
	</jstl:if>
	</jstl:if>
	
	<jstl:if test="${!nutritionistAssigned}">
		<p style="color:red"><spring:message code="diet.notAssignedNutritionist"/></p>
		<acme:button url="/diet/user/follow-diet.do?dietId=${diet.id }"
			code="diet.follow" disabled="disabled"/>

	</jstl:if>
	
	
	<acme:button
		url="/diet/user/list.do?nutritionistId=${diet.nutritionist.id }"
		code="actor.back" />
</security:authorize>

<security:authorize access="hasRole('NUTRITIONIST')">
	<acme:button url="/diet/nutritionist/list.do" code="actor.back" />
</security:authorize>

<br>

<jstl:if test="${diet==null }">
	<spring:message code="diet.none" />
</jstl:if>
