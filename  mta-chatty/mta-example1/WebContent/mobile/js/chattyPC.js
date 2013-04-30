
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
	
	function Draw(){
		
		this.buddiesAndGroups=function(buddies, groups){
			var listRef="#Dashboard .buddiesAndGroupsList";
			function deleteItems(type){
			    $("#Dashboard ."+type).remove();
			}
			
			function appendDivider(name){
			    var e = $('<li data-role="list-divider"><label class="divider-label">'
			    		+name+'</label></li>');
			    $(listRef).append(e);
			}
			function appendLI(clsid,id,name,img){
			    var e = $('<li class="'+clsid+
			    		'"><a href="#ChatRoomUpdate" id="'+id+
			    		'"><label class="row-label">'+name+
			    		'</label><img class="row-image" src="'+img+
			    		'" /></a>');
			    $(listRef).append(e);
			}
			function appendBuddy(b){
				appendLI("buddy",b.email,b.name,b.picture);
			}
			function appendGroup(g){
				appendLI("group",g.group_id,g.name,g.picture);
			}
			function bind(){
				
			}
			deleteItems("divider-label");
			deleteItems("buddy");
			deleteItems("group");
			
			appendDivider("Buddies");
			iterateResults(buddies,appendBuddy);
			appendDivider("Groups");
			iterateResults(groups,appendGroup);
		    $(listRef).listview('refresh');
		    
		    //bind();

		};
		
		this.buddiesMessages=function(messages){
			
		};
	}
	this.draw = new Draw();
	
	function Bind(){
		this.buddyClick=function(){
			$("a[href=#ChatRoomUpdate]").each(function () {
			    var anchor = $(this);
				anchor.bind("click", function () {
					//TODO:continue here...
					clearChatRoom(); // in case of failure - we don't want to see previews room
					ui.chatroom.id = $(this).attr("id");
					ui.chatroom.buddyRoom = (ui.chatroom.id.indexOf("@") != -1);
					console.log("Going to change Chat room with id: " + ui.chatroom.id);
					if (ui.chatroom.buddyRoom)
						activateBuddyRoom(ui.chatroom.id);
					else
						activateGroupRoom(ui.chatroom.id);
			        return true;
			    });
			});
		};
	}
	this.bind = new Bind();
	
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
	
	this.loadContacts=function(){
		var buddies=undefined;
		function getGroupListRes(groups){
			ui.draw.buddiesAndGroups(buddies, groups);
		}
		function getBuddyListRes(res){
			buddies=res;
			bl.getGroupList(getGroupListRes, ui.messages.error);
		}
		bl.getBuddyList(getBuddyListRes, ui.messages.error);
		
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

	$("#Dashboard").bind("pagebeforeshow", ui.loadContacts);
    
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
	