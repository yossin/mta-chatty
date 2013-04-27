var chatroom="buddyChatRoom";

$(document).ready(function(){

	// Skip the login user in case there's a user that was previusly loaded
//	if (getLogedUser() != null)
//		document.location.href = "#Buddies";


	$("a[data-role=tab]").each(function () {
	    var anchor = $(this);
	    anchor.bind("click", function () {
	        $.mobile.changePage(anchor.attr("href"), {
	            transition: "none",
	            changeHash: false
	        });
			var id = $(this).attr("id");
		    updateTab({ "id": id });
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

	
	// bind callback that will be triggered after a page show event
	$("#ChatRoom").bind("callback", function(e, args) {
		if (args.id.indexOf("@") != -1)
			activateBuddyRoom(args.id);
		else
			activateGroupRoom(args.id);
			
	});
 
    $(function() {
        $("#ChatRoom .sendMessage").click(function(){
            var message = new Object();
            var textarea = $("#ChatRoom .textArea");
            message.message = textarea.val();
            textarea.val('');
            message.send_date = (new Date()).toLocaleString();
            //TODO: ui.saveTextMessage()
            if (chatroom=="buddyChatRoom")
            	addBuddyMessageToChatRoom(message);
            else
            	addGroupMessageToChatRoom(message);
            
            // Send message to server...
        });
	});
	
	
	// Bind the login form.
//	$("#LoginForm").submit(function( event ){
	$("#LoginForm .btnLoginToChatty").click(function(){
		// Prevent the default submit.
//		event.preventDefault();
		
		var userLoginData = new Object();
		userLoginData.mail = $("#LoginForm .loginUserMailInput").val();
		userLoginData.pass = $("#LoginForm .loginUserPassInput").val();
		

		
//		if (loginUser(userLoginData)){
			return true;
//            document.location.href = "#Buddies";
//		    updateTab({ "id": "BuddiesTab" });
//		    updateTab({ "id": "GroupsTab" });
            
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
        updateTab({ "id": "BuddiesTab" });
		//if (!registerNewUser(userRegisterData)){
        	return true;
//            document.location.href = "#Buddies";
//		}
		//else if (loginUser(userLoginData))
//			window.alert('Register failed, Please recheck your input');
	});

    $('.editProfileBtn').click(function(){

        // get user profile details
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


function addBuddyMessageToChatRoom(message)
{
	if (message.message == "")
		return;
    var e = $("<li class='message'><label class='messages-text'>" + 
            message.message + 
            "</label><label class='messages-time'>" + 
            message.send_date + 
            "</label></li>");
    $("#ChatRoom .messages").append(e).listview('refresh');
}

function addGroupMessageToChatRoom(message)
{
	if (message.message == "")
		return;
	// TBD - change to group view (add picture/name of sender)
    var e = $("<li class='message'><label class='messages-text'>" + 
            message.message + 
            "</label><label class='messages-time'>" + 
            message.send_date + 
            "</label></li>");
    $("#ChatRoom .messages").append(e).listview('refresh');
}

function addBuddyToBuddiesList(buddy)
{
	if (buddy.email == "")
		return;
    var e = $("<li class='buddy'><a href='ChatRoom' id=" + 
            buddy.email + 
			"><label class='row-label'>" +
            buddy.name +
			"</label><img class='row-image' src='" +
			buddy.picture +
			"'/></a></li>");
				
    $("#Buddies .buddyList").append(e).listview('refresh');;
}

function addGroupToGroupsList(group)
{
	if (group.email == "")
		return;
    var e = $("<li class='group'><a href='ChatRoom' id=" + 
            group.group_id + 
			"><label class='row-label'>" +
            group.name +
			"</label><img class='row-image' src=" +
			group.picture +
			"/></a></li>");
				
    $("#Buddies .groupList").append(e).listview('refresh');;
}


function setBuddyRoomMessages(results)
{
    $("#ChatRoom .message").remove();
    iterateResults(results,addBuddyMessageToChatRoom);
}

function setGroupRoomMessages(results)
{
    $("#ChatRoom .message").remove();
    iterateResults(results, addGroupMessageToChatRoom);
}

function setUserBuddies(results)
{
    $("#Buddies .buddy").remove();
    iterateResults(results, addBuddyToBuddiesList);
}

function setUserGroups(results)
{
    $("#Buddies .group").remove();
    iterateResults(results, addGroupToGroupsList);
}


function setRoomHeader(result)
{
	if (buddy.email == "")
		return;
	console.log("RoomHeader of " + buddy.email + " = Name: "+ result.name + ", buddyImg: "+ result.picture);
	$("#ChatRoom .room-label").text(result.name);
	document.title = "Chat with " +result.name;
	$("#ChatRoom .room-image").attr("src", result.picture);
}

function activateBuddyRoom(buddyId)
{
	chatRoom = "buddyChatRoom"; 
	bl.getBuddyDetails(buddyId, setRoomHeader, printError);
	bl.getBuddyMessages(buddyId, setBuddyRoomMessages, printError);
}

function activateGroupRoom(groupId)
{
	chatRoom = "groupChatRoom"; 
	bl.getGroupDetails(groupId, setRoomHeader, printError);
	bl.getGroupMessages(groupId, setGroupRoomMessages, printError);
}

function updateTab(args)
{
	if (args.id == "BuddiesTab")
		bl.getBuddyList(setUserBuddies, printError);
	else
		bl.getGroupList(setUserGroups, printError);
}


function changePage(page, args) 
{
	$.mobile.changePage(page, { changeHash: true });
	$(page).trigger("callback", args);
}

