

function createTestData(onSuccess, onError){
	ajaxCall('test/test-insert.1.json', function(data){
		var testData = JSON.parse(data);
		createCountriesTestData(testData.countries, function(){
			createCitiesTestData(testData.cities, function(){
				createUsersTestData(testData.users, function(){
					createAddressesTestData(testData.addresses, function(){
						createBuddyListTestData(testData.buddy_list, function(){
							createGroupsTestData(testData.groups, function(){
								createBuddyMessagesTestData(testData.buddy_messages, function(){
									createGroupMembershpisTestData(testData.group_memberships, function(){
										createGroupMessagesTestData(testData.group_messages, onSuccess, onError);
									}, onError);
								}, onError);
							}, onError);
						}, onError);
					}, onError);
				}, onError);
			}, onError);
		}, onError);	
	});
}


function genericCreateTestData(sql, itemName, list, createParams, onSuccess, onError){
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

function createCountriesTestData(list, onSuccess, onError){
	genericCreateTestData("insert into country (country) values (?)", 
			"country", list, 
			function(country){
				return [country.country];
			}, onSuccess, onError
	);
}

function createCitiesTestData(list, onSuccess, onError){
	genericCreateTestData("insert into city (city, country_id) values (?,?)", 
			"city", list, 
			function(city){
				return [city.city, city.country_id];
			}, onSuccess, onError
	);
}


function createAddressesTestData(list, onSuccess, onError){
	genericCreateTestData("insert into address (address_id, address, city_id) values (?,?,?)", 
			"address", list, 
			function(address){
				return [address.address_id, address.address, address.city_id];
			}, onSuccess, onError
	);
}


function createUsersTestData(list, onSuccess, onError){
	genericCreateTestData("insert into 'user' (email, name, picture, password) values (?,?,?,?)", 
			"user", list, 
			function(user){
				return [user.email, user.name, user.picture, user.password];
			}, onSuccess, onError
	);
}


function createBuddyListTestData(list, onSuccess, onError){
	genericCreateTestData("insert into buddy_list (buddy_id, owner_email) values (?,?)", 
			"buddy_list", list, 
			function(buddy_list){
				return [buddy_list.buddy_id, buddy_list.owner_email];
			}, onSuccess, onError
	);
}

//TODO: change group id int -> text(creatoremail)+timestamp(creation). add those fields on creation
function createGroupsTestData(list, onSuccess, onError){
	genericCreateTestData("insert into 'group' (name, picture, description) values (?,?,?)", 
			"group", list, 
			function(group){
				return [group.name, group.picture, group.description];
			}, onSuccess, onError
	);
}

function createBuddyMessagesTestData(list, onSuccess, onError){
	genericCreateTestData("insert into buddy_message (sender_id, receiver_id, message) values (?,?,?)", 
			"buddy_message", list, 
			function(message){
				return [message.sender_id, message.receiver_id, message.message];
			}, onSuccess, onError
	);
}

function createGroupMembershpisTestData(list, onSuccess, onError){
	genericCreateTestData("insert into group_membership (member_email, group_id) values (?,?)", 
			"group_membership", list, 
			function(membership){
				return [membership.member_email, membership.group_id];
			}, onSuccess, onError
	);
}

function createGroupMessagesTestData(list, onSuccess, onError){
	genericCreateTestData("insert into group_message (sender_id, receiver_id, message) values (?,?,?)", 
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

function dummyLoginUserTest(){
	dummyLoginUser('bart@mail.com', 123, dummyOnSuccess, dummyOnError);
}
