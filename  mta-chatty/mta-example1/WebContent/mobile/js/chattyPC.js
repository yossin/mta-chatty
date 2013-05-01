
function UI(){
	var context={lastSelected:{type:undefined, id:undefined}};
	
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
		this.getMessagesError=function(e){
			printError('error has occured while retreiving messages',e);
		};
		this.sendMessagesError=function(e){
			printError('error has occured while sending message',e);
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
			var contractsRef="#Dashboard .buddiesAndGroupsList";
			function deleteItems(type){
			    $("#Dashboard ."+type).remove();
			}
			
			function appendDivider(name){
			    var e = $('<li data-role="list-divider"><label class="divider-label">'
			    		+name+'</label></li>');
			    $(contractsRef).append(e);
			}
			function appendLI(clsid,id,name,img){
			    var e = $('<li class="'+clsid+
			    		'"><a href="#ChatRoom-'+clsid+'" id="'+id+
			    		'"><label class="row-label">'+name+
			    		'</label><img class="row-image" src="'+img+
			    		'" /></a>');
			    $(contractsRef).append(e);
			}
			function appendBuddy(b){
				appendLI("buddy",b.email,b.name,b.picture);
			}
			function appendGroup(g){
				appendLI("group",g.group_id,g.name,g.picture);
			}
			deleteItems("divider-label");
			deleteItems("buddy");
			deleteItems("group");
			
			appendDivider("Buddies");
			iterateResults(buddies,appendBuddy);
			appendDivider("Groups");
			iterateResults(groups,appendGroup);
		    $(contractsRef).listview('refresh');
		    ui.bind.buddyClick();
		    ui.bind.groupClick();
		};
		
		function DrawMessages(){
			var msgRef="#Dashboard .messages";
			function draw(send_date, sender_id, message, sender_name, sender_picture){
				//TODO: draw all message fields
			    var e = $('<li class="message"><label class="messages-text">'+message+
			    		'</label><label class="messages-time">'+send_date+
			    		'</label></li>');
			    $(msgRef).append(e);
			}
			
			function drawFlow(callback, messages){
			    $("#Dashboard .message").remove();
				iterateResults(messages,callback);
			    $(msgRef).listview('refresh');
			};
			this.fromBuddy=function(messages){
				function drawMessage(m){
					draw(m.send_date, m.sender_id, m.message, m.sender_name, m.sender_picture);
				}
				drawFlow(drawMessage, messages);
			};
			this.fromGroup=function(messages){
				function drawMessage(m){
					draw(m.send_date, m.email, m.message, m.name, m.picture);
				}
				drawFlow(drawMessage, messages);
			};
			
		};
		this.messages=new DrawMessages();
		
	}
	this.draw = new Draw();
	
	function Bind(){
		function bindContract(type,callback){
			var ref="a[href=#ChatRoom-"+type+"]";
			$(ref).each(function () {
			    var anchor = $(this);
				anchor.bind("click", function (event) {
					event.preventDefault();
					var id = $(this).attr("id");
					callback(id);
			        return true;
			    });
			});
		}
		this.buddyClick=function(){
			bindContract("buddy", ui.getBuddyMessages);
		};
		this.groupClick=function(){
			bindContract("group", ui.getGroupMessages);
		};
	}
	this.bind = new Bind();
	
	this.initApp=function(){
		recreateDB(ui.navigate.login,ui.messages.error);
	};
	
	this.startApp=function (){
		bl.checkUserLoggedIn(ui.navigate.dashboard,ui.initApp);
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
	
	this.refreshMessages=function(){
		var id=	context.lastSelected.id;
		var type = context.lastSelected.type;
		if (type=='buddy'){
			ui.getBuddyMessages(id);
		} else if (type=='group'){
			ui.getGroupMessages(id);
		} else {
			log.error('unknown message type: '+type+', therfore unable to refresh messages');
		}
	};
	
	this.getBuddyMessages=function(id){
		log.debug("retreive buddy "+id+" messages");
		context.lastSelected.type="buddy";
		context.lastSelected.id=id;
		bl.getBuddyMessages(id, ui.draw.messages.fromBuddy, ui.messages.getMessagesError);
	};
	this.getGroupMessages=function(id){
		log.debug("retreive group "+id+" messages");
		context.lastSelected.type="group";
		context.lastSelected.id=id;
		bl.getGroupMessages(id, ui.draw.messages.fromGroup, ui.messages.getMessagesError);
	};
	this.sendMessage=function(){
		if (context.lastSelected.type!=undefined){
			var text=$("#Dashboard .textArea");
			var message=text.val();
			if (message!=null && message!=""){
				text.val('');
				log.debug("send "+context.lastSelected.type+" "+context.lastSelected.id+" a message: "+message);
				if (context.lastSelected.type=='buddy'){
					bl.sendBuddyMessage(context.lastSelected.id, message, ui.refreshMessages, ui.messages.sendMessagesError);
				} else if (context.lastSelected.type=='group'){
					bl.sendGroupMessage(context.lastSelected.id, message, ui.refreshMessages, ui.messages.sendMessagesError);
				}
				ui.refreshMessages();
			} else {
				log.debug("no message is going to be sent. user must write something..");
			}
			
			//bl.getGroupMessages(id, ui.draw.messages.fromGroup, ui.messages.getMessagesError);
		} else {
			log.debug("no message is going to be sent. user must select a contract");
		}
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
	$("#Dashboard .sendMessage").bind("click", ui.sendMessage);
    
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
	