


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

	
	//TODO: implement
	this.registerNewUser=function(todo_add_params, onSuccess, onError){
	};

	//TODO: implement
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
		dal.selectBuddyList(this.loggedInUser.email, onSuccess, onError);
	};

	this.getBuddyDetails=function(buddyId, onSuccess, onError){
		//email, name, picture
		dal.selectBuddy(buddyId, onSuccess, onError);
	};

	//TODO: implement
	this.getBuddiesByNameOrID=function(searchText, onSuccess, onError){

	};

	//TODO: implement
	this.addBuddyToList=function(buddyId, onSuccess, onError){

	};

	//----------- GROUPS -------------

	this.getGroupList=function(onSuccess, onError){
		//id, name, picture
		dal.selectGroupList(this.loggedInUser.email, onSuccess, onError);
	};

	//TODO: implement
	this.getGroupsByName=function(searchText, onSuccess, onError){

	};

	//TODO: implement
	this.createGroup=function(groupName, onSuccess, onError){

	};

	//TODO: implement
	this.joinGroup=function(groupId, onSuccess, onError){

	};

	//TODO: implement
	this.leaveGroup=function(groupId, onSuccess, onError){

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
		dal.selectBuddyMessages(this.loggedInUser.email, buddyId, onSuccess, onError);
	};

	this.sendBuddyMessage=function (buddyId, message, onSuccess, onError){
		// sender, ts, message
		dal.insertBuddyMessage(this.loggedInUser.email, buddyId, message, onSuccess, onError);
	};

	this.sendGroupMessage=function (groupId, message, onSuccess, onError){
		// sender, ts, message
		dal.insertGroupMessage(this.loggedInUser.email, groupId, message, onSuccess, onError);
	};
}
// ----------- USER -------------








