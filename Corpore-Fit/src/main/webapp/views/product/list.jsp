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


<jstl:if test="${isListingCreated == false}">
	<jstl:if test="${!empty products}">
		<div id="search">
			<form method="get" action="product/search-word.do">
				<spring:message code="product.search" var="searchButton" />
				<input type="text" name="keyword"> 
				<input type="submit" value="${searchButton }">
			</form>
		</div>
		<br>
		<br>
	</jstl:if>
</jstl:if>


<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="products" requestURI="${requestURI }" id="row">


	<!-- Attributes -->
	
	<display:column>
		<img src="<jstl:out value="${row.image}" />" 
			alt="${row.image }" height="100" />
	</display:column>

	<acme:column code="product.name" path="name" />
	
	<spring:message code="product.price" var="priceHeader" />
	<display:column title="${priceHeader}" sortable="true">
		<jstl:choose>
			<jstl:when test="${row.advert != null && row.advert.start.time le now.time && row.advert.end.time ge now.time}">
				<fmt:formatNumber var="oldPriceFormat" type="number" minFractionDigits="2" maxFractionDigits="2" value="${row.price}" />
				<fmt:formatNumber var="newPriceFormat" type="number" minFractionDigits="2" maxFractionDigits="2" value="${((100 - row.advert.discount) * 0.01) * row.price}" />
				<p class="oldPriceList"><jstl:out value="${oldPriceFormat}" /></p>
				<jstl:out value="${newPriceFormat}" />
				<jstl:out value="(${row.advert.discount}%)" />
			</jstl:when>
			<jstl:otherwise>
				<fmt:formatNumber var="priceFormat" type="number" minFractionDigits="2" maxFractionDigits="2" value="${row.price}" />
				<jstl:out value="${priceFormat}" />
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
	<acme:column code="product.units" path="units" />

	<jstl:if test="${isListingCreated == false}">
		<acme:columnOut code="product.provider"
			path="${row.provider.name } ${row.provider.surname }" />
	</jstl:if>

	<acme:columnButton url="product/display.do?productId=${row.id}"
		code="product.display" />
		
	<security:authorize access="hasRole('PROVIDER')">
		<display:column>
			<jstl:if test="${isListingCreated == true}">
				<jstl:if test="${row.provider == principal}">
					<acme:button url="product/provider/edit.do?productId=${row.id}"
						code="product.edit" />
				</jstl:if>
			</jstl:if>
		</display:column>
	</security:authorize>

</display:table>

<jstl:if test="${searchProduct == true}">
	<br>
	<acme:button url="/product/list.do"
		code="product.back" />
</jstl:if>
