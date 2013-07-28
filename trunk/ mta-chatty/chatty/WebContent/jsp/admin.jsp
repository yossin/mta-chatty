<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>

<script type="text/javascript">

	function getDataSetBudies(){
		var dataSetBudies   = []; 
		var start = 1354586000000;
		for (var i = 0; i < 10; i += 0.5){
			dataSetBudies.push([start, Math.tan(i)]); 
	        start+= 100000;
			}
		return dataSetBudies;
	}
	
	function getDataSetGroups(){
		var dataSetGroups   = [];
		var start = 1354586000000;
		for (var i = 0; i < 10; i += 0.5){
			dataSetGroups.push([start, Math.sin(i)]); 
			start+= 100000;
			}
		return dataSetGroups;
	}
	
	function getDataSetMessages(){
		var dataSetMessages = []; 
		
		var start = 1354586000000;
		for (var i = 0; i < 10; i += 0.5){
			dataSetMessages.push([start, Math.cos(i)]); 
	        start+= 100000;
			}
		return dataSetMessages;
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
			<img   class="header-image" src="image/Admin.png" alt="Admin"/>
		</header>

        <div class="adnimContent" data-role="content">
			
			<div>

				<jsp:useBean id="admin_bean" scope="page" class="edu.mta.chatty.domain.admin.CountStatistics" />
				<h1>Buddies:  <jsp:getProperty name="admin_bean" property="numBuddies" /></h1>
				<h1>Groups:   <jsp:getProperty name="admin_bean" property="numGroups" /></h1>   
				<div id="flot_buddies_groups" style="width: 600px !important;height:150px !important; text-align: center; margin:0 auto;"> </div>
				<br><br>
				<h1>Messages: <jsp:getProperty name="admin_bean" property="numMessages" /></h1>
				<div id="flot_messages_per_day" style="width: 600px !important;height:150px !important; text-align: center; margin:0 auto;"> </div>
			</div>
			
        </div>		

	</section>
        