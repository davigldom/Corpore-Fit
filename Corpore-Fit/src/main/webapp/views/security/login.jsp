 <%--
 * login.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="j_spring_security_check" modelAttribute="credentials" id="loginform">

	<acme:textbox code="security.username" path="username" id="username"/>

	<acme:password code="security.password" path="password" />
	
	<div id="error" class="error">
		<jstl:if test="${showError == true}">
			<span id="errorspan"><spring:message code="security.login.failed" /></span>
		</jstl:if>
	<p id="banned" style="display:none;"><spring:message code="security.banned" /></p>
	</div>
	
	<jstl:if test="${banned}">
		<p>Hola</p>
	</jstl:if>
	
	
	<acme:submit code="security.login" name="login" />
	<br><br>
	
</form:form>

<security:authorize access="!isAuthenticated()">
	<acme:button code="security.signup.user" url="actor/create-user.do" />
	<acme:button code="security.signup.nutritionist" url="actor/create-nutritionist.do" />
</security:authorize>

<script type="text/javascript">

var aux = true;

$(document).on('submit','form#loginform',function(e){
	var p = document.getElementById("banned");
	var errorspan = document.getElementById("errorspan");
	var input = document.getElementById("username").value;
	var banned = false;
	
	if(aux){
		e.preventDefault();
		
		$.ajax({
			  url: "security/checkBanned.do",
			  method: "GET",
			  async: false,
			  data: {username: input}
			}).done(function(data){
				banned = data;
			});
		
		if(banned==false){
			aux = false;
			$("form#loginform").submit();
			
		}else{
			p.style.display = "block";
			errorspan.style.display = "none";
		}
	}
});

</script>