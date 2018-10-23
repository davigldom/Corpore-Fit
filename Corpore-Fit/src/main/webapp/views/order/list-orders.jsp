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
	name="orders" requestURI="${requestURI }" id="row">


	<!-- Attributes -->
	
	<fmt:formatDate value="${row.date.time}" type="date" pattern="dd/MM/yyyy HH:mm" var ="formatedStart"/>
	<acme:columnOut code="order.date" path="${formatedStart}" />
	
	<acme:column code="order.creditCard.number" path="creditCard.number" />

	<acme:columnButton url="/order/user/list-products.do?orderId=${row.id }" code="order.list.products"/>


</display:table>

<br>
<acme:button url="/shopping-cart/user/display.do" code="product.back" />
