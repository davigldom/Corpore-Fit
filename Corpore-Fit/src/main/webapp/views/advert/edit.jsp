<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<form:form action="advert/provider/edit.do" modelAttribute="advert" method = "post">

	<form:hidden path="id"/>
	<form:hidden path="version"/>

	
	<acme:textbox code="advert.name" path="name" />
	
	<acme:textbox code="advert.start" path="start" placeholder="dd/MM/yyyy HH:mm" />
	
	<acme:textbox code="advert.end" path="end" placeholder="dd/MM/yyyy HH:mm" />
	
	<acme:textbox code="advert.discount" path="discount" />
	
	<jstl:if test="${empty availablesToAdd}">
		<b><spring:message code="advert.noProductsToAdd" /></b>
	</jstl:if>
	<jstl:if test="${!empty availablesToAdd}">
		<b><spring:message code="advert.addProducts" /></b>
		<br>
		<select multiple="multiple" name="productId" size="10">
			<jstl:forEach items="${availablesToAdd}" var="product">
				<option value="${product.id}">
					<fmt:formatNumber var="priceFormat" type="number" minFractionDigits="2" maxFractionDigits="2" value="${product.price}" />
					<jstl:out value="${product.name} (${priceFormat}" /><spring:message code="advert.euro" />)
				</option>
			</jstl:forEach>
		</select> 	
	</jstl:if>
	<br><br>
	
	<acme:submit code="advert.save" name="save" />
	
	<jstl:if test="${advert.id != 0}">
		<acme:delete url="advert/provider/delete.do?advertId=${advert.id}" code="advert.delete" returnMessage="advert.confirm.delete" />
	</jstl:if>

	<acme:button url="advert/provider/list.do" code="advert.cancel"/>
	
</form:form>
