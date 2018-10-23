<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<div>
	<ul id="schedule">
		<li>		
			<a id="showMonday" style='color:#7CAFB7;' href="javascript:show('monday')" ><spring:message code="activity.day.monday" /></a>
		</li>
		<li>
			<a id="showTuesday" href="javascript:show('tuesday')" ><spring:message code="activity.day.tuesday" /></a>
		</li>
		<li>
			<a id="showWednesday" href="javascript:show('wednesday')" ><spring:message code="activity.day.wednesday" /></a>
		</li>
		<li>
			<a id="showThursday" href="javascript:show('thursday')" ><spring:message code="activity.day.thursday" /></a>
		</li>
		<li>
			<a id="showFriday" href="javascript:show('friday')" ><spring:message code="activity.day.friday" /></a>
		</li>
		<li>
			<a id="showSaturday" href="javascript:show('saturday')" ><spring:message code="activity.day.saturday" /></a>
		</li>
		<li>
			<a id="showSunday" href="javascript:show('sunday')" ><spring:message code="activity.day.sunday" /></a>
		</li>
	</ul>
</div>
<br/>

<div id='monday' style='display:block;'>
	<display:table class="displaytag" keepStatus="false"
		name="mondayActivities" requestURI="javascript:show('monday')" id="row">
	
	
		<!-- Attributes -->
		<acme:column code="activity.title" path="title" sortable="false"/>
	
		<fmt:formatDate value="${row.start.time}" type="date"
			pattern="HH:mm" var="formatedStart" />
	
		<spring:message code="activity.start" var="startHeader" />
		<display:column title="${startHeader}" sortable="false">
			<jstl:out value="${formatedStart}"></jstl:out>
		</display:column>
		
		<fmt:formatDate value="${row.end.time}" type="date"
			pattern="HH:mm" var="formatedEnd" />
	
		<spring:message code="activity.end" var="endHeader" />
		<display:column title="${endHeader}" sortable="false">
			<jstl:out value="${formatedEnd}"></jstl:out>
		</display:column>
		
		<acme:column code="activity.room" path="room.name" />

		<acme:columnButton url="activity/display.do?activityId=${row.id }" code="activity.display" />
		
		<jstl:if test="${isCreator}">
			<acme:columnButton url="activity/manager/edit.do?activityId=${row.id }" code="activity.edit" />
		</jstl:if>
	</display:table>
	<br/>
</div>

<div id='tuesday' style='display:none;'>
	<display:table class="displaytag" keepStatus="false"
		name="tuesdayActivities" requestURI="javascript:show('tuesday')" id="row">
	
	
		<!-- Attributes -->
		<acme:column code="activity.title" path="title" sortable="fasle"/>
	
		<fmt:formatDate value="${row.start.time}" type="date"
			pattern="HH:mm" var="formatedStart" />
	
		<spring:message code="activity.start" var="startHeader" />
		<display:column title="${startHeader}" sortable="false">
			<jstl:out value="${formatedStart}"></jstl:out>
		</display:column>
		
		<fmt:formatDate value="${row.end.time}" type="date"
			pattern="HH:mm" var="formatedEnd" />
	
		<spring:message code="activity.end" var="endHeader" />
		<display:column title="${endHeader}" sortable="false">
			<jstl:out value="${formatedEnd}"></jstl:out>
		</display:column>
		
		<acme:column code="activity.room" path="room.name" />

		<acme:columnButton url="activity/display.do?activityId=${row.id }" code="activity.display" />

		<jstl:if test="${isCreator}">
			<acme:columnButton url="activity/manager/edit.do?activityId=${row.id }" code="activity.edit" />
		</jstl:if>
	</display:table>
	<br/>
</div>


<div id='wednesday' style='display:none;'>
	<display:table class="displaytag" keepStatus="false"
		name="wednesdayActivities" requestURI="javascript:show('wednesday')" id="row">
	
	
		<!-- Attributes -->
		<acme:column code="activity.title" path="title" sortable="false"/>
	
		<fmt:formatDate value="${row.start.time}" type="date"
			pattern="HH:mm" var="formatedStart" />
	
		<spring:message code="activity.start" var="startHeader" />
		<display:column title="${startHeader}" sortable="false">
			<jstl:out value="${formatedStart}"></jstl:out>
		</display:column>
		
		<fmt:formatDate value="${row.end.time}" type="date"
			pattern="HH:mm" var="formatedEnd" />
	
		<spring:message code="activity.end" var="endHeader" />
		<display:column title="${endHeader}" sortable="false">
			<jstl:out value="${formatedEnd}"></jstl:out>
		</display:column>
		
		<acme:column code="activity.room" path="room.name" />
	
		<acme:columnButton url="activity/display.do?activityId=${row.id }" code="activity.display" />
	
		<jstl:if test="${isCreator}">
			<acme:columnButton url="activity/manager/edit.do?activityId=${row.id }" code="activity.edit" />
		</jstl:if>
	</display:table>
	<br/>
