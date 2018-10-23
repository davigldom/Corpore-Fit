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
	name="marks" requestURI="${requestURI }" id="row">


	<!-- Attributes -->


	<fmt:formatDate value="${row.creationDate.time}" type="date"
		pattern="dd/MM/yyyy" var="formatedCreationDate" />

	<spring:message code="mark.creationDate" var="creationDateHeader" />
	<display:column title="${creationDateHeader}" sortable="true">
		<jstl:out value="${formatedCreationDate}"></jstl:out>
	</display:column>


	<acme:column code="mark.benchPress" path="benchPress" />
	<acme:column code="mark.squat" path="squat" />
	<acme:column code="mark.deadlift" path="deadlift" />
	<acme:column code="mark.pullUp" path="pullUp" />
	<acme:column code="mark.rowing" path="rowing" />

	<jstl:if test="${isCreator}">
		<acme:columnButton url="mark/user/delete.do?markId=${row.id }" code="mark.delete" />
	</jstl:if>



</display:table>

<jstl:if test="${isCreator}">
	<acme:button url="mark/user/create.do" code="mark.create" />
</jstl:if>

<br><br>
 <acme:button url="actor/display.do?actorId=${userId }" code="actor.back" /> 
