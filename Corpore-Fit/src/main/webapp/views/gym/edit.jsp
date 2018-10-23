<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="gym/manager/edit.do" modelAttribute="gym" method="post">

		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<acme:textbox code="gym.name" path="name"/>
		
		<acme:textarea code="gym.description" path="description"/>
		
		<acme:textbox code="gym.photo" path="photo"/>
		
		<acme:textbox code="gym.address" path="address"/>
		
		<acme:textbox code="gym.schedule" path="schedule"/>
		
		<acme:textbox code="gym.fees" path="fees"/>
		
		<acme:textarea code="gym.services" path="services"/>
		
		
		<acme:submit name="save" code="gym.save"/>
		<acme:button url="gym/manager/display.do" code="gym.cancel"/>
</form:form>