</div>

<div id='thursday' style='display:none;'>
	<display:table class="displaytag" keepStatus="false"
		name="thursdayActivities" requestURI="javascript:show('thursday')" id="row">
	
	
		<!-- Attributes -->
		<acme:column code="activity.title" path="title" sortable="false"/>
	
		<fmt:formatDate value="${row.start.time}" type="date"
			pattern="HH:mm" var="formatedStart" />
	
		<spring:message code="activity.start" var="startHeader" />
		<display:column title="${startHeader}" sortable="false">
			<jstl:out value="${formatedStart}"></jstl:out>
		</display:column>
		
		<fmt:formatDate value="${row.end.time}" type="date"
			pattern="HH:mm" var="formatedEnd" />
	
		<spring:message code="activity.end" var="endHeader" />
		<display:column title="${endHeader}" sortable="false">
			<jstl:out value="${formatedEnd}"></jstl:out>
		</display:column>

		<acme:column code="activity.room" path="room.name" />
	
		<acme:columnButton url="activity/display.do?activityId=${row.id }" code="activity.display" />
		
		<jstl:if test="${isCreator}">
			<acme:columnButton url="activity/manager/edit.do?activityId=${row.id }" code="activity.edit" />
		</jstl:if>
	</display:table>
	<br/>
</div>

<div id='friday' style='display:none;'>
	<display:table class="displaytag" keepStatus="false"
		name="fridayActivities" requestURI="javascript:show('friday')" id="row">
	
	
		<!-- Attributes -->
		<acme:column code="activity.title" path="title" sortable="false"/>
	
		<fmt:formatDate value="${row.start.time}" type="date"
			pattern="HH:mm" var="formatedStart" />
	
		<spring:message code="activity.start" var="startHeader" />
		<display:column title="${startHeader}" sortable="false">
			<jstl:out value="${formatedStart}"></jstl:out>
		</display:column>
		
		<fmt:formatDate value="${row.end.time}" type="date"
			pattern="HH:mm" var="formatedEnd" />
	
		<spring:message code="activity.end" var="endHeader" />
		<display:column title="${endHeader}" sortable="false">
			<jstl:out value="${formatedEnd}"></jstl:out>
		</display:column>

		<acme:column code="activity.room" path="room.name" />
	
		<acme:columnButton url="activity/display.do?activityId=${row.id }" code="activity.display" />
	
		<jstl:if test="${isCreator}">
			<acme:columnButton url="activity/manager/edit.do?activityId=${row.id }" code="activity.edit" />
		</jstl:if>
	</display:table>
	<br/>
</div>

<div id='saturday' style='display:none;'>
	<display:table class="displaytag" keepStatus="false"
		name="saturdayActivities" requestURI="javascript:show('saturday')" id="row">
	
	
		<!-- Attributes -->
		<acme:column code="activity.title" path="title" sortable="false"/>
	
		<fmt:formatDate value="${row.start.time}" type="date"
			pattern="HH:mm" var="formatedStart" />
	
		<spring:message code="activity.start" var="startHeader" />
		<display:column title="${startHeader}" sortable="false">
			<jstl:out value="${formatedStart}"></jstl:out>
		</display:column>
		
		<fmt:formatDate value="${row.end.time}" type="date"
			pattern="HH:mm" var="formatedEnd" />
	
		<spring:message code="activity.end" var="endHeader" />
		<display:column title="${endHeader}" sortable="false">
			<jstl:out value="${formatedEnd}"></jstl:out>
		</display:column>

		<acme:column code="activity.room" path="room.name" />
	
		<acme:columnButton url="activity/display.do?activityId=${row.id }" code="activity.display" />
	
		<jstl:if test="${isCreator}">
			<acme:columnButton url="activity/manager/edit.do?activityId=${row.id }" code="activity.edit" />
		</jstl:if>
	</display:table>
	<br/>
</div>

<div id='sunday' style='display:none;'>
	<display:table class="displaytag" keepStatus="false"
		name="sundayActivities" requestURI="javascript:show('sunday')" id="row">
	
	
		<!-- Attributes -->
		<acme:column code="activity.title" path="title" sortable="false"/>
	
		<fmt:formatDate value="${row.start.time}" type="date"
			pattern="HH:mm" var="formatedStart" />
	
		<spring:message code="activity.start" var="startHeader" />
		<display:column title="${startHeader}" sortable="false">
			<jstl:out value="${formatedStart}"></jstl:out>
		</display:column>
		
		<fmt:formatDate value="${row.end.time}" type="date"
			pattern="HH:mm" var="formatedEnd" />
	
		<spring:message code="activity.end" var="endHeader" />
		<display:column title="${endHeader}" sortable="false">
			<jstl:out value="${formatedEnd}"></jstl:out>
		</display:column>

		<acme:column code="activity.room" path="room.name" />
	
		<acme:columnButton url="activity/display.do?activityId=${row.id }" code="activity.display" />
	
		<jstl:if test="${isCreator}">
			<acme:columnButton url="activity/manager/edit.do?activityId=${row.id}" code="activity.edit" />
		</jstl:if>
	</display:table>
	<br/>
