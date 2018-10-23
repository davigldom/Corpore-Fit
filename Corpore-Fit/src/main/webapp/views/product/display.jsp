<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="productImg">
	<img class="product" src="<jstl:out value="${product.image}" />"
		alt="${product.image }" />
</div>

<div class="productInfo">
	<div class="productPrice">
		<jstl:if test="${isDiscounted == false}">
			<div class="newPrice">
				<fmt:formatNumber var="priceFormat" type="number"
					minFractionDigits="2" maxFractionDigits="2"
					value="${product.price}" />
				<jstl:out value="${priceFormat}" />
				<spring:message code="advert.euro" />
			</div>
		</jstl:if>
		<jstl:if test="${isDiscounted == true}">
			<div class="newPrice">
				<fmt:formatNumber var="newPriceFormat" type="number"
					minFractionDigits="2" maxFractionDigits="2"
					value="${priceDiscounted}" />
				<jstl:out value="${newPriceFormat}" />
				<spring:message code="advert.euro" />
			</div>
			<div class="oldPrice">
				<fmt:formatNumber var="oldPriceFormat" type="number"
					minFractionDigits="2" maxFractionDigits="2"
					value="${product.price}" />
				<jstl:out value="${oldPriceFormat}" />
				<spring:message code="advert.euro" />
			</div>
			<div class="discountLabel">
				<b><jstl:out value="${discount}" />% <spring:message
						code="product.discount" /></b> <br> <b><jstl:out
						value="${endFormatted}" /></b>
			</div>
		</jstl:if>
	</div>

	<div class="productOtherInfo">
		<div class="productDescription">
			<jstl:out value="${product.description}" />
			<security:authorize access="hasRole('PROVIDER')">
				<jstl:if test="${product.provider == principal}">
					<i>(<spring:message code="product.sku" />:<jstl:out
							value=" ${product.sku }" />)
					</i>
				</jstl:if>
			</security:authorize>
		</div>

		<div class="productProvider">
			<spring:message code="product.soldBy"/> <a href="actor/display.do?actorId=${product.provider.id }"><jstl:out value="${product.provider.name} ${product.provider.surname }" /></a>
		</div>
		
		<security:authorize access="hasRole('USER') or hasRole('ROLE_ANONYMOUS')">
			<jstl:if test="${product.units>=1 }">
				<spring:message code="product.addToCart" var="buy" />
				<form action="shopping-cart/user/addProduct.do">
					<input hidden="true" value="${product.id }" name="productId">
					<div class="productAmount">
						<b><spring:message code="cart.ammount" />: </b><input type="number" min="1" max="${product.units }" required name="amount"> (<jstl:out value="${product.units}" /> <spring:message code="product.stock" />)
					</div>
					<div class="productBuy">
						<input type="submit" class="buyButton" value="${buy }">
					</div>
				</form>
			</jstl:if>
		</security:authorize>
	</div>
</div>
<br>
<br>

<security:authorize access="hasRole('PROVIDER')">
	<jstl:if test="${product.provider == principal}">
		<acme:button url="product/provider/edit.do?productId=${productId}"
			code="product.edit" />
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('ADMIN')">
	<acme:delete url="product/admin/delete.do?productId=${product.id}"
		code="product.delete" returnMessage="product.confirm.delete" />
</security:authorize>

<acme:button url="/product/list.do" code="product.back" />
