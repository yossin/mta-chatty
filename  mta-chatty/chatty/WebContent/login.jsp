<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>


		<header data-role="header">
			<label class="header-label">Please Login to Chatty</label>
			<img   class="header-image" src="image/Login.png" alt="Login"/>
		</header>

		<% if ( request.getParameter("machine").equals("mobile")) { %>
		<form id="LoginForm" class="content" data-role="content" action="#Buddies" method="post">
		<% } else { %>
		<form id="LoginForm" class="content" data-role="content" action="#Dashboard" method="post">
		<% } %>
		
			<div>
				<span class="colorRed">*</span>
				<label class="loginUserMailLable">Email:</label>
				<input class="loginUserMailInput" type="email" required  value="bart@mail.com"/>    
			</div>
			<div>
				<span class="colorRed">*</span>
				<label class="loginUserPassLable">Password:</label>
				<input class="loginUserPassInput" type="password" required value="123"/>    
			</div>
                
			<div>
				<span class="colorRed"><br><br>*Required</span>
			</div>
              
			<div class="loginOrRegistrerDiv">
				<a href="#Register" class="btnRegisterToChatty" data-inline="true" data-role="button">Register</a>
				<a href="" class="btnLoginToChatty" data-inline="true" data-role="button">Login</a>
		<%--	<button type="submit" class="btnLoginToChatty" data-inline="true">Login</button> --%>
			</div>
        </form>		
