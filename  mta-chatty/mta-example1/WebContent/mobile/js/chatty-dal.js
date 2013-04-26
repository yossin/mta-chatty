// TODO: make alerts nicer

var dal={};
function initializeDB(){
	try {
		if (!window.openDatabase) {
			alert('not supported');
		} else {
			// setting for our database
			var shortName = 'chattydb';
			var version = '1.0';
			var displayName = 'chatty db';
			var maxSize = 65536; // in bytes
			var db = openDatabase(shortName, version, displayName, maxSize);

			Object.defineProperty(dal, 'db', {'value':db,'writable':false});
			
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
			dal.db.transaction(function(transaction) {
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

initializeDB();
createTables(true);


dal.genericSqlStatement=function (sql, params, onSuccess, onError){
	this.db.transaction(function(transaction) {
			transaction.executeSql(sql,params,
			function (tx,result){
				onSuccess(result);
			},
			function (tx,error){
				log.error("unable to perform sql statement"+sql+" \nparams: "+params+ "\n. error message is: "+error.message);
				onError(error);
			});

	}, showError, nullDataHandler);	
};





dal.selectBuddyList=function (userId, onSuccess, onError){
	var sql="select u.email, u.name, u.picture from user as u inner join buddy_list as bl on u.email==bl.buddy_id where bl.owner_email=?";
	this.genericSqlStatement(sql, [userId], onSuccess, onError);
};

dal.selectBuddy=function (buddyId, onSuccess, onError){
	var sql="select email, name, picture from user where email=?";
	this.genericSqlStatement(sql, [buddyId], function(results){
		getFirstResult(results, onSuccess);
	}, onError);
};


dal.selectGroupList=function (userId, onSuccess, onError){
	//id, name, picture, description
	var sql="select g.group_id, g.name, g.picture, g.description from 'group' as g inner join group_membership as gm on gm.group_id==g.group_id where gm.member_email==?";
	this.genericSqlStatement(sql, [userId], onSuccess, onError);
};

dal.selectGroupMessages=function (groupId, onSuccess, onError){
	// sender-id, sender-name, sender-pic, send-ts, message
	var sql="select u.email, u.name, u.picture, gm.send_date, gm.message from user as u inner join group_message as gm on gm.sender_id==u.email where gm.receiver_id==?";
	this.genericSqlStatement(sql, [groupId], onSuccess, onError);
};

dal.selectBuddyMessages=function (userId, buddyId, onSuccess, onError){
	// sender-id, sender-name, sender-pic, send-ts, message
	var sql="select bm.send_date, bm.sender_id, bm.receiver_id, bm.message from buddy_message as bm where ((bm.sender_id==? and bm.receiver_id==?) or (bm.sender_id==? and bm.receiver_id==?)) order by bm.send_date";
	this.genericSqlStatement(sql, [userId, buddyId, buddyId, userId], onSuccess, onError);
};

dal.insertBuddyMessage=function (sender_id, receiver_id, message, onSuccess, onError){
	var sql="insert into buddy_message (sender_id, receiver_id, message) values(?,?,?)";
	this.genericSqlStatement(sql, [sender_id, receiver_id, message], onSuccess, onError);
};

dal.insertGroupMessage=function (sender_id, receiver_id, message, onSuccess, onError){
	var sql="insert into group_message (sender_id, receiver_id, message) values(?,?,?)";
	this.genericSqlStatement(sql, [sender_id, receiver_id, message], onSuccess, onError);
};
