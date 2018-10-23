<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="room/manager/edit.do?" modelAttribute="room" method="post">

		<form:hidden path="id"/>
		<form:hidden path="version"/>
<%-- 		<form:hidden path="manager"/> --%>
		
		<acme:textbox code="room.name" path="name"/>
		
		<acme:textbox code="room.capacity" path="capacity"/>
		
		<acme:submit name="save" code="room.save"/>
		<jstl:if test="${room.id!=0}">
			<acme:submit name="delete" code="room.delete"/>
		</jstl:if>
		<br/>
		<br/>
		<acme:button url="room/manager/list.do?gymId=${room.manager.gym.id }" code="room.cancel"/>
</form:form>