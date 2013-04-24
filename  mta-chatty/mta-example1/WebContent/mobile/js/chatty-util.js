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