<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- <div id="banner"> -->
<!-- 	<a title="Corpore Fit, Inc." href="welcome/index.do"> <img -->
<!-- 		src="images/banner2.jpg" alt="Corpore Fit, Inc." width="350" /> -->
<!-- 	</a> -->
<!-- </div> -->

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->

		<!-- NON AUTHENTICATED -->

		<security:authorize access="permitAll">
			<li><a class="fNiv" href="welcome/index.do"><span
					class="glyphicon glyphicon-home"></span></a>
			<li><a class="fNiv"><spring:message code="master.page.users" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/list.do"><spring:message
								code="master.page.users.list" /></a></li>
					<security:authorize access="hasRole('USER')">
						<li><a href="actor/user/list-nutritionist.do"><spring:message
									code="master.page.actors.list.nutritionist" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="actor/admin/list-banned.do"><spring:message
									code="master.page.actors.list.banned" /></a></li>
					</security:authorize>
				</ul></li>
		</security:authorize>


		<!-- USER -->
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv"><spring:message
						code="friendRequest.menu" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/user/list-friends.do"><spring:message
								code="friendRequest.list.friends" /></a></li>
					<li><a href="friend-request/user/list-received.do"><spring:message
								code="friendRequest.list.received" /></a></li>
					<li><a href="friend-request/user/list-sent.do"><spring:message
								code="friendRequest.list.sent" /></a></li>
				</ul></li>

		</security:authorize>


		<li><a class="fNiv"><span class="glyphicon glyphicon-stats"></span>
				<spring:message code="statistics.menu" /></a>
			<ul>
				<li class="arrow"></li>

				<security:authorize access="hasRole('USER')">
					<li><a href="actor/user/seeStatistics.do"><spring:message
								code="statistics.see" /></a></li>
				</security:authorize>
				<li><a href="actor/seeTop.do"><spring:message
							code="top.see" /></a></li>
			</ul></li>


		<!-- EDITOR -->

		<li><a class="fNiv"><spring:message code="article.menu" /></a>
			<ul>
				<li class="arrow"></li>
				<li><a href="article/list.do"><spring:message
							code="article.list.all" /></a></li>
				<security:authorize access="hasRole('EDITOR')">
					<li><a href="article/editor/create.do"><spring:message
								code="article.create" /></a></li>
					<li><a href="article/editor/list.do"><spring:message
								code="article.list.created" /></a></li>
				</security:authorize>
			</ul></li>


		<!-- PROVIDER -->

		<li><a class="fNiv"><spring:message code="product.menu" /></a>
			<ul>
				<li class="arrow"></li>
				<li><a href="product/list.do"><spring:message
							code="product.list.all" /></a></li>
				<li><a href="product/listWithAdverts.do"><spring:message
							code="product.list.withAdverts" /></a></li>
				<security:authorize access="hasRole('PROVIDER')">
					<li><a href="product/provider/create.do"><spring:message
								code="product.create" /></a></li>
					<li><a href="product/provider/list.do"><spring:message
								code="product.list.created" /></a></li>
				</security:authorize>
			</ul></li>

		<li><a class="fNiv"><spring:message code="advert.menu" /></a>
			<ul>
				<li class="arrow"></li>
				<li><a href="advert/list.do"><spring:message
							code="advert.list.all" /></a></li>
				<security:authorize access="hasRole('PROVIDER')">
					<li><a href="advert/provider/create.do"><spring:message
								code="advert.create" /></a></li>
					<li><a href="advert/provider/list.do"><spring:message
								code="advert.list.created" /></a></li>
				</security:authorize>
			</ul></li>


		<!-- ADMIN -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message
						code="master.page.register.actor" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="admin/create-manager.do"><spring:message
								code="master.page.manager" /></a></li>
					<li><a href="admin/create-provider.do"><spring:message
								code="master.page.provider" /></a></li>
					<li><a href="admin/create-editor.do"><spring:message
								code="master.page.editor" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.nutritionists" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="nutritionist/admin/list-not-validated.do"><spring:message
								code="nutritionist.not.validated" /></a></li>
				</ul></li>
		</security:authorize>

		<!-- GYM -->
		<li><a class="fNiv"><span class="glyphicon glyphicon-fire"></span>
				<spring:message code="master.page.gyms" /></a>
			<ul>
				<li class="arrow"></li>
				<li><a href="gym/list.do"><spring:message
							code="master.page.gym.list" /></a></li>
				<security:authorize access="hasRole('USER')">
					<li><a href="gym/user/list-close.do"><spring:message
								code="master.page.user.gym.list.close" /></a></li>
					<li><a href="gym/user/display.do"><spring:message
								code="master.page.user.gym.display" /></a></li>
				</security:authorize>
				<security:authorize access="hasRole('MANAGER')">
					<li><a href="gym/manager/display.do"><spring:message
								code="master.page.manager.gym.display" /></a></li>
				</security:authorize>
				<li><a href="gym/list-top.do"><spring:message
							code="master.page.gym.list.top" /></a></li>
			</ul></li>

		<security:authorize access="isAnonymous()">
			<li id="logout"><a class="fNiv" href="security/login.do"><span
					class="glyphicon glyphicon-log-in"></span></a></li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/display-principal.do"><spring:message
								code="master.page.display" /></a></li>
					<security:authorize access="hasRole('ADMIN')">

						<li><a href="actor/admin/dashboard.do"><spring:message
									code="master.page.admin.dashboard" /></a></li>

					</security:authorize>

					<security:authorize access="hasRole('USER')">
						<li><a href="diet/user/display-following.do"><spring:message
									code="master.page.user.diet" /></a></li>
						<li><a href="actor/user/my-nutritionist.do"><spring:message
									code="master.page.user.nutritionist" /></a></li>
						<li><a href="appointment/user/list.do"><spring:message
									code="appointment.my.appointments" /></a></li>
						<li><a href="credit-card/user/list.do"><spring:message
									code="creditCard.list" /></a></li>
						<li><a href="shopping-cart/user/display.do"><spring:message
									code="master.page.cart" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('NUTRITIONIST')">
						<li><a href="appointment/nutritionist/list.do"><spring:message
									code="appointment.my.appointments" /></a></li>
					</security:authorize>
				</ul></li>
			<li id="logout"><a class="fNiv" href="j_spring_security_logout">
					<span class="glyphicon glyphicon-off"></span>
			</a></li>
		</security:authorize>
	</ul>
</div>


<div id="languages">
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>



