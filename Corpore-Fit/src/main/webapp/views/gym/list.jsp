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
	name="gyms" requestURI="${requestURI }" id="row">

<display:column>
		<img src="${row.photo }" width=150 height="80">
	</display:column>
	<acme:column code="gym.name" path="name" />
	<acme:column code="gym.description" path="description" />
	<acme:column code="gym.address" path="address" />
	<acme:column code="gym.schedule" path="schedule" />
	<acme:column code="gym.fees" path="fees" />
	<acme:column code="gym.services" path="services" />
	
	<acme:column code="gym.manager" path="manager.name" />

	<acme:columnButton url="gym/display.do?gymId=${row.id }" code="gym.display" />
<%-- 	<acme:columnButton url="activity/list.do?gymId=${row.id }" code="gym.activity.list" /> --%>

</display:table>
