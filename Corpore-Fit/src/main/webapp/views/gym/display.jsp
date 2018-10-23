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


<security:authorize access="isAuthenticated()">
	<security:authentication property="principal.username" var="user" />
</security:authorize>

<security:authorize access="hasRole('MANAGER')">
	<jstl:if test="${!hasGym}">
		<acme:button url="gym/manager/create.do" code="gym.create" />
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('USER')">
	<jstl:if test="${!hasGymUser}">
		<acme:button url="gym/list.do" code="gym.list" />
	</jstl:if>
</security:authorize>

<jstl:if test="${hasGym || hasGymUser}">
	<security:authorize access="hasRole('USER')">
		<jstl:if test="${!alreadyRated && isSubscribed}">
			<h4>
				<spring:message code="gym.rate" />
			</h4>
			<div style="display: inline-block; margin: auto;">
				<form id="ratingForm">
					<fieldset class="rating">
						<input type="radio" id="star5" name="rating" value="5" /><label
							class="full" for="star5"></label> <input type="radio" id="star4"
							name="rating" value="4" /><label class="full" for="star4"></label>
						<input type="radio" id="star3" name="rating" value="3" /><label
							class="full" for="star3"></label> <input type="radio" id="star2"
							name="rating" value="2" /><label class="full" for="star2"></label>
						<input type="radio" id="star1" name="rating" value="1" /><label
							class="full" for="star1"></label>
					</fieldset>
				</form>
			</div>

			<br>
			<br>
		</jstl:if>
	</security:authorize>

	<b><spring:message code="gym.averageRating" />: </b>
	<jstl:out value="${averageRating}" />
	<br>
	<br>

	<jstl:if test="${gym!=null}">

		<spring:message code="gym.name" var="name" />
		<b><jstl:out value="${name }: " /></b>
		<jstl:out value="${gym.name}" />
		<br>

		<spring:message code="gym.description" var="description" />
		<b><jstl:out value="${description}: " /></b>
		<jstl:out value="${gym.description}" />
		<br>

		<spring:message code="gym.photo" var="photo" />
		<b><jstl:out value="${photo }: " /></b>
		<br>
		<jstl:choose>
			<jstl:when test="${gym.photo != '' }">
				<img src="<jstl:out value="${gym.photo }" />" alt="${photo }"
					height="200" />
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="gym.noPicture" var="noPicture" />
				<jstl:out value="${noPicture }" />
			</jstl:otherwise>
		</jstl:choose>
		<br>

		<jstl:set var="noTwitter" value="true" />
		<jstl:set var="noFacebook" value="true" />
		<jstl:set var="noYoutube" value="true" />
		<jstl:set var="noInstagram" value="true" />

		<jstl:forEach var="socialNetwork" items="${gym.socialNetworks}">
			<jstl:if test="${socialNetwork.socialNetworkType=='TWITTER' }">
				<a target="_blank" href="${socialNetwork.url }"><img
					width="50px" height="50px" alt="Twitter" src="images/twitter.png" /></a>
				<jstl:set var="noTwitter" value="false" />
				<jstl:if test="${gym.manager.userAccount.username==user}">
					<a
						href="social-network/manager/delete.do?socialNetworkId=${socialNetwork.id }">(<span
						class="glyphicon glyphicon-minus"></span>)
					</a>
				</jstl:if>
			</jstl:if>
			<jstl:if test="${socialNetwork.socialNetworkType=='FACEBOOK' }">
				<a target="_blank" href="${socialNetwork.url }"><img
					width="50px" height="50px" alt="Facebook" src="images/facebook.png" /></a>
				<jstl:set var="noFacebook" value="false" />
				<jstl:if test="${gym.manager.userAccount.username==user}">
					<a
						href="social-network/manager/delete.do?socialNetworkId=${socialNetwork.id }">(<span
						class="glyphicon glyphicon-minus"></span>)
					</a>
				</jstl:if>
			</jstl:if>
			<jstl:if test="${socialNetwork.socialNetworkType=='INSTAGRAM' }">
				<a target="_blank" href="${socialNetwork.url }"><img
					width="50px" height="50px" alt="Instagram"
					src="images/instagram.png" /></a>
				<jstl:set var="noInstagram" value="false" />
				<jstl:if test="${gym.manager.userAccount.username==user}">
					<a
						href="social-network/manager/delete.do?socialNetworkId=${socialNetwork.id }">(<span
						class="glyphicon glyphicon-minus"></span>)
					</a>
				</jstl:if>
			</jstl:if>
			<jstl:if test="${socialNetwork.socialNetworkType=='YOUTUBE' }">
				<a target="_blank" href="${socialNetwork.url }"><img
					width="50px" height="50px" alt="Youtube" src="images/youtube.png" /></a>
				<jstl:set var="noYoutube" value="false" />
				<jstl:if test="${gym.manager.userAccount.username==user}">
					<a
						href="social-network/manager/delete.do?socialNetworkId=${socialNetwork.id }">(<span
						class="glyphicon glyphicon-minus"></span>)
					</a>
				</jstl:if>
			</jstl:if>
		</jstl:forEach>

		<jstl:if test="${gym.manager.userAccount.username==user}">
			<jstl:if test="${noTwitter }">
				<acme:button url="social-network/manager/addTwitter.do"
					code="social.network.addTwitter" />
			</jstl:if>
			<jstl:if test="${noFacebook }">
				<acme:button url="social-network/manager/addFacebook.do"
					code="social.network.addFacebook" />
			</jstl:if>
			<jstl:if test="${noInstagram }">
				<acme:button url="social-network/manager/addInstagram.do"
					code="social.network.addInstagram" />
			</jstl:if>
			<jstl:if test="${noYoutube }">
				<acme:button url="social-network/manager/addYoutube.do"
					code="social.network.addYoutube" />
			</jstl:if>
			<br>

		</jstl:if>

		<br>
		<br>

		<spring:message code="gym.address" var="address" />
		<b><jstl:out value="${address }: " /></b>
		<jstl:out value="${gym.address}" />
		<br>

		<spring:message code="gym.schedule" var="schedule" />
		<b><jstl:out value="${schedule }: " /></b>
		<jstl:out value="${gym.schedule}" />
		<br>

		<spring:message code="gym.fees.display" var="fees" />
		<spring:message code="gym.fees.euro" var="euro" />
		<b><jstl:out value="${fees }" /></b>
		<jstl:out value="${gym.fees}" />
		<br>

		<spring:message code="gym.services" var="services" />
		<b><jstl:out value="${services }: " /></b>
		<jstl:out value="${gym.services}" />
		<br>

		<br />
		<br />

		<spring:message code="gym.manager" var="manager" />
		<b><jstl:out value="${manager }: " /></b>
		<jstl:out value="${gym.manager.name}   " />
		<%-- 		<jstl:if test="${gym.manager.banned=0}"/> --%>
		<jstl:if test="${!gym.manager.banned}" />
		<acme:button url="actor/display.do?actorId=${gym.manager.id }"
			code="gym.manager.display" />
		<%-- 		</jstl:if> --%>
		<br />
		<br />

		<acme:button url="activity/list.do?gymId=${gym.id}"
			code="gym.activity.list" />
		<br />
		<br />

		<security:authorize access="hasRole('ADMIN')">
			<acme:delete url="gym/admin/delete.do?gymId=${gym.id}"
				code="gym.delete" returnMessage="gym.delete.confirm" />
			<br />
			<br />
		</security:authorize>


		<security:authorize access="hasRole('USER')">
			<jstl:if test="${!isSubscribed}">
				<jstl:if test="${!hasSubscription}">
					<acme:button url="subscription/user/create.do?gymId=${gym.id}"
						code="gym.subscription.create" />
					<br />
					<br />
				</jstl:if>
			</jstl:if>
			<jstl:if test="${isSubscribed}">
				<acme:button url="subscription/user/delete.do?gymId=${gym.id}"
					code="gym.subscription.delete" />
				<br />
				<br />
			</jstl:if>
		</security:authorize>

		<security:authorize access="hasRole('MANAGER')">
			<jstl:if test="${gym.manager.userAccount.username == user}">
				<acme:button url="room/manager/list.do?gymId=${gym.id}"
					code="gym.room.list" />
				<br />
				<br />
				<acme:button url="gym/manager/edit.do?gymId=${gym.id}"
					code="gym.edit" />
				<br />
				<br />
			</jstl:if>
		</security:authorize>


		<acme:button url="gym/list.do" code="actor.back" />
		<br />
	</jstl:if>
</jstl:if>

<script>
	$('#ratingForm input').on('change', function() {
		ratingValue = $('input[name=rating]:checked', '#ratingForm').val();
		gymId = "${gym.id}";
		$.get("gym/user/rate.do", {
			gymId : gymId,
			rating : ratingValue
		}).done(function() {
			location.reload(true);
		});
	});
</script>
