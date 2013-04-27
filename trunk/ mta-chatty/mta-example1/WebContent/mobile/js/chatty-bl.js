
var bl={};
bl.loggedInUser={email:"bart@mail.com"};


//TODO: implement
bl.registerNewUser = function (user){
	// validate and register user with server
};

bl.loginUser=function(user, password, onSuccess, onError){
	// validate user with server
	// save in local DB
};

bl.logoutUser=function(){
	// delete from DB?
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

