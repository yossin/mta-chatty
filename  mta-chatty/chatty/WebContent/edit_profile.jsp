<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>


		<header data-role="header">
			<label class="header-label">Edit Your Profile</label>
			<img   class="header-image" src="image/Register.png" alt="Register" />
		</header>
		<form id="EditProfileForm" class="content" data-role="content">
			<div>
				<label class="editProfileUserMailLable">Email:</label>
				<input class="editProfileUserMailInput" type="email" required disabled/>    
			</div>
			<div>
				<span class="colorRed">*</span>
				<label class="editProfileUserNameLable">Name:</label>
				<input class="editProfileUserNameInput" type="text" required />    
			</div>
			<div>
				<span class="colorRed">*</span>
				<label class="editProfileUserPassLable">Password:</label>
				<input class="editProfileUserPassInput" type="password" required/>    
			</div>
			<div>
				<label class="editProfileUserPicLable">Profile Image:</label>
				<input class="editProfileUserPicInput" id="file" type="file"/>
			</div>

			<div>
				<span class="colorRed"><br>*Required</span>
			</div>
				
			<div class="registrerDiv">
   				<a href="" class="btnEditProfileSubmit" data-inline="true" data-role="button">Submit Changes</a>
			<%--	<button type="submit" class="btnEditProfileSubmit" data-inline="true">Submit Changes</button> --%>
			</div>
		</form>				
