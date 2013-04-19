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
	//log.error(error.message + " error.code:" + error.code);
	log.error(error);
	return true;
}

function nullDataHandler(transaction, results) {
	log.debug("nullDataHandler=" + this);
}


function createTables(){
	// Create table
	var client = new XMLHttpRequest();
	client.open('GET', 'db/create.1.sql');
	client.onreadystatechange = function() {
		database.transaction(function(transaction) {
			debugger;
			transaction.executeSql(client.responseText);
			log.info("database has been refreshed");

		}, nullDataHandler, showError);
	};
	client.send();
}

createTables();


//TODO: implement
registerNewUser(){
	
}


