<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<input type="radio" name="display" onclick="displayMeasures()">
<spring:message code="measures" />
<br>
<input type="radio" name="display" onclick="displayMarks()">
<spring:message code="marks" />
<br>
<!-- <input type="radio" name="display"  onclick="showOthers()"> -->
<%-- <spring:message code="marks" /> --%>

<!-- <script type="text/javascript"> -->
<!-- // function showOthers() { -->
<!-- // 	document.getElementById('chart_div').style.display = 'none'; -->
<!-- // 	document.getElementById('others_div').style.display = 'block'; -->
<!-- // } -->


<!-- </script> -->

<spring:message var="time" code="time" />
<spring:message var="creationDate" code="mark.creationDate" />
<spring:message var="benchPress" code="mark.benchPress" />
<spring:message var="squat" code="mark.squat" />
<spring:message var="deadlift" code="mark.deadlift" />
<spring:message var="pullUp" code="mark.pullUp" />
<spring:message var="rowing" code="mark.rowing" />

<spring:message var="weight" code="measure.weight" />
<spring:message var="chest" code="measure.chest" />
<spring:message var="thigh" code="measure.thigh" />
<spring:message var="waist" code="measure.waist" />
<spring:message var="biceps" code="measure.biceps" />
<spring:message var="calf" code="measure.calf" />


<script type="text/javascript">
	var measures_date = [];
	var measures_weight = [];
	var measures_chest = [];
	var measures_thigh = [];
	var measures_waist = [];
	var measures_biceps = [];
	var measures_calf = [];

	var marks_date = [];
	var marks_benchPress = [];
	var marks_squat = [];
	var marks_deadlift = [];
	var marks_pullUp = [];
	var marks_rowing = [];
</script>
<jstl:forEach var="measure" items="${actor.measures }">
	<script type="text/javascript">
		measures_date.push('${measure.creationDate.time.time}');
		measures_weight.push('${measure.weight}');
		measures_chest.push('${measure.chest}');
		measures_thigh.push('${measure.thigh}');
		measures_waist.push('${measure.waist}');
		measures_biceps.push('${measure.biceps}');
		measures_calf.push('${measure.calf}');
	</script>
</jstl:forEach>

<jstl:forEach var="mark" items="${actor.marks }">
	<script type="text/javascript">
		marks_date.push('${mark.creationDate.time.time}');
		marks_benchPress.push('${mark.benchPress}');
		marks_squat.push('${mark.squat}');
		marks_deadlift.push('${mark.deadlift}');
		marks_pullUp.push('${mark.pullUp}');
		marks_rowing.push('${mark.rowing}');
	</script>
</jstl:forEach>
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js">
	
</script>
<script type="text/javascript">
	function displayMarks() {

		google.charts.load('current', {
			packages : [ 'corechart', 'line' ]
		});
		google.charts.setOnLoadCallback(drawLineColors);

		function drawLineColors() {
			var data = new google.visualization.DataTable();
			data.addColumn('date', 'X');
			data.addColumn('number', '${benchPress}');
			data.addColumn('number', '${squat}');
			data.addColumn('number', '${deadlift}');
			data.addColumn('number', '${pullUp}');
			data.addColumn('number', '${rowing}');

			for ( var i = 0, len = marks_date.length; i < len; i++) {
				var d = new Date(parseInt(marks_date[i]));
				data.addRow([ d, parseFloat(marks_benchPress[i]),
						parseFloat(marks_squat[i]),
						parseFloat(marks_deadlift[i]),
						parseFloat(marks_pullUp[i]),
						parseFloat(marks_rowing[i]) ]);

			}

			var options = {
				pointsVisible : true,
				hAxis : {
					title : '${time}',
					maxValue : new Date()
				},
				vAxis : {
					format : 'decimal'
				},
				colors : [ '#a52714', '#097138', 'red', 'blue', 'yellow' ]
			};

			var chart = new google.visualization.LineChart(document
					.getElementById('chart_div'));
			chart.draw(data, options);

		}
	}

	/* --------------------------------------------------------------------------------------------------- */

	function displayMeasures() {
		google.charts.load('current', {
			packages : [ 'corechart', 'line' ]
		});
		google.charts.setOnLoadCallback(drawLineColors);

		function drawLineColors() {
			var data = new google.visualization.DataTable();
			data.addColumn('date', 'X');
			data.addColumn('number', '${weight}');
			data.addColumn('number', '${chest}');
			data.addColumn('number', '${thigh}');
			data.addColumn('number', '${waist}');
			data.addColumn('number', '${biceps}');
			data.addColumn('number', '${calf}');

			for ( var i = 0, len = measures_date.length; i < len; i++) {
				var d = new Date(parseInt(measures_date[i]));
				data.addRow([ d, parseFloat(parseFloat(measures_weight[i])),
						parseFloat(measures_chest[i]),
						parseFloat(measures_thigh[i]),
						parseFloat(measures_waist[i]),
						parseFloat(measures_biceps[i]),
						parseFloat(measures_calf[i]) ]);
			}

			var options = {
				pointsVisible : true,
				hAxis : {
					title : 'Time',
					maxValue : new Date()
				},
				vAxis : {
					format : 'decimal'
				},
				colors : [ '#a52714', '#097138', 'red', 'blue', 'yellow',
						'orange' ]
			};

			var chart = new google.visualization.LineChart(document
					.getElementById('chart_div'));
			chart.draw(data, options);
		}
	}
</script>
<div style="width: 100%;" id="chart_div"></div>

