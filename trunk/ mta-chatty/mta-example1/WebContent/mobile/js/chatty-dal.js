// TODO: make alerts nicer

// generic functions
function initializeDB(errorHandler){
	if (typeof(db)!='undefined') return;
	try {
		if (!window.openDatabase) {
			onError({message:'DB not supported'});
		} else {
			// setting for our database
			var shortName = 'chattydb';
			var version = '1.0';
			var displayName = 'chatty db';
			var maxSize = 65536; // in bytes
			db = openDatabase(shortName, version, displayName, maxSize);

			//Object.defineProperty(this, 'db', {'value':db,'writable':false});
		}
	} catch (e) {
		// Error handling code goes here.
		if (e == 2) {
			// Version number mismatch.
			errorHandler({message:'Invalid database version'});
		} else {
			errorHandler({message:'Unknown error '+e+'.'});
		}
		return false;
	}
	return true;
}


function Tables(){
	function dbExecute(list, onSuccess, onError){
		ajaxCalls(list, function(statmentList){
			var statements = statmentList[0].split(";").concat(statmentList[1].split(";"));
			db.transaction(function(transaction) {
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
				

			}, onError, onSuccess());
		});
	}
	var dropSqls='db/drop.1.sql';
	var createSqls='db/create.1.sql';
	
	this.drop=function(onSuccess, onError){
		dbExecute([dropSqls], onSuccess, onError);
	};
	this.create=function(onSuccess, onError){
		dbExecute([createSqls], onSuccess, onError);
	};
	this.recreate=function(onSuccess, onError){
		dbExecute([dropSqls,createSqls], onSuccess, onError);
	};
}

var tables=new Tables();


function genericSqlStatement(sql, params, onSuccess, onError){
	db.transaction(function(transaction) {
			transaction.executeSql(sql,params,
			function (tx,result){
				onSuccess(result);
			},
			function (tx,error){
				log.error("unable to perform sql statement"+sql+" \nparams: "+params+ "\n. error message is: "+error.message);
				onError(error);
			});

	}, onError, nullDataHandler);	
}

//TODO: verify
function genericSqlInsertStatement(sql, params, onSuccess, onError){
	db.transaction(function(transaction) {
		transaction.executeSql(sql,params,
			function (tx,result){
			if (typeof(result)!='undefined'){
				onSuccess(result.insertId);
			} else {
				onError({message:"unable to get insert id"});
			}
		},
		function (tx,error){
			log.error("unable to perform sql statement"+sql+" \nparams: "+params+ "\n. error message is: "+error.message);
			onError(error);
		});
	}, onError, nullDataHandler);	
}
function genericSqlInsertStatement1(sql, params, onSuccess, onError){
	db.transaction(function(transaction) {
			transaction.executeSql(sql,params,
				function (tx,result){
					transaction.executeSql("last_insert_rowid()",[],
						function (tx,result){
							getFirstResult(result);
						},
						function (tx,error){
							log.error("unable to get last inserted index using: last_insert_rowid().\n error message is: "+error.message);
							onError(error);
						});
			},
			function (tx,error){
				log.error("unable to perform sql statement"+sql+" \nparams: "+params+ "\n. error message is: "+error.message);
				onError(error);
			});

	}, onError, nullDataHandler);	
}


