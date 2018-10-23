<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



	<fmt:formatNumber var="ratioWithoutGymFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioWithoutGym*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioWithoutGym"/><jstl:out value=": ${ratioWithoutGymFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioWithoutDietFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioWithoutDiet*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioWithoutDiet"/><jstl:out value=": ${ratioWithoutDietFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioWithClosedPrivacyFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioWithClosedPrivacy*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioWithClosedPrivacy"/><jstl:out value=": ${ratioWithClosedPrivacyFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioWithOpenPrivacyFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioWithOpenPrivacy*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioWithFriendsPrivacy"/><jstl:out value=": ${ratioWithOpenPrivacyFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioWithFriendsPrivacyFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioWithFriendsPrivacy*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioWithOpenPrivacy"/><jstl:out value=": ${ratioWithFriendsPrivacyFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioBodybuilderFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioBodybuilder*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioBodybuilder"/><jstl:out value=": ${ratioBodybuilderFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioCrossfitterFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioCrossfitter*100.0}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioCrossfitter"/><jstl:out value=": ${ratioCrossfitterFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioCalisthenicsFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioCalisthenics*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioCalisthenics"/><jstl:out value=": ${ratioCalisthenicsFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioPowerlifterFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioPowerlifter*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioPowerlifter"/><jstl:out value=": ${ratioPowerlifterFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioBodybuilderFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioBodybuilder*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioBodybuilder"/><jstl:out value=": ${ratioBodybuilderFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioWeightlifterFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioWeightlifter*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioWeightlifter"/><jstl:out value=": ${ratioWeightlifterFormat}%"></jstl:out>
	<br>
	
	<fmt:formatNumber var="ratioRoleNoneFormat" type="number" minFractionDigits="2" maxFractionDigits="3" value="${ratioRoleNone*100}" />
	<spring:message code="actor.admin.dashboard.ratio.ratioRoleNone"/><jstl:out value=": ${ratioRoleNoneFormat}%"></jstl:out>
	