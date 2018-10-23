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
	<jstl:if test="${alreadyRated == false}">

		<h4>
			<b><spring:message code="article.rate" /></b>
		</h4>
		<div style="display:inline-block;margin:auto;">
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
	</jstl:if>
	<jstl:if test="${alreadyRated == true}">
		<p class="averageNumber"><b><spring:message code="article.averageRating" />: </b>
		<jstl:out value="${averageRating}" /></p>
	</jstl:if>
</security:authorize>

<div id="articleBody">
	<div class="publishedDiv">
		<jstl:choose>
			<jstl:when test="${momentFormated != null }">
				<spring:message code="article.publishedOn"/> <jstl:out value="${momentFormated}" /> <spring:message code="article.by"/> <a href="actor/display.do?actorId=${article.editor.id }"><jstl:out value="${article.editor.name} ${article.editor.surname }" /></a>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="article.noPublishedYet" var="noPublishedYet" />
				<jstl:out value="${noPublishedYet}" />
			</jstl:otherwise>
		</jstl:choose>
	</div>
	
	
	<security:authorize access="hasRole('EDITOR')">
		<div class="draftDiv">
			<spring:message code="article.draftMode" var="draftMode" />
			<spring:message code="article.finalMode" var="finalMode" />
			<jstl:choose>
				<jstl:when test="${article.draft == true}">
					[<jstl:out value="${draftMode}" />]
				</jstl:when>
				<jstl:otherwise>
					[<jstl:out value="${finalMode}" />]
				</jstl:otherwise>
			</jstl:choose>
		</div>
	</security:authorize>
	
	<div class="articleText">
		${article.text}
	</div>
</div>
<br>


<security:authorize access="hasRole('EDITOR')">
	<jstl:if test="${article.editor == principal}">
		<jstl:if test="${article.publicationDate == null}">
			<jstl:if test="${article.draft == true}">
				<acme:button url="article/editor/edit.do?articleId=${articleId}"
					code="article.edit" />
			</jstl:if>
		</jstl:if>
		<button type="button" class="btn btn-primary" onclick="returnDelete('article/editor/delete.do?articleId=${article.id}','article')" >
			<spring:message code="article.delete" />
		</button>
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${article.draft == false}">
		<button type="button" class="btn btn-primary" onclick="returnDelete('article/admin/delete.do?articleId=${article.id}','article')" >
			<spring:message code="article.delete" />
		</button>
	</jstl:if>
</security:authorize>

<jstl:if test="${article.editor != principal}">
	<acme:button url="/article/list.do" code="article.back" />
</jstl:if>
<jstl:if test="${article.editor == principal}">
	<acme:button url="/article/editor/list.do" code="article.back" />
</jstl:if>

<jstl:if test="${message != null}">
	<br>
	<br>
	<span class="message"><spring:message code="${message}" /></span>
</jstl:if>


<br>
<br>
<img src="images/line.png" width="80%" />
<br>
<br>
<a id="aComments"></a>

<jstl:if test="${principal != null}">
	<div class="addComment">
		<form:form
			action="comment/edit.do?articleId=${articleId }&parentCommentId=0"
			modelAttribute="comment" method="post" id="formularioAdd">

			<form:hidden path="id" />
			<form:hidden path="version" />


			<div class="principalPhotoAdd">
				<img src="<jstl:out value='${principal.photo}' />"
					alt="${principal.photo }" class="photo" />
			</div>

			<div id="contenedorAdd">
				<div id="textDivAdd" contenteditable="true" onfocus="clearDivAdd()"><spring:message code="comment.placeholder" /></div>
			</div>
			<br>
			<form:errors path="text" cssClass="error" />

			<form:hidden path="text" id="commentInput" />

			<div id="addButtons">
				<acme:submit code="article.save" name="save" id="saveComment" />
				<button type="button" class="btn btn-primary"
					onclick="cancelComment()">
					<spring:message code="article.cancel" />
				</button>
			</div>
		</form:form>
	</div>
</jstl:if>



