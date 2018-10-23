<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="isAuthenticated()">
	<security:authentication property="principal.username" var="user" />
</security:authorize>


<jstl:set var="isUser" value="true" />
<jstl:if test="${username == user}">
	<jstl:set var="isUser" value="false" />
</jstl:if>

<spring:message var="createAppointmentMessage" code="appointment.create" />

<jstl:if test="${canMakeAppointment }">
	<form action="appointment/user/create.do" method="get">
		<input id="datefield" type="date" required name="date"> <input
			type="submit" value="${createAppointmentMessage}">
	</form>
</jstl:if>
<br>
<div>
	<ul id="schedule">
		<li><a id="showMonday" href="javascript:show('monday')"><spring:message
					code="nutritionist.monday" /></a></li>
		<li><a id="showTuesday" href="javascript:show('tuesday')"><spring:message
					code="nutritionist.tuesday" /></a></li>
		<li><a id="showWednesday" href="javascript:show('wednesday')"><spring:message
					code="nutritionist.wednesday" /></a></li>
		<li><a id="showThursday" href="javascript:show('thursday')"><spring:message
					code="nutritionist.thursday" /></a></li>
		<li><a id="showFriday" href="javascript:show('friday')"><spring:message
					code="nutritionist.friday" /></a></li>
		<li><a id="showSaturday" href="javascript:show('saturday')"><spring:message
					code="nutritionist.saturday" /></a></li>
	</ul>
</div>
<br>
<br>




<jstl:forEach items="${schedule}" var="day">
	<div id="${day.day }" style='display: none;'>
		<form:form action="schedule/nutritionist/edit.do"
			modelAttribute="DaySchedule">

			<input type="text" hidden="true" id="dayId" value="${day.day }"
				name="day" />

			<div id="timeMorning1">
				<jstl:if test="${!isUser }">
					<spring:message code="nutritionist.morningStart" />
					<input type="time" id="inputMorning1" name="morningStart"
						min="7:00" max="14:00" step="1800" value="${day.morningStart}">
				</jstl:if>
				<jstl:if test="${isUser }">
					<spring:message code="nutritionist.morningStart" />
					<input type="time" disabled id="inputMorning1" name="morningStart"
						min="7:00" max="14:00" step="1800" value="${day.morningStart}">
				</jstl:if>
			</div>
			<br>
			<div id="timeAfternoon1">
				<jstl:if test="${!isUser }">
					<spring:message code="nutritionist.morningEnd" />
					<input type="time" id="inputMorning2" step="1800" name="morningEnd"
						min="7:00" max="14:00" value="${day.morningEnd}">
				</jstl:if>
				<jstl:if test="${isUser }">
					<spring:message code="nutritionist.morningEnd" />
					<input type="time" id="inputMorning2" step="1800" disabled
						name="morningEnd" min="7:00" max="14:00" value="${day.morningEnd}">
				</jstl:if>
			</div>
			<br>
			<div id="timeMorning2">
				<jstl:if test="${!isUser }">
					<spring:message code="nutritionist.afternoonStart" />
					<input type="time" id="inputAfternoon1" step="1800"
						name="afternoonStart" min="15:00" max="20:00"
						value="${day.afternoonStart}">

				</jstl:if>
				<jstl:if test="${isUser }">
					<spring:message code="nutritionist.afternoonStart" />
					<input type="time" id="inputAfternoon1" step="1800" disabled
						name="afternoonStart" min="15:00" max="20:00"
						value="${day.afternoonStart}">
				</jstl:if>
			</div>
			<br>
			<div id="timeAfternoon2">
				<jstl:if test="${!isUser }">
					<spring:message code="nutritionist.afternoonEnd" />
					<input type="time" id="inputAfternoon2" step="1800"
						name="afternoonEnd" min="16:00" max="22:00"
						value="${day.afternoonEnd}">
				</jstl:if>
				<jstl:if test="${isUser }">
					<spring:message code="nutritionist.afternoonEnd" />
					<input type="time" disabled id="inputAfternoon2"
						name="afternoonEnd" min="16:00" max="22:00" step="1800"
						value="${day.afternoonEnd}">
				</jstl:if>
			</div>
			<br>

			<jstl:if test="${!message==null }">
				<spring:message code="nutritionist.schedule.error" />
			</jstl:if>

			<jstl:if test="${username == user}">
				<acme:submit name="save" code="schedule.save" />
			</jstl:if>
		</form:form>

	</div>

</jstl:forEach>

<acme:button url="actor/display.do?actorId=${nutritionist.id }" code="actor.back"/>



<script type="text/javascript">
	var today = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
	var dd = today.getDate();
	var mm = today.getMonth() + 1; //January is 0!
	var yyyy = today.getFullYear();
	if (dd < 10) {
		dd = '0' + dd
	}
	if (mm < 10) {
		mm = '0' + mm
	}

	today = yyyy + '-' + mm + '-' + dd;
	document.getElementById("datefield").setAttribute("min", today);

	function show(contents) {

		document.getElementById("dayId").setAttribute("value",
				contents.toUpperCase());
		if (contents == "monday") {
			document.getElementById('showMonday').style.color = '#7CAFB7';
			document.getElementById('MONDAY').style.display = 'block';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('TUESDAY').style.display = 'none';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('WEDNESDAY').style.display = 'none';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('THURSDAY').style.display = 'none';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('FRIDAY').style.display = 'none';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('SATURDAY').style.display = 'none';

		} else if (contents == "tuesday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('MONDAY').style.display = 'none';
			document.getElementById('showTuesday').style.color = '#7CAFB7';
			document.getElementById('TUESDAY').style.display = 'block';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('WEDNESDAY').style.display = 'none';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('THURSDAY').style.display = 'none';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('FRIDAY').style.display = 'none';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('SATURDAY').style.display = 'none';
		}

		else if (contents == "wednesday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('MONDAY').style.display = 'none';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('TUESDAY').style.display = 'none';
			document.getElementById('showWednesday').style.color = '#7CAFB7';
			document.getElementById('WEDNESDAY').style.display = 'block';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('THURSDAY').style.display = 'none';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('FRIDAY').style.display = 'none';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('SATURDAY').style.display = 'none';
		}

		else if (contents == "thursday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('MONDAY').style.display = 'none';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('TUESDAY').style.display = 'none';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('WEDNESDAY').style.display = 'none';
			document.getElementById('showThursday').style.color = '#7CAFB7';
			document.getElementById('THURSDAY').style.display = 'block';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('FRIDAY').style.display = 'none';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('SATURDAY').style.display = 'none';
		}

		else if (contents == "friday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('MONDAY').style.display = 'none';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('TUESDAY').style.display = 'none';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('WEDNESDAY').style.display = 'none';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('THURSDAY').style.display = 'none';
			document.getElementById('showFriday').style.color = '#7CAFB7';
			document.getElementById('FRIDAY').style.display = 'block';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('SATURDAY').style.display = 'none';
		}

		else if (contents == "saturday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('MONDAY').style.display = 'none';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('TUESDAY').style.display = 'none';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('WEDNESDAY').style.display = 'none';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('THURSDAY').style.display = 'none';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('FRIDAY').style.display = 'none';
			document.getElementById('showSaturday').style.color = '#7CAFB7';
			document.getElementById('SATURDAY').style.display = 'block';

		}

	}
</script>
