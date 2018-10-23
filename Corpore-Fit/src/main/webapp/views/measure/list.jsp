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
	name="measures" requestURI="${requestURI }" id="row">


	<!-- Attributes -->


	<fmt:formatDate value="${row.creationDate.time}" type="date"
		pattern="dd/MM/yyyy" var="formatedCreationDate" />

	<spring:message code="measure.creationDate" var="creationDateHeader" />
	<display:column title="${creationDateHeader}" sortable="true">
		<jstl:out value="${formatedCreationDate}"></jstl:out>
	</display:column>


	<acme:column code="measure.weight" path="weight" />
	<acme:column code="measure.chest" path="chest" />
	<acme:column code="measure.thigh" path="thigh" />
	<acme:column code="measure.waist" path="waist" />
	<acme:column code="measure.biceps" path="biceps" />
	<acme:column code="measure.calf" path="calf" />

	<jstl:if test="${isCreator}">
		<acme:columnButton url="measure/user/delete.do?measureId=${row.id }"
			code="measure.delete" />
	</jstl:if>



</display:table>

<jstl:if test="${isCreator}">
	<acme:button url="measure/user/create.do" code="measure.create" />
</jstl:if>


<br>
<br>
<acme:button url="actor/display.do?actorId=${userId }" code="actor.back" />
