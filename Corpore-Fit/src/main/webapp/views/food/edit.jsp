<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="food/nutritionist/edit.do?dayId=${dayId }"
	modelAttribute="food" method="post">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="food.name" path="name" />

	<acme:textbox code="food.calories" path="calories" />

	<acme:textbox code="food.amount" path="amount" />

	<acme:textbox code="food.image" path="image" />

	<div>
		<form:label path="time">
			<spring:message code="food.time" />
		</form:label>
		<form:errors cssClass="error" path="time" />
		<br />
		<form:select path="time">
			<form:option selected="true" value="BREAKFAST">
				<spring:message code="food.breakfast" />
			</form:option>
			<form:option value="MIDMORNING_SNACK">
				<spring:message code="food.midmorning.snack" />
			</form:option>
			<form:option value="LUNCH">
				<spring:message code="food.lunch" />
			</form:option>
			<form:option value="MIDAFTERNOON_SNACK">
				<spring:message code="food.midafternoon.snack" />
			</form:option>
			<form:option value="DINNER">
				<spring:message code="food.dinner" />
			</form:option>
		</form:select>
	</div>
	<br>
	<br>

	<acme:submit name="save" code="food.save" />
	<jstl:if test="${dietId!=null }">
		<acme:button url="diet/nutritionist/display.do?dietId=${dietId}"
			code="food.cancel" />
	</jstl:if>
	<jstl:if test="${dietId==null }">
		<acme:button url="diet/nutritionist/list.do" code="food.cancel" />
	</jstl:if>

</form:form>