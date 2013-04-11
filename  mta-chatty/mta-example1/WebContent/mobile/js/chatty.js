
$(document).ready(function(){
	$("a[data-role=tab]").each(function () {
		console.log("tab-data role");
	    var anchor = $(this);
	    anchor.bind("click", function () {
	        $.mobile.changePage(anchor.attr("href"), {
	            transition: "none",
	            changeHash: false
	        });
	    	console.log("tab-data role..");
	        return false;
	    });
	});

	$("div[data-role=page]").bind("pagebeforeshow", function (e, data) {
		console.log("pagebeforeshow");
	    $.mobile.silentScroll(0);
	    $.mobile.changePage.defaults.transition = 'slide';
	});
});
