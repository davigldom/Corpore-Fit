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

<jstl:if test="${shoppingCartPrice != null && shoppingCartPrice != 0.0}">
	<h3>
		<fmt:formatNumber var="priceTotalFormated" type="number"
			minFractionDigits="2" maxFractionDigits="2"
			value="${shoppingCartPrice}" />
		<spring:message code="cart.price.total" />
		:
		<jstl:out value="${priceTotalFormated }" />
		euros.
	</h3>
</jstl:if>

<br>
<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="shoppingCarts" requestURI="${requestURI }" id="row">


	<!-- Attributes -->

	<display:column>
		<img src="<jstl:out value="${row.product.image}" />"
			alt="${row.product.image }" height="100" />
	</display:column>

	<acme:column code="product.name" path="product.name" />

	<acme:column code="cart.ammount" path="amount" />

	<spring:message code="product.cart.price" var="priceHeader" />
	<display:column title="${priceHeader}" sortable="true">
		<jstl:choose>
			<jstl:when
				test="${row.product.advert != null && row.product.advert.start.time.time le now.time.time && row.product.advert.end.time.time ge now.time.time}">
				<fmt:formatNumber var="oldPriceFormat" type="number"
					minFractionDigits="2" maxFractionDigits="2"
					value="${row.product.price}" />
				<fmt:formatNumber var="newPriceFormat" type="number"
					minFractionDigits="2" maxFractionDigits="2"
					value="${((100 - row.product.advert.discount) * 0.01) * row.product.price}" />
				<p class="oldPriceList">
					<jstl:out value="${oldPriceFormat}" />
				</p>
				<jstl:out value="${newPriceFormat}" />
				<jstl:out value="(${row.product.advert.discount}%)" />
			</jstl:when>
			<jstl:otherwise>
				<fmt:formatNumber var="priceFormat" type="number"
					minFractionDigits="2" maxFractionDigits="2"
					value="${row.product.price}" />
				<jstl:out value="${priceFormat}" />
			</jstl:otherwise>
		</jstl:choose>
	</display:column>


	<acme:columnButton url="product/display.do?productId=${row.product.id}"
		code="product.display" />

	<acme:columnButton
		url="shopping-cart/user/removeProduct.do?cartId=${row.id }"
		code="cart.remove" />

</display:table>

<br>


<jstl:if test="${! empty creditCards && ! empty shoppingCarts}">
	<form action="shopping-cart/user/pay.do" method="get">

		<spring:message code="creditCard.list" />
		<select name="creditCardId">
			<jstl:forEach var="creditCard" items="${creditCards }">
				<option value="${creditCard.id }">
					<jstl:out value="${creditCard.number }" />
				</option>
			</jstl:forEach>
		</select>
		<acme:submit name="pay" code="product.buy" />
	</form>

</jstl:if>

<acme:button url="order/user/list-orders.do" code="myOrders" />
<br>
<acme:button url="/welcome/index.do" code="product.back" />
