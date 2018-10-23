<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table pagesize="5" class="displaytag" name="friendRequests"
	id="row" requestURI="${requestURI}">


	<!-- Attributes -->

	<display:column>
		<jstl:if test="${row.receiver.photo == '' or row.receiver.photo == null}">
			<img src="images/profile.jpg" width=100 height="150">
		</jstl:if>
		<jstl:if test="${row.receiver.photo != '' and row.receiver.photo != null}">
			<img src="${row.receiver.photo }" width=100 height="150">
		</jstl:if>
	</display:column>
	<acme:column code="actor.name" path="receiver.name" />
	<acme:column code="actor.surname" path="receiver.surname" />
	<acme:column code="actor.email" path="receiver.email" />


	<display:column>
			<acme:button url="actor/display.do?actorId=${row.receiver.id}"
				code="actor.display" />
	</display:column>
	
	<acme:columnButton url="friend-request/user/delete.do?friendRequestId=${row.id }" code="friendRequest.delete"/>
</display:table>
