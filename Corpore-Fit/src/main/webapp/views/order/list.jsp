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


<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="orderLines" requestURI="${requestURI }" id="row">


	<!-- Attributes -->

	<display:column>
		<img src="<jstl:out value="${row.image}" />" alt="${row.image }"
			height="100" />
	</display:column>

	<acme:column code="product.name" path="name" />
	<acme:column code="product.sku" path="sku" />
	<acme:column code="cart.ammount" path="amount" />

	<spring:message code="product.cart.price" var="priceHeader" />
	<display:column title="${priceHeader}" sortable="true">
		<fmt:formatNumber var="newPriceFormat" type="number"
			minFractionDigits="2" maxFractionDigits="2" value="${row.price}" />
		<jstl:out value="${newPriceFormat}" />
	</display:column>


</display:table>

<br>
<acme:button url="/order/user/list-orders.do" code="product.back" />
