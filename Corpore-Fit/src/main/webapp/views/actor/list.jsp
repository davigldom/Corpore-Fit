<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<jstl:if test="${!empty actors}">
	<div id="search">
		<form method="get" action="actor/search-word.do">
			<spring:message code="actor.search" var="searchButton" />
			<input type="text" name="keyword"> <input type="submit"
				value="${searchButton }">
		</form>
	</div>
	<br>
	<br>
</jstl:if>


<display:table pagesize="5" class="displaytag" name="actors" id="row"
	requestURI="${requestURI}">


	<!-- Attributes -->

	<display:column>
		<jstl:if test="${row.photo == '' or row.photo == null}">
			<img id="circularPhoto" src="images/profile.jpg" width=150 height="150">
		</jstl:if>
		<jstl:if test="${row.photo != '' and row.photo != null}">
			<img id="circularPhoto" src="${row.photo }" width=150 height="150">
		</jstl:if>
	</display:column>

	<acme:column code="actor.name" path="name" />
	<acme:column code="actor.surname" path="surname" />
	<acme:column code="actor.email" path="email" />
	<jstl:if test="${row.userAccount.authorities[0] eq 'ADMIN' }">
		<spring:message code="actor.authority.admin" var="admin" />
		<acme:columnOut code="actor.authority" path="${admin }" />
	</jstl:if>
	<jstl:if test="${row.userAccount.authorities[0] eq 'USER' }">
		<spring:message code="actor.authority.user" var="user" />
		<acme:columnOut code="actor.authority" path="${user }" />
	</jstl:if>
	<jstl:if test="${row.userAccount.authorities[0] eq 'MANAGER' }">
		<spring:message code="actor.authority.manager" var="manager" />
		<acme:columnOut code="actor.authority" path="${manager }" />
	</jstl:if>
	<jstl:if test="${row.userAccount.authorities[0] eq 'EDITOR' }">
		<spring:message code="actor.authority.editor" var="editor" />
		<acme:columnOut code="actor.authority" path="${editor }" />
	</jstl:if>
	<jstl:if test="${row.userAccount.authorities[0] eq 'PROVIDER' }">
		<spring:message code="actor.authority.provider" var="provider" />
		<acme:columnOut code="actor.authority" path="${provider }" />
	</jstl:if>
	<jstl:if test="${row.userAccount.authorities[0] eq 'NUTRITIONIST' }">
		<spring:message code="actor.authority.nutritionist" var="nutritionist" />
		<acme:columnOut code="actor.authority" path="${nutritionist }" />
	</jstl:if>

	<display:column>
		<acme:button url="actor/display.do?actorId=${row.id}"
			code="actor.display" />
	</display:column>

	<jstl:if test="${requestURI=='actor/user/list-nutritionist.do' }">
		<jstl:if test="${nutritionistId==row.id }">
			<display:column>
				<acme:button
					url="actor/user/unchoose-nutritionist.do?nutritionistId=${row.id}"
					code="actor.user.nutritionist.unchoose" />
			</display:column>
		</jstl:if>
		<jstl:if test="${nutritionistId!=row.id }">
			<display:column>
				<acme:button
					url="actor/user/choose-nutritionist.do?nutritionistId=${row.id}"
					code="actor.user.nutritionist" />
			</display:column>
		</jstl:if>
	</jstl:if>

	<jstl:if test="${isFriendList }">
		<display:column>
			<acme:button
				url="friend-request/user/remove-friend.do?actorId=${row.id}"
				code="friendRequest.remove.friend" />
		</display:column>
	</jstl:if>
</display:table>

<jstl:if test="${searchActor == true}">
	<br>
	<acme:button url="/actor/list.do" code="actor.back" />

</jstl:if>
