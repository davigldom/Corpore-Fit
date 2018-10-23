<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="activity/manager/edit.do" modelAttribute="activity" method="post">

		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<acme:textbox code="activity.title" path="title"/>
		
		<acme:textarea code="activity.description" path="description"/>
		
		<acme:textbox code="activity.photo" path="photo"/>
		
		<acme:textbox code="activity.trainer" path="trainer"/>
		
		<acme:textbox code="activity.start" path="start"
		placeholder="HH:mm" />
		
		<acme:textbox code="activity.end" path="end"
		placeholder="HH:mm" />
		
		<form:label path="day">
			<spring:message code="activity.day" />
		</form:label>
		<form:errors cssClass="error" path="day" />
		<br/>
		<form:select path="day">
				<form:option selected="true" value="MONDAY">
					<spring:message code="activity.day.monday" />
				</form:option>
				<form:option value="TUESDAY">
					<spring:message code="activity.day.tuesday" />
				</form:option>
				<form:option value="WEDNESDAY">
					<spring:message code="activity.day.wednesday" />
				</form:option>
				<form:option value="THURSDAY">
					<spring:message code="activity.day.thursday" />
				</form:option>
				<form:option value="FRIDAY">
					<spring:message code="activity.day.friday" />
				</form:option>
				<form:option value="SATURDAY">
					<spring:message code="activity.day.saturday" />
				</form:option>
				<form:option value="SUNDAY">
					<spring:message code="activity.day.sunday" />
				</form:option>
		</form:select>
		<br/>
		<br/>
		
		
		<form:label path="room">
			<spring:message code="activity.room" />
		</form:label>
		<form:errors cssClass="error" path="room" />
		<br/>
		<form:select id="rooms" path="room">
			<form:option value="0" label="----" />		
			<jstl:forEach var="room" items="${rooms }">
				<form:option value="${room.id}">
					<jstl:out value="${room.name}" />
				</form:option>
			</jstl:forEach>
		</form:select>
		<br/><br>
		
		
		
		<acme:submit name="save" code="activity.save"/>
		
		<jstl:if test="${activity.id!=0}">
			<acme:submit name="delete" code="activity.delete"/>
		</jstl:if>
		<br/>
		<br/>
		<acme:button url="gym/manager/display.do" code="activity.cancel"/>
</form:form>