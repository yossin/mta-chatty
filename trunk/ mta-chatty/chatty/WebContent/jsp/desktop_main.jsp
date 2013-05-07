<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

	<section id="Dashboard" data-role="page">

        <header></header>


        <div class="dashboardContent" data-role="content">

                <div class="divBuddiesAndGroups">
                            
                    <ul class="buddiesAndGroupsList" data-role="listview">

                        <%-- Example 

                        <li data-role="list-divider"><label class="divider-label">Buddies</label></li>
                   
                        <li class="buddy"> 
                            <a href="#ChatRoomUpdate" id="bart@mail.com">
                                <label class="row-label">Bart</label>
                                <img   class="row-image" src="image/Bart.jpg" alt="SearchBuddy"/>
                            </a>
                        </li>
                        
                        <li data-role="list-divider"><label class="divider-label">Groups</label></li>
                    
                        <li class="group"> 
                            <a href="#ChatRoomUpdate" id="1">
                                <label class="row-label">Simsons</label>
                                <img   class="row-image" src="image/Simsons.jpg" alt="Simsons"/>
                            </a>
                        </li>

                        --%>
                        
                    </ul>	
                    
                </div>		

                <div class="divChatRoom">
                    <div class="chatRoom">
                        <ul class="messages" data-role="listview">
                        <%-- Example 
                            <li class="message">
                                <label class="messages-text">message</label>
                                <label class="messages-time">01/02/2013 12:02</label>    
                            </li>
                        --%>
                        </ul>
                    </div>		
                    
                    <div class="messageErea">
                        <textarea autofocus="autofocus" class="textArea" placeholder="Enter your message here"></textarea>
                        <a href="#" class="sendMessage" data-role="button" data-icon="arrow-r" data-iconpos="notext">Send</a>
                    </div>
                </div>
               
			
		</div>		
		
   	
    		
		<footer data-role="footer" class="ui-bar" id="dashboardFooter">
            <div class="dashboardFooterDiv">
                <a href="#Login"       class="logoutBtn"      data-role="button" data-icon="delete">Logout</a>
                <a href="#EditProfile" class="editProfileBtn" data-role="button" data-icon="gear">Edit Profile</a>
                
                <a href="#SearchGroup" class="searchGroupBtn" data-role="button" data-icon="search">Search Group</a>
                <a href="#CreateGroup" class="createGroupBtn" data-role="button" data-icon="plus">Create Group</a>
                <a href="#LeaveGroup"  class="leaveGroupBtn"  data-role="button" data-icon="minus">Leave Group</a>
                
                <a href="#SearchBuddy" class="searchBuddyBtn" data-role="button" data-icon="search" >Search Buddy</a>
                
            </div>
        </footer>

	</section>	
        