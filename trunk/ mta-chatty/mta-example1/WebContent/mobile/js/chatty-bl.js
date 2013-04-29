
var bl={};
bl.loggedInUser={email:"bart@mail.com"};


//TODO: implement
bl.registerNewUser = function (user){
	// validate and register user with server
};


bl.checkUserLoggedIn=function(onLoggedIn, onNoLoggedIn){
	var userId = dal.getLoggedInUserId();
	if (typeof(userId) == 'undefined'){
		onNoLoggedIn();
	} else {
		dal.selectBuddy(userId, 
			function(buddy){
				bl.loggedInUser=buddy;
				onLoggedIn(userId);
			}, 
			function(error){
				dal.removeLoggedInUserId();
				onNoLoggedIn(error);
			});
		
	}
};

bl.loginUser=function(email, password, onSuccess, onError){
	// TODO: validate user with server
	dummyLoginUser(email, password, onSuccess, onError);
	// save in local DB
};


bl.logoutUser=function(onSuccess){
	// TODO: delete data from DB?
	dal.removeLoggedInUserId();
	onSuccess();
};

bl.getBuddyList=function(onSuccess, onError){
	//email, name, picture
	dal.selectBuddyList(this.loggedInUser.email, onSuccess, onError);
};

bl.getGroupList=function(onSuccess, onError){
	//id, name, picture
	dal.selectGroupList(this.loggedInUser.email, onSuccess, onError);
};

bl.getGroupMessages=function (groupId, onSuccess, onError){
	// sender, ts, message
	dal.selectGroupMessages(this.loggedInUser.email, groupId, onSuccess, onError);
};

bl.getBuddyMessages=function (buddyId, onSuccess, onError){
	// sender, ts, message
	dal.selectBuddyMessages(this.loggedInUser.email, buddyId, onSuccess, onError);
};

bl.sendBuddyMessage=function (buddyId, message, onSuccess, onError){
	// sender, ts, message
	dal.insertBuddyMessage(this.loggedInUser.email, buddyId, message, onSuccess, onError);
};

bl.sendGroupMessage=function (groupId, message, onSuccess, onError){
	// sender, ts, message
	dal.insertBuddyMessage(this.loggedInUser.email, groupId, message, onSuccess, onError);
};

bl.getBuddyDetails=function(buddyId, onSuccess, onError){
	//email, name, picture
	dal.selectBuddy(buddyId, onSuccess, onError);
};

//TODO: implement
bl.registerUser=function(todo_add_params, onSuccess, onError){
	
};

bl.updateUserProfile=function(todo_add_params, onSuccess, onError){
	
};

bl.getBuddiesByNameOrID=function(searchText, onSuccess, onError){

};

bl.getGroupsByName=function(searchText, onSuccess, onError){

};

bl.creaetGroup=function(groupName, onSuccess, onError){

};

bl.joinGroup=function(groupId, onSuccess, onError){

};

bl.leaveGroup=function(groupId, onSuccess, onError){

};

bl.getGroupDetails=function(groupId, onSuccess, onError){
	//TODO:dal.selectGroup(buddyId, onSuccess, onError);
};

bl.addBuddyToList=function(buddyId, onSuccess, onError){

};

bl.addGroupToList=function(groupId, onSuccess, onError){

};
