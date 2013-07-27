<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<title>Chatty</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/chatty-desktop.css">
	<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
	<script	src="<%=request.getContextPath()%>/js/imported/log4javascript.js"></script>
	<script	src="<%=request.getContextPath()%>/js/imported/SimpleAjaxUploader.js"></script>
	<script	src="<%=request.getContextPath()%>/js/chatty-util.js"></script>
	<script	src="<%=request.getContextPath()%>/js/chatty-fillData.js"></script>
	<script	src="<%=request.getContextPath()%>/js/chatty-test.js"></script>
	<script	src="<%=request.getContextPath()%>/js/chatty-dal.js"></script>
	<script	src="<%=request.getContextPath()%>/js/chatty-bl.js"></script>
	<script	src="<%=request.getContextPath()%>/js/chatty-desktop.js"></script>
	
	<script	src="http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.js"></script>
</head>

<body>
	
	<% String device = "desktop"; %>
	
	
	<section id="Loading" data-role="dialog">
		<jsp:include page="loading.jsp" />
	</section>

	<section id="Login" data-role="dialog">
		<jsp:include page="login.jsp" >
		    <jsp:param name="device" value="<%=device%>" />
		</jsp:include>
	</section>

	<section id="Register" data-role="dialog" data-add-back-btn="true">
		<jsp:include page="register.jsp" >
		    <jsp:param name="device" value="<%=device%>" />
		</jsp:include>
	</section>
	
	<section id="EditProfile" data-role="dialog" data-add-back-btn="true">
		<jsp:include page="edit_profile.jsp" >
		    <jsp:param name="device" value="<%=device%>" />
		</jsp:include>
	</section>
    
    <jsp:include page="admin.jsp" />
    
    <section id="SearchBuddy" data-role="dialog" data-add-back-btn="true">
		<jsp:include page="search_buddy.jsp" >
		    <jsp:param name="device" value="<%=device%>" />
		</jsp:include>
	</section>

    <section id="SearchGroup" data-role="dialog" data-add-back-btn="true">
		<jsp:include page="search_group.jsp" >
		    <jsp:param name="device" value="<%=device%>" />
		</jsp:include>
	</section>	

    <section id="LeaveGroup" data-role="dialog" data-add-back-btn="true">
    	<jsp:include page="leave_group.jsp" >
		    <jsp:param name="device" value="<%=device%>" />
		</jsp:include>
	</section>	
    
    <section id="CreateGroup" data-role="dialog" data-add-back-btn="true">
    	<jsp:include page="create_group.jsp" >
		    <jsp:param name="device" value="<%=device%>" />
		</jsp:include>
	</section>	
	
   	<jsp:include page="desktop_main.jsp" />
	

</body>
</html>