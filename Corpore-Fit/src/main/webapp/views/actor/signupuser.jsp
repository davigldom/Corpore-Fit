<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" modelAttribute="registerUser">

	<%--<form:hidden path="id"/>
	<form:hidden path="version"/>
		
		<form:hidden path="userAccount.authorities" />--%>

	<acme:textbox code="actor.username" path="username" />

	<acme:password code="actor.password" path="password" />

	<acme:password code="actor.repeatpassword" path="repeatedPassword" />

	<acme:textbox code="actor.name" path="name" />

	<acme:textbox code="actor.surname" path="surname" />

	<acme:textbox code="actor.email" path="email" />

	<acme:textbox code="actor.photo" path="photo" />


	<acme:textbox code="actor.phone" path="phone"
		placeholder="+34954645178" />

	<acme:textbox code="actor.address" path="address" />

	<acme:textbox code="actor.birthdate" path="birthdate"
		placeholder="dd/MM/yyyy" />


	<br>
	<br>
	<jstl:if test="${isUser}">
		<form:label path="role">
			<spring:message code="user.role" />
		</form:label>
		<form:errors cssClass="error" path="role" />
		<br />
		<form:select path="role">
			<form:option selected="true" value="NONE">
				<jstl:out value="None" />
			</form:option>
			<form:option value="POWERLIFTER">
				<jstl:out value="Powerlifter" />
			</form:option>
			<form:option value="CALISTHENICS">
				<spring:message code="user.CALISTHENICS" />
			</form:option>
			<form:option value="STRONGMAN">
				<jstl:out value="Strongman" />
			</form:option>
			<form:option value="CROSSFITTER">
				<jstl:out value="Crossfitter" />
			</form:option>
			<form:option value="WEIGHTLIFTER">
				<jstl:out value="Weightlifter" />
			</form:option>
			<form:option value="BODYBUILDER">
				<jstl:out value="Bodybuilder" />
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
				<spring:message code="user.open" />
			</form:option>
			<form:option value="FRIENDS">
				<spring:message code="user.friends" />
			</form:option>
			<form:option value="CLOSED">
				<spring:message code="user.closed" />
			</form:option>
		</form:select>
		<br>
		<br>
	</jstl:if>

	<br>

	<form:label path="acceptedTerms">
		<spring:message code="actor.acceptterms" />
	</form:label>
	<a href="./misc/seeterms.do" target="_blank"><spring:message
			code="actor.terms" /></a>
	<form:checkbox path="acceptedTerms" required="required" id="terms"
		onchange="javascript: toggleSubmit()" />
	<form:errors path="acceptedTerms" cssClass="error" />
	<br>
	<br>

	<acme:submit name="save" code="actor.save" id="save"
		disabled="disabled" onload="javascript: toggleSubmit()" />
	<acme:button url="welcome/index.do" code="actor.cancel" />

	<script type="text/javascript">
		function toggleSubmit() {
			var accepted = document.getElementById("terms");
			if (accepted.checked) {
				document.getElementById("save").disabled = false;
			} else {
				document.getElementById("save").disabled = true;
			}
		}
	</script>

</form:form>