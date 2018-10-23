<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<script src="https://cdn.ckeditor.com/4.9.2/full/ckeditor.js"></script>

<form:form action="article/editor/edit.do" modelAttribute="article" method = "post">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="article.title" path="title"/>

	<form:textarea path="text" />
	
	<jstl:if test="${article.id != 0 }">
		<acme:checkbox code="article.draft" path="draft"/>
		<br>
	</jstl:if>
	
	<br>
	<acme:submit code="article.save" name="save" />
	
	<jstl:if test="${article.id != 0 }">
		<acme:delete url="article/editor/delete.do?articleId=${article.id}" code="article.delete" returnMessage="article.confirm.delete" />
	</jstl:if>

	<acme:button url="article/editor/list.do" code="article.cancel"/>
	
</form:form>

<script>
	CKEDITOR.replace('text');
</script>
