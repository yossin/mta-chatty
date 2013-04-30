var ui={};
ui.chatroom={buddyRoom:true, id:"name@mail.com"};



function firstPageNavigation(){
	bl.checkUserLoggedIn(
		function(){
			//log.debug('user should be redirected to buddies page');
			$.mobile.changePage("#Buddies");
		},
		function(){
			recreateDB(setLoginForm,displayError);
		});
}



function dummy(){}

function displayError(error){
	log.error(e);
	alert('error has occured:'+e);
}

$(document).ready(function(){
	initChatty(firstPageNavigation, displayError);

	$("#Buddies").bind("pagebeforeshow", function (e) {
	    updateTab({ "id": "BuddiesTab" });
	});

  	$("#Groups").bind("pagebeforeshow", function (e) {
	    updateTab({ "id": "GroupsTab" });
	});

	
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


	$("div[data-role=page]").bind("pagebeforeshow", function (e, data) {
	    $.mobile.silentScroll(0);
	    $.mobile.changePage.defaults.transition = 'slide';
	});

    $(function() {
        $("#ChatRoom .sendMessage").click(function(){
            var message = new Object();
            var textarea = $("#ChatRoom .textArea");
        	message.name = bl.loggedInUser.name;
        	message.message = textarea.val();
            textarea.val('');
            message.send_date = (new Date()).toLocaleString();
            // TODO: we might want to retrieve messages from server 2, how do we keep correct order?
            if (ui.chatroom.buddyRoom)
            {
            	addBuddyMessageToChatRoom(message);
            	bl.sendBuddyMessage(ui.chatroom.id, message.message, dummy, printError);
            }
            else
            {
            	addGroupMessageToChatRoom(message);
            	bl.sendGroupMessage(ui.chatroom.id, message.message, dummy, printError);
            }
            
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

		bl.loginUser(userLoginData.mail, userLoginData.pass, login_register_updateProfile_Success, loginFailed);
		return true;
	});
    
	// Bind the register form.
//	$("#RegisterForm").submit(function( event ){
	$("#RegisterForm .btnRegisterSubmit").click(function(){
		// Prevent the default submit.
		event.preventDefault();
		
		var regData = new Object();
		regData.name = $("#RegisterForm .registerUserNameInput").val();
		regData.email = $("#RegisterForm .registerUserMailInput").val();
		regData.pass = $("#RegisterForm .registerUserPassInput").val();
		regData.pic  = $("#RegisterForm .registerUserPicInput" ).val();

		//TODO: ?load info from user?
		//regData.address = "Generated Dummy Address From UI";
		//regData.cityId = 1; //1=Tel-Aviv
		//regData.postalCode=undefined; 
		bl.registerNewUser(regData.email, regData.name, regData.pic, regData.pass, 
				login_register_updateProfile_Success, registerFailed);
        return true;
	});

    // Bind the login form.
//	$("#EditProfileForm").submit(function( event ){
	$("#EditProfileForm .btnEditProfileSubmit").click(function(){
		// Prevent the default submit.
//		event.preventDefault();
		
        var userEditProfileData = new Object();
		userEditProfileData.name = $("#EditProfileForm .editProfileUserNameInput").val();
		userEditProfileData.pass = $("#EditProfileForm .editProfileUserPassInput").val();
		userEditProfileData.pic  = $("#EditProfileForm .editProfileUserPicInput" ).val();
        
		bl.updateUserProfile(userEditProfileData, login_register_updateProfile_Success, updateProfileFailed);
        return true;
	});

    $('.editProfileBtn').click(function(){
        bl.getBuddyDetails(buddyId, setEditProfileForm, printError);
    });

    $('.logoutBtn').click(function(event){
    	event.preventDefault();
        bl.logoutUser(setLoginForm, printError);
    });

    $('.searchBuddyBtn').click(function(){
   		$("#searchBuddyText").val("");
   	    $("#SearchBuddy .searchResultBuddy").remove();
    });

    $('.searchBuddiesBtn').click(function(){
   		searchBuddyText = $("#searchBuddyText").val();
        if (searchBuddyText != "")
        	bl.searchBuddiesByNameOrID(searchBuddyText, setSearchBuddyRes, printError);
    });

    $('.searchGroupBtn').click(function(){
   		$("#searchGroupText").val("");
   	    $("#SearchGroup .searchResultBuddy").remove();
    });

    $('.searchGroupsBtn').click(function(){
   		searchGroupText = $("#searchGroupText").val();
        if (searchGroupText != "")
        	bl.searchGroupsByName(searchGroupText, setSearchGroupRes, printError);
    });
    
    // Bind the login form.
//	$("#CreateGroupForm").submit(function( event ){
	$("#CreateGroupForm .btnCreateGroup").click(function(){

       	var name = $("#CreateGroupForm .createGroupNameInput").val();
        var pic  = $("#CreateGroupForm .createGroupPicInput" ).val();
        var desc = $("#CreateGroupForm .createGroupDescInput" ).val();
        bl.createGroup(name, pic, desc, displayGroupList, messageCantAddGroup);
    });
	
	$("#LeaveGroup").bind("pagebeforeshow", function (e) {
		bl.getGroupList(setUserGroupsToLeave, printError);
	});
	
   
});

function displayGroupList(){
    bl.getGroupList(setUserGroups, printError);
}

function addBuddyMessageToChatRoom(message){
	if (message.message == "")
		return;
    var e = $("<li class='message'><label class='messages-text'>" + 
            message.name + ": \t" + message.message + 
            "</label><label class='messages-time'>" + 
            message.send_date + 
            "</label></li>");
    $("#ChatRoom .messages").append(e).listview('refresh');
}

function addGroupMessageToChatRoom(message){
	if (message.message == "")
		return;
    var e = $("<li class='message'><label class='messages-text'>" + 
            message.name + ": \t" + message.message + 
            "</label><label class='messages-time'>" + 
            message.send_date + 
            "</label></li>");
    $("#ChatRoom .messages").append(e).listview('refresh');
}

function addBuddyToBuddiesList(buddy){
	if (buddy.email == "")
		return;
    var e = $("<li class='buddy'><a href='#ChatRoom' id=" + 
            buddy.email + 
			"><label class='row-label'>" +
            buddy.name +
			"</label><img class='row-image' src='" +
			buddy.picture +
			"'/></a></li>");
				
    $("#Buddies .buddyList").append(e).listview('refresh');
}

function addGroupToGroupsList(group){
    var e = $("<li class='group'><a href='#ChatRoom' id=" + 
            group.group_id + 
			"><label class='row-label'>" +
            group.name +
			"</label><img class='row-image' src='" +
			group.picture +
			"'/></a></li>");
    $("#Groups .groupList").append(e).listview('refresh');
}

function addGroupToLeaveGroupsList(group){
    var e = $("<li class='group'><a href='#Groups' class='hrefLeaveGroup' id=" + 
            group.group_id + 
			"><label class='row-label'>" +
            group.name +
			"</label><img class='row-image' src='" +
			group.picture +
			"'/></a></li>");
    $("#LeaveGroup .groupList").append(e).listview('refresh');
}

function setBuddyRoomMessages(results){
    iterateResults(results,addBuddyMessageToChatRoom);
}

function setGroupRoomMessages(results){
    iterateResults(results, addGroupMessageToChatRoom);
}

function reBindChatRoomClick(){
	$("a[href=#ChatRoom]").each(function () {
	    var anchor = $(this);
		anchor.bind("click", function () {
			clearChatRoom(); // in case of failure - we don't want to see previews room
			ui.chatroom.id = $(this).attr("id");
			ui.chatroom.buddyRoom = (ui.chatroom.id.indexOf("@") != -1);
			console.log("Going to change Chat room with id: " + ui.chatroom.id);
			if (ui.chatroom.buddyRoom)
				activateBuddyRoom(ui.chatroom.id);
			else
				activateGroupRoom(ui.chatroom.id);
	        return true;
	    });
	});
}

function setUserBuddies(results){
    $("#Buddies .buddy").remove();
    iterateResults(results, addBuddyToBuddiesList);
    reBindChatRoomClick();
}

function setUserGroups(results){
    $("#Groups .group").remove();
    iterateResults(results, addGroupToGroupsList);    
    reBindChatRoomClick();
}

function reBindLeaveGroupClick(){
	$("#LeaveGroup .hrefLeaveGroup").click(function(){
		var id = $(this).attr("id");
        console.log("Leaving group: " + id);
        bl.leaveGroup(id, dummy, printError);
        return true;
    });
}

function setUserGroupsToLeave(results){
    $("#LeaveGroup .group").remove();
    iterateResults(results, addGroupToLeaveGroupsList);    
    reBindLeaveGroupClick();
}


function setRoomHeader(result){
	if (result.email == "")
		return;
	console.log("RoomHeader of " + result.email + " = Name: "+ result.name + ", buddyImg: "+ result.picture);
	$("#ChatRoom .header-label").text(result.name);
	document.title = "Chat with " +result.name;
	$("#ChatRoom .chatroom-image").attr("src", result.picture);
}

function setGroupRoomHeader(result){
	console.log("RoomHeader of " + result.group_id + " = Name: "+ result.name + ", groupImg: "+ result.picture);
	$("#ChatRoom .header-label").text(result.name);
	document.title = "Chat with " +result.name;
	$("#ChatRoom .chatroom-image").attr("src", result.picture);
}


function clearChatRoom(){
	$("#ChatRoom .header-label").text("");
	document.title = "";
	$("#ChatRoom .chatroom-image").attr("src", "");
    $("#ChatRoom .message").remove();
}

function activateBuddyRoom(buddyId){
	bl.getBuddyDetails(buddyId, setRoomHeader, printError);
	bl.getBuddyMessages(buddyId, setBuddyRoomMessages, printError);
}

function activateGroupRoom(groupId){
	bl.getGroupDetails(groupId, setGroupRoomHeader, printError);
	bl.getGroupMessages(groupId, setGroupRoomMessages, printError);
}

function updateTab(args){
	if (args.id == "BuddiesTab")
		bl.getBuddyList(setUserBuddies, printError);
	else
		bl.getGroupList(setUserGroups, printError);
}

function setEditProfileForm(result){
	if (result.email == "")
		return;
    $("#EditProfileForm .editProfileUserNameInput").val(result.name);
    $("#EditProfileForm .editProfileUserMailInput").val(result.email);
    $("#EditProfileForm .editProfileUserPassInput").val(result.pass);
    $("#EditProfileForm .editProfileUserPicInput ").val(result.picture);
}
function setLoginForm(){
	log.debug("navigate back to login page");
	$.mobile.changePage("#Login");
}

function addBuddyToSearchResultList(buddy){
	if (buddy.email == "")
		return;

    var e = $("<li class='searchResultBuddy'><a href='#Buddies' class='searchResultBuddy_href' id=" + 
            buddy.email + 
			"><label class='searchResultBuddyLabel'>" +
            buddy.name + ', ' + buddy.email +
			"</label><img class='searchResultBuddyImg' src='" +
			buddy.picture +
			"'/></a></li>");
				
    $("#SearchBuddy .searchResultBuddies").append(e).listview('refresh');
}

function reBindBuddySearchResultClick(){
	$(".searchResultBuddy_href").each(function () {
	    var anchor = $(this);
		anchor.bind("click", function () {
			id = $(this).attr("id");
            bl.addBuddyToList(id, dummy, printError);
			return true;
	    });
	});
}

function setSearchBuddyRes(results){
    $("#SearchBuddy .searchResultBuddy").remove();
    iterateResults(results, addBuddyToSearchResultList);
    reBindBuddySearchResultClick();
}

function addGroupToSearchResultList(group){
    var e = $("<li class='searchResultGroup'><a href='#Buddies' class='searchResultGroup_href' id=" + 
            group.group_id + 
			"><label class='searchResultGroupLabel'>" +
            group.name +
			"</label><img class='searchResultGroupImg' src='" +
			group.picture +
			"'/></a></li>");
				
    $("#SearchGroup .searchResultGroups").append(e).listview('refresh');
}

function reBindGroupSearchResultClick(){
	$(".searchResultGroup_href").each(function () {
	    var anchor = $(this);
		anchor.bind("click", function () {
			id = $(this).attr("id");
            bl.joinGroup(id, dummy, printError);
			return true;
	    });
	});
}

function setSearchGroupRes(results){
    $("#SearchGroup .searchResultGroup").remove();
    iterateResults(results, addGroupToSearchResultList);
    reBindGroupSearchResultClick();
}

function messageCantAddGroup(){
	window.alert("Chatty can not add your new group");
}

function login_register_updateProfile_Success(){
	$.mobile.changePage("#Buddies");
}

function loginFailed(){
	window.alert('Login failed, Please recheck your input');
}

function registerFailed(){
	window.alert('Register failed, Please recheck your input');
}

function updateProfileFailed(){
	window.alert('Edit profile failed, Please recheck your input');
}

function skipLogIn(){
    updateTab({ "id": "BuddiesTab" });
	$.mobile.changePage("#Buddies");
}
