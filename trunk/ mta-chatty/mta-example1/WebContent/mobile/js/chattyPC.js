
function UI(){
	this.context={buddyRoom:true, id:"name@mail.com"};
	
	function Messages(){
		function printError(msg,e){
			log.error(e);
			alert(msg+':'+e);
		}
		this.error=function(e){
			printError('error has occured',e);
		};
		this.loginError=function(e){
			printError('error has occured while login please try again',e);
		};

	}
	this.messages=new Messages();
	
	function Navigate(){
		this.login=function(){
			log.debug("navigate into login page");
			$.mobile.changePage("#Login");
		};
		this.dashboard=function(){
			log.debug("navigate into dashboard page");
			$.mobile.changePage("#Dashboard");
		};

	}
	this.navigate=new Navigate();
	
	this.startLogin=function(){
		recreateDB(ui.navigate.login,ui.messages.error);
	};
	
	this.startApp=function (){
		bl.checkUserLoggedIn(ui.navigate.dashboard,ui.startLogin);
	};

	this.login=function(){
		var email = $("#LoginForm .loginUserMailInput").val();
		var pass = $("#LoginForm .loginUserPassInput").val();

		bl.loginUser(email, pass, ui.navigate.dashboard, ui.messages.loginError);
		return true;
	};
}
var ui=new UI();




$(document).ready(function(){
	initChatty(ui.startApp, ui.messages.error);
	
    $(window).resize(function(){
        updateMainDivsHeight();
    });
    
    $(document).bind('pagebeforeshow', function(){
        updateMainDivsHeight();
    });
    
	$("#LoginForm .btnLoginToChatty").click(ui.login);

    
});


function updateMainDivsHeight(){
	   docHeight = $(document).height();
	   updateDashboardHeight = ((docHeight - 55) +"px");
	   $(".divBuddiesAndGroups").css({ height: updateDashboardHeight });
	   $(".divChatRoom").css({ height: updateDashboardHeight });
	   updateDialogHeight = ((docHeight - 250) +"px");
	   $(".searchBuddyContent").css({ height: updateDialogHeight });
	   $(".searchGroupContent").css({ height: updateDialogHeight });    
	   $(".leaveGroupContent").css({ height: updateDialogHeight });    
}
	