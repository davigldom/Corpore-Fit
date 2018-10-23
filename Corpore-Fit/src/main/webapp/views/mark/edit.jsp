<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="mark/user/edit.do" modelAttribute="mark" method="post">

		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<acme:textbox code="mark.creationDate" readonly="true" path="creationDate"/>
		
		<acme:textbox code="mark.benchPress" path="benchPress"/>
		
		<acme:textbox code="mark.squat" path="squat"/>
		
		<acme:textbox code="mark.deadlift" path="deadlift"/>
		
		<acme:textbox code="mark.pullUp" path="pullUp"/>
		
		<acme:textbox code="mark.rowing" path="rowing"/>
		
		<acme:submit name="save" code="mark.save"/>
		<acme:button url="actor/display-principal.do" code="mark.cancel"/>
</form:form>