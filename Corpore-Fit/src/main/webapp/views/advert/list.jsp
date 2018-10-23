<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="adverts" requestURI="${requestURI }" id="row">


	<!-- Attributes -->

	<acme:column code="advert.name" path="name" />

	<fmt:formatDate value="${row.start.time}" type="date" pattern="dd/MM/yyyy HH:mm" var ="formatedStart"/>
	<acme:columnOut code="advert.start" path="${formatedStart}" />
	
	<fmt:formatDate value="${row.end.time}" type="date" pattern="dd/MM/yyyy HH:mm" var ="formatedEnd"/>
	<acme:columnOut code="advert.end" path="${formatedEnd}" />

	<acme:columnOut code="advert.discount" path="${row.discount } %" />

	<jstl:if test="${isListingCreated == false}">
		<acme:columnOut code="advert.provider"
			path="${row.provider.name } ${row.provider.surname }" />
	</jstl:if>

	<acme:columnButton url="advert/display.do?advertId=${row.id}"
		code="advert.display" />
		
	<security:authorize access="hasRole('PROVIDER')">
		<display:column>
			<jstl:if test="${isListingCreated == true}">
				<acme:button url="advert/provider/edit.do?advertId=${row.id}"
					code="advert.edit" />
			</jstl:if>
		</display:column>
	</security:authorize>

</display:table>
