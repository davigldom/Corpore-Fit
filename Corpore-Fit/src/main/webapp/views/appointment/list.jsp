<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<display:table pagesize="5" class="displaytag" name="appointments"
	id="row" requestURI="${requestURI}">


	<!-- Attributes -->


	<acme:column code="actor.name" path="user.name" />
	<acme:column code="actor.surname" path="user.surname" />
	<acme:column code="actor.name"
		path="nutritionist.name" />
	<acme:column code="actor.surname"
		path="nutritionist.surname" />
	<acme:columnMoment code="appointment.time" property="time.time" />
	<display:column>
		<jstl:if test="${row.cancelled }">
			<spring:message code="appointment.cancelled" />
		</jstl:if>
	</display:column>
	<display:column>
		<security:authorize access="hasRole('USER')">
			<acme:button
				url="appointment/user/display.do?appointmentId=${row.id}"
				code="advert.display" />
		</security:authorize>

		<security:authorize access="hasRole('NUTRITIONIST')">
			<acme:button
				url="appointment/nutritionist/display.do?appointmentId=${row.id}"
				code="advert.display" />
		</security:authorize>
	</display:column>

</display:table>
<br>
<acme:button url="/welcome/index.do" code="actor.back" />