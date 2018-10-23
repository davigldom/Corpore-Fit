<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="${requestURI}" modelAttribute="${registerActorForm}">


	<%--<form:hidden path="id"/>
	<form:hidden path="version"/>
		
		<form:hidden path="userAccount.authorities" />--%>
		
		<acme:textbox code="actor.username" path="username"/>
		
		<acme:password code="actor.password" path="password"/>
		
		<acme:password code="actor.repeatpassword" path="repeatedPassword"/>
		
		<acme:textbox code="actor.name" path="name"/>
		
		<acme:textbox code="actor.surname" path="surname"/>
		
		<acme:textbox code="actor.email" path="email"/>
				
		<acme:textbox code="actor.phone" path="phone" placeholder="+34954645178"/>
				
		<acme:textbox code="actor.address" path="address"/>
		
		<acme:textbox code="actor.photo" path="photo"/>
				
		<acme:textbox code="actor.birthdate" path="birthdate" placeholder="dd/MM/yyyy"/>		
		
				
		<jstl:if test="${registerActorForm == 'registerNutritionist'}">
		
		<acme:textbox code="actor.officeAddress" path="officeAddress"/>
		
		<acme:textbox code="actor.curriculum" path="curriculum"/>
		
		</jstl:if>
	
	<security:authorize access="!hasRole('ADMIN')">
		<form:label path="acceptedTerms" >
			<spring:message code="actor.acceptterms" />
		</form:label>
		<a href="./misc/seeterms.do" target="_blank"><spring:message code="actor.terms" /></a>
		<form:checkbox path="acceptedTerms" required="required" id="terms" onchange="javascript: toggleSubmit()"/>
		<form:errors path="acceptedTerms" cssClass="error" />
	</security:authorize>
<br>
	<security:authorize access="!isAuthenticated()">
		<acme:submit name="save" code="actor.save" id="save" disabled="disabled" onload="javascript: toggleSubmit()"/>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<acme:submit name="save" code="actor.save" id="save"/>
	</security:authorize>
	
	<acme:button url="welcome/index.do" code="actor.cancel"/>
	
	<script type="text/javascript">
		function toggleSubmit() {
			var accepted = document.getElementById("terms");
			if(accepted.checked){
				document.getElementById("save").disabled = false;
			} else{
				document.getElementById("save").disabled = true;
			}
		}
	</script>

</form:form>