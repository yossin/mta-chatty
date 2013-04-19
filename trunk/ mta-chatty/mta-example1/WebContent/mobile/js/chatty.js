
$(document).ready(function(){
	$("a[data-role=tab]").each(function () {
	    var anchor = $(this);
	    anchor.bind("click", function () {
	        $.mobile.changePage(anchor.attr("href"), {
	            transition: "none",
	            changeHash: false
	        });
	        return false;
	    });
	});

	
	$("a[href=#ChatRoom]").each(function () {
	    var anchor = $(this);
		anchor.bind("click", function () {
		      var id = $(this).attr("id");
		      console.log("Going to change Chat room with id: " + id);
		      changePage("#ChatRoom", { "id": id });
	        return false;
	    });
	});

	$("div[data-role=page]").bind("pagebeforeshow", function (e, data) {
	    $.mobile.silentScroll(0);
	    $.mobile.changePage.defaults.transition = 'slide';
	});

	
	// bind callback that will be triggered after a pageshow event
	$("#ChatRoom").bind("callback", function(e, args) {
		activateRoom(args.id);
	});
 
    $(function() {
        $("#ChatRoom .sendMessage").click(function(){
            message = new Object();
            textarea = $("#ChatRoom .textArea");
            message.text = textarea.val();
            textarea.val('');
            message.time = (new Date()).toLocaleString();
            addMessagesToChatRoom(message);
            // Send message to server...
        });
	});
	
	/* TBD
	// Bind the register form.
	$( "form#login" ).submit(
		function( event ){
			// Prevent the default submit.
			event.preventDefault();

			var  = form.find( "input.name" ).val()
			form.find( "input.email" ).val(),

			
			// Save the student.
			saveStudent(
				form.find( "input.name" ).val(),form.find( "input.email" ).val(),
				function(){
					// Reset the form and focus the input.
					form.find( "input.name" )
						.val( "" )
						form.find( "input.email" ).val("")
						.focus()
					;

					// Refresh the student list.
					getstudents( refreshstudents );
				});
		});
	*/

});


/* should be taken from server */
function getImgLinkFromID(Id)
{
	switch(Id)
	{
	case "bart":    return "image/Bart.jpg"    ; break;
	case "homer":   return "image/Homer.jpg"   ; break;
	case "maggie":  return "image/Maggie.jpg"  ; break;
	case "lisa":    return "image/Lisa.jpg"    ; break;
	case "marge":   return "image/Marge.jpg"   ; break;
	case "simsons": return "image/Simsons.jpg" ; break;
	}
	return "image/PhotoUnavailable.jpg";
}

/* should be taken from server */
function getNameFromID(Id)
{
	return Id;
}

/* should be taken from server */
function getMessages(Id)
{
	var numMesseges = Math.floor((Math.random()*10)+1);
	var arrMesseges = [];
	for (var i=0; i<numMesseges; i++)
	{
		arrMesseges[i] = new Object();
		arrMesseges[i].text = "Message " + (i+1);
        var from = new Date(2010, 0, 1).getTime();
        var to   = new Date(2013, 0, 1).getTime();
		var randDate = new Date(from + Math.random() * (to - from));
		arrMesseges[i].time = randDate.toLocaleString();
	}
	return arrMesseges;
}

function addMessagesToChatRoom(message)
{
	if (message.text == "")
		return;
    var e = $("<li class='message'><label class='messages-text'>" + 
            message.text + 
            "</label><label class='messages-time'>" + 
            message.time + 
            "</label></li>");
    $("#ChatRoom .messages").append(e).listview('refresh');
}

function setRoomMessages(Id)
{
	messages = getMessages(Id);

    $("#ChatRoom .message").remove();
    
	for (var i = 0; i<messages.length; i++)
	{
        addMessagesToChatRoom(messages[i]);
	}
}

function activateRoom(roomId)
{
	var buddyName = getNameFromID(roomId);
	$("#ChatRoom .room-label").text(buddyName);
	document.title = buddyName;
	var buddyImg = getImgLinkFromID(roomId);	
	console.log("buddyName: "+ buddyName + ", buddyImg: "+ buddyImg);
	$("#ChatRoom .room-image").attr("src", buddyImg);
	setRoomMessages(roomId);
}

function changePage(page, args) 
{
		$.mobile.changePage(page, { changeHash: true });
        $(page).trigger("callback", args);
}

