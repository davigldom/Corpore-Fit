<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<table id="myTable">
	<tr>
		<th style="text-align: center;"><spring:message code="gym.name" /></th>
		<th style="text-align: center;"><spring:message
				code="gym.address" /></th>
		<th style="text-align: center;"><spring:message
				code="gym.distance" /></th>
		<th></th>
	</tr>
	<jstl:forEach var="entry" items="${gyms2}">

		<tr>
			<td><jstl:out value="${entry.key.name}"></jstl:out></td>
			<td><jstl:out value="${entry.key.address}"></jstl:out></td>
			<td><jstl:out value="${entry.value}"></jstl:out></td>
			<td><acme:button url="gym/display.do?gymId=${entry.key.id }" code="gym.display"/></td>
		</tr>

	</jstl:forEach>
</table>

<script type="text/javascript">
<!--
	function sortTable() {
		var table, rows, switching, i, x, y, shouldSwitch;
		table = document.getElementById("myTable");
		switching = true;
		/* Make a loop that will continue until
		no switching has been done: */
		while (switching) {
			// Start by saying: no switching is done:
			switching = false;
			rows = table.getElementsByTagName("TR");
			/* Loop through all table rows (except the
			first, which contains table headers): */
			for (i = 1; i < (rows.length - 1); i++) {
				// Start by saying there should be no switching:
				shouldSwitch = false;
				/* Get the two elements you want to compare,
				one from current row and one from the next: */
				x = rows[i].getElementsByTagName("TD")[0];
				y = rows[i + 1].getElementsByTagName("TD")[0];
				// Check if the two rows should switch place:
				if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
					// If so, mark as a switch and break the loop:
					shouldSwitch = true;
					break;
				}
			}
			if (shouldSwitch) {
				/* If a switch has been marked, make the switch
				and mark that a switch has been done: */
				rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
				switching = true;
			}
		}
	}
//-->
</script>

