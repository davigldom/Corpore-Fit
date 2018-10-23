<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="measure/user/edit.do" modelAttribute="measure" method="post">

		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<acme:textbox code="measure.creationDate" readonly="true" path="creationDate"/>
		
		<acme:textbox code="measure.weight" path="weight"/>
		
		<acme:textbox code="measure.chest" path="chest"/>
		
		<acme:textbox code="measure.thigh" path="thigh"/>
		
		<acme:textbox code="measure.waist" path="waist"/>
		
		<acme:textbox code="measure.biceps" path="biceps"/>
		
		<acme:textbox code="measure.calf" path="calf"/>
		
		<acme:submit name="save" code="measure.save"/>
		<acme:button url="actor/display-principal.do" code="measure.cancel"/>
</form:form>