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

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<security:authorize access="isAuthenticated()">

	<security:authentication property="principal.username" var="user" />

</security:authorize>



<jstl:if test="${authority eq 'nutritionist'}">

	<jstl:if test="${!actor.validated}">

		<h1 style="color: red;">

			<spring:message code="actor.authority.nutritionist.notValidated" />

		</h1>

	</jstl:if>

</jstl:if>



<jstl:set var="allowed" value="true" />

<jstl:if test="${authority eq 'user' }">

	<jstl:if
		test="${actor.privacy=='CLOSED' and actor.userAccount.username!=user}">

		<jstl:set var="allowed" value="false" />

	</jstl:if>



	<jstl:if
		test="${actor.privacy=='FRIENDS' and actor.userAccount.username!=user and !isFriend}">

		<jstl:set var="allowed" value="false" />

	</jstl:if>

</jstl:if>







<spring:message code="actor.name" var="name" />

<b><jstl:out value="${name }: " /></b>

<jstl:out value="${actor.name}" />

<br>

<br>



<spring:message code="actor.surname" var="surname" />

<b><jstl:out value="${surname}: " /></b>

<jstl:out value="${actor.surname}" />

<br>

<br>







<jstl:if test="${actor.photo != '' and actor.photo != null}">

	<spring:message code="actor.photo" var="photo" />

	<b><jstl:out value="${photo}: " /></b>

	<br>

	<img src="<jstl:out value="${actor.photo}" />" alt="${actor.photo }"
		height="200" />

	<br>

	<br>

</jstl:if>



<jstl:if test="${actor.photo == '' or actor.photo == null}">

	<spring:message code="actor.photo" var="photo" />

	<b><jstl:out value="${photo}: " /></b>

	<br>

	<img src="images/profile.jpg" alt="${actor.photo }" height="200" />

	<br>

	<br>

</jstl:if>



