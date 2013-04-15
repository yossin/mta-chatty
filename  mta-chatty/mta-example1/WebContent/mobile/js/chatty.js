
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
	
});


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

function getNameFromID(Id)
{
	return Id;
}

function activateRoom(roomId)
{
	var buddyName = getNameFromID(roomId);
	$("#ChatRoom .room-label").text(buddyName);
	document.title = buddyName;
	var buddyImg = getImgLinkFromID(roomId);	
	console.log("buddyName: "+ buddyName + ", buddyImg: "+ buddyImg);
	$("#ChatRoom .room-image").attr("src", buddyImg);
}

function changePage(page, args) 
{
		$.mobile.changePage(page, { changeHash: true });
        $(page).trigger("callback", args);
}



