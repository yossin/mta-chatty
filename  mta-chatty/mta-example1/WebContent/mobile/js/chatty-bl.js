function initChatty(onSuccess, onError){
	bl=new BL();

	if (initializeDB(onError)){
		onSuccess();
	}
}

function recreateDB(onSuccess, onError){
	tables.recreate(function(){
		createTestData(onSuccess, onError);
	}, onError);	
}




function BL(onSuccessInit, onError){
	var dal = new DAL();
	this.loggedInUser=undefined;
	
	this.checkUserLoggedIn=function(onLoggedIn, onNoLoggedIn){
		var userId = dal.getLoggedInUserId();
		if (typeof(userId) == 'undefined' || userId==null){
			onNoLoggedIn();
		} else {
			dal.selectBuddy(userId, 
				function(buddy){
					bl.loggedInUser=buddy;
					if (typeof(buddy)=='undefined') {
						onNoLoggedIn();
					} else {
						onLoggedIn(userId);
					}
				}, 
				function(error){
					dal.removeLoggedInUserId();
					onNoLoggedIn(error);
				});
			
		}
	};

	// ----------- USER -------------
	
	//TODO: verify
	// email, name, picture, password, address, cityId, postalCode? **where? must be undefined** 
	this.registerNewUser=function(email, name, picture, password, onSuccess, onError){
		dal.insertUser(email, name, picture, password, onSuccess, onError);
	};

	//TODO: implement - do we need all parameters??
	this.updateUserProfile=function(todo_add_params, onSuccess, onError){

	};


	this.loginUser=function(email, password, onSuccess, onError){
		// TODO: create dummy tables - replace with real data from server
		
		// TODO: validate user with server
		dummyLoginUser(email, password, function(user){
			dal.setLoggedInUserId(user.email);
			bl.loggedInUser=user;
			onSuccess();
		}, onError);
		// save in local DB
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
		dal.selectBuddiesByNameOrID(bl.loggedInUser.email, searchText, onSuccess, onError);		
	};

	this.addBuddyToList=function(buddyId, onSuccess, onError){
		dal.addBuddy(bl.loggedInUser.email, buddyId, onSuccess, onError);		
	};

	//----------- GROUPS -------------

	this.getGroupList=function(onSuccess, onError){
		//id, name, picture
		dal.selectGroupList(bl.loggedInUser.email, onSuccess, onError);
	};

	this.searchGroupsByName=function(searchText, onSuccess, onError){
		dal.selectGroupsByName(bl.loggedInUser.email, searchText, onSuccess, onError);		
	};

	this.createGroup=function(name, picture, description, onSuccess, onError){
		// create group
		dal.insertGroup(name, picture, description, function(groupId){
			dal.insertGroupMembership(bl.loggedInUser.email, groupId, onSuccess, onError);
		}, onError);		
	};

	//TODO: tested on create group flow. should be tested separately 
	this.joinGroup=function(groupId, onSuccess, onError){
		dal.insertGroupMembership(bl.loggedInUser.email, groupId, onSuccess, onError);
	};

	//TODO: untested! - verify works correctly - concurrent requests!
	this.leaveGroup=function(groupId, onSuccess, onError){
		dal.deleteGroupMembership(bl.loggedInUser.email, groupId, function(){
			dal.deleteGroupIfEmpry(groupId, onSuccess, onError);
		}, onError);

	};

	this.getGroupDetails=function(groupId, onSuccess, onError){
		dal.selectGroup(groupId, onSuccess, onError);
	};


	//----------- MESSAGES -------------

	this.getGroupMessages=function (groupId, onSuccess, onError){
		// sender, ts, message
		dal.selectGroupMessages(groupId, onSuccess, onError);
	};

	this.getBuddyMessages=function (buddyId, onSuccess, onError){
		// sender, ts, message
		dal.selectBuddyMessages(bl.loggedInUser.email, buddyId, onSuccess, onError);
	};

	this.sendBuddyMessage=function (buddyId, message, onSuccess, onError){
		// sender, ts, message
		dal.insertBuddyMessage(bl.loggedInUser.email, buddyId, message, onSuccess, onError);
	};

	this.sendGroupMessage=function (groupId, message, onSuccess, onError){
		// sender, ts, message
		dal.insertGroupMessage(bl.loggedInUser.email, groupId, message, onSuccess, onError);
	};
}









