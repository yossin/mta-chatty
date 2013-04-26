var loggedInUser={email:"bart@mail.com"};


var bl={};


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
	dal.selectBuddyList(loggedInUser.email, onSuccess, onError);
};

bl.getGroupList=function(onSuccess, onError){
	//id, name, picture
	dal.selectGroupList(loggedInUser.email, onSuccess, onError);
};

bl.getGroupMessages=function (groupId, onSuccess, onError){
	// sender, ts, message
	dal.selectGroupMessages(loggedInUser.email, groupId, onSuccess, onError);
};

bl.getBuddyMessages=function (buddyId, onSuccess, onError){
	// sender, ts, message
	dal.selectBuddyMessages(loggedInUser.email, buddyId, onSuccess, onError);
};

bl.sendBuddyMessage=function (buddyId, message, onSuccess, onError){
	// sender, ts, message
	dal.insertBuddyMessages(loggedInUser.email, buddyId, message, onSuccess, onError);
};

bl.sendGroupMessage=function (groupId, message, onSuccess, onError){
	// sender, ts, message
	dal.insertBuddyMessages(loggedInUser.email, groupId, message, onSuccess, onError);
};


