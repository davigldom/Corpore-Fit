<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- Añadida -->
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table name="creditCards" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag" keepStatus="true">

	<acme:column code="credit-card.name" path="name" />
	<acme:column code="credit-card.number" path="number" />
	<acme:column code="credit-card.cvv" path="CVV" />
	<acme:column code="credit-card.expirationMonth" path="expirationMonth" />
	<acme:column code="credit-card.expirationYear" path="expirationYear" />

	<acme:columnButton
		url="credit-card/user/delete.do?creditCardId=${row.id }"
		code="credit-card.delete" />

</display:table>


<div>
<acme:button url="credit-card/user/create.do" code="credit-card.create"/>
</div>