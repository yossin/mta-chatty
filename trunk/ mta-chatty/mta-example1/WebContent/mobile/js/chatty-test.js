

function createTestData(){
	ajaxCall('test/test-insert.1.json', function(data){
		var testData = JSON.parse(data);
		createCountriesTestData(testData.countries);	
		createCitiesTestData(testData.cities);
		createAddressesTestData(testData.addresses);
		createUsersTestData(testData.users);
		createBuddyListTestData(testData.buddy_list);
		createGroupsTestData(testData.groups);
		createBuddyMessagesTestData(testData.buddy_messages);
		createGroupMembershpisTestData(testData.group_memberships);
		createGroupMessagesTestData(testData.group_messages);
	});
}


function genericCreateTestData(insertSql, itemName, list, createParams){
	database.transaction(function(transaction) {
		var inserted=0;
		list.forEach( function(item) {
			var params = createParams(item);
			transaction.executeSql(insertSql,params,
			function (tx,callback){
				inserted++;
			},
			function (tx,error){
				log.error("unable to insert "+itemName+": "+params+ ". error message is: "+error.message);
			});
			inserted++;
		});
		log.debug("finished inserting "+inserted+" "+itemName+"(s)");

	}, showError, nullDataHandler);
	
}

function createCountriesTestData(list){
	genericCreateTestData("insert into country (country) values (?)", 
			"country", list, 
			function(country){
				return [country.country];
			}
	);
}

function createCitiesTestData(list){
	genericCreateTestData("insert into city (city, country_id) values (?,?)", 
			"city", list, 
			function(city){
				return [city.city, city.country_id];
			}
	);
}


function createAddressesTestData(list){
	genericCreateTestData("insert into address (address, city_id) values (?,?)", 
			"address", list, 
			function(address){
				return [address.address, address.city_id];
			}
	);
}


function createUsersTestData(list){
	genericCreateTestData("insert into 'user' (email, name, picture, address_id, password) values (?,?,?,?,?)", 
			"user", list, 
			function(user){
				return [user.email, user.name, user.picture, user.address_id, user.password];
			}
	);
}


function createBuddyListTestData(list){
	genericCreateTestData("insert into buddy_list (buddy_id, owner_email) values (?,?)", 
			"buddy_list", list, 
			function(buddy_list){
				return [buddy_list.buddy_id, buddy_list.owner_email];
			}
	);
}

function createGroupsTestData(list){
	genericCreateTestData("insert into 'group' (name, picture, description) values (?,?,?)", 
			"group", list, 
			function(group){
				return [group.name, group.picture, group.description];
			}
	);
}

function createBuddyMessagesTestData(list){
	genericCreateTestData("insert into buddy_message (sender_id, receiver_id, message) values (?,?,?)", 
			"buddy_message", list, 
			function(message){
				return [message.sender_id, message.receiver_id, message.message];
			}
	);
}

function createGroupMembershpisTestData(list){
	genericCreateTestData("insert into group_membership (member_email, group_id) values (?,?)", 
			"group_membership", list, 
			function(membership){
				return [membership.member_email, membership.group_id];
			}
	);
}

function createGroupMessagesTestData(list){
	genericCreateTestData("insert into group_message (sender_id, receiver_id, message) values (?,?,?)", 
			"group_messages", list, 
			function(message){
				return [message.sender_id, message.receiver_id, message.message];
			}
	);
}


// dummy functions
function dummyLoginUser(email, password, onSuccessLogin, onLoginError){
	var sql="select u.email, u.name, u.picture from 'user' as u where u.email==?";
	genericSelectStatement(sql, [email], 
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
