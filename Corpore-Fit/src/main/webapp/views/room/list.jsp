<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="rooms" requestURI="${requestURI }" id="row">


	<!-- Attributes -->
	<acme:column code="room.name" path="name"/>
	<acme:column code="room.capacity" path="capacity"/>
	
	<acme:column code="room.gym" path="manager.gym.name"/>


	<jstl:if test="${isCreator}">
		<acme:columnButton url="room/manager/edit.do?roomId=${row.id }" code="room.edit" />
<%-- 		<acme:columnButton url="room/manager/delete.do?roomId=${row.id }" code="room.delete" /> --%>
	</jstl:if>
</display:table>
<br/>

<jstl:if test="${isCreator}">
	<acme:button url="room/manager/create.do?" code="room.create"/>
</jstl:if>
