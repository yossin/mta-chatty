
function UI(){
	var context={lastSelected:{type:undefined, id:undefined}};
	
	function Messages(){
		function printError(msg,e){
			log.error(e);
			alert(msg+':'+e);
		}
		function printMessage(msg){
			log.info(msg);
		}
		this.error=function(e){
			printError('error has occured',e);
		};
		this.loginError=function(e){
			printError('error has occured while login please try again',e);
		};
		this.registerError=function(e){
			printError('error has occured while register please try again',e);
		};
		this.getMessagesError=function(e){
			printError('error has occured while retreiving messages',e);
		};
		this.sendMessagesError=function(e){
			printError('error has occured while sending a message',e);
		};
		this.searchBuddiesError=function(e){
			printError('error has occured while searching buddies',e);
		};
		this.searchGroupsError=function(e){
			printError('error has occured while searching groups',e);
		};
		this.addBuddyError=function(e){
			printError('error has occured while adding a buddy',e);
		};
		this.addGroupError=function(e){
			printError('error has occured while adding a group',e);
		};
		this.createGroup=function(res){
			printMessage('a new group has been created');
		};
		this.createGroupError=function(e){
			printError('error has occured while creating a group',e);
		};
		this.searchGroupsToLeaveError=function(e){
			printError('error has occured while searching a group to leave',e);
		};
		this.leaveGroup=function(res){
			printMessage('you have left a group');
			ui.loadContacts();
		};
		this.leaveGroupError=function(e){
			printError('error has occured while leaving a group',e);
		};
		this.adminError=function(e){
			printError('error has occured while retrieving admin data',e);
		};
	}
	this.messages=new Messages();
	
	function Navigate(){
		var messInterval = 0;
		var contInterval = 0;
		
		this.login=function(){
			log.debug("navigate into login page");
			$.mobile.changePage("#Login");

			if (messInterval != 0)
				clearInterval(messInterval);
			if (contInterval != 0)
				clearInterval(contInterval);
		};
		this.dashboard=function(){
			log.debug("navigate into dashboard page");
			$.mobile.changePage("#Dashboard");
			
			messInterval = window.setInterval(ui.refreshMessages, 2000);
			contInterval = window.setInterval(ui.loadContacts   , 2000);
		};
	}
	this.navigate=new Navigate();
	
	function Draw(){
		
		this.buddiesAndGroups=function(buddies, groups){
			var contractsRef="#Dashboard .buddiesAndGroupsList";
			
			function appendDivider(name){
			    var e = $('<li data-role="list-divider"><label class="divider-label">'
			    		+name+'</label></li>');
			    $(contractsRef).append(e);
			}
			function appendContact(clsid,id,name,img){
			    var e = $('<li class="'+clsid+
			    		'"><a href="#ChatRoom-'+clsid+'" id="'+id+
			    		'"><label class="row-label">'+name+
			    		'</label><img class="row-image" src="'+img+'" alt="'+name+
			    		'" /></a></li>');
			    $(contractsRef).append(e);
			}
			function appendBuddy(b){
				appendContact("buddy",b.email,b.name,b.picture);
			}
			function appendGroup(g){
				appendContact("group",g.group_id,g.name,g.picture);
			}
			$(contractsRef).empty();
			
			appendDivider("Buddies");
			iterateResults(buddies,appendBuddy);
			appendDivider("Groups");
			iterateResults(groups,appendGroup);
		    $(contractsRef).listview('refresh');
		    ui.bind.buddyClick();
		    ui.bind.groupClick();
		};
		
		function drawSearchResultFlow(listRef, createItemAction, items, bindAction){
			
			function appendItem(data){
				var item=createItemAction(data);
			    var e = $(item);
			    $(listRef).append(e);
			}

			$(listRef).empty();
			iterateResults(items,appendItem);
		    $(listRef).listview('refresh');
		    bindAction();
			
		}
		this.searchBuddiesResult=function(buddies){
			var listRef="#SearchBuddy .searchResultBuddies";
			function createBuddy(buddy){
				return '<li class="searchResultBuddy">'+
	    		'<a href="#Dashboard" class="searchResultBuddy_href" id="'+buddy.email+
	    		'"><label class="searchResultBuddyLabel">'+buddy.name+', '+buddy.email+
	    		'</label><img   class="searchResultBuddyImg" src="'+buddy.picture+'" alt="'+buddy.name+
	    		'" /></a></li>';
			}
			drawSearchResultFlow(listRef,createBuddy,buddies,ui.bind.addBuddyClick);
		};
		
		this.searchGroupsResult=function(groups){
			var listRef="#SearchGroup .searchResultGroups";
			function createGroup(group){
				return '<li class="searchResultGroup">'+
	    		'<a href="#Dashboard" class="searchResultGroup_href" id="'+group.group_id+
	    		'"><label class="searchResultGroupLabel">'+group.name+', '+group.group_id+
	    		'</label><img   class="searchResultGroupImg" src="'+group.picture+'" alt="'+group.name+
	    		'" /></a></li>';
			}
			drawSearchResultFlow(listRef,createGroup,groups,ui.bind.addGroupClick);
		};
		
		this.searchGroupsToLeaveResult=function(groups){
			var listRef="#LeaveGroup .groupList";
			function createGroup(group){
				return '<li class="group">'+
	    		'<a href="#Dashboard" class="hrefLeaveGroup" id="'+group.group_id+
	    		'"><label class="searchResultGroupLabel">'+group.name+', '+group.group_id+
	    		'</label><img   class="searchResultGroupImg" src="'+group.picture+'" alt="'+group.name+
	    		'" /></a></li>';
			}
			drawSearchResultFlow(listRef,createGroup,groups,ui.bind.leaveGroupClick);
		};


		function DrawMessages(){
			var msgRef="#Dashboard .messages";
			function draw(send_date, sender_id, message, sender_name, sender_picture){
			    var e = $('<li class="message">'+
			    		'<label class="messages-senderName">'+sender_name+
			    		'</label><img   class="messages-senderImg" src="'+sender_picture+'" alt="'+sender_name+
			    		'" /><label class="messages-text">'+message+
			    		'</label><label class="messages-time">'+(new Date(send_date)).toLocaleString()+
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
		function callByIdBinding(selector,callback, prevent){
			$(selector).each(function () {
			    var anchor = $(this);
				anchor.bind("click", function (event) {
					if (typeof(prevent)==undefined || prevent)
						event.preventDefault();
					var id = $(this).attr("id");
					callback(id);
			        return true;
			    });
			});
		}

		
		function bindContract(type,callback){
			var ref="a[href=#ChatRoom-"+type+"]";
			callByIdBinding(ref, callback, true);
		}
		this.buddyClick=function(){
			bindContract("buddy", ui.getBuddyMessages);
		};
		this.groupClick=function(){
			bindContract("group", ui.getGroupMessages);
		};
		this.addBuddyClick=function(){
			var ref='.searchResultBuddy_href';
			callByIdBinding(ref, ui.addBuddy, false);
		};
		this.addGroupClick=function(){
			var ref='.searchResultGroup_href';
			callByIdBinding(ref, ui.addGroup, false);
		};
		this.leaveGroupClick=function(){
			var ref='.hrefLeaveGroup';
			callByIdBinding(ref, ui.removeGroup, false);
		};
	}
	this.bind = new Bind();
	
	this.initApp = function(){
		setTimeout(function(){
			initChatty(ui.startApp, ui.messages.error);
		}, 500);
	};

	this.initDashboard=function(){
		recreateDB(function(){
			ui.loadContacts(); 
			ui.navigate.dashboard();
			},
			ui.messages.error);
	};
	
	this.startApp=function (){
		bl.checkUserLoggedIn(ui.initDashboard ,ui.navigate.login);
	};

	this.login=function(){
		var email = $("#LoginForm .loginUserMailInput").val();
		var pass  = $("#LoginForm .loginUserPassInput").val();

		bl.loginUser(email, pass, ui.initDashboard, ui.messages.loginError);
		return true;
	};

	this.register=function(){
		name  = $("#RegisterForm .registerUserNameInput").val();
		email = $("#RegisterForm .registerUserMailInput").val();
		pass  = $("#RegisterForm .registerUserPassInput").val();
		pic   = $("#RegisterForm .registerUserPicInput" ).val();

		bl.registerNewUser(email, name, pic, pass, ui.navigate.login, ui.messages.registerError);
        return true;
	};
	
	this.loadContacts=function(){
		log.debug("loadContacts");
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
			} else {
				log.debug("no message is going to be sent. user must write something..");
			}
			
			//bl.getGroupMessages(id, ui.draw.messages.fromGroup, ui.messages.getMessagesError);
		} else {
			log.debug("no message is going to be sent. user must select a contract");
		}
	};
	
	this.searchBuddies=function(){
		var text=$("#SearchBuddy .textArea");
		var search=text.val();
		bl.searchBuddiesByNameOrID(search, ui.draw.searchBuddiesResult, ui.messages.searchBuddiesError);
		return true;
	};
	this.searchGroups=function(){
		var text=$("#SearchGroup .textArea");
		var search=text.val();
		bl.searchGroupsByName(search, ui.draw.searchGroupsResult, ui.messages.searchGroupsError);
		return true;
	};
	this.addBuddy=function(id){
		bl.addBuddyToList(id, ui.loadContacts, ui.messages.addBuddyError);
		return true;
	};
	this.addGroup=function(id){
		bl.joinGroup(id, ui.loadContacts, ui.messages.addGroupError);
		return true;
	};
	this.createGroup=function(){
		var name=$("#CreateGroup .createGroupNameInput").val();
		var pic =$("#CreateGroup .createGroupPicInput").val();
		var desc =$("#CreateGroup .createGroupDescInput").val();
		bl.createGroup(name, pic, desc, ui.messages.createGroup, ui.messages.createGroupError);
		return true;
	};
	this.leaveGroup=function(){
		bl.getGroupList(ui.draw.searchGroupsToLeaveResult, ui.messages.searchGroupsToLeaveError);
	};
	this.removeGroup=function(id){
		bl.leaveGroup(id, ui.messages.leaveGroup, ui.messages.leaveGroupError);
		return true;
	};

}
var ui=new UI();

$(document).ready(function(){
    $(window).resize(updateMainDivsHeight);
    
    $(document).bind('pagebeforeshow', updateMainDivsHeight);
    
	$("#LoginForm .btnLoginToChatty").click(ui.login);

	$("#RegisterForm .btnRegisterSubmit").click(ui.register);

	$("#Dashboard").bind("pagebeforeshow", ui.loadContacts);
	$("#Dashboard .sendMessage").bind("click", ui.sendMessage);
	$("#Dashboard .logoutBtn").bind("click", ui.navigate.login);

	$("#SearchBuddy .searchBuddiesBtn").bind("click", ui.searchBuddies);
	$("#SearchGroup .searchGroupsBtn").bind("click", ui.searchGroups);
	$("#CreateGroup .btnCreateGroup").bind("click", ui.createGroup);
	
	$("#LeaveGroup").bind('pagebeforeshow', ui.leaveGroup);
	
	$("#Loading").bind("pagebeforeshow", ui.initApp);
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
