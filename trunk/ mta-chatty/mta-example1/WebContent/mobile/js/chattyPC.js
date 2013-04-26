
$(document).ready(function(){

	// Bind the login form.
	$("#LoginFormPC").submit(function( event ){
		// Prevent the default submit.
		event.preventDefault();
		
		var userLoginData = new Object();
		userLoginData.mail = $("#LoginForm .loginUserMailInput").val();
		userLoginData.pass = $("#LoginForm .loginUserPassInput").val();
//		if (loginUser(userLoginData)){
            document.location.href = "#Dashboard";
//		    updateTab({ "id": "BuddiesTab" });
            window.location.reload();
//        }
//		else
//			window.alert('Login failed, Please recheck your input');
	});
    
	// Bind the register form.
	$("#RegisterFormPC").submit(function( event ){
		// Prevent the default submit.
		event.preventDefault();
		
		var userRegisterData = new Object();
		userRegisterData.name = $("#RegisterForm .registerUserNameInput").val();
		userRegisterData.mail = $("#RegisterForm .registerUserMailInput").val();
		userRegisterData.pass = $("#RegisterForm .registerUserPassInput").val();
		userRegisterData.pic  = $("#RegisterForm .registerUserPicInput" ).val();
		//if (!registerNewUser(userRegisterData)){
            document.location.href = "#Dashboard";
//            updateTab({ "id": "BuddiesTab" });
			window.location.reload();
//		}
		//else if (loginUser(userLoginData))
//			window.alert('Register failed, Please recheck your input');
	});

    
});
