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


function selectBuddyList(userid){
	database.transaction(function(transaction) {
		transaction.executeSql("select * from bu");
		log.info("database has been refreshed: "+sql);

	}, nullDataHandler, showError);
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

