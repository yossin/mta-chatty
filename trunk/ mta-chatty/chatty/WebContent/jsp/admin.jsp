<%@page import="edu.mta.chatty.domain.admin.DailyCountStatistic"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
<jsp:useBean id="statistics" scope="request" class="edu.mta.chatty.domain.admin.CountStatistics" />

<script type="text/javascript">

	function getDataSetBudies(){
		var arr   = [];
		<%
		for (DailyCountStatistic day: statistics.getBuddyStatistics()){
		%>
		//arr.push([new Date(<%=day.getDay().getTime()%>),<%=day.getCount()%>]);
		arr.push([<%=day.getDay().getTime()%>,<%=day.getCount()%>]);
		<%}%>
		return arr;
	}
	
	function getDataSetGroups(){
		var arr   = [];
		<%
		for (DailyCountStatistic day: statistics.getGroupStatistics()){
		%>
		//arr.push([new Date(<%=day.getDay().getTime()%>),<%=day.getCount()%>]);
		arr.push([<%=day.getDay().getTime()%>,<%=day.getCount()%>]);
		<%}%>
		return arr;
	}
	
	function getDataSetMessages(){
		var arr   = [];
		<%
		for (DailyCountStatistic day: statistics.getBuddyMessageStatistics()){
		%>
		//arr.push([new Date(<%=day.getDay().getTime()%>),<%=day.getCount()%>]);
		arr.push([<%=day.getDay().getTime()%>,<%=day.getCount()%>]);
		<%}%>
		return arr;
	}
	
	$(function() {
		
		var dataSetBudies   = getDataSetBudies();
		var dataSetGroups   = getDataSetGroups();
		var dataSetMessages = getDataSetMessages();

		
		var grid_options = {series:{lines:{fill:false}}, grid:{backgroundColor: {colors: ["#D1D1D1", "#7A7A7A"]}}, xaxis:{mode:"time", timeformat:"%0y/%0d/%0m %H:%0M"}};

		var dataBG = [ { data: dataSetBudies, label:"Budies", points: {show: true}, lines: {show: true} },
		               { data: dataSetGroups, label:"Groups", points: {show: true}, lines: {show: true} } ];
		
		var dataM = [ { data: dataSetMessages, label:"Messages/Day", points: {show: true}, lines: {show: true} } ];
		
		$.plot($("#flot_buddies_groups"  ), dataBG , grid_options );
		$.plot($("#flot_messages_per_day"), dataM  , grid_options );
	});
</script>

	

	
    <section id="AdminPage" data-role="page" data-add-back-btn="true">
			
		<script src="http://www.pureexample.com/js/flot/excanvas.min.js"></script>
		<script src="http://www.pureexample.com/js/flot/jquery.flot.min.js"></script>

		<header data-role="header" class="adminHeader">
			<label class="header-label">Chatty Admin</label>
			<img   class="header-image" src="<%=request.getContextPath()%>/image/Admin.png" alt="Admin"/>
		</header>

        <div class="adnimContent" data-role="content">
			
			<div>

				<h1>Buddies:  <jsp:getProperty name="statistics" property="buddyStatisticsSize" /></h1>
				<h1>Groups:   <jsp:getProperty name="statistics" property="groupStatisticsSize" /></h1>
				<div id="flot_buddies_groups" style="width: 600px !important;height:150px !important; text-align: center; margin:0 auto;"> </div>
				<br><br>
				<h1>Messages: <jsp:getProperty name="statistics" property="messageStatisticsSize" /></h1>
				<div id="flot_messages_per_day" style="width: 600px !important;height:150px !important; text-align: center; margin:0 auto;"> </div>
			</div>
			
        </div>		

	</section>
        