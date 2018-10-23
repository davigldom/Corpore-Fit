<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<form:form action="credit-card/user/edit.do" modelAttribute="creditCard"
	method="post">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="credit-card.name" path="name" />
	<acme:textbox code="credit-card.number" path="number" placeholder="XXXX XXXX XXXX XXXX" />
	
	<acme:textbox code="credit-card.expirationMonth" path="expirationMonth" placeholder="mm"/>
	<acme:textbox code="credit-card.expirationYear" path="expirationYear" placeholder="yyyy"/>
	
	<acme:textbox code="credit-card.cvv" path="CVV" />

	<acme:submit name="save" code="credit-card.save" />
	<acme:button url="credit-card/user/list.do" code="credit-card.cancel" />


</form:form>