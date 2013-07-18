<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>


		<header data-role="header">
			<label class="header-label">Please Register to Chatty</label>
			<img   class="header-image" src="image/Register.png" alt="Register"/>
		</header>

		
		<% String action; if(request.getParameter("device").equals("mobile")) action="#Buddies"; else action="#Dashboard"; %>
		<form id="RegisterForm" class="content" data-role="content" action="<%=action%>" method="post">
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
				<a href="" class="btnRegisterSubmit" data-inline="true" data-role="button">Register</a>
			</div>
		</form>				
