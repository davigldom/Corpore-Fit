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


<spring:message code="advert.provider" var="provider" />
<b><jstl:out value="${provider }: " /></b>
<jstl:out value="${advertProvider.name} ${advertProvider.surname }" />

<acme:button url="/actor/display.do?actorId=${advertProvider.id }"
	code="advert.providerProfile" />
<br><br>

<spring:message code="advert.start" var="start" />
<b><jstl:out value="${start}: " /></b>
<jstl:out value="${startFormatted}" />
<br><br>

<spring:message code="advert.end" var="end" />
<b><jstl:out value="${end}: " /></b>
<jstl:out value="${endFormatted}" />
<br><br>

<spring:message code="advert.discount" var="discount" />
<b><jstl:out value="${discount }: " /></b>
<jstl:out value="${advert.discount}%" />
<br><br>

<h2><b><spring:message code="advert.list.products" /></b></h2>
<jstl:if test="${!empty products}">
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
				<jstl:when test="${row.advert != null && row.advert.start.time.time le now.time.time && row.advert.end.time.time ge now.time.time}">
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
		
	
		<acme:columnButton url="product/display.do?productId=${row.id}"
			code="product.display" />
			
		<security:authorize access="hasRole('PROVIDER')">
			<display:column>
				<jstl:if test="${row.provider == principal}">
					<acme:button url="product/provider/edit.do?productId=${row.id}"
						code="product.edit" />
				</jstl:if>
			</display:column>
			<display:column>
				<jstl:if test="${row.provider == principal}">
					<acme:button url="advert/provider/remove.do?advertId=${advertId }&productId=${row.id}"
						code="advert.removeProduct" />
				</jstl:if>
			</display:column>
		</security:authorize>
	
	</display:table>
	<br>
</jstl:if>
<jstl:if test="${empty products}">
	<b><spring:message code="advert.noProducts" /></b>
	<br><br>
</jstl:if>


<security:authorize access="hasRole('PROVIDER')">
	<jstl:if test="${advertProvider == principal}">
		<acme:button url="advert/provider/edit.do?advertId=${advertId}"
			code="advert.edit" />
	</jstl:if>
</security:authorize>

<acme:button url="/advert/list.do"
	code="advert.back" />
<br>
