<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>


		<header data-role="header">
			<label class="header-label">Search Group</label>
			<img   class="header-image" src="image/SearchGroup.png" alt="Search Group"/>
		</header>
        <div class="searchGroupContent" data-role="content">
   			<div class="searchGroupErea">
                <textarea autofocus="autofocus" id="searchGroupsText" class="textArea" placeholder="Search group by name"></textarea>
                <a href="#" class="searchGroupsBtn" data-role="button" data-icon="search" data-iconpos="notext"></a>
			</div>         
            <div class="searchResultGroupsDiv">
                <ul class="searchResultGroups" data-role="listview">
                    <%-- Example
                    <li class="searchResultGroup"> 
                        <a href="#Groups" class="searchResultGroup_href" id=0>
                            <label class="searchResultGroupLabel">Simsons</label>
                            <img   class="searchResultGroupImg" src="image/Simsons.jpg" alt="Simsons"/>
                        </a>
                    </li>
                    --%>                       
                </ul>
            </div>
        </div>
