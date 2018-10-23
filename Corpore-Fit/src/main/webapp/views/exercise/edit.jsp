<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="exercise/user/edit.do?dayId=${dayId }" modelAttribute="exercise" method="post">

		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<acme:textbox code="exercise.title" path="title"/>
		
		<acme:textbox code="exercise.description" path="description"/>
		
		<acme:textbox code="exercise.photo" path="photo"/>
		
		<acme:textbox code="exercise.video" path="video"/>
		
		<acme:submit name="save" code="exercise.save"/>
		<acme:button url="routine/display.do?userId=${userId }" code="exercise.cancel"/>
</form:form>