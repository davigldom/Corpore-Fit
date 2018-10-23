<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="isAuthenticated()">
	<security:authentication property="principal.username" var="user" />
</security:authorize>

<form:form action="${requestURI }" modelAttribute="socialNetwork"
	method="post">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="socialNetworkType" />


	<%-- 	<form:label path="socialNetworkType">
		<spring:message code="social.network.type" />
	</form:label>
	<form:errors cssClass="error" path="socialNetworkType" />
	<br />
	<form:select path="socialNetworkType">
		<form:option selected="true" value="TWITTER">
			<jstl:out value="Twitter" />
		</form:option>
		<form:option value="INSTAGRAM">
			<jstl:out value="Instagram" />
		</form:option>
		<form:option value="FACEBOOK">
			<jstl:out value="Facebook" />
		</form:option>
		<form:option value="YOUTUBE">
			<jstl:out value="Youtube" />
		</form:option>
	</form:select> --%>

	<jstl:if test="${socialNetwork.socialNetworkType=='TWITTER' }">
		<h4>
			<jstl:out value="Twitter" />
		</h4>
	</jstl:if>
	<jstl:if test="${socialNetwork.socialNetworkType=='FACEBOOK' }">
		<h4>
			<jstl:out value="Facebook" />
		</h4>
	</jstl:if>
	<jstl:if test="${socialNetwork.socialNetworkType=='INSTAGRAM' }">
		<h4>
			<jstl:out value="Instagram" />
		</h4>
	</jstl:if>
	<jstl:if test="${socialNetwork.socialNetworkType=='YOUTUBE' }">
		<h4>
			<jstl:out value="YouTube" />
		</h4>
	</jstl:if>
	<br />
	<br />


	<acme:textbox code="social.network.url" path="url" />

	<acme:submit name="save" code="social.network.save" />
	<br />
	<br />

	<security:authorize access="hasRole('MANAGER')">
		<acme:button url="gym/display.do?gymId=${gymId }"
			code="social.network.cancel" />
	</security:authorize>

	<security:authorize access="hasRole('USER')">
		<acme:button url="actor/display.do?actorId=${userId }"
			code="social.network.cancel" />
	</security:authorize>
</form:form>