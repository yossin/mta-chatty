
$(document).ready(function(){

	// Skip the login user incase there's a user that was previusly loaded
//	if (getLogedUser() != null)
//		document.location.href = "#Buddies";


	$("a[data-role=tab]").each(function () {
	    var anchor = $(this);
	    anchor.bind("click", function () {
	        $.mobile.changePage(anchor.attr("href"), {
	            transition: "none",
	            changeHash: false
	        });
	        return false;
	    });
	});

	
	$("a[href=#ChatRoom]").each(function () {
	    var anchor = $(this);
		anchor.bind("click", function () {
		      var id = $(this).attr("id");
		      console.log("Going to change Chat room with id: " + id);
		      changePage("#ChatRoom", { "id": id });
	        return false;
	    });
	});

	$("div[data-role=page]").bind("pagebeforeshow", function (e, data) {
	    $.mobile.silentScroll(0);
	    $.mobile.changePage.defaults.transition = 'slide';
	});

	
	// bind callback that will be triggered after a pageshow event
	$("#ChatRoom").bind("callback", function(e, args) {
		activateRoom(args.id);
	});
 
    $(function() {
        $("#ChatRoom .sendMessage").click(function(){
            var message = new Object();
            textarea = $("#ChatRoom .textArea");
            message.text = textarea.val();
            textarea.val('');
            message.time = (new Date()).toLocaleString();
            addMessagesToChatRoom(message);
            // Send message to server...
        });
	});
	
	
	// Bind the login form.
	$("#LoginForm").submit(function( event ){
		// Prevent the default submit.
		event.preventDefault();
		
		var userLoginData = new Object();
		userLoginData.mail = $("#LoginForm .loginUserMailInput").val();
		userLoginData.pass = $("#LoginForm .loginUserPassInput").val();
//		if (loginUser(userLoginData)){
            document.location.href = "#Buddies";
            window.location.reload();
//        }
//		else
//			window.alert('Login failed, Please recheck your input');
	});
    
	// Bind the register form.
	$("#RegisterForm").submit(function( event ){
		// Prevent the default submit.
		event.preventDefault();
		
		var userRegisterData = new Object();
		userRegisterData.name = $("#RegisterForm .registerUserNameInput").val();
		userRegisterData.mail = $("#RegisterForm .registerUserMailInput").val();
		userRegisterData.pass = $("#RegisterForm .registerUserPassInput").val();
		userRegisterData.pic  = $("#RegisterForm .registerUserPicInput" ).val();
		//if (!registerNewUser(userRegisterData)){
            document.location.href = "#Buddies";
            window.location.reload();
//		}
		//else if (loginUser(userLoginData))
//			window.alert('Register failed, Please recheck your input');
	});

    $('.editProfileBtn').click(function(){

        // get user profile deatails
        //        userEdi	tProfileData = getUserProfile();
        
            var userEditProfileData  = new Object();
            userEditProfileData.name = "user2";
            userEditProfileData.mail = "user2@user.com";
            userEditProfileData.pass = "user2";
            userEditProfileData.pic  = "user2.tif";
            
            $("#EditProfileForm .editProfileUserNameInput").val(userEditProfileData.name);
            $("#EditProfileForm .editProfileUserMailInput").val(userEditProfileData.mail);
            $("#EditProfileForm .editProfileUserPassInput").val(userEditProfileData.pass);
            $("#EditProfileForm .editProfileUserPicInput ").val(userEditProfileData.pic );
    });

    
});


/* should be taken from server */
function getImgLinkFromID(Id)
{
	switch(Id)
	{
	case "bart":    return "image/Bart.jpg"    ; break;
	case "homer":   return "image/Homer.jpg"   ; break;
	case "maggie":  return "image/Maggie.jpg"  ; break;
	case "lisa":    return "image/Lisa.jpg"    ; break;
	case "marge":   return "image/Marge.jpg"   ; break;
	case "simsons": return "image/Simsons.jpg" ; break;
	}
	return "image/PhotoUnavailable.jpg";
}

/* should be taken from server */
function getNameFromID(Id)
{
	return Id;
}

/* should be taken from server */
function getMessages(Id)
{
	var numMesseges = Math.floor((Math.random()*10)+1);
	var arrMesseges = [];
	for (var i=0; i<numMesseges; i++)
	{
		arrMesseges[i] = new Object();
		arrMesseges[i].text = "Message " + (i+1);
        var from = new Date(2010, 0, 1).getTime();
        var to   = new Date(2013, 0, 1).getTime();
		var randDate = new Date(from + Math.random() * (to - from));
		arrMesseges[i].time = randDate.toLocaleString();
	}
	return arrMesseges;
}

function addMessagesToChatRoom(message)
{
	if (message.text == "")
		return;
    var e = $("<li class='message'><label class='messages-text'>" + 
            message.message + 
            "</label><label class='messages-time'>" + 
            message.send_date + 
            "</label></li>");
    $("#ChatRoom .messages").append(e).listview('refresh');
}

function setRoomMessages(results)
{
    $("#ChatRoom .message").remove();
    
    iterateResults(results,addMessagesToChatRoom);
}

function activateRoom(roomId)
{
	var buddyName = getNameFromID(roomId);
	$("#ChatRoom .room-label").text(buddyName);
	document.title = buddyName;
	var buddyImg = getImgLinkFromID(roomId);	
	console.log("buddyName: "+ buddyName + ", buddyImg: "+ buddyImg);
	$("#ChatRoom .room-image").attr("src", buddyImg);
	getBuddyMessages(roomId, setRoomMessages, printError);
}

function changePage(page, args) 
{
		$.mobile.changePage(page, { changeHash: true });
        $(page).trigger("callback", args);
}