<jstl:if test="${!empty comments}">
	<div class="commentsDiv">
		<jstl:forEach items="${comments}" var="comment">
			<div class="commentDiv"
				onmouseover="showDeleteComment('${comment.id}')"
				onmouseout="hideDeleteComment('${comment.id}')">
				<div class="commentPhoto">
					<img src="<jstl:out value='${comment.owner.photo}' />"
						alt="${comment.owner.photo }" class="photo" />
				</div>

				<div class="commentText">
					<b><spring:message code="comment.commentedOn"/> <fmt:formatDate value="${comment.moment.time}" type="date" pattern="dd/MM/yyyy HH:mm"/> <spring:message code="article.by"/> <a href="actor/display.do?actorId=${comment.owner.id }"><jstl:out value="${comment.owner.name} ${comment.owner.surname }" /></a>:</b>
					<br>
					<jstl:out value="${comment.text}"></jstl:out>
				</div>
				
				<jstl:if test="${comment.owner eq principal}">
					<div class="deleteComment" id="deleteComment${comment.id }">	
						<button type="button" class="btn btn-primary" onclick="returnDelete('comment/delete.do?commentId=${comment.id}','comment')" >
							<spring:message code="article.delete" />
						</button>				
					</div>
				</jstl:if>

				<security:authorize access="hasRole('ADMIN')">
					<div class="deleteComment" id="deleteComment${comment.id }">
						<button type="button" class="btn btn-primary" onclick="returnDelete('comment/admin/delete.do?commentId=${comment.id}','comment')" >
							<spring:message code="article.delete" />
						</button>
					</div>
				</security:authorize>

				<jstl:if test="${principal != null}">
					<div class="commentReply">
						<form:form
							action="comment/edit.do?articleId=${articleId }&parentCommentId=${comment.id }"
							modelAttribute="comment" method="post" id="formularioReply">

							<form:hidden path="id" />
							<form:hidden path="version" />


							<form:errors path="text" cssClass="error" />
							<div class="contenedorReply">
								<div class="textDivReply" id="textDivReply${comment.id }" contenteditable="true" onfocus="clearDivReply('${comment.id}')"><spring:message code="comment.reply.placeholder" /></div>
							</div>
							<div class="principalPhotoReply">
								<img src="<jstl:out value='${principal.photo}' />"
									alt="${principal.photo }" class="photo" />
							</div>
							<br>

							<form:hidden path="text" id="replyInput${comment.id }" />

							<div class="replyButtons" id="replyButtons${comment.id }">
								<button type="submit" name="save"
									onclick="submitReply('${comment.id}')" class="btn btn-primary">
									<spring:message code="comment.reply" />
								</button>
								<button type="button" class="btn btn-primary"
									onclick="cancelReply('${comment.id}')">
									<spring:message code="article.cancel" />
								</button>
							</div>

						</form:form>
					</div>
				</jstl:if>

				<jstl:if test="${!empty comment.replies}">
					<jstl:forEach items="${comment.replies}" var="reply">
						<div class="replyDiv" onmouseover="showDeleteReply('${reply.id}')"
							onmouseout="hideDeleteReply('${reply.id}')">
							<div class="replyPhoto">
								<img src="<jstl:out value='${reply.owner.photo}' />"
									alt="${reply.owner.photo }" class="photo" />
							</div>

							<div class="replyText">
								<b><spring:message code="comment.reply.repliedOn"/> <fmt:formatDate value="${reply.moment.time}" type="date" pattern="dd/MM/yyyy HH:mm"/> <spring:message code="article.by"/> <a href="actor/display.do?actorId=${reply.owner.id }"><jstl:out value="${reply.owner.name} ${reply.owner.surname }" /></a>:</b>
								<br>
								<jstl:out value="${reply.text}"></jstl:out>
							</div>

							<jstl:if test="${reply.owner eq principal}">
								<div class="deleteReply" id="deleteReply${reply.id }">
									<button type="button" class="btn btn-primary" onclick="returnDelete('comment/delete.do?commentId=${reply.id}','reply')" >
										<spring:message code="article.delete" />
									</button>
								</div>
							</jstl:if>
							<security:authorize access="hasRole('ADMIN')">
								<div class="deleteReply" id="deleteReply${reply.id }">
									<button type="button" class="btn btn-primary" onclick="returnDelete('comment/admin/delete.do?commentId=${reply.id}','reply')" >
										<spring:message code="article.delete" />
									</button>
								</div>
							</security:authorize>
						</div>
					</jstl:forEach>
				</jstl:if>
			</div>		
		</jstl:forEach>
	</div>
</jstl:if>


<script>
	$('#ratingForm input').on('change', function() {
		ratingValue = $('input[name=rating]:checked', '#ratingForm').val();
		articleId = "${article.id}";
		$.get("article/rate.do", {
			articleId : articleId,
			rating : ratingValue
		}).done(function() {
			location.reload(true);
		});
	});
</script>

<script type="text/javascript">
	var a = document.getElementById("textDivAdd");

	function clearDivAdd() {
		document.getElementById("addButtons").style.display = "inline-block";
		if (a.innerText == '<spring:message code="comment.placeholder" />') {
			a.innerHTML = '';
		}
	};

	function cancelComment() {
		document.getElementById("addButtons").style.display = "none";
		a.innerHTML = '<spring:message code="comment.placeholder" />';
	};

	document.forms["formularioAdd"].onsubmit = function() {
		if (a.innerText != '<spring:message code="comment.placeholder" />') {
			document.getElementById("commentInput").value = a.innerText;
		}
	};

	function clearDivReply(id) {
		document.getElementById("replyButtons" + id).style.display = "inline-block";
		if (document.getElementById("textDivReply" + id).innerText == '<spring:message code="comment.reply.placeholder" />') {
			document.getElementById("textDivReply" + id).innerHTML = '';
		}
	};

	function cancelReply(id) {
		document.getElementById("replyButtons" + id).style.display = "none";
		document.getElementById("textDivReply" + id).innerHTML = '<spring:message code="comment.reply.placeholder" />';
	};

	function submitReply(id) {
		if (document.getElementById("textDivReply" + id).innerText != '<spring:message code="comment.placeholder" />') {
			document.getElementById("replyInput" + id).value = document.getElementById("textDivReply" + id).innerText;
		}
	};

	function showDeleteReply(id) {
		document.getElementById("deleteReply" + id).style.display = "inline-block";
	};

	function hideDeleteReply(id) {
		document.getElementById("deleteReply" + id).style.display = "none";
	};

	function showDeleteComment(id) {
		document.getElementById("deleteComment" + id).style.display = "inline-block";
	};

	function hideDeleteComment(id) {
		document.getElementById("deleteComment" + id).style.display = "none";
	};
	
	function returnDelete(url, type){
		var result = null;
		if (type == 'article') {
			result = confirm('<spring:message code="article.confirm.delete" />');
		} else if (type == 'comment') {
			result = confirm('<spring:message code="comment.confirm.delete" />');
		} else if (type == 'reply') {
			result = confirm('<spring:message code="comment.reply.confirm.delete" />');
		}
		if (result) {
			relativeRedir(url);
		}
	};
</script>
