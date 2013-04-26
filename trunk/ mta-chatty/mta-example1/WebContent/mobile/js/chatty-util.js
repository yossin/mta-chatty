var log = log4javascript.getDefaultLogger();

// initialize logger
function initLogger(){
	var appender = new log4javascript.BrowserConsoleAppender();
	log.addAppender(appender);
}

initLogger();


function ajaxCall(url, action, method){
	method = typeof method !== 'undefined' ? method : "GET";
	var client = new XMLHttpRequest();
	client.open(method, url);
	client.onreadystatechange = function() {
		if (client.readyState==4 && client.status==200){
			action(client.responseText);
		}
	};
	client.send();

}

function printError(error){
	log.error(error.message);
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


function iterateResults(results,callback){
    if (!results){
		return;
	}
    for (var i=0; i<results.rows.length;i++){
    	callback(results.rows.item(i));
    }
}
