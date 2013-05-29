

function fillUserData(userData, onSuccess, onError){
	createCountriesData(userData.countries, function(){
		createCitiesData(userData.cities, function(){
			createUsersData(userData.users, function(){
				createAddressesData(userData.addresses, function(){
					createBuddyListData(userData.buddy_list, function(){
						createGroupsData(userData.groups, function(){
							createBuddyMessagesData(userData.buddy_messages, function(){
								createGroupMembershpisData(userData.group_memberships, function(){
									createGroupMessagesData(userData.group_messages, onSuccess, onError);
								}, onError);
							}, onError);
						}, onError);
					}, onError);
				}, onError);
			}, onError);
		}, onError);
	}, onError);	
}


function genericCreateData(sql, itemName, list, createParams, onSuccess, onError){
	if (list!=null){
		db.transaction(function(transaction) {
			var inserted=0;
			list.forEach( function(item) {
				var params = createParams(item);
				transaction.executeSql(sql,params,
				function (tx,callback){
					inserted++;
				},
				function (tx,error){
					log.error("unable to insert "+itemName+": "+params+ ". error message is: "+error.message);
				});
				inserted++;
			});
			log.debug("finished inserting "+inserted+" "+itemName+"(s)");
	
		}, onError, onSuccess);
	}
	else 
		onSuccess();
}

function createCountriesData(list, onSuccess, onError){
	genericCreateData("insert into country (country_id,country) values (?,?)", 
			"country", list, 
			function(country){
				return [country.country_id, country.country];
			}, onSuccess, onError
	);
}

function createCitiesData(list, onSuccess, onError){
	genericCreateData("insert into city (city_id, city, country_id) values (?,?,?)", 
			"city", list, 
			function(city){
				return [city.city_id, city.city, city.country_id];
			}, onSuccess, onError
	);
}


function createAddressesData(list, onSuccess, onError){
	genericCreateData("insert into address (address_id, address, city_id) values (?,?,?)", 
			"address", list, 
			function(address){
				return [address.address_id, address.address, address.city_id];
			}, onSuccess, onError
	);
}


function createUsersData(list, onSuccess, onError){
	genericCreateData("insert into 'user' (email, name, picture, password) values (?,?,?,?)", 
			"user", list, 
			function(user){
				return [user.email, user.name, user.picture, user.password];
			}, onSuccess, onError
	);
}


function createBuddyListData(list, onSuccess, onError){
	genericCreateData("insert into buddy_list (buddy_id, owner_email) values (?,?)", 
			"buddy_list", list, 
			function(buddy_list){
				return [buddy_list.buddy_id, buddy_list.owner_email];
			}, onSuccess, onError
	);
}

//TODO: change group id int -> text(creatoremail)+timestamp(creation). add those fields on creation
function createGroupsData(list, onSuccess, onError){
	genericCreateData("insert into 'group' (name, picture, description) values (?,?,?)", 
			"group", list, 
			function(group){
				return [group.name, group.picture, group.description];
			}, onSuccess, onError
	);
}

function createBuddyMessagesData(list, onSuccess, onError){
	genericCreateData("insert into buddy_message (sender_id, receiver_id, message) values (?,?,?)", 
			"buddy_message", list, 
			function(message){
				return [message.sender_id, message.receiver_id, message.message];
			}, onSuccess, onError
	);
}

function createGroupMembershpisData(list, onSuccess, onError){
	genericCreateData("insert into group_membership (member_email, group_id) values (?,?)", 
			"group_membership", list, 
			function(membership){
				return [membership.member_email, membership.group_id];
			}, onSuccess, onError
	);
}

function createGroupMessagesData(list, onSuccess, onError){
	genericCreateData("insert into group_message (sender_id, receiver_id, message) values (?,?,?)", 
			"group_messages", list, 
			function(message){
				return [message.sender_id, message.receiver_id, message.message];
			}, onSuccess, onError
	);
}


// dummy functions
function dummyLoginUser(email, password, onSuccessLogin, onLoginError){
	var sql="select u.email, u.name, u.picture from 'user' as u where u.email==? and u.password==?";
	genericSqlStatement(sql, [email, password], 
			function(result){
				if (result.rows.length==1){
					onSuccessLogin(result.rows.item(0));
				} else {
					onLoginError({message:'invalid username or password'});
				}
			}, 
			onLoginError);
}

function dummyOnSuccess(res){
	log.debug(res);
}

function dummyOnError(err){
	log.error(err.message);
}

function dummyLoginUser(){
	dummyLoginUser('bart@mail.com', 123, dummyOnSuccess, dummyOnError);
}
