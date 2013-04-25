var loggedInUser={email:"bart@mail.com"};

//TODO: implement
function registerNewUser(user){
	// validate and register user with server
}

function loginUser(user, password, onSuccess, onError){
	// validate user with server
	// save in local DB
}

function logoutUser(){
	// delete from DB?
}

function getBuddyList(onSuccess, onError){
	//email, name, picture
	selectBuddyList(loggedInUser.email, onSuccess, onError);
}

function getGroupList(){
	//id, name, picture
}

function getGroupMessages(groupId){
	// sender, ts, message
}

function getBuddyMessages(buddyId){
	// sender, ts, message
}

