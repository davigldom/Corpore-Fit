<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


	<div id="select">
		<form method="get" action="actor/seeTop.do">
			<spring:message code="user.role" />
			: <select name="roleSent">
				<option value="STRONGMAN">Strongman</option>
				<option value="POWERLIFTER">Powerlifter</option>
				<option value="CROSSFITTER">Crossfitter</option>
				<option value="CALISTHENICS"><spring:message code="user.CALISTHENICS" /></option>
				<option value="BODYBUILDER">Bodybuilder</option>
				<option value="WEIGHTLIFTER">Weightlifter</option>
				<option value="NONE"><spring:message code="user.NONE" /></option>
			</select>
			
			<spring:message code="mark" />
			: <select name="mark">
				<option value="benchPress"><spring:message code="mark.benchPress" /></option>
				<option value="pullUp"><spring:message code="mark.pullUp" /></option>
				<option value="deadlift"><spring:message code="mark.deadlift" /></option>
				<option value="squat"><spring:message code="mark.squat" /></option>
				<option value="rowing"><spring:message code="mark.rowing" /></option>
			</select>
			
			<acme:submit name="top" code="actor.search"/>			
		</form>
	</div>
	<br>
	<br>


<display:table pagesize="15" class="displaytag" name="users" id="row"
	requestURI="${requestURI}">


	<!-- Attributes -->

		<display:column>
		<jstl:if test="${row.photo == '' or row.photo == null}">
			<img id="circularPhoto" src="images/profile.jpg" width=150 height="150">
		</jstl:if>
		<jstl:if test="${row.photo != '' and row.photo != null}">
			<img id="circularPhoto" src="${row.photo }" width=150 height="150">
		</jstl:if>
	</display:column>
	<acme:column code="actor.name" path="name" />
	<acme:column code="actor.surname" path="surname" />
	<acme:column code="actor.email" path="email" />

	<display:column>
		<acme:button url="actor/display.do?actorId=${row.id}"
			code="actor.display" />
	</display:column>
</display:table>

	<br>
	<acme:button url="/welcome/index.do" code="actor.back" />