function DAL(){
	
	this.getLoggedInUserId=function(){
		return localStorage.getItem('loggedInUserId');
	};

	this.setLoggedInUserId=function(userId){
		return localStorage.setItem('loggedInUserId', userId);
	};

	this.removeLoggedInUserId=function(){
		localStorage.removeItem('loggedInUserId');
	};

	this.insertUser=function(email, name, picture, password, onSuccess, onError){
		var sql="insert into 'user' (email, name, picture, password) values(?,?,?,?)";
		genericSqlStatement(sql, [email, name, picture, password], onSuccess, onError);
	};


	//----------- BUDDIES -------------
	
	this.selectBuddyList=function (userId, onSuccess, onError){
		var sql="select u.email, u.name, u.picture from 'user' as u inner join buddy_list as bl on u.email==bl.buddy_id where bl.owner_email==?";
		genericSqlStatement(sql, [userId], onSuccess, onError);
	};

	this.selectBuddy=function (buddyId, onSuccess, onError){
		var sql="select email, name, picture from 'user' where email==?";
		genericSqlStatement(sql, [buddyId], function(results){
			getFirstResult(results, onSuccess);
		}, onError);
	};
	
	this.addBuddy=function (ownerEmail, buddyId, onSuccess, onError){
		var sql="insert into buddy_list (owner_email, buddy_id) values(?,?)";
		genericSqlStatement(sql, [ownerEmail, buddyId], function(results){
			getFirstResult(results, onSuccess);
		}, onError);
	};
	
	//TODO: verify
	this.selectBuddiesByNameOrID=function (ownerEmail, searchText, onSuccess, onError){
		var sql="select u.email, u.name, u.picture from 'user' as u left join buddy_list as bl on u.email==bl.buddy_id where (bl.buddy_id is null) and (u.name like ? or u.email like ?) and u.email!=?";
		var serachWithLike='%'+searchText+'%';
		genericSqlStatement(sql, [serachWithLike, serachWithLike, ownerEmail], onSuccess, onError);
	};


	//----------- GROUPS -------------

	this.selectGroupList=function (userId, onSuccess, onError){
		//id, name, picture, description
		var sql="select g.group_id, g.name, g.picture, g.description from 'group' as g inner join group_membership as gm on gm.group_id==g.group_id where gm.member_email==?";
		genericSqlStatement(sql, [userId], onSuccess, onError);
	};
	
	this.selectGroup=function (groupId, onSuccess, onError){
		var sql="select name, picture, description from 'group' where group_id==?";
		genericSqlStatement(sql, [groupId], function(results){
			getFirstResult(results, onSuccess);
		}, onError);
	};

	this.selectGroupsByName=function (ownerEmail, searchText, onSuccess, onError){
		var sql="select g.group_id, g.name, g.picture, g.description from 'group' as g  where g.name like ? and g.group_id not in (select gm.group_id from group_membership as gm where gm.member_email==?)";
		var serachWithLike='%'+searchText+'%';
		genericSqlStatement(sql, [serachWithLike, ownerEmail], onSuccess, onError);
	};

	this.insertGroup=function (name, picture, description, onSuccess, onError){
		var sql="insert into 'group' (name, picture, description) values (?,?,?)";
		genericSqlInsertStatement(sql, [name, picture, description], onSuccess, onError);
	};
	
	this.insertGroupMembership=function (memberMail, groupId, onSuccess, onError){
		var sql="insert into 'group_membership' (member_email, group_id) values (?,?)";
		genericSqlStatement(sql, [memberMail, groupId], onSuccess, onError);
	};

	//----------- MESSAGES -------------

	this.selectGroupMessages=function (groupId, onSuccess, onError){
		// sender-id, sender-name, sender-pic, send-ts, message
		var sql="select u.email, u.name, u.picture, gm.send_date, gm.message from 'user' as u inner join group_message as gm on gm.sender_id==u.email where gm.receiver_id==?";
		genericSqlStatement(sql, [groupId], onSuccess, onError);
	};

	this.selectBuddyMessages=function (userId, buddyId, onSuccess, onError){
		// send_date, sender_id, receiver_id, message, sender_name, sender_picture
		var sql="select bm.send_date, bm.sender_id, bm.receiver_id, bm.message, u.name as sender_name, u.picture as sender_picture from buddy_message as bm join 'user' as u on u.email==bm.sender_id where ((bm.sender_id==? and bm.receiver_id==?) or (bm.sender_id==? and bm.receiver_id==?)) order by bm.send_date";
		genericSqlStatement(sql, [userId, buddyId, buddyId, userId], onSuccess, onError);
	};

	this.insertBuddyMessage=function (sender_id, receiver_id, message, onSuccess, onError){
		var sql="insert into buddy_message (sender_id, receiver_id, message) values(?,?,?)";
		genericSqlStatement(sql, [sender_id, receiver_id, message], onSuccess, onError);
	};

	this.insertGroupMessage=function (sender_id, receiver_id, message, onSuccess, onError){
		var sql="insert into group_message (sender_id, receiver_id, message) values(?,?,?)";
		genericSqlStatement(sql, [sender_id, receiver_id, message], onSuccess, onError);
	};


}









