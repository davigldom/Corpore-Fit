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



<jstl:if test="${routine!=null }">

	<!-- Attributes -->

	<h4>
		<jstl:out value="${routine.name }" />
	</h4>

	<table>
		<tr>
			<jstl:forEach items="${routine.days }" var="day">

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
			<jstl:forEach var="day" items="${routine.days}">
				<td><jstl:forEach var="exercise" items="${day.exercises }">
						<a href="exercise/display.do?exerciseId=${exercise.id }"><jstl:out
								value="${exercise.title }" /></a>
						<jstl:if test="${isCreator }">
							<a href="exercise/user/delete.do?exerciseId=${exercise.id }"><span
								class="glyphicon glyphicon-minus"></span></a>
						</jstl:if>
						<br>
					</jstl:forEach></td>
			</jstl:forEach>

		</tr>

		<jstl:if test="${isCreator }">
			<tr>
				<jstl:forEach var="day" items="${routine.days}">
					<td><a href="exercise/user/create.do?dayId=${day.id }"><span
							class="glyphicon glyphicon-plus"></span></a></td>

				</jstl:forEach>
			</tr>
		</jstl:if>
	</table>

	<br>
	<br>


	<jstl:if test="${isCreator }">
		<acme:button url="/routine/user/delete.do?routineId=${routine.id }"
			code="routine.discard" />
	</jstl:if>

	<br>

</jstl:if>
<jstl:if test="${routine==null }">
	<spring:message code="routine.null" />
</jstl:if>

	<br>
	<br>

<acme:button url="/actor/display.do?actorId=${routine.user.id }" code="actor.back"/>