</div>

<jstl:if test="${isCreator}">
	<acme:button url="activity/manager/create.do?" code="gym.add.activity"/>
	<br/>
	<br/>
</jstl:if>

<acme:button url="${backURI}" code="activity.back"/>



<script type="text/javascript">

	function show(contents){
		
		if (contents == "monday"){
			document.getElementById('showMonday').style.color = '#7CAFB7';
			document.getElementById('monday').style.display = 'block';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('tuesday').style.display = 'none';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('wednesday').style.display = 'none';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('thursday').style.display = 'none';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('friday').style.display = 'none';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('saturday').style.display = 'none';
			document.getElementById('showSunday').style.color = 'white';
			document.getElementById('sunday').style.display = 'none';
		} else if (contents == "tuesday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('monday').style.display = 'none';
			document.getElementById('showTuesday').style.color = '#7CAFB7';
			document.getElementById('tuesday').style.display = 'block';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('wednesday').style.display = 'none';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('thursday').style.display = 'none';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('friday').style.display = 'none';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('saturday').style.display = 'none';
			document.getElementById('showSunday').style.color = 'white';
			document.getElementById('sunday').style.display = 'none';
		} else if (contents == "wednesday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('monday').style.display = 'none';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('tuesday').style.display = 'none';
			document.getElementById('showWednesday').style.color = '#7CAFB7';
			document.getElementById('wednesday').style.display = 'block';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('thursday').style.display = 'none';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('friday').style.display = 'none';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('saturday').style.display = 'none';
			document.getElementById('showSunday').style.color = 'white';
			document.getElementById('sunday').style.display = 'none';
		} else if (contents == "thursday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('monday').style.display = 'none';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('tuesday').style.display = 'none';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('wednesday').style.display = 'none';
			document.getElementById('showThursday').style.color = '#7CAFB7';
			document.getElementById('thursday').style.display = 'block';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('friday').style.display = 'none';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('saturday').style.display = 'none';
			document.getElementById('showSunday').style.color = 'white';
			document.getElementById('sunday').style.display = 'none';
		} else if (contents == "friday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('monday').style.display = 'none';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('tuesday').style.display = 'none';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('wednesday').style.display = 'none';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('thursday').style.display = 'none';
			document.getElementById('showFriday').style.color = '#7CAFB7';
			document.getElementById('friday').style.display = 'block';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('saturday').style.display = 'none';
			document.getElementById('showSunday').style.color = 'white';
			document.getElementById('sunday').style.display = 'none';
		} else if (contents == "saturday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('monday').style.display = 'none';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('tuesday').style.display = 'none';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('wednesday').style.display = 'none';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('thursday').style.display = 'none';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('friday').style.display = 'none';
			document.getElementById('showSaturday').style.color = '#7CAFB7';
			document.getElementById('saturday').style.display = 'block';
			document.getElementById('showSunday').style.color = 'white';
			document.getElementById('sunday').style.display = 'none';
		} else if (contents == "sunday") {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('monday').style.display = 'none';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('tuesday').style.display = 'none';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('wednesday').style.display = 'none';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('thursday').style.display = 'none';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('friday').style.display = 'none';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('saturday').style.display = 'none';
			document.getElementById('showSunday').style.color = '#7CAFB7';
			document.getElementById('sunday').style.display = 'block';
		} else {
			document.getElementById('showMonday').style.color = 'white';
			document.getElementById('monday').style.display = 'none';
			document.getElementById('showTuesday').style.color = 'white';
			document.getElementById('tuesday').style.display = 'none';
			document.getElementById('showWednesday').style.color = 'white';
			document.getElementById('wednesday').style.display = 'none';
			document.getElementById('showThursday').style.color = 'white';
			document.getElementById('thursday').style.display = 'none';
			document.getElementById('showFriday').style.color = 'white';
			document.getElementById('friday').style.display = 'none';
			document.getElementById('showSaturday').style.color = 'white';
			document.getElementById('saturday').style.display = 'none';
			document.getElementById('showSunday').style.color = 'white';
			document.getElementById('sunday').style.display = 'none';
		} 
	}

</script>

