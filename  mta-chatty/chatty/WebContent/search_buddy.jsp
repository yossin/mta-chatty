<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>


		<header data-role="header">
			<label class="header-label">Search Buddy</label>
			<img   class="header-image" src="image/SearchBuddy.png" alt="SearchBuddy"/>
		</header>
        <div class="searchBuddyContent" data-role="content">
   			<div class="searchBuddyErea">
                <textarea autofocus="autofocus" id="searchBuddyText" class="textArea" placeholder="Search by buddy name or mail address"></textarea>
                <a href="#" class="searchBuddiesBtn" data-role="button" data-icon="search" data-iconpos="notext"></a>
			</div>         
            <div class="searchResultBuddiesDiv">
                <ul class="searchResultBuddies" data-role="listview">
                    <%-- Example 
                    <li class="searchResultBuddy"> 
                        <a href="#Dashboard" class="searchResultBuddy_href" id="Lisa@Lisa.com">
                            <label class="searchResultBuddyLabel">Lisa, Lisa@Lisa.com</label>
                            <img   class="searchResultBuddyImg" src="image/Lisa.jpg" alt="lisa"/>
                        </a>
                    </li>
                    --%> 
                </ul>
            </div>
        </div>