<jstl:if test="${allowed }">



	<jstl:if test="${authority eq 'user' }">



		<jstl:set var="noTwitter" value="true" />

		<jstl:set var="noFacebook" value="true" />

		<jstl:set var="noYoutube" value="true" />

		<jstl:set var="noInstagram" value="true" />



		<jstl:forEach var="socialNetwork" items="${actor.socialNetworks}">

			<jstl:if test="${socialNetwork.socialNetworkType=='TWITTER' }">

				<a target="_blank" href="${socialNetwork.url }"><img
					width="50px" height="50px" alt="Twitter" src="images/twitter.png" /></a>

				<jstl:set var="noTwitter" value="false" />

				<jstl:if test="${actor.userAccount.username==user}">

					<a
						href="social-network/user/delete.do?socialNetworkId=${socialNetwork.id }">(<span
						class="glyphicon glyphicon-minus"></span>)

					</a>

				</jstl:if>

			</jstl:if>

			<jstl:if test="${socialNetwork.socialNetworkType=='FACEBOOK' }">

				<a target="_blank" href="${socialNetwork.url }"><img
					width="50px" height="50px" alt="Facebook" src="images/facebook.png" /></a>

				<jstl:set var="noFacebook" value="false" />

				<jstl:if test="${actor.userAccount.username==user}">

					<a
						href="social-network/user/delete.do?socialNetworkId=${socialNetwork.id }">(<span
						class="glyphicon glyphicon-minus"></span>)

					</a>

				</jstl:if>

			</jstl:if>

			<jstl:if test="${socialNetwork.socialNetworkType=='INSTAGRAM' }">

				<a target="_blank" href="${socialNetwork.url }"><img
					width="50px" height="50px" alt="Instagram"
					src="images/instagram.png" /></a>

				<jstl:set var="noInstagram" value="false" />

				<jstl:if test="${actor.userAccount.username==user}">

					<a
						href="social-network/user/delete.do?socialNetworkId=${socialNetwork.id }">(<span
						class="glyphicon glyphicon-minus"></span>)

					</a>

				</jstl:if>

			</jstl:if>

			<jstl:if test="${socialNetwork.socialNetworkType=='YOUTUBE' }">

				<a target="_blank" href="${socialNetwork.url }"><img
					width="50px" height="50px" alt="Youtube" src="images/youtube.png" /></a>

				<jstl:set var="noYoutube" value="false" />

				<jstl:if test="${actor.userAccount.username==user}">

					<a
						href="social-network/user/delete.do?socialNetworkId=${socialNetwork.id }">(<span
						class="glyphicon glyphicon-minus"></span>)

					</a>

				</jstl:if>

			</jstl:if>

		</jstl:forEach>



		<jstl:if test="${actor.userAccount.username==user}">

			<jstl:if test="${noTwitter }">

				<acme:button url="social-network/user/addTwitter.do"
					code="social.network.addTwitter" />

			</jstl:if>

			<jstl:if test="${noFacebook }">

				<acme:button url="social-network/user/addFacebook.do"
					code="social.network.addFacebook" />

			</jstl:if>

			<jstl:if test="${noInstagram }">

				<acme:button url="social-network/user/addInstagram.do"
					code="social.network.addInstagram" />

			</jstl:if>

			<jstl:if test="${noYoutube }">

				<acme:button url="social-network/user/addYoutube.do"
					code="social.network.addYoutube" />

			</jstl:if>

			<br>

		</jstl:if>

	</jstl:if>

	<br>

	<br>



	<fmt:formatDate value="${actor.birthdate.time}" type="date"
		pattern="dd/MM/yyyy" var="formatedBirthdate" />

	<spring:message code="actor.birthdate" var="birthdate" />

	<b><jstl:out value="${birthdate }: " /></b>

	<jstl:out value="${formatedBirthdate}" />

	<br>

	<br>



	<spring:message code="actor.email" var="email" />

	<b><jstl:out value="${email }: " /></b>

	<jstl:out value="${actor.email}" />

	<br>

	<br>



	<jstl:if test="${!actor.phone == '' }">

		<spring:message code="actor.phone" var="phone" />

		<b><jstl:out value="${phone }: " /></b>

		<jstl:out value="${actor.phone}" />

		<br>

		<br>

	</jstl:if>



	<jstl:if test="${!actor.address == '' }">

		<spring:message code="actor.address" var="address" />

		<b><jstl:out value="${address}: " /></b>

		<jstl:out value="${actor.address}" />

		<br>

		<br>

	</jstl:if>



	<jstl:if test="${authority eq 'nutritionist' }">





		<spring:message code="actor.curriculum" var="curriculum" />

		<b><jstl:out value="${curriculum}: " /></b>

		<a href="${actor.curriculum }">link</a>

		<br>

		<spring:message code="actor.officeAddress" var="offAddress" />

		<b><jstl:out value="${offAddress}: " /></b>

		<jstl:out value="${actor.officeAddress}" />

		<br>

		<jstl:if
			test="${actor.validated || actor.userAccount.username eq user}">

			<br>

			<br>

			<security:authorize access="!hasRole('ADMIN')">
				<acme:button url="schedule/display.do?nutritionistId=${actor.id }"
					code="actor.schedule" />

				<br>
			</security:authorize>

			<security:authorize access="hasRole('NUTRITIONIST')">



				<jstl:if test="${actor.validated}">

					<acme:button url="diet/nutritionist/list.do" code="actor.user.diet" />

				</jstl:if>

			</security:authorize>



		</jstl:if>





		<security:authorize access="hasRole('USER')">

			<jstl:if test="${actor.validated}">

				<br>

				<acme:button url="diet/user/list.do?nutritionistId=${actor.id }"
					code="actor.user.diet" />

			</jstl:if>

		</security:authorize>





		<security:authorize access="hasRole('ADMIN')">

			<acme:button url="schedule/display.do?nutritionistId=${actor.id }"
				code="actor.schedule" />

			<jstl:if test="${!actor.validated and isValid}">

				<acme:button
					url="nutritionist/admin/validate.do?nutritionistId=${actor.id}"
					code="nutritionist.validate" />

			</jstl:if>

		</security:authorize>



	</jstl:if>



	<jstl:if test="${authority eq 'user' }">

		<spring:message code="user.role" var="role" />

		<b><jstl:out value="${role}: " /></b>

		<jstl:if test="${actor.role=='CALISTHENICS' }">

			<spring:message code="user.CALISTHENICS" />

		</jstl:if>

		<jstl:if test="${actor.role!='CALISTHENICS' }">

			<jstl:out value="${actor.role}" />

		</jstl:if>

		<br>



		<spring:message code="user.privacy" var="privacy" />

		<b><jstl:out value="${privacy}: " /></b>

		<jstl:if test="${actor.privacy=='FRIENDS' }">

			<spring:message code="user.friends" />

		</jstl:if>

		<jstl:if test="${actor.privacy=='CLOSED' }">

			<spring:message code="user.closed" />

		</jstl:if>

		<jstl:if test="${actor.privacy=='OPEN' }">

			<spring:message code="user.open" />

		</jstl:if>

		<br>

		<br>

	</jstl:if>



	<jstl:if test="${authority eq 'user' }">



		<jstl:if
			test="${dietOfTheDay!=null && actor.userAccount.username==user}">





			<table>

				<tr>

					<th style="text-align: center;"><spring:message
							code="user.dietOfTheDay" /></th>

				</tr>

				<jstl:forEach var="food" items="${dietOfTheDay}">

					<tr>

						<td><a href="food/user/display.do?foodId=${food.id }"><jstl:out
									value="${food.name }" /></a></td>

					</tr>

				</jstl:forEach>

			</table>





		</jstl:if>



		<br>

		<br>



		<jstl:if
			test="${actor.privacy=='OPEN' or actor.userAccount.username==user or isFriend}">

			<acme:button url="mark/list.do?userId=${actor.id }" code="mark.see" />

			<acme:button url="measure/list.do?userId=${actor.id }"
				code="measure.see" />



			<jstl:if test="${exercisesOfTheDay!=null }">





				<table>

					<tr>

						<th style="text-align: center;"><spring:message
								code="user.exercisesOfTheDay" /></th>

					</tr>

					<jstl:forEach var="exercise" items="${exercisesOfTheDay}">

						<tr>

							<td><a href="exercise/display.do?exerciseId=${exercise.id }"><jstl:out
										value="${exercise.title }" /></a></td>

						</tr>

					</jstl:forEach>

				</table>





			</jstl:if>



			<br>

			<br>



			<jstl:if test="${actor.routine != null}">

				<acme:button url="routine/display.do?userId=${actor.id }"
					code="user.routine" />

			</jstl:if>



			<jstl:if test="${actor.userAccount.username==user}">

				<jstl:if test="${actor.routine == null}">

					<acme:button url="routine/user/create.do?userId=${actor.id }"
						code="routine.create" />

				</jstl:if>

			</jstl:if>

			<br>

			<br>

		</jstl:if>

	</jstl:if>

