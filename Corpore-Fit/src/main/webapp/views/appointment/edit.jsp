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


<form:form action="appointment/user/edit.do"
	modelAttribute="appointment" method="post">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="appointment.comments" path="comments" />

	<%-- <acme:textbox code="appointment.time" placeholder="dd/MM/yyyy HH:mm" path="time"/> --%>
<%-- 	<fmt:formatDate value="${row.start.time}" type="date"
			pattern="HH:mm" var="formatedStart" />
	<acme:select items="${dates }" itemLabel="date" code="appointment.time" path="time"/> --%>
	
	
	<div>
	<form:label path="time">
		<spring:message code="appointment.time" />
	</form:label>	
	<form:errors path="time" cssClass="error" />
	<br>
	<form:select id="${UUID.randomUUID().toString()}" path="time">	
		<jstl:forEach var="date" items="${dates }">
		
		<fmt:formatDate value="${date.time}" type="date"
			pattern="HH:mm" var="formatedDate" />
				<form:option value="${date}">
					<jstl:out value="${formatedDate}" />
				</form:option>
			</jstl:forEach>
	</form:select>	
	<br><br>
</div>
	<br>
	<br>

	<acme:submit name="save" code="appointment.save" />
	<acme:button
		url="appointment/user/list.do"
		code="appointment.cancel" />
</form:form>