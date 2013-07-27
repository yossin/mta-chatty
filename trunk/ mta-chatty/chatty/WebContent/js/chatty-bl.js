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
	
	this.registerNewUser=function(email, name, picture, password, onSuccess, onError){
		var att = {"email":email, "name":name, "picture":picture, "password":password};	
		ajaxPost("register-user", att, function(data){
//			loginUser(email, password, onSuccess, onError);
		}, onError);
		
		if (picture != "")
		{
			var uploader = new ss.SimpleUpload({
				button: '#RegisterForm .btnRegisterSubmit', // HTML element used as upload button 
				url:  "/chatty/services/upload_file", // URL of server-side upload handler
				name: picture // Parameter name of the uploaded file
			});
		}		
		onSuccess();	
		//dal.insertUser(email, name, picture, password, onSuccess, onError);
	};

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
		var att = {"member_email":bl.loggedInUser.email, "name":name, "description":description, "picture":picture};
		ajaxPost("create-group", att, function(){
		}, onError);
		
		if (picture != "")
		{
			var uploader = new ss.SimpleUpload({
				button: '#CreateGroup .btnCreateGroup', // HTML element used as upload button 
				url:  "/chatty/services/upload_file", // URL of server-side upload handler
				name: picture // Parameter name of the uploaded file
			});
		}		

		refreshUserData(onSuccess, onError);
					
//		dal.insertGroup(name, picture, description, function(groupId){
//			dal.insertGroupMembership(bl.loggedInUser.email, groupId, onSuccess, onError);
//		}, onError);		
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
	
	//----------- ADMIN -------------

	this.getAdminDataSets=function (setAdminPageChartsValues, onError){
		var dataSetBudies   = []; 
		var dataSetGroups   = []; 
		var dataSetMessages = []; 
		
		var start = 1354586000000;
		for (var i = 0; i < 10; i += 0.5){
			dataSetBudies.push([start, Math.tan(i)]); 
			dataSetGroups.push([start, Math.sin(i)]); 
			dataSetMessages.push([start, Math.cos(i)]); 
	        start+= 100000;
			}
		
		setAdminPageChartsValues(dataSetBudies, dataSetGroups, dataSetMessages);
	};
}









