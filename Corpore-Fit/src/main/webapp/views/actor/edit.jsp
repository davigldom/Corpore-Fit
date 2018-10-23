<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="isAuthenticated()">
	<security:authentication property="principal.username" var="user" />
</security:authorize>
<form:form action="actor/${authority}/edit.do"
	modelAttribute="actor" method="post">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<form:hidden path="userAccount" />

	<acme:textbox code="actor.name" path="name" />

	<acme:textbox code="actor.surname" path="surname" />

	<acme:textbox code="actor.email" path="email" />
	
	<acme:textbox code="actor.photo" path="photo"/>

	<acme:textbox code="actor.phone" path="phone"
		placeholder="+34954645178" />

	<acme:textbox code="actor.address" path="address" />

	<acme:textbox code="actor.birthdate" path="birthdate"
		placeholder="dd/MM/yyyy" />
		
	<jstl:if test="${authority eq 'nutritionist' }">
	
	<acme:textbox code="actor.officeAddress" path="officeAddress" />
	
	<acme:textbox code="actor.curriculum" path="curriculum" />
	
	</jstl:if>
	

	<jstl:if test="${authority eq 'user' }">
		<form:label path="role">
			<spring:message code="user.role" />
		</form:label>
		<form:errors cssClass="error" path="role" />
		<br />
		<form:select path="role">
			<form:option selected="true" value="BODYBUILDER">
				<jstl:out value="Bodybuilder"/>
			</form:option>
			<form:option value="POWERLIFTER">
				<jstl:out value="Powerlifter"/>
			</form:option>
			<form:option value="CALISTHENICS">
				<spring:message code="user.CALISTHENICS"/>
			</form:option>
			<form:option value="STRONGMAN">
				<jstl:out value="Strongman"/>
			</form:option>
			<form:option value="CROSSFITTER">
				<jstl:out value="Crossfitter"/>
			</form:option>
			<form:option value="WEIGHTLIFTER">
				<jstl:out value="Weightlifter"/>
			</form:option>
			<form:option value="NONE">
				<jstl:out value="None" />
			</form:option>
		</form:select>
		<br>
		<br>
		
		<form:label path="privacy">
			<spring:message code="user.privacy" />
		</form:label>
		<form:errors cssClass="error" path="privacy" />
		<br />
		<form:select path="privacy">
				<form:option selected="true" value="OPEN">
					<spring:message code="user.open"/>
				</form:option>
				<form:option value="FRIENDS">
					<spring:message code="user.friends"/>
				</form:option>
				<form:option value="CLOSED">
					<spring:message code="user.closed"/>
				</form:option>
		</form:select>
		<br>
		<br>
	</jstl:if>

	<jstl:if test="${user == actor.userAccount.username }">
		<acme:submit name="save" code="actor.save" />
		<jstl:if test="${authority eq 'provider' || authority eq 'editor'}">
			<acme:delete url="actor/${authority}/delete.do?actorId=${actor.id}"
				code="actor.delete" returnMessage="actor.confirm.delete" />
		</jstl:if>
		<acme:button url="actor/display-principal.do" code="actor.cancel" />
	</jstl:if>
</form:form>

<form:form action="actor/${authority}/edit.do">
	<jstl:if test="${user == actor.userAccount.username }">
		<jstl:if test="${authority eq 'user' || authority eq 'nutritionist' || authority eq 'manager'}">
			<input type="hidden" name="actorId" value="${actor.id}" />
			<acme:submit name="delete" code="actor.delete" />
		</jstl:if>
	</jstl:if>
</form:form>
