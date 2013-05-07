<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>


	<section id="Buddies" data-role="page">
		<header data-role="header" class="tabheader">
		 <div data-role="navbar" data-iconpos="left">
			 <ul>
				 <li><a href="#Buddies" id="BuddiesTab" data-role="tab" data-icon="custom" class="ui-btn-active">Buddies</a></li>
				 <li><a href="#Groups"  id="GroupsTab"  data-role="tab" data-icon="custom" >Groups</a></li>
			 </ul>
		 </div>
		</header>
		<div class="buddiesContent" data-role="content">
			<ul class="buddyList" data-role="listview" data-filter="true">
                <%-- Example 
				<li class="buddy"> 
					<a href="#ChatRoom" id="bart@mail.com">
						<label class="row-label">Bart</label>
						<img   class="row-image" src="image/Bart.jpg" alt="Bart"/>
					</a>
				</li>
				--%>				
			</ul>
		</div>		
		
		<footer data-role="footer" class="ui-bar">
            <div>
                <a href="#Login"       class="logoutBtn"      data-role="button" data-icon="delete" data-iconpos="notext">Logout</a>
                <a href="#EditProfile" class="editProfileBtn" data-role="button" data-icon="gear"   data-iconpos="notext">Edit Profile</a>
                <a href="#SearchBuddy" class="searchBuddyBtn" data-role="button" data-icon="search" data-iconpos="notext">Search Buddy</a>
            </div>
        </footer>
	</section>


	<section id="Groups" data-role="page">
		<header data-role="header" class="tabheader">
		 <div data-role="navbar" data-iconpos="left">
			 <ul>
				 <li><a href="#Buddies" id="BuddiesTab" data-role="tab" data-icon="custom" >Buddies</a></li>
				 <li><a href="#Groups"  id="GroupsTab"  data-role="tab" data-icon="custom" class="ui-btn-active">Groups</a></li>
			 </ul>
		 </div>
		</header>
		<div class="groupsContent" data-role="content">
			<ul class="groupList" data-role="listview" data-filter="true">
                <%-- Example 
				<li class="group"> 
					<a href="#ChatRoom" id=1>
						<label class="row-label">Simsons</label>
						<img   class="row-image" src="image/Simsons.jpg" alt="Simsons"/>
					</a>
				</li>
				--%>	
			</ul>
		</div>		
		
		<footer data-role="footer" class="ui-bar">
            <div>
                <a href="#Login"       class="logoutBtn"      data-role="button" data-icon="delete" data-iconpos="notext">Logout</a>
                <a href="#EditProfile" class="editProfileBtn" data-role="button" data-icon="gear" data-iconpos="notext">Edit Profile</a>

                <a href="#SearchGroup" class="searchGroupBtn" data-role="button" data-icon="search" data-iconpos="notext">Search Group</a>
                <a href="#CreateGroup" class="createGroupBtn" data-role="button" data-icon="plus" data-iconpos="notext">Create Group</a>
                <a href="#LeaveGroup"  class="leaveGroupBtn"  data-role="button" data-icon="minus" data-iconpos="notext">Leave Group</a>
            </div>
        </footer>
	</section>

	<section id="ChatRoom" data-role="page" data-add-back-btn="true">
		<header data-role="header" class="room-header">
			<label class="header-label"></label>
			<img   class="chatroom-image" src="tbd" alt="chat room img"/>
		</header>
		<div data-role="content" class="chatRoom">
			<ul class="messages" data-role="listview">
				<%-- Example 
				<li class="message">
					<label class="messages-senderName">Bart</label>
					<img   class="messages-senderImg" src="image/Bart.jpg" alt="Bart"/>
					<label class="messages-text">message</label>
					<label class="messages-time">01/02/2013 12:02</label>    
				</li>
				--%>
			</ul>
		</div>		
		
		<footer data-role="footer">
			<div class="messageErea">
                <textarea autofocus="autofocus" class="textArea" placeholder="Enter your message here"></textarea>
                <a href="#" class="sendMessage" data-role="button" data-icon="arrow-r" data-iconpos="notext">Send</a>
			</div>
		</footer>
	</section>
