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


<spring:message code="appointment.appointment.with" />
<security:authorize access="hasRole('USER')">
<jstl:out
	value=" ${appointment.nutritionist.name} ${appointment.nutritionist.surname }" />
	</security:authorize>
<security:authorize access="hasRole('NUTRITIONIST')">
<jstl:out
	value=" ${appointment.user.name} ${appointment.user.surname }" />
	</security:authorize>	
	
<br>
<jstl:if
	test="${appointment.comments!=null && appointment.comments!='' }">
"
<jstl:out value="${appointment.comments }" />
"
</jstl:if>
<br>
<br>


<fmt:formatDate value="${appointment.time.time}" type="date"
	pattern="dd/MM/yyyy HH:mm" var="formatedTime" />
<spring:message code="appointment.time" var="time" />
<b><jstl:out value="${time}: " /></b>
<jstl:out value="${formatedTime}" />
<br>

<jstl:if test="${appointment.cancelled }">
	<spring:message code="appointment.cancelled" />
	<jstl:if
		test="${appointment.cancelReason!=null && appointment.cancelReason!='' }">- "<jstl:out
			value="${appointment.cancelReason }" />"</jstl:if>

</jstl:if>
<br>
<br>

<security:authorize access="hasRole('USER')">
	<acme:button
		url="appointment/user/delete.do?appointmentId=${appointment.id}"
		code="appointment.delete" />
</security:authorize>
<security:authorize access="hasRole('NUTRITIONIST')">
	<jstl:if test="${!appointment.cancelled }">
		<button id="cancelButton" type="button" onclick="cancelAppointment()"
			class="btn btn-primary">
			<spring:message code="appointment.cancel.appointment" />
		</button>
	</jstl:if>
</security:authorize>

<acme:button
	url="schedule/display.do?nutritionistId=${appointment.nutritionist.id }"
	code="actor.schedule" />
<br />
<br />


<spring:message var="cancelReasonScript"
	code="appointment.cancel.reason" />
<script type="text/javascript">
<!--
	function cancelAppointment() {
		/* var button = document.getElementById("cancelButton"); */
		var reason = prompt('${cancelReasonScript}');
		if (reason != null)
			relativeRedir('appointment/nutritionist/cancel.do?appointmentId=${appointment.id}&cancelReason='
					+ reason);
	}
//-->
</script>