<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<div class="background">

	
	<jstl:if test="${empty diets}">
		<p>
			<spring:message code="diet.empty" />
		</p>
	</jstl:if>

	<jstl:if test="${!empty diets}">
		<jstl:forEach items="${diets}" var="entry">
		<div id="dietElement">
			
			<security:authorize access="hasRole('USER')">
					<a id="name" href="diet/user/display.do?dietId=<jstl:out value="${entry.id}"/>"><jstl:out
						value="${entry.name}" /></a>
			</security:authorize>	
			
			<security:authorize access="hasRole('NUTRITIONIST')">
			<a id="name"
					href="diet/nutritionist/display.do?dietId=<jstl:out value="${entry.id}"/>"><jstl:out
						value="${entry.name}" /></a>
			<a href="diet/nutritionist/delete.do?dietId=${entry.id }" id="delete"><span style="line-height: inherit;" class="glyphicon glyphicon-trash"></span></a>
			</security:authorize>
		
		</div>
				
		</jstl:forEach>
	</jstl:if>
		
		<security:authorize access="hasRole('NUTRITIONIST')">
		
		<div id="dietElement">
	
		<a id="name" href="diet/nutritionist/create.do"><span style="line-height: inherit;" class="glyphicon glyphicon-plus"></span></a>
				</div>
		</security:authorize>	
				
</div>
	
<!-- 	<div style="clear:both;"> -->
<%-- 		<acme:button url="actor/nutritionist/diet/create.do" code="diet.create" /> --%>
	
<!-- 	</div> -->
		