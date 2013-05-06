<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>


		<header data-role="header">
			<label class="header-label">Please Register to Chatty</label>
			<img   class="header-image" src="image/Register.png" alt="Register"/>
		</header>
		
		<% if ( request.getParameter("machine").equals("mobile")) { %>
		<form id="RegisterForm" class="content" data-role="content" action="#Buddies">
		<% } else { %>
		<form id="RegisterForm" class="content" data-role="content" action="#Dashboard">
		<% } %>
			<div>
				<span class="colorRed">*</span>
				<label class="registerUserMailLable">Email:</label>
				<input class="registerUserMailInput" type="email" required />    
			</div>
			<div>
				<span class="colorRed">*</span>
				<label class="registerUserNameLable">Name:</label>
				<input class="registerUserNameInput" type="text" required />    
			</div>
			<div>
				<span class="colorRed">*</span>
				<label class="registerUserPassLable">Password:</label>
				<input class="registerUserPassInput" type="password" required/>    
			</div>
			<div>
				<label class="registerUserPicLable">Profile Image:</label>
				<input class="registerUserPicInput" id="file" type="file"/>
			</div>

			<div>
				<span class="colorRed"><br>*Required</span>
			</div>
				
			<div class="RegistrerDiv">
				<% if ( request.getParameter("machine").equals("mobile")) { %>
				<a href="#Buddies" class="btnRegisterSubmit" data-inline="true" data-role="button">Register</a>
				<% } else { %>
				<a href="#Dashboard" class="btnRegisterSubmit" data-inline="true" data-role="button">Register</a>
				<% } %>
			<%--	<button type="submit" class="btnRegisterSubmit" data-inline="true">Register</button> --%>
			</div>
		</form>				
