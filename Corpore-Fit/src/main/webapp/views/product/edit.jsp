<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="product/provider/edit.do" modelAttribute="product"
	method="post">

	<form:hidden path="id" />
	<form:hidden path="version" />



	<acme:textbox code="product.sku" path="sku" />

	<acme:textbox code="product.name" path="name" />

	<acme:textarea code="product.description" path="description" />

	<acme:textbox code="product.image" path="image" />

	<acme:textbox code="product.price" path="price" />

	<acme:textbox code="product.units" path="units" />


	<acme:submit code="product.save" name="save" />

	<security:authentication property="principal.username" var="username" />
	<jstl:if
		test="${product.id != 0 && product.provider.userAccount.username == username}">
		<acme:delete url="product/provider/delete.do?productId=${product.id}"
			code="product.delete" returnMessage="product.confirm.delete" />
	</jstl:if>

	<acme:button url="product/provider/list.do" code="product.cancel" />

</form:form>