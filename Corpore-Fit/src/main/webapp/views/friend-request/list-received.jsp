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
		<jstl:if test="${row.sender.photo == '' or row.sender.photo == null}">
			<img src="images/profile.jpg" width=100 height="150">
		</jstl:if>
		<jstl:if test="${row.sender.photo != '' and row.sender.photo != null}">
			<img src="${row.sender.photo }" width=100 height="150">
		</jstl:if>
	</display:column>
	<acme:column code="actor.name" path="sender.name" />
	<acme:column code="actor.surname" path="sender.surname" />
	<acme:column code="actor.email" path="sender.email" />


	<display:column>
			<acme:button url="actor/display.do?actorId=${row.sender.id}"
				code="actor.display" />
	</display:column>
	
	<acme:columnButton url="friend-request/user/accept.do?friendRequestId=${row.id }" code="friendRequest.accept"/>

	<acme:columnButton url="friend-request/user/reject.do?friendRequestId=${row.id }" code="friendRequest.reject"/>
</display:table>
