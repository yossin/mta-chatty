function initChatty(onSuccess, onError){
	bl=new BL();

	if (initializeDB(onError)){
		onSuccess();
	}
}

function recreateTestDB(onSuccess, onError){
	createTestData(onSuccess, onError);
}

function recreateDB(onSuccess, onError){
	tables.recreate(function(){
		ajaxPost("sync-complete-data" /*"complete-data"*/, null  /*bl.loggedInUser*/, function(userData){
			fillUserData(userData, onSuccess, onError);
		}, onError);
	});
}

function refreshUserData(onSuccess, onError){
	ajaxPost("sync-user-data", null , function(userData){
			fillUserData(userData, onSuccess, onError);
	}, onError);
}

function BL(onSuccessInit, onError){
	var dal = new DAL();
	this.loggedInUser=undefined;
	

	this.loginUser=function(email, password, onSuccess, onError){
		var user = {"email":email, "password":password};
		ajaxPost("login", user, function(data){
			dal.setLoggedInUserId(user.email);
			bl.loggedInUser=user;
			onSuccess();
		}, onError);
	};
	
	this.checkUserLoggedIn=function(onLoggedIn, onNoLoggedIn){
		onNoLoggedIn();	
	};

	// ----------- USER -------------
	
	//TODO: verify
	// email, name, picture, password, address, cityId, postalCode? **where? must be undefined** 
	this.registerNewUser=function(email, name, picture, password, onSuccess, onError){
		var att = {"email":email, "name":name, "picture":picture, "password":password};	
		ajaxPost("register-user", att, function(data){
			loginUser(email, password, onSuccess, onError);
		}, onError);
		
		//dal.insertUser(email, name, picture, password, onSuccess, onError);
	};

	//TODO: implement - do we need all parameters??
	this.updateUserProfile=function(todo_add_params, onSuccess, onError){

	};

	this.logoutUser=function(onSuccess){
		// TODO: delete data from DB?
		dal.removeLoggedInUserId();
		onSuccess();
	};


	//----------- BUDDIES -------------

	this.getBuddyList=function(onSuccess, onError){
		//email, name, picture
		dal.selectBuddyList(bl.loggedInUser.email, onSuccess, onError);
	};

	this.getBuddyDetails=function(buddyId, onSuccess, onError){
		//email, name, picture
		dal.selectBuddy(buddyId, onSuccess, onError);
	};

	this.searchBuddiesByNameOrID=function(searchText, onSuccess, onError){
		var att = {"email":bl.loggedInUser.email, "text":searchText};	
		ajaxPost("find-users", att, onSuccess, onError);
	};

	this.addBuddyToList=function(buddyId, onSuccess, onError){
		var att = {"owner_email":bl.loggedInUser.email, "buddy_id":buddyId};	
		ajaxPost("add-buddy", att, function(){
		}, onError);
		refreshUserData(onSuccess, onError);
		
//		dal.addBuddy(bl.loggedInUser.email, buddyId, onSuccess, onError);
	};

	//----------- GROUPS -------------

	this.getGroupList=function(onSuccess, onError){
		//id, name, picture
		dal.selectGroupList(bl.loggedInUser.email, onSuccess, onError);
	};

	this.searchGroupsByName=function(searchText, onSuccess, onError){
		var att = {"email":bl.loggedInUser.email, "text":searchText};	
		ajaxPost("find-groups", att, onSuccess, onError);
	};

	this.createGroup=function(name, picture, description, onSuccess, onError){
		// create group
		dal.insertGroup(name, picture, description, function(groupId){
			dal.insertGroupMembership(bl.loggedInUser.email, groupId, onSuccess, onError);
		}, onError);		
	};

	this.joinGroup=function(groupId, onSuccess, onError){
		var att = {"member_email":bl.loggedInUser.email, "group_id":groupId};	
		ajaxPost("join-group", att, function(){
		}, onError);
		refreshUserData(onSuccess, onError);
		
//		dal.insertGroupMembership(bl.loggedInUser.email, groupId, onSuccess, onError);
	};

	this.leaveGroup=function(groupId, onSuccess, onError){
		var att = {"member_email":bl.loggedInUser.email, "group_id":groupId};	
		ajaxPost("leave-group", att, function(){}, onError);
		dal.deleteGroupMembership(bl.loggedInUser.email, groupId, function(){
			dal.deleteGroupIfEmpry(groupId, function(){}, onError);
			onSuccess();
		}, onError);
	};

	this.getGroupDetails=function(groupId, onSuccess, onError){
		dal.selectGroup(groupId, onSuccess, onError);
	};


	//----------- MESSAGES -------------

	this.getGroupMessages=function (groupId, onSuccess, onError){
		dal.selectGroupMessages(groupId, onSuccess, onError);
	};

	this.getBuddyMessages=function (buddyId, onSuccess, onError){
		dal.selectBuddyMessages(bl.loggedInUser.email, buddyId, onSuccess, onError);
	};

	this.sendBuddyMessage=function (buddyId, message, onSuccess, onError){
		var att = {"sender_id":bl.loggedInUser.email, "receiver_id":buddyId, "message":message};	
		ajaxPost("buddy-message", att, function(){}, onError);
		refreshUserData(onSuccess, onError);
		
//		dal.insertBuddyMessage(bl.loggedInUser.email, buddyId, message, onSuccess, onError);
	};

	this.sendGroupMessage=function (groupId, message, onSuccess, onError){
		var att = {"sender_id":bl.loggedInUser.email, "receiver_id":groupId, "message":message};	
		ajaxPost("group-message", att, function(){}, onError);
		refreshUserData(onSuccess, onError);
		
//		dal.insertGroupMessage(bl.loggedInUser.email, groupId, message, onSuccess, onError);
	};
}









