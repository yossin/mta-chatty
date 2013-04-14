
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

	
	$("a[data-role=buddy]").each(function () {
	    var anchor = $(this);
	    anchor.bind("click", function () {
		      var id = $(this).attr("id");
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
		console.log("bind callback: "+args.id);
		$("#ChatRoom .room-title").text(args.id);
	});
	
});


function changePage(page, args) 
{
		$.mobile.changePage(page, { changeHash: true });
        $(page).trigger("callback", args);
}



