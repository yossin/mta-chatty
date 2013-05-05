<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>


		<header data-role="header">
			<label class="header-label">Create New Group</label>
			<img   class="header-image" src="image/SearchGroup.png" alt="SearchGroup"/>
		</header>
        <form id="CreateGroupForm" class="content" data-role="content" action="#Groups">
			<div>
				<span class="colorRed">*</span>
				<label class="createGroupNameLable">Group Name</label>
				<input class="createGroupNameInput" type="text" required/>    
			</div>
			<div>
				<span class="colorRed">*</span>
				<label class="createGroupPicLable">Group Image:</label>
				<input class="createGroupPicInput" id="file" type="file"/>
			</div>
			<div>
				<label class="createGroupDescLable">Group Description:</label>
				<input class="createGroupDescInput" type="text"/>
			</div>

			<div>
				<span class="colorRed"><br><br>*Required</span>
			</div>
				
			<div class="createGroupDiv">
   				<% if ( request.getParameter("machine").equals("mobile")) { %>
				<a href="#Groups" class="btnCreateGroup" data-inline="true" data-role="button">Create Group</a>
				<% } else { %>
				<a href="#Dashboard" class="btnCreateGroup" data-inline="true" data-role="button">Create Group</a><% } %>
			<%--	<button type="submit" class="btnCreateGroup" data-inline="true">Create Group</button> --%>
			</div>
		</form>		
