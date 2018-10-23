<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="subscription/user/edit.do" modelAttribute="subscription" method="post">

		<form:hidden path="id"/>
		<form:hidden path="version"/>
		<form:hidden path="gym"/>
		
		
		<jstl:if test="${empty creditCards }">
			<spring:message code="subscription.creditCard.empty" var="creditCardsEmpty" />
			<jstl:out value="${creditCardsEmpty }: " />
			<br/>
			<br/>
		</jstl:if>
		
		<jstl:if test="${!empty creditCards }">
			<form:label path="creditCard">
				<spring:message code="subscription.creditCard" />
			</form:label>
			<form:errors cssClass="error" path="creditCard" />
			<br/>
			<form:select id="creditCards" path="creditCard">
				<form:option value="0" label="----" />		
				<jstl:forEach var="creditCard" items="${creditCards }">
					<form:option value="${creditCard.id}">
						<jstl:out value="${creditCard.number}" />
					</form:option>
				</jstl:forEach>
			</form:select>
			<br/><br>
			
			<acme:submit name="save" code="subscription.save"/>
			<br/>
			<br/>
		</jstl:if>
		
		<acme:button url="credit-card/user/create.do?" code="subscription.creditCard.create"/>
		<br/><br/>
		
		<acme:button url="gym/display.do?gymId=${subscription.gym.id }" code="subscription.cancel"/>
</form:form>