</jstl:if>





<security:authorize access="hasRole('USER')">

	<jstl:if test="${authority eq 'user' }">

		<jstl:if
			test="${actor.userAccount.username!=user and !isFriend and !existsRequest}">

			<acme:button url="friend-request/user/send.do?userId=${actor.id }"
				code="friendRequest.create" />

			<br>

			<br>

		</jstl:if>

	</jstl:if>

</security:authorize>





<security:authorize access="isAuthenticated()">

	<jstl:if test="${user == actor.userAccount.username }">

		<acme:button url="actor/edit.do?actorId=${actor.id}" code="actor.edit" />

	</jstl:if>

	<br>

	<br>

</security:authorize>



<security:authorize access="hasRole('ADMIN')">

	<jstl:if
		test="${!(actor.userAccount.authorities[0] eq 'ADMIN') and !actor.banned}">

		<acme:button url="actor/admin/ban.do?actorId=${actor.id}"
			code="actor.ban" />

	</jstl:if>

	<jstl:if test="${actor.banned}">

		<p style="color: red;">

			<spring:message code="actor.banned" />

		</p>

		<acme:button url="actor/admin/unban.do?actorId=${actor.id}"
			code="actor.unban" />

	</jstl:if>



	<jstl:if test="${authority eq 'provider' || authority eq 'editor'}">

		<acme:delete url="${authority}/admin/delete.do?actorId=${actor.id}"
			code="actor.delete" returnMessage="actor.confirm.delete" />

	</jstl:if>



</security:authorize>



<acme:button url="welcome/index.do" code="actor.back" />

<br>