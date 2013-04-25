// TODO: make alerts nicer
try {
	if (!window.openDatabase) {
		alert('not supported');
	} else {
		// setting for our database
		var shortName = 'chattydb';
		var version = '1.0';
		var displayName = 'chatty db';
		var maxSize = 65536; // in bytes
		var database = openDatabase(shortName, version, displayName, maxSize);

		// You should have a database instance in db.
	}
} catch (e) {
	// Error handling code goes here.
	if (e == 2) {
		// Version number mismatch.
		alert("Invalid database version.");
	} else {
		alert("Unknown error " + e + ".");
	}
	throw e;
}

function showError(transaction, error) {
	if (error){
		log.error(error);
		//log.error(error.message + " error.code:" + error.code);
	}
	return true;
}

function nullDataHandler(transaction, results) {
	log.debug("nullDataHandler=" + this);
}


function createTables(insertTestData){
	// Create table
	var successCallback=nullDataHandler;
	if (typeof insertTestData != 'undefined' && insertTestData){
		successCallback=createTestData;
	}
	var statements=[];
	ajaxCall('db/drop.1.sql', function(data){
		statements = statements.concat(data.split(";"));
		
		ajaxCall('db/create.1.sql', function(data){
			statements = statements.concat(data.split(";"));
			database.transaction(function(transaction) {
				statements.forEach( function(sql) { 
					if (sql.trim()){
						transaction.executeSql(sql,[],
							function (tx,callback){
								log.info("database has been refreshed: "+sql);
							},
							function (tx,error){
								log.error("unable to refresh database: "+sql+ ". error message is: "+error.message);
							}
						);
					}
				});
				

			}, showError, successCallback);
			
		});
		
	});

}

createTables(true);


function genericSelectStatement(sql, params, onSuccess, onError){
	database.transaction(function(transaction) {
			transaction.executeSql(sql,params,
			function (tx,result){
				onSuccess(result);
			},
			function (tx,error){
				log.error("unable to perform select statement"+sql+" \nparams: "+params+ "\n. error message is: "+error.message);
				onError(error);
			});

	}, showError, nullDataHandler);	
}



function selectBuddyList(userId, onSuccess, onError){
	var sql="select u.email, u.name, u.picture from user as u inner join buddy_list as bl on u.email==bl.buddy_id where bl.owner_email=?";
	genericSelectStatement(sql, [userId], onSuccess, onError);
}

function selectGroupList(userId, onSuccess, onError){
	//id, name, picture, description
	var sql="select g.group_id, g.name, g.picture, g.description from 'group' as g inner join group_membership as gm on gm.group_id==g.group_id where gm.member_email==?";
	genericSelectStatement(sql, [userId], onSuccess, onError);
}

function selectGroupMessages(groupId, onSuccess, onError){
	// sender-id, sender-name, sender-pic, send-ts, message
	var sql="select u.email, u.name, u.picture, gm.send_date, gm.message from user as u inner join group_message as gm on gm.sender_id==u.email where gm.receiver_id==?";
	genericSelectStatement(sql, [groupId], onSuccess, onError);
}

function selectBuddyMessages(buddyId, onSuccess, onError){
	// sender-id, sender-name, sender-pic, send-ts, message
	var sql="select u.email, u.name, u.picture, bm.send_date, bm.message from user as u inner join buddy_message as bm on bm.sender_id==u.email where bm.receiver_id==?";
	genericSelectStatement(sql, [buddyId], onSuccess, onError);
}

